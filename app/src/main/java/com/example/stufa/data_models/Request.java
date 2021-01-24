package com.example.stufa.data_models;

public class Request
{
    //private String id;
    private boolean isAnswered;
    private String date;
    private boolean financiallyCleared;
    private String requestType;
    private String studNum;
    private String staffEmail;
    private String studEmail;

    public Request() {
    }

    public Request(/*String id, */boolean isAnswered, String date, boolean financiallyCleared,
                                  String requestType, String studNum , String staffEmail, String studEmail) {
        //this.id = id;
        this.isAnswered = isAnswered;
        this.date = date;
        this.financiallyCleared = financiallyCleared;
        this.requestType = requestType;
        this.studNum = studNum;
        this.staffEmail = staffEmail;
        this.studEmail = studEmail;
    }

//    public String getId() {
//        return id;
//    }
//
//    public void setId(String id) {
//        this.id = id;
//    }

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

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public boolean isFinanciallyCleared() {
        return financiallyCleared;
    }

    public void setFinanciallyCleared(boolean financiallyCleared) {
        this.financiallyCleared = financiallyCleared;
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

    public String getStudEmail() {
        return studEmail;
    }

    public void setStudEmail(String studEmail) {
        this.studEmail = studEmail;
    }
}
