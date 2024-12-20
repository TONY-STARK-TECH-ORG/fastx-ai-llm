import os
import torch
from transformers import BertTokenizer, BertModel

tokenizer = BertTokenizer.from_pretrained("bert-base-uncased")
model = BertModel.from_pretrained("bert-base-uncased")

def process_pdf_vec(file_path, output_path):
    result = os.system("paddleocr --image_dir=" + file_path + " --type=structure --recovery=true --recovery_to_markdown=true --output=" + output_path)
    if result == 0:
        file_name_o = output_path + file_path.split("/")[-1].split(".")[0] + "_ocr.md"
        print(f"process pdf success, now start structure md file {file_name_o}")
        # read md file
        max_chunk_size = 500
        markdown_content = read_markdown_file(file_name_o)
        chunks = split_markdown(markdown_content, max_chunk_size, tokenizer)

        embedding_result = []
        for chunk in chunks:
            text_inner = chunk
            from text2vec import text_to_bert_vector
            er = text_to_bert_vector(text_inner)
            embedding_result.append(er)
        return embedding_result, chunks
    else:
        print("process failed")
        return [], []

def read_markdown_file(file_path):
    """读取 markdown 文件内容"""
    with open(file_path, 'r', encoding='utf-8') as file:
        return file.read()

def split_markdown(content, max_chunk_tokens, tokenizer):
    chunks = []  # 用于存储分块的结果
    current_chunk = ""  # 当前块

    for line in content.splitlines(keepends=True):  # 保留换行符
        # 如果是标题行（逻辑分块依据，如 #, ##, ###）
        if line.strip().startswith(('#', '##', '###', '-', '*', '>')):
            # 如果当前块不为空，则优先将当前块保存
            if current_chunk:
                chunks.append(current_chunk)
                current_chunk = ""

        # 临时添加新行，检查块的 token 长度是否会超标
        temp_chunk = current_chunk + line
        tokenized = tokenizer(temp_chunk, add_special_tokens=False, return_attention_mask=False, return_tensors=None)
        if len(tokenized["input_ids"]) > max_chunk_tokens:
            # 如果超出 token 限制，保存当前块
            if current_chunk:  # 先保存当前块（确保为空行不加入 chunks）
                chunks.append(current_chunk)
            current_chunk = line  # 将当前行作为下一个新块的开头
        else:
            # 否则继续追加当前行到块
            current_chunk = temp_chunk

    # 将剩余内容保存为最后一个块
    if current_chunk:
        chunks.append(current_chunk)

    # 逐个处理超过 max_chunk_tokens 的块，进行拆分
    def split_large_chunk(chunk):
        tokenized = tokenizer(chunk, add_special_tokens=False, return_attention_mask=False, return_tensors=None)
        input_ids = tokenized["input_ids"]

        # 按 max_chunk_tokens 分割
        sub_chunks = [input_ids[i:i + max_chunk_tokens] for i in range(0, len(input_ids), max_chunk_tokens)]

        # 解码成文本
        return [tokenizer.decode(sub_chunk, skip_special_tokens=True) for sub_chunk in sub_chunks]

    final_chunks = []
    for chunk in chunks:
        tokenized = tokenizer(chunk, add_special_tokens=False, return_attention_mask=False, return_tensors=None)
        if len(tokenized["input_ids"]) > max_chunk_tokens:
            # 如果块仍然过大，进一步拆分此块
            final_chunks.extend(split_large_chunk(chunk))
        else:
            final_chunks.append(chunk)

    return final_chunks

# process_pdf_vec("process/29-2412.09618v1.pdf", "process/29/")