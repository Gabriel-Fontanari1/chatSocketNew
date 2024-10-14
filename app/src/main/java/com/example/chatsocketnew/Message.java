package com.example.chatsocketnew;

public class Message {
    private String content;
    private boolean isSentByUser;
    private String username;

    public Message(String content, boolean isSentByUser, String username) {
        this.content = content;
        this.isSentByUser = isSentByUser;
        this.username = username;
    }

    public String getContent() {
        return content;
    }

    public boolean isSentByUser() {
        return isSentByUser;
    }

    public String getUsername() {
        return username;
    }
}
