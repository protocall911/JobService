package com.example.jobservice.Models;

public class MessagesList {
    private String username, lastMessage;

    public MessagesList(String username, String lastMessage) {
        this.username = username;
        this.lastMessage = lastMessage;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }
}
