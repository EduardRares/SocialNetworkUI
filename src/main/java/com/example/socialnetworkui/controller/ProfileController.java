package com.example.socialnetworkui.controller;

import com.example.socialnetworkui.HelloApplication;
import com.example.socialnetworkui.domain.Entity;
import com.example.socialnetworkui.domain.Message;
import com.example.socialnetworkui.domain.User;
import com.example.socialnetworkui.service.FriendshipService;
import com.example.socialnetworkui.service.MessageService;
import com.example.socialnetworkui.service.UserService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ProfileController {
    @FXML
    ImageView imageView;
    @FXML
    Button FriendRequest;
    @FXML
    Button Message;

    private User user;
    private UserService userService;
    private FriendshipService friendshipService;
    private MessageService messageService;
    private List<User> listofFriends;
    private List<Entity> listofNotifications;
    private javafx.scene.control.Button buttonNofication;

    public void setService(UserService Userservice, FriendshipService friendshipService, MessageService messageService, User user, List<User> listofFriends, List<Entity> listofNotifications, javafx.scene.control.Button buttonNofication) {
        this.userService = Userservice;
        this.friendshipService = friendshipService;
        this.messageService = messageService;
        this.user = user;
        this.listofFriends = listofFriends;
        this.listofNotifications = listofNotifications;
        this.buttonNofication = buttonNofication;
        init();
    }

    private void init() {
        ;
    }

    public void handleText() throws IOException {
        Stage stage = new Stage();
        stage.setTitle("Text");
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("views/NewMessageView.fxml"));
        AnchorPane userLayout = fxmlLoader.load();
        stage.setScene(new Scene(userLayout));
        stage.initModality(Modality.APPLICATION_MODAL);
        MessageController messageController = fxmlLoader.getController();
        messageController.setService(messageService, userService);
        messageController.setReceivers(user, listofFriends);
        if(listofNotifications.size() == 1) {
            com.example.socialnetworkui.domain.Message m = new Message("", listofFriends.getFirst(), null, null);
            while(listofNotifications.remove(m)) {};
            buttonNofication.setText(listofNotifications.size() + " Not.");
        }
        stage.showAndWait();
    }
}
