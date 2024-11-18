package com.example.socialnetworkui.controller;

import com.example.socialnetworkui.domain.User;
import com.example.socialnetworkui.service.UserService;
import com.example.socialnetworkui.utils.observer.Observer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.util.Optional;

public class AdminController {
    UserService service;
    ObservableList<User> model = FXCollections.observableArrayList();
    private User user;
    @FXML
    TableView<User> tableView;
    @FXML
    TableColumn<User,String> tableColumnFirstName;
    @FXML
    TableColumn<User,String> tableColumnLastName;
    @FXML
    TextField textFieldFirstName;
    @FXML
    TextField textFieldLastName;
    @FXML
    TextField textFieldEmail;
    @FXML
    TextField textFieldPassword;
    Stage dialogStage;
    Stage loginStage;

    public void setUtilizatorService(UserService service) {
        this.service = service;
    }

    public void setStages(Stage thisStage, Stage LoginStage) {
        this.dialogStage = thisStage;
        this.loginStage = LoginStage;
        initModel();
    }

    @FXML
    public void initialize() {
        tableColumnFirstName.setCellValueFactory(new PropertyValueFactory<User, String>("firstName"));
        tableColumnLastName.setCellValueFactory(new PropertyValueFactory<User, String>("lastName"));
        tableView.getSelectionModel().getSelectedItem();
        tableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
           User user = tableView.getSelectionModel().getSelectedItem();
           textFieldFirstName.setText(user.getFirstName());
           textFieldLastName.setText(user.getLastName());
           textFieldEmail.setText(user.getEmail());
           textFieldPassword.setText(user.getPassword());
        });
    }

    private void initModel() {
        dialogStage.setTitle("Admin View");
        dialogStage.show();
        model.clear();
        for(User utilizator : service.getAll()) {
            model.add(utilizator);
        }
        tableView.setItems(model);
    }

    public void handleDeleteUtilizator(ActionEvent actionEvent) {
        User user= tableView.getSelectionModel().getSelectedItem();
        if (user!=null) {
            service.deleteUser(user.getId());
        }
        initModel();
        clearFields();
    }

    public void handleAddUtilizator(ActionEvent actionEvent) {
        User user = new User(textFieldFirstName.getText(),textFieldLastName.getText(),textFieldEmail.getText(),textFieldPassword.getText());
        service.addUser(user);
        initModel();
        clearFields();
    }

    public void handleUpdateUtilizator(ActionEvent actionEvent) {
        User user=(User) tableView.getSelectionModel().getSelectedItem();
        user.setFirstName(textFieldFirstName.getText());
        user.setLastName(textFieldLastName.getText());
        user.setEmail(textFieldEmail.getText());
        user.setPassword(textFieldPassword.getText());
        service.updateUser(user);
        initModel();
        clearFields();
    }

    private void clearFields() {
        textFieldFirstName.setText("");
        textFieldLastName.setText("");
        textFieldEmail.setText("");
        textFieldPassword.setText("");

    }
    public void handleLogout(ActionEvent actionEvent) {
        dialogStage.close();
        loginStage.show();
    }
}
