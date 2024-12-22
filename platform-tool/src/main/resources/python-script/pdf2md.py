import os, sys, requests
from transformers import BertTokenizer, BertModel
import shutil

tokenizer = BertTokenizer.from_pretrained("bert-base-uncased")
model = BertModel.from_pretrained("bert-base-uncased")

def process_pdf_vec(file_path, output_path):
    try:
        result = os.system("/opt/anaconda3/envs/kbe/bin/paddleocr --image_dir=" + file_path + " --type=structure --recovery=true --recovery_to_markdown=true --output=" + output_path)
        if result == 0:
            return output_path + file_path.split("/")[-1].split(".")[0] + "_ocr.md"
    except:
        raise ValueError("parse meet exception!")
    raise ValueError("parse not has result!")

def download_pdf(url, save_path):
    try:
        response = requests.get(url, stream=True)
        response.raise_for_status()
        with open(save_path, 'wb') as f:
            for chunk in response.iter_content(chunk_size=8192):
                f.write(chunk)
    except:
        raise ValueError("not found this file!")

def read_markdown_file(file_path):
    """读取 markdown 文件内容"""
    with open(file_path, 'r', encoding='utf-8') as file:
        return file.read()

def main():
    pdf_url = sys.argv[1]
    download_pdf(pdf_url, "./process.pdf")
    md_path = process_pdf_vec("./process.pdf", "./process-temp/")
    print("---FAST-LLM-START---")
    print(read_markdown_file(md_path))
    print("---FAST-LLM-END---")
    os.remove("./process.pdf")
    shutil.rmtree("./process-temp/")

if __name__ == "__main__":
    main()