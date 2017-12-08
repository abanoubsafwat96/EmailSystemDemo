package com.example.abanoub.emailsystemdemo;

/**
 * Created by Abanoub on 2017-12-03.
 */

public class Email {
    public String sender;
    public String receiver;
    public String title;
    public String body;
    public String date;

    public Email(){}

    public Email(String sender, String receiver, String title, String body, String date) {
        this.sender = sender;
        this.receiver = receiver;
        this.title = title;
        this.body = body;
        this.date = date;
    }
}
