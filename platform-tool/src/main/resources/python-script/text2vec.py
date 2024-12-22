import torch
from transformers import BertTokenizer, BertModel
import sys

tokenizer = BertTokenizer.from_pretrained("bert-base-uncased")
model = BertModel.from_pretrained("bert-base-uncased")

def main():
    text_content = sys.argv[1]
    inputs = tokenizer(text_content, return_tensors="pt", max_length=512, truncation=True)
    with torch.no_grad():
        outputs = model(**inputs)
    print(outputs.last_hidden_state.mean(dim=1).squeeze().numpy().tolist())

if __name__ == "__main__":
    main()