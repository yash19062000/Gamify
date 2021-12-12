package com.example.gamify;

public class Messages {

    private String from, message, senderID;

    public Messages()
    {

    }

    public Messages(String from, String message, String msgID) {
        this.from = from;
        this.message = message;
        this.senderID = msgID;
    }

    public String getFrom() {
        return from;
    }

    public String getsenderID() {
        return senderID;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
