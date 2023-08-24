package com.fodala.pojo;

public class ErrorLog {
    private Integer rowId;
    private String message;
    private String type;
    private String fileName;
    private String username;
    private String created;

    public ErrorLog() {
    }

    public ErrorLog(Integer rowId, String message, String type, String fileName, String username, String created) {
        this.rowId = rowId;
        this.message = message;
        this.type = type;
        this.fileName = fileName;
        this.username = username;
        this.created = created;
    }

    public Integer getRowId() {
        return rowId;
    }

    public void setRowId(Integer rowId) {
        this.rowId = rowId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }
}
