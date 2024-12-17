package com.fastx.ai.llm.platform.exec.workflow;

import com.alibaba.fastjson2.JSON;
import org.springframework.context.expression.MapAccessor;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.Map;

/**
 * @author stark
 */
public class Spel {

    public static void main(String[] args) {
        String json = "{\"inputs\":{\"modelId\":\"start.outputs.modelId\",\"messages\":[{\"role\":\"start.outputs[0].role\",\"content\":\"start.outputs[0].content\"}]},\"start\":{\"outputs\":{\"modelId\":\"gpt-4o-mini\",\"messages\":[{\"role\":\"user\",\"content\":\"hi\"},{\"role\":\"user\",\"content\":\"hi\"}]},\"inputs\":{\"modelId\":\"gpt-4o-mini\",\"messages\":[{\"role\":\"user\",\"content\":\"hi\"},{\"role\":\"user\",\"content\":\"hi\"}]}}}";

        ExpressionParser parser = new SpelExpressionParser();

        StandardEvaluationContext context = new StandardEvaluationContext();

        Map<String, Object> innerData = JSON.parseObject(json, Map.class);
        context.addPropertyAccessor(new MapAccessor());
        context.setVariable("context", innerData);

        Expression exp = parser.parseExpression("#context.start.outputs.modelId");
        System.out.println(exp.getValue(context));

    }

}
