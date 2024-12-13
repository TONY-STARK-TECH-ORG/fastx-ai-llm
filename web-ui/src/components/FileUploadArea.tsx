import {InboxOutlined} from '@ant-design/icons';
import type { UploadProps } from 'antd';
import { message, Upload } from 'antd';
import { api, http } from "../api/Http.ts";

const { Dragger } = Upload;

export type UploadDataProps = {
    workspaceId: string
}

export default function FileUploadArea( { data }: {data: UploadDataProps} ) {
    const props: UploadProps = {
        name: 'files',
        multiple: true,
        data: data,
        headers: http.uploadHeaders(),
        action: api("file/uploadToWorkSpace"),
        onChange(info) {
            const { status } = info.file;
            if (status === 'uploading') {
                return ;
            }

            const resp = info.file.response;
            const fileUploadSuccess = status === 'done' && resp.code === 200;
            message.success(fileUploadSuccess ? `${info.file.name} 文件上传成功` : `${info.file.name} 文件上传失败`);

            if (fileUploadSuccess) {
                info.file.url = resp.data[0][info.file.name];
            }
        },
        onDrop(e) {
            console.log('Dropped files', e.dataTransfer.files);
        },
        showUploadList: {
            extra: ({ size = 0 }) => (
                <span className="ml-2" style={{ color: '#cccccc' }}>({(size / 1024 / 1024).toFixed(2)}MB)</span>
            ),
            showRemoveIcon: false,
            showDownloadIcon: true
        },
        progress: {
            strokeColor: {
                '0%': '#108ee9',
                '100%': '#87d068',
            },
            strokeWidth: 3,
            format: (percent) => percent && `${parseFloat(percent.toFixed(2))}%`,
        },
    };

    return (
        <div>
            <Dragger {...props}>
                <p className="ant-upload-drag-icon">
                    <InboxOutlined />
                </p>
                <p className="ant-upload-text">点击或拖拽至该区域进行文件上传</p>
            </Dragger>
        </div>
    );
}