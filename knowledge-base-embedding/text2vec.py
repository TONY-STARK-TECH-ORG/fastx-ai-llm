import torch
from transformers import BertTokenizer, BertModel

tokenizer = BertTokenizer.from_pretrained("bert-base-uncased")
model = BertModel.from_pretrained("bert-base-uncased")

def text_to_bert_vector(text):
    inputs = tokenizer(text, return_tensors="pt")
    with torch.no_grad():
        outputs = model(**inputs)
    return outputs.last_hidden_state.mean(dim=1).squeeze().numpy()

text_vector = text_to_bert_vector("I love Fast LLM!")
print("Text feature vector shape:", text_vector.shape)
print("vector:", text_vector)
