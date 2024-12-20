from pymilvus import MilvusClient
from main import MilvusData

client = MilvusClient(
    uri="https://in03-099d4d04797b236.serverless.gcp-us-west1.cloud.zilliz.com",
    token="31e9ebc8e5f562f78924dd7f06c652afe52bed46b2b4a123bcb395f5b27e9b71a450727e7b31da71187dffccc3a87a26b89dcf65",
    username="db_099d4d04797b236",
    password="Et2|(>)/g)6cd-fp",
)

def insert_to(collection_name: str, data: MilvusData):
    print(f"will insert to {collection_name}! {data.extension}")
    res = client.insert(
        collection_name=collection_name,
        data=data.to_milvus()
    )
    print("insert result" + str(res["ids"][0]))
    if len(res["ids"]) > 0:
        return res["ids"][0]
    else:
        raise Exception("milvus insert failed")