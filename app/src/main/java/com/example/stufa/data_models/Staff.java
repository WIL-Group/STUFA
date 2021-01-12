package com.example.stufa.data_models;

public class Staff
{
    private String firstName;
    private String lastName;
    private String StaffNumber;

    public Staff(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getStaffNumber() {
        return StaffNumber;
    }

}
