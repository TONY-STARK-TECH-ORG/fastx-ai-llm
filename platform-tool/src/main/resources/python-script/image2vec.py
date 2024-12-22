import os
import requests
from torchvision import models, transforms
from torchvision.models import ResNet50_Weights  # Import weights
from PIL import Image
import torch
import os, sys

# Initialize the model (put this at the top level)
def initialize_model():
    model = models.resnet50(weights=ResNet50_Weights.IMAGENET1K_V1)
    # remove last layer
    model = torch.nn.Sequential(*list(model.children())[:-1])
    model.eval()
    return model

# Global model variable
model = initialize_model()

def download_and_verify_image(url, save_path):
    try:
        response = requests.get(url, stream=True)
        response.raise_for_status()
        with open(save_path, 'wb') as f:
            for chunk in response.iter_content(chunk_size=8192):
                f.write(chunk)
    except Exception as e:
        print(f"Download/verification failed: {str(e)}")

def image_to_vector_resnet(image_path):
    try:
        # Check if file exists
        if not os.path.exists(image_path):
            raise FileNotFoundError(f"File not found: {image_path}")

        # Check file size
        file_size = os.path.getsize(image_path)
        if file_size == 0:
            raise ValueError(f"File is empty: {image_path}")

        # Try to open and verify the image
        try:
            with Image.open(image_path) as img:
                img.verify()
        except Exception as e:
            raise ValueError(f"Invalid image file: {image_path}. Error: {str(e)}")

        # Process the image
        preprocess = transforms.Compose([
            transforms.Resize((224, 224)),
            transforms.ToTensor(),
            transforms.Normalize([0.485, 0.456, 0.406], [0.229, 0.224, 0.225])
        ])

        image = Image.open(image_path).convert("RGB")
        image_tensor = preprocess(image).unsqueeze(0)

        with torch.no_grad():
            vector = model(image_tensor)

        os.remove(image_path)
        return vector.squeeze().numpy().tolist()
    except Exception as e:
        print(f"Error processing image {image_path}: {str(e)}")
        return None

def main():
    image_url = sys.argv[1]
    download_and_verify_image(image_url, "./process.png")
    print(image_to_vector_resnet("./process.png"))

if __name__ == "__main__":
    main()