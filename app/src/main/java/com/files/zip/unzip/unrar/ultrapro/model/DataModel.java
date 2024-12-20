package com.files.zip.unzip.unrar.ultrapro.model;

public class DataModel {
    boolean check;
    String name;
    String path;
    String size;
    String time;
    boolean isCheckboxVisible;
    private boolean isPasswordProtected; // New field

    public DataModel(String str, String str2, String str3, String str4, boolean z, boolean isPasswordProtected) {
        this.name = str;
        this.path = str2;
        this.size = str3;
        this.time = str4;
        this.check = z;
        this.isCheckboxVisible = false;
        this.isPasswordProtected = isPasswordProtected;
    }

    public boolean isPasswordProtected() {
        return isPasswordProtected;
    }

    public void setPasswordProtected(boolean passwordProtected) {
        isPasswordProtected = passwordProtected;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String str) {
        this.name = str;
    }

    public String getPath() {
        return this.path;
    }

    public void setPath(String str) {
        this.path = str;
    }

    public String getSize() {
        return this.size;
    }

    public void setSize(String str) {
        this.size = str;
    }

    public String getTime() {
        return this.time;
    }

    public void setTime(String str) {
        this.time = str;
    }

    public boolean isCheck() {
        return this.check;
    }

    public void setCheck(boolean z) {
        this.check = z;
    }
    public boolean isCheckboxVisible() {
        return this.isCheckboxVisible;
    }

    public void setCheckboxVisible(boolean isCheckboxVisible) {
        this.isCheckboxVisible = isCheckboxVisible;
    }

}
