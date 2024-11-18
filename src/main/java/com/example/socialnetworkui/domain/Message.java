package com.example.socialnetworkui.domain;

import java.time.LocalDateTime;
import java.util.List;

public class Message extends Entity<Long> {
    private User from;
    private List<User> to;
    private String text;
    private LocalDateTime date;
    public Message(String text, User from, LocalDateTime date, List<User> to) {
        this.from = from;
        this.to = to;
        this.text = text;
        this.date = date;
    }

    public User getFrom() {
        return from;
    }

    public List<User> getTo() {
        return to;
    }

    public String getText() {
        return text;
    }

    public LocalDateTime getDate() {
        return date;
    }
    public void addToUser(User user) {
        to.add(user);
    }
}
