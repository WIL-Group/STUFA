package com.example.stufa.data_models;

public class Request
{
    private String rID;
    private String rType;

    public Request() {
    }

    public Request(//String rID,
                   String rType) {
       // this.rID = rID;
        this.rType = rType;
    }

    public String getrID() {
        return rID;
    }

//    public void setrID(String rID) {
//        this.rID = rID;
//    }

    public String getrType() {
        return rType;
    }

    public void setrType(String rType) {
        this.rType = rType;
    }
}
