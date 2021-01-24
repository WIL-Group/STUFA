package com.example.stufa.data_models;

public class Form
{
    String  Name,
            Surname,
            IDNumber,
            StudentNumber,
            AppRef,
            InstName,
            Course,
            YearOfStudy,
            LastYearOfStudy,
            FundingStatus,
            DateOfAppeal;

    Boolean newAppeal, returningStudent, failureToMeetAcademicPerformanceRequirements,
            changeInFinancialCircumstances, lossOfBursarySponsor,
            incorrectAcademicResultsSubmittedResultingInNonrenewalOfFunding,
            iCompletedAGapYearDueToAcademicPerformance, fullNameDeclaration;

    public Form() {
    }

    public Form(String name, String surname, String IDNumber, String studentNumber, String appRef,
                String instName, String course, String yearOfStudy, String lastYearOfStudy,
                String fundingStatus, String dateOfAppeal, Boolean newAppeal, Boolean returningStudent,
                Boolean failureToMeetAcademicPerformanceRequirements, Boolean changeInFinancialCircumstances,
                Boolean lossOfBursarySponsor, Boolean incorrectAcademicResultsSubmittedResultingInNonrenewalOfFunding,
                Boolean iCompletedAGapYearDueToAcademicPerformance, Boolean fullNameDeclaration)
    {
        Name = name;
        Surname = surname;
        this.IDNumber = IDNumber;
        StudentNumber = studentNumber;
        AppRef = appRef;
        InstName = instName;
        Course = course;
        YearOfStudy = yearOfStudy;
        LastYearOfStudy = lastYearOfStudy;
        FundingStatus = fundingStatus;
        DateOfAppeal = dateOfAppeal;
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
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getSurname() {
        return Surname;
    }

    public void setSurname(String surname) {
        Surname = surname;
    }

    public String getIDNumber() {
        return IDNumber;
    }

    public void setIDNumber(String IDNumber) {
        this.IDNumber = IDNumber;
    }

    public String getStudentNumber() {
        return StudentNumber;
    }

    public void setStudentNumber(String studentNumber) {
        StudentNumber = studentNumber;
    }

    public String getAppRef() {
        return AppRef;
    }

    public void setAppRef(String appRef) {
        AppRef = appRef;
    }

    public String getInstName() {
        return InstName;
    }

    public void setInstName(String instName) {
        InstName = instName;
    }

    public String getCourse() {
        return Course;
    }

    public void setCourse(String course) {
        Course = course;
    }

    public String getYearOfStudy() {
        return YearOfStudy;
    }

    public void setYearOfStudy(String yearOfStudy) {
        YearOfStudy = yearOfStudy;
    }

    public String getLastYearOfStudy() {
        return LastYearOfStudy;
    }

    public void setLastYearOfStudy(String lastYearOfStudy) {
        LastYearOfStudy = lastYearOfStudy;
    }

    public String getFundingStatus() {
        return FundingStatus;
    }

    public void setFundingStatus(String fundingStatus) {
        FundingStatus = fundingStatus;
    }

    public String getDateOfAppeal() {
        return DateOfAppeal;
    }

    public void setDateOfAppeal(String dateOfAppeal) {
        DateOfAppeal = dateOfAppeal;
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
