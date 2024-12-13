package com.example.cs262.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;

public class BWelcomePageController {
    @FXML
    private ImageView ImageBackground;

    @FXML
    private Button WelcomeBTN;

    @FXML
    private Label labelName;

    @FXML
    private Label labelNameS;

    @FXML
    private Button StartShoppingBTN;

    @FXML
    private void handleStartShoppingBTN() {
        try {

            Stage cartStage = (Stage) StartShoppingBTN.getScene().getWindow();
            cartStage.close();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/cs262/FXML.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize() {
     //   Image background = new Image(getClass().getResourceAsStream("/com/example/aniika/picture/high-angle-customer-paying-with-.jpg"));
     //   ImageBackground.setImage(background);
    }
    @FXML
    public void setUsername(String username) {
        labelName.setText("Welcome, " + username + "!");
    }
    @FXML
    public void setUsernameS(String usernameS) {
        labelNameS.setText("Welcome, " + usernameS + "!");
    }


}
