package com.example.cs262.gui;

import com.example.cs262.model.Customer;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class BSignUpPageController {

    @FXML
    private TextField EmailTXT;

    @FXML
    private TextField PasswordTXT;

    @FXML
    private Button loginBTN;

    @FXML
    private Button signupBTN;

    private  String Username;

    @FXML
    private void hadlesignupBTN(ActionEvent event) throws Exception {

        Stage cartStage = (Stage) signupBTN.getScene().getWindow();
        cartStage.close();
        String email=EmailTXT.getText();
        String password=PasswordTXT.getText();
        String address= AddressTXT.getText();
        Username = usernameTXT.getText();
        String contactnumber= ContactnumberTXT.getText();
        Customer customer = new Customer(Username,password,address,email,contactnumber);
        customer.addCustomer(Username,email,password,address,contactnumber);

        goToLoginPage();
    }
    @FXML
    private TextField AddressTXT;

    @FXML
    private TextField ContactnumberTXT;

    @FXML
    private TextField usernameTXT;


    @FXML
    public void goToWelcomePage() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/cs262/BWelcomePage.fxml"));
        Parent root = loader.load();

        BWelcomePageController welcomeController = loader.getController();
        String username = Username; // Get text from TextField
        welcomeController.setUsername(username);

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    public void goToLoginPage(){

        Stage cartStage = (Stage) loginBTN.getScene().getWindow();
        cartStage.close();
        Platform.runLater(() -> {
            try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/cs262/BLoginPage.fxml"));
            Parent root =loader.load();

                Stage stage = new Stage();
                stage.setScene(new Scene(root));

                stage.show();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        });



    }

    public TextField getEmailTXT() {
        return EmailTXT;
    }

    public void setEmailTXT(TextField emailTXT) {
        EmailTXT = emailTXT;
    }

    public TextField getPasswordTXT() {
        return PasswordTXT;
    }

    public void setPasswordTXT(TextField passwordTXT) {
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
}
