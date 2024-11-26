package com.raktatech.winzip.model;

public class Language {
    private String code;
    private String name;

    public Language(String code, String name) {
        this.code = code;
        this.name = name;
    }

    // Getter for language code
    public String getCode() {
        return code;
    }

    // Getter for language name
    public String getName() {
        return name;
    }
}
