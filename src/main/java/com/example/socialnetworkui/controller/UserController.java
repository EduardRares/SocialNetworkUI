package com.example.socialnetworkui.controller;

import com.example.socialnetworkui.HelloApplication;
import com.example.socialnetworkui.domain.*;
import com.example.socialnetworkui.service.FriendshipService;
import com.example.socialnetworkui.service.MessageService;
import com.example.socialnetworkui.service.UserService;
import com.example.socialnetworkui.utils.events.ChangeEventType;
import com.example.socialnetworkui.utils.events.EntityChangeEvent;
import com.example.socialnetworkui.utils.observer.Observer;
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

public class UserController implements Observer<EntityChangeEvent> {
    public Button buttonText;
    public Button buttonNofication;
    private Stage dialogStage;
    private Stage loginStage;
    private User user;
    private User friend;
    private List<User> listofFriends;
    private UserService userService;
    private FriendshipService friendshipService;
    private MessageService messageService;
    private List<Entity> listofNotifications = new ArrayList<>();
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
        messageService.addObserver(this);
        friend = null;
        listofFriends = new ArrayList<>();
        noofNofications();
    }
    public void setStages(Stage dialogStage, Stage loginStage) {
        this.dialogStage = dialogStage;
        this.loginStage = loginStage;
        dialogStage.setTitle("Social Network");
        dialogStage.show();
    }

    private void noofNofications() {
        for(Long m : messageService.noofMessages(user.getId()))
        {
            listofNotifications.add(new Message("", userService.userById(m).get(), null, null));
        }
        for(Long f : friendshipService.noofFriendRequests(user.getId()))
        {
            Friendship d = new Friendship();
            d.setId(new Tuple<Long, Long>(f, user.getId()));
            listofNotifications.add(d);
        }
        buttonNofication.setText(listofNotifications.size() + " Not.");
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
        for(Entity entity : listofNotifications) {
            if(entity instanceof Friendship) {
                Friendship d = (Friendship) entity;
                model.add(userService.userById(d.getId().getLeft()).get());
            }
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
            Friendship d = new Friendship();
            d.setId(new Tuple<Long, Long>(friend.getId(), user.getId()));
            listofNotifications.remove(d);
            buttonNofication.setText(listofNotifications.size() + " Not.");
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
            Friendship d = new Friendship();
            d.setId(new Tuple<Long, Long>(friend.getId(), user.getId()));
            listofNotifications.remove(d);
            buttonNofication.setText(listofNotifications.size() + " Not.");
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
        if(listofNotifications.size() == 1) {
            Message m = new Message("", listofFriends.getFirst(), null, null);
            while(listofNotifications.remove(m)) {};
            buttonNofication.setText(listofNotifications.size() + " Not.");
        }
        stage.showAndWait();
    }

    public void handleNotification() throws IOException {
        Stage stage = new Stage();
        stage.setTitle("Notifications");
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("views/NotificationView.fxml"));
        AnchorPane userLayout = fxmlLoader.load();
        stage.setScene(new Scene(userLayout));
        stage.initModality(Modality.APPLICATION_MODAL);
        NotificationController notificationController = fxmlLoader.getController();
        notificationController.setService(userService);
        notificationController.setList(listofNotifications);
        stage.showAndWait();
    }

    @Override
    public void update(EntityChangeEvent event) {
        ChangeEventType eventType = event.getType();

    }
}
