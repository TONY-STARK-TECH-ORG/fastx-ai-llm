package com.fastx.ai.llm.domains.test;

import com.fastx.ai.llm.domains.FastLlmDomainApplication;
import com.fastx.ai.llm.domains.entity.User;
import com.fastx.ai.llm.domains.service.IUserService;
import jakarta.annotation.Resource;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author stark
 */
@SpringBootTest(classes = FastLlmDomainApplication.class)
@RunWith(SpringRunner.class)
public class springBootApplicationTester {

    @Resource
    private IUserService userService;

    @Test
    public void conflictTime() {
        List<User> userList = userService.list();
        assertThat(userList, Matchers.empty());
    }
}
