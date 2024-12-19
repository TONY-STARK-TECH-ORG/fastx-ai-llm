from torchvision import models, transforms
from PIL import Image
import torch

model = models.resnet50(pretrained=True)
# remove last layer
model = torch.nn.Sequential(*list(model.children())[:-1])
model.eval()

def image_to_vector_resnet(image_path):
    preprocess = transforms.Compose([
        transforms.Resize((224, 224)),
        transforms.ToTensor(),
        transforms.Normalize([0.485, 0.456, 0.406], [0.229, 0.224, 0.225])
    ])
    image = Image.open(image_path).convert("RGB")
    image_tensor = preprocess(image).unsqueeze(0)
    with torch.no_grad():
        vector = model(image_tensor)
    return vector.squeeze().numpy()

image_vector = image_to_vector_resnet("process/figure-1-1.jpg")
print("Image feature vector shape:", image_vector.shape)
print("vec was:", image_vector)
