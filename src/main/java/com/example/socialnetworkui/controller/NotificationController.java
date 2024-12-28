package com.example.socialnetworkui.controller;

import com.example.socialnetworkui.domain.Entity;
import com.example.socialnetworkui.domain.Friendship;
import com.example.socialnetworkui.domain.Message;
import com.example.socialnetworkui.service.UserService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class NotificationController {
    public ScrollPane scrollPanel;
    private VBox vBox;
    private List<Entity> listofNotifications;
    private UserService userService;

    public void setList(List<Entity> listofNotifications) {
        this.listofNotifications = listofNotifications;
        init();
    }
    public void setService(UserService userService) {
        this.userService = userService;
    }
    @FXML
    public void initialize() {
        vBox = new VBox();
        scrollPanel.setContent(vBox);
    }

    private void init() {
        List<Entity> listofNotificationsCopy = new ArrayList<>(listofNotifications);
        for(int i = 0; i < listofNotificationsCopy.size(); i++) {
            Entity entity = listofNotificationsCopy.get(i);
            if(entity instanceof Friendship) {
                Label label = new Label();
                label.autosize();
                Friendship f = (Friendship) entity;
                label.setText(userService.userById(f.getId().getLeft()).get().getFirstName() + " send you a friend request");
                vBox.getChildren().add(label);
            }
            else {
                Label label = new Label();
                Message m = (Message) entity;
                int contor = 0;
                while(listofNotificationsCopy.remove(m)) {
                    contor++;
                }
                i--;
                if(contor == 1) {
                    label.setText(m.getFrom().getFirstName() + " send you a message");
                }
                else {
                    label.setText(m.getFrom().getFirstName() + " send you " + contor + " messages");
                }
                label.autosize();
                vBox.getChildren().add(label);
            }
        }
    }
}
