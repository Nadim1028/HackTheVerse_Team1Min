package com.example.team1min.ChatBox;

public class Chat {
    private String sender;
    private String message;
    private String name;

    public Chat() {
    }

    public Chat(String sender, String message, String name) {
        this.sender = sender;
        this.message = message;
        this.name = name;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
