package com.example.socialnetworkui.controller;

import com.example.socialnetworkui.HelloApplication;
import com.example.socialnetworkui.domain.User;
import com.example.socialnetworkui.service.FriendshipService;
import com.example.socialnetworkui.service.MessageService;
import com.example.socialnetworkui.service.UserService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserController {
    public Button buttonText;
    private Stage dialogStage;
    private Stage loginStage;
    private User user;
    private User friend;
    private List<User> listofFriends;
    private UserService userService;
    private FriendshipService friendshipService;
    private MessageService messageService;
    ObservableList<User> model = FXCollections.observableArrayList();
    ObservableList<User> modelMessageFriends = FXCollections.observableArrayList();

    @FXML
    TableView<User> tableViewFriends;
    @FXML
    TableView<User> tableViewFriends1;
    @FXML
    TableColumn<User,String> tableColumnFirstNameFriends;
    @FXML
    TableColumn<User,String> tableColumnLastNameFriends;
    @FXML
    TableColumn<User,String> tableColumnFirstNameFriends1;
    @FXML
    TableColumn<User,String> tableColumnLastNameFriends1;
    @FXML
    Button buttonAdd;
    @FXML
    Button buttonDelete;

    public void setService(UserService Userservice, FriendshipService friendshipService, MessageService messageService, User user) {
        this.userService = Userservice;
        this.friendshipService = friendshipService;
        this.messageService = messageService;
        this.user = user;
        friend = null;
        listofFriends = new ArrayList<>();
    }
    public void setStages(Stage dialogStage, Stage loginStage) {
        this.dialogStage = dialogStage;
        this.loginStage = loginStage;
        dialogStage.setTitle("Social Network");
        dialogStage.show();
    }

    @FXML
    public void initialize() {
        buttonAdd.setVisible(false);
        buttonDelete.setVisible(false);
        buttonText.setVisible(false);
        tableViewFriends1.setVisible(false);
        tableViewFriends.setVisible(false);
        tableColumnFirstNameFriends1.setCellValueFactory(new PropertyValueFactory<User, String>("firstName"));
        tableColumnLastNameFriends1.setCellValueFactory(new PropertyValueFactory<User, String>("lastName"));
        tableViewFriends.getSelectionModel().getSelectedItem();
        tableViewFriends.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            friend = (User) tableViewFriends.getSelectionModel().getSelectedItem();
        });

        tableColumnFirstNameFriends.setCellValueFactory(new PropertyValueFactory<User, String>("firstName"));
        tableColumnLastNameFriends.setCellValueFactory(new PropertyValueFactory<User, String>("lastName"));
        tableViewFriends1.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        tableViewFriends1.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            listofFriends = tableViewFriends1.getSelectionModel().getSelectedItems();
        });
    }

    @FXML
    public void handleFriends() {
        tableViewFriends1.setVisible(false);
        tableViewFriends.setVisible(true);
        model.clear();
        for(Long id : friendshipService.FriendsofanId(user.getId())) {
            Optional<User> user = userService.userById(id);
            user.ifPresent(value -> model.add(value));
        }
        tableViewFriends.setItems(model);
        buttonDelete.setVisible(true);
        buttonAdd.setVisible(false);
        buttonText.setVisible(false);
        friend = null;
    }

    @FXML
    public void handleFriendRequest() {
        tableViewFriends1.setVisible(false);
        tableViewFriends.setVisible(true);
        model.clear();
        for(Long id : friendshipService.friendRequests(user.getId())) {
            Optional<User> user = userService.userById(id);
            user.ifPresent(value -> model.add(value));
        }
        tableViewFriends.setItems(model);
        buttonDelete.setVisible(true);
        buttonAdd.setVisible(true);
        buttonText.setVisible(false);
        friend = null;
    }

    @FXML
    public void handleAddFriends() {
        tableViewFriends1.setVisible(false);
        tableViewFriends.setVisible(true);
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
        buttonText.setVisible(false);
        friend = null;
    }

    @FXML
    public void handleMessageFriend() {
        tableViewFriends1.setVisible(true);
        tableViewFriends.setVisible(false);
        modelMessageFriends.clear();
        for(Long id : friendshipService.FriendsofanId(user.getId())) {
            Optional<User> user = userService.userById(id);
            user.ifPresent(value -> modelMessageFriends.add(value));
        }
        tableViewFriends1.setItems(modelMessageFriends);
        buttonAdd.setVisible(false);
        buttonDelete.setVisible(false);
        buttonText.setVisible(true);
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
        stage.showAndWait();
    }
}
