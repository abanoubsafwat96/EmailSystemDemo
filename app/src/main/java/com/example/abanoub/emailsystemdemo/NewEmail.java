package com.example.abanoub.emailsystemdemo;

/**
 * Created by Abanoub on 2017-12-03.
 */

public class NewEmail {
    public String sender;
    public String receiver;
    public String title;
    public String body;
    public String date;
    public String pushID;


    public NewEmail(){}

    public NewEmail(String sender, String receiver, String title, String body, String date, String pushID) {
        this.sender = sender;
        this.receiver = receiver;
        this.title = title;
        this.body = body;
        this.date = date;
        this.pushID=pushID;
    }
}
