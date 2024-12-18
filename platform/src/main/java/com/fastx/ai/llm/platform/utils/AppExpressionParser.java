package com.fastx.ai.llm.platform.utils;

import com.alibaba.fastjson2.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.POJONode;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.Iterator;
import java.util.Map;

/**
 * @author stark
 */
public class AppExpressionParser {

    @FunctionalInterface
    public interface InnerProcessor {
        /**
         * process str
         * @param input i
         * @param context c
         * @param parser p
         * @return o
         */
        Object process(ExpressionParser parser, StandardEvaluationContext context, String input);
    }

    public static InnerProcessor processor = (parser, context, input) -> {
        try {
            return parser.parseExpression("#" + input).getValue(context, Object.class);
        } catch (Exception e) {
            return input;
        }
    };

    public static Map<String, Object> getInputs(
            ExpressionParser parser, StandardEvaluationContext _context, String _inputs) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonTree = mapper.valueToTree(JSON.parseObject(_inputs, Map.class));
        expressionFields(jsonTree, processor, parser, _context);
        return mapper.treeToValue(jsonTree, Map.class);
    }

    public static void expressionFields(
            JsonNode node, InnerProcessor processor, ExpressionParser parser, StandardEvaluationContext _context) {
        if (node == null) {
            return ;
        }
        if (node.isObject()) {
            ObjectNode objectNode = (ObjectNode) node;
            Iterator<String> fieldNames = objectNode.fieldNames();
            while (fieldNames.hasNext()) {
                String fieldName = fieldNames.next();
                JsonNode childNode = objectNode.get(fieldName);
                if (childNode.isTextual()) {
                    String oldValue = childNode.asText();
                    Object newValue = processor.process(parser, _context, oldValue);
                    objectNode.set(fieldName, new POJONode(newValue));
                } else {
                    expressionFields(childNode, processor, parser, _context);
                }
            }
        }
        else if (node.isArray()) {
            for (JsonNode item : node) {
                expressionFields(item, processor, parser, _context);
            }
        }
    }

}
