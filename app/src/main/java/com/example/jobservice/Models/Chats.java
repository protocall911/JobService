package com.example.jobservice.Models;

public class Chats {

    private String emailFrom, emailTo, message;

    public Chats(String emailFrom, String emailTo, String message){
        this.emailFrom = emailFrom;
        this.emailTo = emailTo;
        this.message = message;
    }
    public Chats(){

    }

    public String getEmailFrom() {
        return emailFrom;
    }

    public void setEmailFrom(String emailFrom) {
        this.emailFrom = emailFrom;
    }

    public String getEmailTo() {
        return emailTo;
    }

    public void setEmailTo(String emailTo) {
        this.emailTo = emailTo;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
