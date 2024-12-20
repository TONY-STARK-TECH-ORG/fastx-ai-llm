from fastapi import FastAPI, Depends, HTTPException, BackgroundTasks
from typing import Annotated
import requests, os
from peewee import *
from playhouse.db_url import connect
from unstructured.partition.pdf import partition_pdf
# process
from unstructured.cleaners.core import clean_non_ascii_chars
from unstructured.cleaners.core import remove_punctuation
# chunk
from unstructured.chunking.basic import chunk_elements

# create uvicorn application.
app = FastAPI()
# mysql db connection string.
mysql_conn_str = "mysql://root:123456@172.17.0.4:3306/fast-llm"
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

@app.get("/")
async def root():
    return "Welcome to Fast LLM knowledge-base-embedding(KBE) system."

def embedding_file(id: str):
    ff = KnowledgeBaseFile.get(KnowledgeBaseFile.id == id)
    try:
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
        if ff.extension == "txt":
            from text2vec import text_to_bert_vector
            inner_file = open(file_name, "r")
            try:
                inner_text = inner_file.read()
                embedding_result = text_to_bert_vector(inner_text)
            finally:
                inner_file.close()
        elif ff.extension in ["png", "jpg", "jpeg"]:
            from image2vec import image_to_vector_resnet
            embedding_result = image_to_vector_resnet(file_name)
        elif ff.extension == "pdf":
            # parse pdf
            raise ValueError("we don't support pdf now: " + ff.extension)
        elif ff.extension in ["docx", "doc", "ppt", "pptx", "xls", "xlsx"]:
            # parse document
            raise ValueError("we don't support office file now: " + ff.extension)
        else:
            raise ValueError("unsupported file extension: " + ff.extension)
        # call self api with embedding result.
        print(f"end embedding with result: {embedding_result}")
        # TODO update this vector to milvus

        # set file status to success
        ff.status = "active"
        ff.save()
        # clean local file
        os.remove(file_name)
        print(f"clean workspace: {file_name}")
    except Exception as e:
        # reset file status to wait.
        ff.status = "wait"
        ff.save()
        print(f"embedding error: {ff.id} {e}")

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