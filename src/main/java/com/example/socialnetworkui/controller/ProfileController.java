package com.example.socialnetworkui.controller;

import com.example.socialnetworkui.HelloApplication;
import com.example.socialnetworkui.domain.*;
import com.example.socialnetworkui.service.FriendshipService;
import com.example.socialnetworkui.service.MessageService;
import com.example.socialnetworkui.service.UserService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ProfileController {
    @FXML
    public Label nameOfUser;
    @FXML
    public TextArea description;
    @FXML
    public
    Button addButton;
    @FXML
    public Button textButton;
    @FXML
    public Label noFriends;
    @FXML
    ImageView imageView;

    private User user;
    private User mainUser;
    private UserService userService;
    private FriendshipService friendshipService;
    private MessageService messageService;
    private List<User> listofFriends;
    private List<Entity> listofNotifications;
    private javafx.scene.control.Button buttonNofication;

    public void setService(UserService Userservice, FriendshipService friendshipService, MessageService messageService, User mainUser, User user, List<User> listofFriends, List<Entity> listofNotifications, javafx.scene.control.Button buttonNofication) {
        this.userService = Userservice;
        this.friendshipService = friendshipService;
        this.messageService = messageService;
        this.mainUser = mainUser;
        this.user = user;
        this.listofFriends = listofFriends;
        this.listofNotifications = listofNotifications;
        this.buttonNofication = buttonNofication;
        init();
    }

    private void init() {
        nameOfUser.setText(user.getFirstName() + " " + user.getLastName());
        description.setText(user.getDescription());
        try {
            Image profilePhoto = new Image(user.getPathToImage());
            imageView.setImage(profilePhoto);
        } catch (Exception e) {
            e.printStackTrace();
        }
        noFriends.setText(String.valueOf(friendshipService.noofFriends(user.getId())) + " friends");
        if(user == mainUser) {
            addButton.setVisible(false);
            textButton.setVisible(false);
        }
        else {
            boolean ok = false;
            for(Long id : friendshipService.FriendsofanId(mainUser.getId())) {
                if(id.equals(user.getId())) {
                    ok = true;
                    break;
                }
            }
            if(ok) {
                addButton.setText("Delete");
            }
            if(!ok) {
                textButton.setVisible(false);
                if(friendshipService.inPending(mainUser.getId(), user.getId())) {
                    addButton.setText("Pending");
                }
                else if(friendshipService.inPending(user.getId(), mainUser.getId())) {
                    addButton.setText("Accept");
                }
                else {
                    addButton.setText("Add");
                }
            }
        }
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
        messageController.setReceivers(mainUser, listofFriends);
        if(!listofNotifications.isEmpty()) {
            com.example.socialnetworkui.domain.Message m = new Message("", listofFriends.getFirst(), null, null);
            while(listofNotifications.remove(m)) {};
            buttonNofication.setText(listofNotifications.size() + " Not.");
        }
        stage.showAndWait();
    }

    @FXML
    public void handleFriends() throws IOException {
        if(addButton.getText().equals("Add")) {
            friendshipService.sendFriendRequest(mainUser.getId(), user.getId());
            addButton.setText("Pending");
        }
        else if(addButton.getText().equals("Accept")) {
            friendshipService.sendFriendRequest(mainUser.getId(), user.getId());
            Friendship d = new Friendship();
            d.setId(new Tuple<>(user.getId(), mainUser.getId()));
            listofNotifications.remove(d);
            buttonNofication.setText(listofNotifications.size() + " Not.");
            addButton.setText("Delete");
            textButton.setVisible(true);
        }
        else if(addButton.getText().equals("Delete")) {
            friendshipService.deleteFriendship(mainUser.getId(), user.getId());
            addButton.setText("Add");
            textButton.setVisible(false);
        }
        else if(addButton.getText().equals("Pending")) {
            friendshipService.deleteFriendship(mainUser.getId(), user.getId());
            addButton.setText("Add");
        }
    }
}
