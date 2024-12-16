package com.fastx.ai.llm.platform.tool.utils;

import com.fastx.ai.llm.platform.tool.llm.model.openai.types.OpenAIMessage;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author stark
 */
public class Fields {

    private static boolean isSystemClass(Class<?> clazz) {
        return clazz != null && clazz.getClassLoader() == null;
    }

    private static Field[] getAllFields(Class<?> clazz) {
        List<Field> fieldList = new ArrayList<>();
        while (clazz != null){
            fieldList.addAll(new ArrayList<>(Arrays.asList(clazz.getDeclaredFields())));
            clazz = clazz.getSuperclass();
        }
        Field[] fields = new Field[fieldList.size()];
        return fieldList.toArray(fields);
    }

    // @TODO (stark)  find all sub fields in class with loop.

    public static void main(String[] args) {
        for (Field allField : getAllFields(OpenAIMessage.class)) {
            System.out.println(allField.getName());
            System.out.println(allField.getType());
            System.out.println(allField.getGenericType());
            System.out.println("==========================");
            if (isSystemClass(allField.getType())) {
                // find subClass fields.
                for (Field subField : getAllFields(allField.getType())) {
                    System.out.println(subField.getName());
                    System.out.println(subField.getType());
                    System.out.println(subField.getGenericType());
                    System.out.println("==========================");
                }
            }
        }

    }
}
