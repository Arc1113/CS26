package com.example.cs262.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class BCoverPageController {
    @FXML
    private Button loginButton;

    @FXML
    private Button signupButton;

    @FXML
    private ImageView image;


    @FXML
    public void initialize() {

       Image background = new Image(getClass().getResourceAsStream("/com/example/cs262/Fruits/photo-1601599963565-b7ba29c8e3ff (1).jpeg"));
       image.setImage(background);
    }

    @FXML
    private void goToLogin() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/cs262/BLoginPage.fxml"));
        Parent root = loader.load();

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    private void goToSignUp() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/cs262/BSignUpPage.fxml"));
        Parent root = loader.load();

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    }
}
