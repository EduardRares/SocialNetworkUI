package com.example.socialnetworkui;

import com.example.socialnetworkui.controller.LogInController;
import com.example.socialnetworkui.domain.Friendship;
import com.example.socialnetworkui.domain.Message;
import com.example.socialnetworkui.domain.Tuple;
import com.example.socialnetworkui.domain.User;
import com.example.socialnetworkui.domain.validators.UtilizatorValidator;
import com.example.socialnetworkui.repository.Repository;
import com.example.socialnetworkui.repository.db.FriendshipDBRepository;
import com.example.socialnetworkui.repository.db.MessageDBRepository;
import com.example.socialnetworkui.repository.db.UserDBRepository;
import com.example.socialnetworkui.repository.memory.InMemoryRepository;
import com.example.socialnetworkui.service.FriendshipService;
import com.example.socialnetworkui.service.MessageService;
import com.example.socialnetworkui.service.UserService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    private UserService service;
    private FriendshipService friendshipService;
    private MessageService messageService;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage login) throws IOException {
        System.out.println("Reading data from database");
        Repository<Long, User> utilizatorRepository =
                new UserDBRepository(new UtilizatorValidator());

        service =new UserService(utilizatorRepository);

        Repository<Tuple<Long, Long>, Friendship> friendshipRepository = new FriendshipDBRepository();
        friendshipService = new FriendshipService(friendshipRepository);

        Repository<Long, Message> messageRepository = new MessageDBRepository();
        messageService = new MessageService(messageRepository);

        login.setTitle("Login");

        Stage admin = new Stage();
        admin.setTitle("Admin Page");
        Stage app = new Stage();
        app.setTitle("Social Network");

        initView(login);
        login.setWidth(500);
        login.show();

    }

    private void initView(Stage login) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("views/StartView.fxml"));
        AnchorPane userLayout = fxmlLoader.load();
        login.setScene(new Scene(userLayout));
        LogInController editUserController = fxmlLoader.getController();
        editUserController.setService(service, friendshipService, messageService, login);
    }
}