package com.example.socialnetworkui.domain;

import java.time.LocalDateTime;
import java.util.List;

public class ReplyMessage extends Message {
    private Message message;
    public ReplyMessage(User from, List<User> to, String text, LocalDateTime date, Message message) {
        super(text, from, date, to);
        this.message = message;
    }
    public Message getMessage() {
        return message;
    }
}
