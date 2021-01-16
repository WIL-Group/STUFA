package com.example.stufa.data_models;

public class Form
{
    String  name,
            surname,
            idNumber,
            studentNumber,
            applicationRefNumber,
            instNumber,
            courseOrProgramme,
            yearOfStudy,
            lastYearOfFunding,
            currentFundingStatus,
            dateOfAppeal;

    Boolean newAppeal, returningStudent, failureToMeetAcademicPerformanceRequirements,
            changeInFinancialCircumstances, lossOfBursarySponsor,
            incorrectAcademicResultsSubmittedResultingInNonrenewalOfFunding,
            iCompletedAGapYearDueToAcademicPerformance, fullNameDeclaration;

    public Form() {
    }

    public Form(String name, String surname, String idNumber, String studentNumber,
                String applicationRefNumber, String instNumber, String courseOrProgramme,
                String yearOfStudy, String lastYearOfFunding, String currentFundingStatus,
                String dateOfAppeal, Boolean newAppeal, Boolean returningStudent,
                Boolean failureToMeetAcademicPerformanceRequirements,
                Boolean changeInFinancialCircumstances,
                Boolean lossOfBursarySponsor,
                Boolean incorrectAcademicResultsSubmittedResultingInNonrenewalOfFunding,
                Boolean iCompletedAGapYearDueToAcademicPerformance, Boolean fullNameDeclaration)
    {
        this.name = name;
        this.surname = surname;
        this.idNumber = idNumber;
        this.studentNumber = studentNumber;
        this.applicationRefNumber = applicationRefNumber;
        this.instNumber = instNumber;
        this.courseOrProgramme = courseOrProgramme;
        this.yearOfStudy = yearOfStudy;
        this.lastYearOfFunding = lastYearOfFunding;
        this.currentFundingStatus = currentFundingStatus;
        this.dateOfAppeal = dateOfAppeal;
        this.newAppeal = newAppeal;
        this.returningStudent = returningStudent;
        this.failureToMeetAcademicPerformanceRequirements = failureToMeetAcademicPerformanceRequirements;
        this.changeInFinancialCircumstances = changeInFinancialCircumstances;
        this.lossOfBursarySponsor = lossOfBursarySponsor;
        this.incorrectAcademicResultsSubmittedResultingInNonrenewalOfFunding = incorrectAcademicResultsSubmittedResultingInNonrenewalOfFunding;
        this.iCompletedAGapYearDueToAcademicPerformance = iCompletedAGapYearDueToAcademicPerformance;
        this.fullNameDeclaration = fullNameDeclaration;
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

    public String getApplicationRefNumber() {
        return applicationRefNumber;
    }

    public void setApplicationRefNumber(String applicationRefNumber) {
        this.applicationRefNumber = applicationRefNumber;
    }

    public String getInstNumber() {
        return instNumber;
    }

    public void setInstNumber(String instNumber) {
        this.instNumber = instNumber;
    }

    public String getCourseOrProgramme() {
        return courseOrProgramme;
    }

    public void setCourseOrProgramme(String courseOrProgramme) {
        this.courseOrProgramme = courseOrProgramme;
    }

    public String getYearOfStudy() {
        return yearOfStudy;
    }

    public void setYearOfStudy(String yearOfStudy) {
        this.yearOfStudy = yearOfStudy;
    }

    public String getLastYearOfFunding() {
        return lastYearOfFunding;
    }

    public void setLastYearOfFunding(String lastYearOfFunding) {
        this.lastYearOfFunding = lastYearOfFunding;
    }

    public String getCurrentFundingStatus() {
        return currentFundingStatus;
    }

    public void setCurrentFundingStatus(String currentFundingStatus) {
        this.currentFundingStatus = currentFundingStatus;
    }

    public String getDateOfAppeal() {
        return dateOfAppeal;
    }

    public void setDateOfAppeal(String dateOfAppeal) {
        this.dateOfAppeal = dateOfAppeal;
    }

    public Boolean getNewAppeal() {
        return newAppeal;
    }

    public void setNewAppeal(Boolean newAppeal) {
        this.newAppeal = newAppeal;
    }

    public Boolean getReturningStudent() {
        return returningStudent;
    }

    public void setReturningStudent(Boolean returningStudent) {
        this.returningStudent = returningStudent;
    }

    public Boolean getFailureToMeetAcademicPerformanceRequirements() {
        return failureToMeetAcademicPerformanceRequirements;
    }

    public void setFailureToMeetAcademicPerformanceRequirements(Boolean failureToMeetAcademicPerformanceRequirements) {
        this.failureToMeetAcademicPerformanceRequirements = failureToMeetAcademicPerformanceRequirements;
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

    public Boolean getIncorrectAcademicResultsSubmittedResultingInNonrenewalOfFunding() {
        return incorrectAcademicResultsSubmittedResultingInNonrenewalOfFunding;
    }

    public void setIncorrectAcademicResultsSubmittedResultingInNonrenewalOfFunding(Boolean incorrectAcademicResultsSubmittedResultingInNonrenewalOfFunding) {
        this.incorrectAcademicResultsSubmittedResultingInNonrenewalOfFunding = incorrectAcademicResultsSubmittedResultingInNonrenewalOfFunding;
    }

    public Boolean getiCompletedAGapYearDueToAcademicPerformance() {
        return iCompletedAGapYearDueToAcademicPerformance;
    }

    public void setiCompletedAGapYearDueToAcademicPerformance(Boolean iCompletedAGapYearDueToAcademicPerformance) {
        this.iCompletedAGapYearDueToAcademicPerformance = iCompletedAGapYearDueToAcademicPerformance;
    }

    public Boolean getFullNameDeclaration() {
        return fullNameDeclaration;
    }

    public void setFullNameDeclaration(Boolean fullNameDeclaration) {
        this.fullNameDeclaration = fullNameDeclaration;
    }
}
