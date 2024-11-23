package com.example.dynamicform.models;

public class FormField {
    private String type;
    private String label;
    private boolean required;
    private String value;

    public FormField(String type, String label, boolean required) {
        this.type = type;
        this.label = label;
        this.required = required;
        this.value = "";
    }

    public String getType() { return type; }
    public String getLabel() { return label; }
    public boolean isRequired() { return required; }
    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }
}