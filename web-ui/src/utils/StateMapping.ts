export class StateMapping {

    private static taskMapping: Map<string, string> = new Map()
    private static colorMapping: Map<String, string> = new Map()

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
    }

    static getTaskStatusZh(status: string) {
        return this.taskMapping.get(status) ?? "未知状态: " + status
    }

    static getTaskStatusColor(status: string) {
        return this.colorMapping.get(status) ?? "未知颜色: " + status
    }

}
StateMapping.init();