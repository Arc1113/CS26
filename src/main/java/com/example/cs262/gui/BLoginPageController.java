package com.example.cs262.gui;

import com.example.cs262.model.Admin;
import com.example.cs262.model.Customer;
import com.example.cs262.model.DatabaseConnection;
import com.example.cs262.model.UserSession;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class BLoginPageController {

    @FXML
    private Button LogInBTN;

    @FXML
    private Button SignUpBTN;

    @FXML
    private PasswordField passwordField; // Replaces original TextField for hidden password input

    @FXML
    private TextField passwordVisibleField; // TextField for showing the password

    @FXML
    private Button showPasswordBtn; // Toggle button to show/hide password

    @FXML
    private TextField usernameTxt;

    @FXML
    private TextField emailField;

    @FXML
    private Button loginButton;

    private boolean isPasswordVisible = false; // Flag to track password visibility

    @FXML
    public void initialize() {
        // Sync the visible TextField and hidden PasswordField
        passwordVisibleField.setManaged(false);
        passwordVisibleField.setVisible(false);
        passwordVisibleField.textProperty().bindBidirectional(passwordField.textProperty());

        // Toggle password visibility when the button is clicked
        showPasswordBtn.setOnAction(event -> togglePasswordVisibility());
    }

    @FXML
    private void togglePasswordVisibility() {
        isPasswordVisible = !isPasswordVisible;

        if (isPasswordVisible) {
            // Show password
            passwordVisibleField.setVisible(true);
            passwordVisibleField.setManaged(true);
            passwordField.setVisible(false);
            passwordField.setManaged(false);
            showPasswordBtn.setText("Hide");
        } else {
            // Hide password
            passwordVisibleField.setVisible(false);
            passwordVisibleField.setManaged(false);
            passwordField.setVisible(true);
            passwordField.setManaged(true);
            showPasswordBtn.setText("Show");
        }
    }

    @FXML
    private void handleLoginButtonAction() throws IOException {
        String email = emailField.getText();
        String password = passwordField.getText();

        if (email.equals("admin") && password.equals("robertpogi")) {
            // Admin login
            Stage cartStage = (Stage) loginButton.getScene().getWindow();
            cartStage.close();
            goToAdminFrame();
            Admin admin = new Admin();
            admin.loadproducts();
        } else {
            // Attempt to validate the user credentials
            Customer currentCustomer = validateUser(email, password);
            if (currentCustomer != null) {
                // Valid user, set the session and go to the welcome page
                UserSession.setCurrentCustomer(currentCustomer);
                Stage cartStage = (Stage) loginButton.getScene().getWindow();
                cartStage.close();
                goToWelcomePage();
            } else {
                // User is not registered, show registration prompt
                showRegistrationPrompt();
            }
        }
    }

    // Method to show a registration prompt if login fails
    private void showRegistrationPrompt() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("User Not Registered");
        alert.setHeaderText("Your email is not registered.");
        alert.setContentText("Would you like to register now?");

        // Add buttons to the alert dialog
        ButtonType registerButton = new ButtonType("Register");
        ButtonType cancelButton = new ButtonType("Cancel");

        alert.getButtonTypes().setAll(registerButton, cancelButton);

        // Show the alert and handle user response
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == registerButton) {
            // User clicked "Register" button, go to the sign-up page
            try {
                goToSignUpPage();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            // User clicked "Cancel", no action
            System.out.println("User chose not to register.");
        }
    }


    private Customer validateUser(String email, String password) {
        String sql = "SELECT * FROM customers WHERE email = ? AND password = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, email);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                // Retrieve the customer's details from the database
                String username = rs.getString("username");
                String address = rs.getString("address");
                String contactNumber = rs.getString("contact_number");

                // Create and return a Customer object
                return new Customer(username, password, address, email, contactNumber);
            }
        } catch (SQLException e) {
            System.err.println("SQL error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
        return null;
    }


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

    @FXML
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
