package com.example.stufa.data_models;

public class Query {

    String type;
    String message;
    String qId;

    public Query(String type, String message, String qid) {
        this.type = type;
        this.message = message;
        this.qId = qid;

    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getqId() {
        return qId;
    }

    public void setqId(String qId) {
        this.qId = qId;
    }

    public Query() {
    }
}
