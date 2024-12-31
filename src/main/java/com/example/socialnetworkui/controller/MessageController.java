package com.example.socialnetworkui.controller;

import com.example.socialnetworkui.domain.Message;
import com.example.socialnetworkui.domain.User;
import com.example.socialnetworkui.service.MessageService;
import com.example.socialnetworkui.service.UserService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

import java.awt.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MessageController {
    public javafx.scene.control.TextArea textField;
    public javafx.scene.control.Label Label;
    public ScrollPane scrollPanel;
    private MessageService messageService;
    private UserService userService;
    private User user;
    private List<User> receivers;
    private VBox vBox;


    public void setService(MessageService service, UserService userService) {
        this.messageService = service;
        this.userService = userService;
    }

    public void setReceivers(User sender, List<User> receivers) {
        this.user = sender;
        this.receivers = receivers;
        init();
    }

    @FXML
    public void init() {
        StringBuilder text = new StringBuilder("to: ");
        for (User receiver : receivers) {
            text.append(receiver.getFirstName()).append(" ").append(receiver.getLastName()).append("   ");
        }
        Label.setText(text.toString());

        vBox = new VBox();
        scrollPanel.setContent(vBox);
        scrollPanel.setVvalue(1.0);
        if(receivers.size() == 1) {
            messageService.markRead(receivers.getFirst().getId(), user.getId());
            List<Message> messages = new ArrayList<>();
            for(Message message : messageService.findAllbyUser(user.getId())) {
                if(message.getTo().contains(receivers.getFirst())) {
                    messages.add(message);
                }
            }
            for(Message message : messageService.findAllbyUser(receivers.getFirst().getId())) {
                if(message.getTo().contains(user)) {
                    messages.add(message);
                }
            }
            if(!messages.isEmpty()) {
                Comparator<Message> comparator = (x, y) -> x.getDate().compareTo(y.getDate());
                messages.sort(comparator);
                for (Message message : messages) {
                    Label mess = new Label();
                    mess.setText(message.getFrom().getFirstName() + ": " + message.getText() + "\n");
                    vBox.getChildren().add(mess);
                }
            }
        }
    }

    private void addtoPanel(Message message) {
        Label mess = new Label();
        mess.setText(message.getFrom().getFirstName() + ": " + message.getText() + "\n");
        vBox.getChildren().add(mess);
    }

    @FXML
    public void handleSend() {
        Message m = new Message(textField.getText(), user, LocalDateTime.now(), receivers);
        messageService.addMessage(m);
        addtoPanel(m);
        textField.setText("");
    }
}
