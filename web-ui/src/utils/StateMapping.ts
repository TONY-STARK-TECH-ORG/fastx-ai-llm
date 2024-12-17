export class StateMapping {

    private static taskMapping: Map<string, string> = new Map()
    private static colorMapping: Map<String, string> = new Map()
    private static toolMapping: Map<String, string> = new Map()

    static init() {
        this.taskMapping.set("wait", "等待中")
        this.taskMapping.set("running", "执行中")
        this.taskMapping.set("complete", "完成")
        this.taskMapping.set("active", "激活")
        this.taskMapping.set("inactive", "未激活")
        this.taskMapping.set("finish", "完成")

        this.colorMapping.set("wait", "#FF6A00")
        this.colorMapping.set("running", "#00AA00")
        this.colorMapping.set("complete", "#005100")
        this.colorMapping.set("active", "#00AA00")
        this.colorMapping.set("inactive", "#AA0000")
        this.colorMapping.set("finish", "#00AA00")

        this.toolMapping.set("llm-model", "LLM 模型")
        this.toolMapping.set("llm-function", "LLM 函数")
        this.toolMapping.set("llm-tool", "LLM 工具")
        this.toolMapping.set("train-dataset", "数据预处理")
        this.toolMapping.set("train-post", "后处理")
        this.toolMapping.set("train-upload", "上传/下载")
        this.toolMapping.set("train-tokenizer", "Tokenizer")
        this.toolMapping.set("train-finetune", "微调")
        this.toolMapping.set("train-hyperParams", "超参数")
        this.toolMapping.set("train-benchmark", "评测")
        this.toolMapping.set("other", "其他")
    }

    static getToolName(key: string) {
        return this.toolMapping.get(key) ?? "未知工具: " + key
    }

    static getTaskStatusZh(status: string) {
        return this.taskMapping.get(status) ?? "未知状态: " + status
    }

    static getTaskStatusColor(status: string) {
        return this.colorMapping.get(status) ?? "未知颜色: " + status
    }

}
StateMapping.init();