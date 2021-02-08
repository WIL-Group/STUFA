package com.example.stufa.data_models;

public class Student
{
    private String id;

    private String name;

    private String surname;

    private String email;

    private String studentNumber;

    private String fundingType;

    private String bursar;

    private String campus;

    public Student() { }

    public Student(String id, String name, String surname, String email, String studentNumber,
                   String fundingType, String bursar, String campus) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.studentNumber = studentNumber;
        this.fundingType = fundingType;
        this.bursar = bursar;
        this.campus = campus;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }

    public String getFundingType() {
        return fundingType;
    }

    public void setFundingType(String fundingType) {
        this.fundingType = fundingType;
    }

    public String getBursar() {
        return bursar;
    }

    public void setBursar(String bursar) {
        this.bursar = bursar;
    }

    public String getCampus() {
        return campus;
    }

    public void setCampus(String campus) {
        this.campus = campus;
    }
}
