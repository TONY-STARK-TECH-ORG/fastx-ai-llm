from fastapi import FastAPI, Depends, HTTPException, BackgroundTasks
from typing import Annotated
import requests, os
app = FastAPI()

@app.get("/")
async def root():
    return "Welcome to Fast LLM knowledge-base-embedding(KBE) system."

def embedding_file(file_name: str, file_download_url: str):
    print("process file: " + file_download_url)
    r = requests.get(file_download_url)
    with open(file_name, "wb") as f:
        f.write(r.content)
    print("download file success: " + file_name)
    # load file from this file.
    file_obj = open(file_name, "rb")
    if not file_obj:
        return
    print("load file success: " + file_name)
    ##################################
    ### embedding file.
    ##################################
    print("embedding file: " + file_download_url)
    # update file status.
    # call self api with embedding result.
    print("will call embedding api... with result: ")
    # clean local file
    os.remove(file_name)

@app.post("/embedding/{file_name}/{file_url}")
async def embedding(file_name: str, file_url: str):
    print("will process: " + file_name + ":" + file_url)
    background_tasks.add_task(embedding_file, file_name, file_url)
    return "in progress"