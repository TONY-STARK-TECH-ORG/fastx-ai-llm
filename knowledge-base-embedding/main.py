from fastapi import FastAPI, Depends, HTTPException, BackgroundTasks
import requests, os
from peewee import *
from playhouse.db_url import connect
import numpy as np
import shutil

# create uvicorn application.
app = FastAPI()
# mysql db connection string.
mysql_conn_str = "mysql://root:123456@127.0.0.1:3306/fast-llm"
# db connection.
db = connect(mysql_conn_str)

class KnowledgeBaseFile(Model):
    id = BigIntegerField(primary_key=True)
    knowledge_base_id = BigIntegerField()
    name = CharField()
    extension = CharField()
    download_url = TextField()
    vec_collection_name = CharField()
    vec_collection_id = CharField()
    vec_partition_key = CharField()
    vec_partition_value = CharField()
    status = CharField()
    deleted = IntegerField()
    create_time = DateTimeField()
    update_time = DateTimeField()

    class Meta:
        table_name = "t_knowledge_base_file"
        database = db

class MilvusData:
    def __init__(self, extension, vector, kb_id, file_id, file_part_id, download_url, content):
        if extension == "txt":
            self.text_content = vector
        elif extension == "png" or extension == "jpg" or extension == "jpeg":
            self.image_content = vector
        else:
            raise ValueError("unsupported file extension: " + extension)
        self.extension = extension
        self.kb_id = kb_id
        self.file_id = file_id
        self.file_part_id = file_part_id
        self.download_url = download_url
        self.content = content

    def to_milvus(self):
        d_i = {
            "kb_id": str(self.kb_id),
            "file_id": str(self.file_id),
            "file_part_id": str(self.file_part_id),
            "extension": self.extension,
            "download_url": self.download_url,
            "content": self.content
        }
        d_i["text_content"] = np.zeros(1536)
        d_i["image_content"] = np.zeros(2048)
        d_i["audio_content"] = np.zeros(512)
        d_i["video_content"] = np.zeros(2048)

        if self.extension == "txt":
            vector = np.asarray(self.text_content)
            current_length = vector.shape[0]

            padded_vector = np.zeros(1536)
            padded_vector[:current_length] = vector

            d_i["text_content"] = padded_vector
        elif self.extension == "png" or self.extension == "jpg" or self.extension == "jpeg":
            vector = np.asarray(self.image_content)
            current_length = vector.shape[0]
            padded_vector = np.zeros(2048)
            padded_vector[:current_length] = vector
            d_i["image_content"] = self.image_content
        else:
            raise ValueError("unsupported file extension: " + extension)
        return [d_i]

@app.get("/")
async def root():
    return "Welcome to Fast LLM knowledge-base-embedding(KBE) system."

def embedding_file(id: str):
    ff = KnowledgeBaseFile.get(KnowledgeBaseFile.id == id)
    # try:
    print("process file: " + ff.download_url)
    r = requests.get(ff.download_url)
    file_name = "process/" + str(ff.id) + "-" + ff.name
    # download file
    with open(file_name, "wb") as fi:
        fi.write(r.content)
    print("download file success: " + file_name)
    ##################################
    ###      embedding file        ###
    ##################################
    print(f"will embedding file: {file_name}, extension: {ff.extension}")
    embedding_result = None
    embedding_content = ff.download_url
    pdf_chunks = []
    if ff.extension == "txt":
        from text2vec import text_to_bert_vector
        inner_file = open(file_name, "r")
        try:
            inner_text = inner_file.read()
            embedding_result = text_to_bert_vector(inner_text)
            embedding_content = inner_text
        finally:
            inner_file.close()
    elif ff.extension in ["png", "jpg", "jpeg"]:
        from image2vec import image_to_vector_resnet
        embedding_result = image_to_vector_resnet(file_name)
    elif ff.extension == "pdf":
        # parse pdf
        from pdf2vec import process_pdf_vec
        rr = process_pdf_vec(file_name, "process/" + str(ff.id) + "/")
        embedding_result, chunks = rr
        pdf_chunks = chunks
        shutil.rmtree("process/" + str(ff.id))
    elif ff.extension in ["docx", "doc", "ppt", "pptx", "xls", "xlsx"]:
        # parse document
        raise ValueError("we don't support office file now: " + ff.extension)
    else:
        raise ValueError("unsupported file extension: " + ff.extension)
    ##################################
    ###    save  embedding file    ###
    ##################################
    if ff.extension != "pdf":
        # save to milvus
        from vec2milvus import insert_to
        di = MilvusData(ff.extension, embedding_result, ff.knowledge_base_id, ff.id, 1, ff.download_url,
                        embedding_content)
        ff.vec_collection_id = insert_to(ff.vec_collection_name, di)
    else:
        from vec2milvus import insert_to
        if not embedding_result:
            embedding_result = []
        # foreach save to milvus
        vec_ids = []
        idx = 0
        for ei in embedding_result:
            di = MilvusData("txt", ei, ff.knowledge_base_id, ff.id, idx + 1, ff.download_url, pdf_chunks[idx])
            vec_ids.append(str(insert_to(ff.vec_collection_name, di)))
            idx = idx + 1
        ff.vec_collection_id = ",".join(vec_ids)
    # set file status to success
    ff.status = "active"
    ff.save()
    # clean local file
    os.remove(file_name)
    print(f"clean workspace: {file_name}")
    # except Exception as e:
    #     # reset file status to wait.
    #     print(e)
    #     ff.status = "wait"
    #     ff.save()
    #     print(f"embedding error: {ff.id} {e}")

@app.post("/embedding/{id}")
async def embedding(id: str, background_tasks: BackgroundTasks):
    f = KnowledgeBaseFile.get(KnowledgeBaseFile.id == id)
    if not f:
        raise HTTPException(status_code=404, detail="file not found")
    print("will process: " + f.name)
    background_tasks.add_task(embedding_file, str(f.id))
    # update file to process status.
    f.status = "process"
    f.save()
    return { "status": f.status}