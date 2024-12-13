package com.example.cs262.gui;


import com.example.cs262.model.Admin;
import com.example.cs262.model.DatabaseConnection;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BLoginPageController {

    @FXML
    private Button LogInBTN;

    @FXML
    private Button SignUpBTN;

    @FXML
    private TextField passwordTxt;

    @FXML
    private TextField usernameTxt;

    @FXML
    private TextField emailField; // Assuming you have this TextField for email input
    @FXML
    private TextField passwordField; // Assuming you have this TextField for password input
    @FXML
    private Button loginButton; // The button to trigger the login action

    @FXML
    private void handleLoginButtonAction() throws IOException {
        String email = emailField.getText();
        String password = passwordField.getText();

        if(email.equals("admin") && password.equals("robertpogi")) {
            Stage cartStage = (Stage) loginButton.getScene().getWindow();
            cartStage.close();
            goToAdminFrame();
            Admin admin = new Admin();
            admin.loadproducts();
        }  else{
            if (validateUser (email, password)) {
                Stage cartStage = (Stage) loginButton.getScene().getWindow();
                cartStage.close();
                    goToWelcomePage();
            } else {
                // Inform user of failed login
                System.out.println("Login failed. Please check your credentials.");
            }

        }

        
    }

    // Method to validate user credentials
    private boolean validateUser (String email, String password) {
        String sql = "SELECT COUNT(*) FROM customers WHERE email = ? AND password = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, email);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0; // Return true if count is greater than 0
            }
        } catch (SQLException e) {
            System.err.println("SQL error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
        return false; // Default to false if an error occurs
    }


    // Method to go to the Admin Frame
    private void goToAdminFrame() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/cs262/Admin.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            Stage cartStage = (Stage) loginButton.getScene().getWindow();
            cartStage.close();
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML// Method to go to the User Welcome Page
    private void goToWelcomePage() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/cs262/BWelcomePage.fxml"));
        Parent root = loader.load();

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        Stage cartStage = (Stage) loginButton.getScene().getWindow();
        cartStage.close();

        stage.show();
    }

    @FXML
    private void goToSignUpPage() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/cs262/BSignUpPage.fxml"));
        Parent root = loader.load();

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    }


    @FXML
    private void closeApplication() {
        Platform.exit();
        System.exit(0);
    }

}
