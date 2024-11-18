package com.example.socialnetworkui.controller;

import com.example.socialnetworkui.HelloApplication;
import com.example.socialnetworkui.domain.User;
import com.example.socialnetworkui.domain.validators.ValidationException;
import com.example.socialnetworkui.service.UserService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class CreateAccountController {

    private UserService service;
    Stage dialogStage;

    @FXML
    TextField textFieldFirstName;
    @FXML
    TextField textFieldLastName;
    @FXML
    TextField textFieldEmail;
    @FXML
    TextField textFieldPassword;

    public void setService(UserService service, Stage stage) {
        this.service = service;
        dialogStage = stage;
    }

    @FXML
    public void handleCreate() throws IOException {
        String firstname = textFieldFirstName.getText();
        String lastname = textFieldLastName.getText();
        String email = textFieldEmail.getText();
        String password = textFieldPassword.getText();
        User user = new User(firstname, lastname, email, password);
        try {
            service.addUser(user);
        } catch (ValidationException e) {
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION,"Error","Userul exista deja");
        }
    }
}
