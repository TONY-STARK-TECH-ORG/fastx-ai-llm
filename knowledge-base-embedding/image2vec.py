import os
import requests
import magic
import hashlib
from torchvision import models, transforms
from torchvision.models import ResNet50_Weights  # Import weights
from PIL import Image
import torch

# Initialize the model (put this at the top level)
def initialize_model():
    model = models.resnet50(weights=ResNet50_Weights.IMAGENET1K_V1)
    # remove last layer
    model = torch.nn.Sequential(*list(model.children())[:-1])
    model.eval()
    return model

# Global model variable
model = initialize_model()

def debug_file(file_path):
    print("\n=== File Debugging Information ===")

    # 1. Check file existence
    if not os.path.exists(file_path):
        print(f"File does not exist at: {file_path}")
        return

    # 2. Check file size
    file_size = os.path.getsize(file_path)
    print(f"File size: {file_size} bytes")

    # 3. Check file type using magic numbers
    mime = magic.Magic(mime=True)
    file_type = mime.from_file(file_path)
    print(f"File MIME type: {file_type}")

    # 4. Read first few bytes
    try:
        with open(file_path, 'rb') as f:
            header = f.read(16)
            print(f"First 16 bytes (hex): {header.hex()}")
    except Exception as e:
        print(f"Error reading file: {str(e)}")

    # 5. Calculate file hash
    try:
        with open(file_path, 'rb') as f:
            md5_hash = hashlib.md5(f.read()).hexdigest()
            print(f"MD5 hash: {md5_hash}")
    except Exception as e:
        print(f"Error calculating hash: {str(e)}")

def download_and_verify_image(url, save_path):
    print(f"\n=== Downloading and Verifying Image ===")
    print(f"URL: {url}")
    print(f"Save path: {save_path}")

    try:
        # Download with requests
        response = requests.get(url, stream=True)
        response.raise_for_status()

        # Check Content-Type
        content_type = response.headers.get('Content-Type', '')
        print(f"Response Content-Type: {content_type}")

        # Save file
        with open(save_path, 'wb') as f:
            for chunk in response.iter_content(chunk_size=8192):
                f.write(chunk)

        # Debug downloaded file
        debug_file(save_path)

        # Try to open with PIL
        try:
            img = Image.open(save_path)
            print(f"Successfully opened with PIL")
            print(f"Image format: {img.format}")
            print(f"Image size: {img.size}")
            print(f"Image mode: {img.mode}")
        except Exception as e:
            print(f"Failed to open with PIL: {str(e)}")

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

        return vector.squeeze().numpy()

    except Exception as e:
        print(f"Error processing image {image_path}: {str(e)}")
        return None