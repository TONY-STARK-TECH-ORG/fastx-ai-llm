package com.fastx.ai.llm.domains.test;

import com.alibaba.fastjson.JSON;
import com.fastx.ai.llm.domains.FastLlmDomainApplication;
import com.fastx.ai.llm.domains.api.IDubboTaskService;
import com.fastx.ai.llm.domains.dto.TaskNodeExecDTO;
import jakarta.annotation.Resource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@SpringBootTest(classes = FastLlmDomainApplication.class)
@RunWith(SpringRunner.class)
public class TaskNodeExecServiceTest {

    @Resource
    IDubboTaskService taskService;

    @Test
    public void test() {
        // 获取任务节点执行列表
        boolean parentTaskNodeFinished = taskService.isParentTaskNodeFinished("0");
        System.out.println(parentTaskNodeFinished);
    }

    @Test
    public void test2() {
        // 获取任务节点执行列表
        List<TaskNodeExecDTO> nodes = taskService.getParentChainTaskNodeExecByNodeId("2");
        System.out.println(JSON.toJSONString(nodes));
    }

    @Test
    public void test3() {
        // 获取任务节点执行列表
        Boolean taskExecNodeFinished = taskService.isTaskExecNodeFinished(2L);
        System.out.println(taskExecNodeFinished);
    }
}
