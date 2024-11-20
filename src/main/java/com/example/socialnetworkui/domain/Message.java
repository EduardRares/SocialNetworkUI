package com.example.socialnetworkui.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Message)) return false;
        Message other = (Message) obj;
        if(this.date == null && other.date != null) return false;
        if(this.date == null) return this.text.equals(other.text) && this.from.equals(other.from);
        return this.text.equals(other.text) && this.from.equals(other.from) && this.date.equals(other.date);
    }
}
