package com.example.stufa.data_models;

public class Booking {


    String studentNumber;
    String bType;
    String date;

    public Booking() {
    }

    public Booking( String studentNumber, String bType, String date) {

        this.studentNumber = studentNumber;
        this.bType = bType;
        this.date = date;
    }


    public String getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }

    public String getbType() {
        return bType;
    }

    public void setbType(String bType) {
        this.bType = bType;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
