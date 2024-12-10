package com.fastx.ai.llm.platform.tool.entity;

import java.io.Serializable;

/**
 * @author stark
 */
public class Fields implements Serializable {

    private String name;
    private Class type;
    private boolean required = true;
    private boolean isArray = false;

    /**
     * JSON Value
     */
    private String value;

    /**
     * Parsed Value
     */
    private Object execValue;

    public Fields() {
    }

    public static Fields of(String name, Class type) {
        return Fields.of(name, type, true);
    }

    public static Fields ofArray(String name, Class type) {
        return Fields.ofArray(name, type, true);
    }

    public static Fields of(String name, Class type, boolean required) {
        return Fields.of(name, type, required, null);
    }

    public static Fields ofArray(String name, Class type, boolean required) {
        return Fields.ofArray(name, type, required, null);
    }

    public static Fields of(String name, Class type, String value) {
        return Fields.of(name, type, true, value);
    }

    public static Fields ofArray(String name, Class type, String value) {
        return Fields.ofArray(name, type, true, value);
    }

    public static Fields of(String name, Class type, boolean required, String value) {
        Fields fields = new Fields();
        fields.name = name;
        fields.type = type;
        fields.required = required;
        fields.value = value;
        return fields;
    }

    public static Fields ofArray(String name, Class type, boolean required, String value) {
        Fields fields = new Fields();
        fields.name = name;
        fields.type = type;
        fields.required = required;
        fields.isArray = true;
        fields.value = value;
        return fields;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Class getType() {
        return type;
    }

    public void setType(Class type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public boolean isArray() {
        return isArray;
    }

    public void setArray(boolean array) {
        isArray = array;
    }

    public Object getExecValue() {
        return execValue;
    }

    public void setExecValue(Object execValue) {
        this.execValue = execValue;
    }
}
