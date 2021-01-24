package com.example.stufa.data_models;


public class Booking {

    //String id;
    String reason;
    boolean attendedTo;
    int bookingNum;
    String date;
    String studNum;
    String studEmail;


    public Booking()
    {
    }

    public Booking(/*String id,*/ String reason, boolean attendedTo, int bookingNum, String date, String studNum
                   , String studEmail)
    {
        //this.id = id;
        this.reason = reason;
        this.attendedTo = attendedTo;
        this.bookingNum = bookingNum;
        this.date = date;
        this.studNum = studNum;
        this.studEmail = studEmail;
    }

    public String getStudEmail() {
        return studEmail;
    }

    public void setStudEmail(String studEmail) {
        this.studEmail = studEmail;
    }

//    public String getId() {
//        return id;
//    }
//
//    public void setId(String id) {
//        this.id = id;
//    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public boolean isAttendedTo() {
        return attendedTo;
    }

    public void setAttendedTo(boolean attendedTo) {
        this.attendedTo = attendedTo;
    }

    public int getBookingNum() {
        return bookingNum;
    }

    public void setBookingNum(int bookingNum) {
        this.bookingNum = bookingNum;
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
}
