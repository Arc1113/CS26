package com.example.cs262.gui;

import com.example.cs262.model.Admin;
import com.example.cs262.model.CartItems;
import com.example.cs262.model.Controller;
import com.example.cs262.model.DatabaseConnection;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.LinkedList;

public class PaymentController {

    @FXML
    private AnchorPane PMethodBTN;

    @FXML
    private AnchorPane PMethodBTN111;

    @FXML
    private AnchorPane PMethodBTN1111;

    @FXML
    private AnchorPane PMethodBTN11111;

    @FXML
    private AnchorPane PMethodBTN111111;

    @FXML
    private Button PlaceOrder;

    @FXML
    private Button BackButton;


    @FXML
    private ComboBox<String> methodPayment;
    @FXML
    private CheckBox termsCheckBox;

    @FXML
    private ComboBox<String> selectedDeliveryMethod;

    private LinkedList<CartItems> cartItems = new LinkedList<>();

    public void setcartItems(LinkedList<CartItems> cartItems) {
        this.cartItems = cartItems;
    }

    private ObservableList<String> list1 = FXCollections.observableArrayList("Credit/Debit Cards", "Digital Wallets", "Cash on Delivery (COD)");
    private ObservableList<String> list2 = FXCollections.observableArrayList(
            "Standard Delivery",
            "Express Delivery",
            "Same-Day Delivery",
            "Pickup Point"
    );


    @FXML
    public void handleExit() {
        Platform.exit();
    }

    @FXML
    void handleMethodDelivery(ActionEvent event) {
        String selectedMethod = selectedDeliveryMethod.getValue();
        System.out.println("Selected Delivery Method: " + (selectedMethod != null ? selectedMethod : "None"));
    }

    @FXML
    void handleMethodPayment(ActionEvent event) {
        String selectedMethod = methodPayment.getValue();
        System.out.println("Selected Payment Method: " + (selectedMethod != null ? selectedMethod : "None"));
    }

    public void initialize() {
        selectedDeliveryMethod.setItems(list1);
        methodPayment.setItems(list2);
    }

    @FXML
    public void handlePlaceOrder() {
        if (termsCheckBox.isSelected()) {
            if (selectedDeliveryMethod.getValue() == null || methodPayment.getValue() == null) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Missing Selection");
                alert.setHeaderText(null);
                alert.setContentText("Please select both delivery method and payment method before placing the order.");
                alert.showAndWait();
                return;
            }

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/cs262/payment2.fxml"));
                Parent root = loader.load();

                Payment2Controller controller = loader.getController();

                // Set details for the second scene (Payment2Controller)
                controller.setDetails(selectedDeliveryMethod.getValue(), methodPayment.getValue(), totalCost.getText());

                // Ensure displayCartItems() is executed after the FXML components are initialized
                Platform.runLater(() -> {
                    controller.displayCartItems(cartItems);
                });

                // Show the second scene
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.show();

            } catch (IOException e) {
                e.printStackTrace(); // Handle loading errors
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Terms and Conditions");
            alert.setHeaderText(null);
            alert.setContentText("You must agree to the terms and conditions before placing the order.");
            alert.showAndWait();
        }

    }

    @FXML
    private Label totalCost;

    public Label getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(String totalcost) {
            totalCost.setText(totalcost);
    }


    @FXML
    private void handleBackButtonAction() {
        // Get the current stage (CartFrame)
        Stage cartStage = (Stage) BackButton.getScene().getWindow();

        // Find the main stage
        Stage mainStage = (Stage) cartStage.getOwner(); // Assuming the CartFrame was opened from the MainFrame

        // Apply a slide-out animation to the CartFrame
        TranslateTransition slideOut = new TranslateTransition(Duration.millis(300), cartStage.getScene().getRoot());
        slideOut.setFromX(0); // Start at the current position
        slideOut.setToX(-cartStage.getScene().getWidth()); // Slide out to the left

        slideOut.setOnFinished(event -> {
            // After the slide-out animation, close the CartFrame stage
            cartStage.close();

            // Bring the MainFrame to the foreground
            if (mainStage != null) {
                mainStage.show();

                // Optional: Add a slide-in animation for the MainFrame
                Parent mainRoot = mainStage.getScene().getRoot();
                TranslateTransition slideIn = new TranslateTransition(Duration.millis(100), mainRoot);
                mainRoot.setTranslateX(-mainStage.getScene().getWidth()); // Start outside the view
                slideIn.setFromX(-mainStage.getScene().getWidth());
                slideIn.setToX(0); // Slide into view
                slideIn.play();
            }
        });

        // Play the slide-out animation
        slideOut.play();
    }
}

