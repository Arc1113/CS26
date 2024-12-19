package com.example.cs262.gui;

import com.example.cs262.model.Customer;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.regex.Pattern;

public class BSignUpPageController {

    @FXML
    private TextField EmailTXT;

    @FXML
    private PasswordField PasswordTXT;

    @FXML
    private Button loginBTN;

    @FXML
    private Button signupBTN;

    @FXML
    private TextField AddressTXT;

    @FXML
    private TextField ContactnumberTXT;

    @FXML
    private TextField usernameTXT;

    @FXML
    private Button togglePasswordVisibility;

    @FXML
    private TextField passwordPlainTextField;  // This should be a TextField for plain text visibility

    private String Username;

    @FXML
    public void togglePasswordVisibility(ActionEvent event) {
        // Toggle visibility of password
        if (PasswordTXT.isVisible()) {
            PasswordTXT.setVisible(false);
            passwordPlainTextField.setText(PasswordTXT.getText());
            passwordPlainTextField.setVisible(true);
            togglePasswordVisibility.setText("Hide");
        } else {
            passwordPlainTextField.setVisible(false);
            PasswordTXT.setText(passwordPlainTextField.getText());
            PasswordTXT.setVisible(true);
            togglePasswordVisibility.setText("Show");
        }
    }

    @FXML
    private void handleSignUpBTN(ActionEvent event) throws Exception {
        // Validate user input
        String email = EmailTXT.getText();
        String password = PasswordTXT.getText();
        String address = AddressTXT.getText();
        Username = usernameTXT.getText();
        String contactnumber = ContactnumberTXT.getText();

        if (!isValidEmail(email)) {
            showAlert("Error", "Invalid email format.", AlertType.ERROR);
            return;
        }

        if (!isValidUsername(Username)) {
            showAlert("Error", "Username can only contain letters, numbers, and underscores.", AlertType.ERROR);
            return;
        }

        if (!isValidPassword(password)) {
            showAlert("Error", "Password must be at least 8 characters long and contain both letters and numbers.", AlertType.ERROR);
            return;
        }

        if (!isValidContactNumber(contactnumber)) {
            showAlert("Error", "Contact number must be 10 digits.", AlertType.ERROR);
            return;
        }

        // Close current signup window
        Stage cartStage = (Stage) signupBTN.getScene().getWindow();
        cartStage.close();

        // Check if user already exists
        if (Customer.checkIfUserExists(Username, email)) {
            showAlert("Error", "Username or email already exists!", AlertType.ERROR);
        } else {
            // Create and add the customer
            Customer customer = new Customer(Username, password, address, email, contactnumber);
            customer.addCustomer(Username, email, password, address, contactnumber);

            showAlert("Success", "Registration successful!", AlertType.INFORMATION);

            // After success, go to the login page
            goToLoginPage();
        }
    }

    // Helper method to check if username is valid (only alphanumeric and underscores)
    private boolean isValidUsername(String username) {
        return username != null && Pattern.matches("^[a-zA-Z0-9_]+$", username);
    }

    // Helper method to check if email is valid
    private boolean isValidEmail(String email) {
        return email != null && Pattern.matches("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$", email);
    }

    // Helper method to check if password is valid
    private boolean isValidPassword(String password) {
        return password != null && password.length() >= 8 && Pattern.matches(".*[a-zA-Z].*", password) && Pattern.matches(".*[0-9].*", password);
    }

    // Helper method to check if contact number is valid
    private boolean isValidContactNumber(String contactnumber) {
        return contactnumber != null && Pattern.matches("^[0-9]{10}$", contactnumber);  // Example: Must be 10 digits
    }

    private void showAlert(String title, String message, AlertType alertType) {
        // Show an alert message to the user
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void goToLoginPage() {
        // Close the signup window and open the login page
        Stage currentStage = (Stage) signupBTN.getScene().getWindow();
        currentStage.close();

        Platform.runLater(() -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/cs262/BLoginPage.fxml"));
                Parent root = loader.load();
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    // Getter and Setter methods for FXML components

    public TextField getEmailTXT() {
        return EmailTXT;
    }

    public void setEmailTXT(TextField emailTXT) {
        EmailTXT = emailTXT;
    }

    public PasswordField getPasswordTXT() {
        return PasswordTXT;
    }

    public void setPasswordTXT(PasswordField passwordTXT) {
        PasswordTXT = passwordTXT;
    }

    public Button getLoginBTN() {
        return loginBTN;
    }

    public void setLoginBTN(Button loginBTN) {
        this.loginBTN = loginBTN;
    }

    public Button getSignupBTN() {
        return signupBTN;
    }

    public void setSignupBTN(Button signupBTN) {
        this.signupBTN = signupBTN;
    }

    public TextField getAddressTXT() {
        return AddressTXT;
    }

    public void setAddressTXT(TextField addressTXT) {
        AddressTXT = addressTXT;
    }

    public TextField getContactnumberTXT() {
        return ContactnumberTXT;
    }

    public void setContactnumberTXT(TextField contactnumberTXT) {
        ContactnumberTXT = contactnumberTXT;
    }

    public TextField getUsernameTXT() {
        return usernameTXT;
    }

    public void setUsernameTXT(TextField usernameTXT) {
        this.usernameTXT = usernameTXT;
    }


}
