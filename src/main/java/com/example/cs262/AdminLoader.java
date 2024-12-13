package com.example.cs262;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class AdminLoader extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        // Load the FXML file for the application
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/com/example/cs262/BLoginPage.fxml"));

        // Verify that the FXML resource is correctly located
        if (fxmlLoader.getLocation() == null) {
            throw new IOException("FXML resource not found");
        }

        // Create and set up the scene using the loaded FXML
        Scene scene = new Scene(fxmlLoader.load());

        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {

        launch(); // Launch the JavaFX application
    }
}
