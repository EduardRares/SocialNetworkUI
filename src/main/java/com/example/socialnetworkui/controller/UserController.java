package com.example.socialnetworkui.controller;

import com.example.socialnetworkui.domain.User;
import com.example.socialnetworkui.service.FriendshipService;
import com.example.socialnetworkui.service.UserService;
import com.example.socialnetworkui.utils.observer.Observer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.util.Optional;

public class UserController {
    private Stage dialogStage;
    private Stage loginStage;
    private User user;
    private User friend;
    private UserService userService;
    private FriendshipService friendshipService;
    ObservableList<User> model = FXCollections.observableArrayList();

    @FXML
    TableView<User> tableViewFriends;
    @FXML
    TableColumn<User,String> tableColumnFirstNameFriends;
    @FXML
    TableColumn<User,String> tableColumnLastNameFriends;
    @FXML
    Button buttonAdd;
    @FXML
    Button buttonDelete;

    public void setService(UserService Userservice, FriendshipService friendshipService, User user) {
        this.userService = Userservice;
        this.friendshipService = friendshipService;
        this.user = user;
        friend = null;
    }
    public void setStages(Stage dialogStage, Stage loginStage) {
        this.dialogStage = dialogStage;
        this.loginStage = loginStage;
        buttonAdd.setVisible(false);
        buttonDelete.setVisible(false);
        dialogStage.setTitle("Social Network");
        dialogStage.show();
    }

    @FXML
    public void initialize() {
        tableColumnFirstNameFriends.setCellValueFactory(new PropertyValueFactory<User, String>("firstName"));
        tableColumnLastNameFriends.setCellValueFactory(new PropertyValueFactory<User, String>("lastName"));
        tableViewFriends.getSelectionModel().getSelectedItem();
        tableViewFriends.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            friend = (User) tableViewFriends.getSelectionModel().getSelectedItem();
        });
    }

    @FXML
    public void handleFriends() {
        model.clear();
        for(Long id : friendshipService.FriendsofanId(user.getId())) {
            Optional<User> user = userService.userById(id);
            user.ifPresent(value -> model.add(value));
        }
        tableViewFriends.setItems(model);
        buttonDelete.setVisible(true);
        buttonAdd.setVisible(false);
        friend = null;
    }

    @FXML
    public void handleFriendRequest() {
        model.clear();
        for(Long id : friendshipService.friendRequests(user.getId())) {
            Optional<User> user = userService.userById(id);
            user.ifPresent(value -> model.add(value));
        }
        tableViewFriends.setItems(model);
        buttonDelete.setVisible(true);
        buttonAdd.setVisible(true);
        friend = null;
    }

    @FXML
    public void handleAddFriends() {
        model.clear();
        for(User u : userService.getAll())
        {
            if(u.getId() != user.getId()) {
                boolean ok = true;
                for(Long id : friendshipService.FriendsofanId(user.getId())) {
                    if(id == u.getId()) {
                        ok = false;
                    }
                }
                if(ok) {
                    if(!friendshipService.inPending(user.getId(), u.getId()))
                        model.add(u);
                }
            }
        }
        tableViewFriends.setItems(model);
        buttonAdd.setVisible(true);
        buttonDelete.setVisible(false);
        friend = null;
    }

    @FXML
    public void handleAdd() {
        if(!buttonDelete.isVisible()) {
            friendshipService.sendFriendRequest(user.getId(), friend.getId());
            handleAddFriends();
        }
        else {
            friendshipService.sendFriendRequest(user.getId(), friend.getId());
            handleFriendRequest();
        }
        friend = null;
    }

    @FXML
    public void handleDelete() {
        if(!buttonAdd.isVisible()) {
            friendshipService.deleteFriendship(user.getId(), friend.getId());
            handleFriends();
        }
        else {
            friendshipService.deleteFriendship(user.getId(), friend.getId());
            handleFriendRequest();
        }
        friend = null;
    }

    public void handleLogout() {
        dialogStage.close();
        loginStage.show();
    }
}
