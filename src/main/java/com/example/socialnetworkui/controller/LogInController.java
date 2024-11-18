package com.example.socialnetworkui.controller;

import com.example.socialnetworkui.HelloApplication;
import com.example.socialnetworkui.domain.User;
import com.example.socialnetworkui.domain.validators.ValidationException;
import com.example.socialnetworkui.service.FriendshipService;
import com.example.socialnetworkui.utils.observer.Observer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import com.example.socialnetworkui.service.UserService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class LogInController {
    @FXML
    private TextField textFieldEmail;
    @FXML
    private TextField textFieldPassword;

    private UserService service;
    private FriendshipService friendshipService;
    private Stage dialogStage;
    private User user;

    @FXML
    private void initialize() {
    }

    public void setService(UserService service, FriendshipService friendshipService, Stage stage) {
        this.service = service;
        this.friendshipService = friendshipService;
        this.dialogStage=stage;
    }

    @FXML
    public void handleLogIn() throws IOException {
        String emailText= textFieldEmail.getText();
        String passwordText= textFieldPassword.getText();
        this.user = null;
        this.user = service.login(emailText, passwordText);
        if(user != null) {
            dialogStage.hide();
            if("admin".equals(user.getEmail()) && "admin".equals(user.getPassword())) {
                Stage admin = new Stage();
                FXMLLoader fxmlLoaderAdmin = new FXMLLoader(HelloApplication.class.getResource("views/AdminView.fxml"));
                AnchorPane adminLayout = fxmlLoaderAdmin.load();
                admin.setScene(new Scene(adminLayout));
                AdminController adminController = fxmlLoaderAdmin.getController();
                adminController.setUtilizatorService(service);
                adminController.setStages(admin, this.dialogStage);
            }
            else {
                Stage app = new Stage();
                FXMLLoader fxmlLoaderUser = new FXMLLoader(HelloApplication.class.getResource("views/UserView.fxml"));
                AnchorPane UserLayout = fxmlLoaderUser.load();
                app.setScene(new Scene(UserLayout));
                UserController userController = fxmlLoaderUser.getController();
                userController.setService(service, friendshipService, user);
                userController.setStages(app, this.dialogStage);
            }
        }
        else {
            saveMessageError();
            clearFields();
        }
    }

    private void updateMessage(User m)
    {
        try {
            User r= this.service.updateUser(m);
            if (r==null)
                MessageAlert.showMessage(null, Alert.AlertType.INFORMATION,"Modificare user","Userul a fost modificat");
        } catch (ValidationException e) {
            MessageAlert.showErrorMessage(null,e.getMessage());
        }
        dialogStage.hide();
    }


    private void saveMessageError()
    {
        MessageAlert.showMessage(null, Alert.AlertType.INFORMATION,"Credentiale incorecte","Userul nu exista");
    }

    private void clearFields() {
        textFieldPassword.setText("");

    }
    private void setFields(User u)
    {
        textFieldEmail.setText(u.getEmail());
        textFieldPassword.setText(u.getPassword());
    }

    @FXML
    public void handleSignIn() throws IOException {
        Stage stage = new Stage();
        stage.setTitle("Create Account");
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("views/createAccountView.fxml"));
        AnchorPane userLayout = fxmlLoader.load();
        stage.setScene(new Scene(userLayout));
        stage.initModality(Modality.APPLICATION_MODAL);
        CreateAccountController createAccountController = fxmlLoader.getController();
        createAccountController.setService(service, stage);
        stage.showAndWait();
    }
}
