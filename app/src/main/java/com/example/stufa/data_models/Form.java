package com.example.stufa.data_models;

public class Form
{
    String  id,
            name,
            surname,
            idNumber,
            studentNumber,
            appRef,
            instName,
            course,
            yearOfStudy,
            lastYearOfStudy,
            fundingStatus,
            dateOfAppeal;

    Boolean newApplicant,
            returningStudent,
            failureToMeet,
            changeInFinancialCircumstances,
            lossOfBursarySponsor,
            incorrectAcademicResults,
            gapYear,
            fullNameDeclaration;

    public Form() {
    }

    public Form(String id, String name, String surname, String idNumber, String studentNumber,
                String appRef, String instName, String course, String yearOfStudy,
                String lastYearOfStudy, String fundingStatus, String dateOfAppeal,
                Boolean newApplicant, Boolean returningStudent, Boolean failureToMeet,
                Boolean changeInFinancialCircumstances, Boolean lossOfBursarySponsor,
                Boolean incorrectAcademicResults, Boolean gapYear, Boolean fullNameDeclaration) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.idNumber = idNumber;
        this.studentNumber = studentNumber;
        this.appRef = appRef;
        this.instName = instName;
        this.course = course;
        this.yearOfStudy = yearOfStudy;
        this.lastYearOfStudy = lastYearOfStudy;
        this.fundingStatus = fundingStatus;
        this.dateOfAppeal = dateOfAppeal;
        this.newApplicant = newApplicant;
        this.returningStudent = returningStudent;
        this.failureToMeet = failureToMeet;
        this.changeInFinancialCircumstances = changeInFinancialCircumstances;
        this.lossOfBursarySponsor = lossOfBursarySponsor;
        this.incorrectAcademicResults = incorrectAcademicResults;
        this.gapYear = gapYear;
        this.fullNameDeclaration = fullNameDeclaration;
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

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }

    public String getAppRef() {
        return appRef;
    }

    public void setAppRef(String appRef) {
        this.appRef = appRef;
    }

    public String getInstName() {
        return instName;
    }

    public void setInstName(String instName) {
        this.instName = instName;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getYearOfStudy() {
        return yearOfStudy;
    }

    public void setYearOfStudy(String yearOfStudy) {
        this.yearOfStudy = yearOfStudy;
    }

    public String getLastYearOfStudy() {
        return lastYearOfStudy;
    }

    public void setLastYearOfStudy(String lastYearOfStudy) {
        this.lastYearOfStudy = lastYearOfStudy;
    }

    public String getFundingStatus() {
        return fundingStatus;
    }

    public void setFundingStatus(String fundingStatus) {
        this.fundingStatus = fundingStatus;
    }

    public String getDateOfAppeal() {
        return dateOfAppeal;
    }

    public void setDateOfAppeal(String dateOfAppeal) {
        this.dateOfAppeal = dateOfAppeal;
    }

    public Boolean getNewApplicant() {
        return newApplicant;
    }

    public void setNewApplicant(Boolean newApplicant) {
        this.newApplicant = newApplicant;
    }

    public Boolean getReturningStudent() {
        return returningStudent;
    }

    public void setReturningStudent(Boolean returningStudent) {
        this.returningStudent = returningStudent;
    }

    public Boolean getFailureToMeet() {
        return failureToMeet;
    }

    public void setFailureToMeet(Boolean failureToMeet) {
        this.failureToMeet = failureToMeet;
    }

    public Boolean getChangeInFinancialCircumstances() {
        return changeInFinancialCircumstances;
    }

    public void setChangeInFinancialCircumstances(Boolean changeInFinancialCircumstances) {
        this.changeInFinancialCircumstances = changeInFinancialCircumstances;
    }

    public Boolean getLossOfBursarySponsor() {
        return lossOfBursarySponsor;
    }

    public void setLossOfBursarySponsor(Boolean lossOfBursarySponsor) {
        this.lossOfBursarySponsor = lossOfBursarySponsor;
    }

    public Boolean getIncorrectAcademicResults() {
        return incorrectAcademicResults;
    }

    public void setIncorrectAcademicResults(Boolean incorrectAcademicResults) {
        this.incorrectAcademicResults = incorrectAcademicResults;
    }

    public Boolean getGapYear() {
        return gapYear;
    }

    public void setGapYear(Boolean gapYear) {
        this.gapYear = gapYear;
    }

    public Boolean getFullNameDeclaration() {
        return fullNameDeclaration;
    }

    public void setFullNameDeclaration(Boolean fullNameDeclaration) {
        this.fullNameDeclaration = fullNameDeclaration;
    }
}
