package com.example.stufa.data_models;

public class Query {

    String qId;
    boolean isAnswered;
    String staffEmail;
    String queryMessage;
    String queryType;
    String date;
    String studNum;
    String queryResponseMessage;

    public Query() {
    }

    public Query(String qId, boolean isAnswered, String staffEmail, String queryMessage, String queryType,
                 String date, String studNum, String queryResponseMessage) {
        this.qId = qId;
        this.isAnswered = isAnswered;
        this.staffEmail = staffEmail;
        this.queryMessage = queryMessage;
        this.queryType = queryType;
        this.date = date;
        this.studNum = studNum;
        this.queryResponseMessage = queryResponseMessage;
    }

    public String getqId() {
        return qId;
    }

    public void setqId(String qId) {
        this.qId = qId;
    }

    public boolean isAnswered() {
        return isAnswered;
    }

    public void setAnswered(boolean answered) {
        isAnswered = answered;
    }

    public String getStaffEmail() {
        return staffEmail;
    }

    public void setStaffEmail(String staffEmail) {
        this.staffEmail = staffEmail;
    }

    public String getQueryMessage() {
        return queryMessage;
    }

    public void setQueryMessage(String queryMessage) {
        this.queryMessage = queryMessage;
    }

    public String getQueryType() {
        return queryType;
    }

    public void setQueryType(String queryType) {
        this.queryType = queryType;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStudNum() {
        return studNum;
    }

    public void setStudNum(String studNum) {
        this.studNum = studNum;
    }

    public String getQueryResponseMessage() {
        return queryResponseMessage;
    }

    public void setQueryResponseMessage(String queryResponseMessage) {
        this.queryResponseMessage = queryResponseMessage;
    }
}
