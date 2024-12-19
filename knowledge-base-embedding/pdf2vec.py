import fitz  # PyMuPDF
import json
import os
import cv2
import numpy as np
from PIL import Image
import pytesseract  # OCR文字识别
import pix2tex  # 数学公式识别
import camelot  # 表格识别

class PDFParser:
    def __init__(self, pdf_path):
        self.pdf_path = pdf_path
        self.doc = fitz.open(pdf_path)
        self.output_dir = "output"
        os.makedirs(self.output_dir, exist_ok=True)

    def parse_pdf(self):
        result = []

        for page_num in range(len(self.doc)):
            page = self.doc[page_num]
            page_dict = {
                "page_num": page_num + 1,
                "text": [],
                "images": [],
                "tables": [],
                "formulas": [],
                "page_image": f"page_{page_num+1}.png"
            }

            # 1. 保存页面图片
            self.save_page_image(page, page_num)

            # 2. 提取文本
            text_blocks = self.extract_text(page)
            page_dict["text"] = text_blocks

            # 3. 提取图片
            images = self.extract_images(page)
            page_dict["images"] = images

            # 4. 提取表格
            tables = self.extract_tables(page_num)
            page_dict["tables"] = tables

            # 5. 识别数学公式
            formulas = self.extract_formulas(page)
            page_dict["formulas"] = formulas

            result.append(page_dict)

        # 保存结构化数据
        self.save_json(result)
        return result

    def save_page_image(self, page, page_num):
        pix = page.get_pixmap()
        img_path = os.path.join(self.output_dir, f"page_{page_num+1}.png")
        pix.save(img_path)

    def extract_text(self, page):
        text_blocks = []
        blocks = page.get_text("blocks")
        for block in blocks:
            if block[6] == 0:  # 文本类型
                text_blocks.append({
                    "text": block[4],
                    "bbox": block[:4]
                })
        return text_blocks

    def extract_images(self, page):
        images = []
        image_list = page.get_images(full=True)
        for img_index, img in enumerate(image_list):
            xref = img[0]
            base_image = self.doc.extract_image(xref)
            image_path = os.path.join(self.output_dir, f"image_{page.number+1}_{img_index+1}.png")

            with open(image_path, "wb") as f:
                f.write(base_image["image"])

            images.append({
                "image_path": image_path,
                "bbox": img[1]  # 图片位置信息
            })
        return images

    def extract_tables(self, page_num):
        tables = []
        # 使用camelot提取表格
        tables_found = camelot.read_pdf(self.pdf_path, pages=str(page_num+1))

        for idx, table in enumerate(tables_found):
            tables.append({
                "table_id": idx + 1,
                "data": table.data.tolist(),
                "bbox": table._bbox
            })
        return tables

    def extract_formulas(self, page):
        formulas = []
        # 使用pix2tex识别数学公式
        # 这里需要先识别可能包含公式的区域，然后进行公式识别
        # 实现略复杂，这里只是示例
        return formulas

    def save_json(self, result):
        output_path = os.path.join(self.output_dir, "structure.json")
        with open(output_path, "w", encoding="utf-8") as f:
            json.dump(result, f, ensure_ascii=False, indent=2)

def main():
    pdf_parser = PDFParser("process/2412.09618v1.pdf")
    result = pdf_parser.parse_pdf()
    print("PDF解析完成!")

if __name__ == "__main__":
    main()