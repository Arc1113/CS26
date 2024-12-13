package com.example.cs262.model;

import com.example.cs262.gui.BSignUpPageController;
import com.example.cs262.gui.PaymentController;
import com.example.cs262.products.*;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class
Customer extends User {
    static LinkedList<CartItems> cartItems = new LinkedList<>();
    private static Customer instance; // Singleton instance
    static Map<String, Integer> productQuantities = new HashMap<>();

    @FXML
    private Button BackBtnShoppingCart;

    public Customer(String username, String password, String address, String email, String contactnumber) {
        super(username, password, address, email, contactnumber);
    }

    @FXML
    private void handleBackButtonAction() {
        // Get the current stage (CartFrame)
        Stage cartStage = (Stage) BackBtnShoppingCart.getScene().getWindow();

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

    @FXML
    private Label totalPriceofItemsInCart;

    public String getTotalPrice() {
        return totalPriceofItemsInCart.getText();
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPriceofItemsInCart.setText(totalPrice);
    }

    @FXML
    private VBox scrollbar; // Container for cart items

    private static Stage cartStage;

    // Private constructor to prevent direct instantiation
    public Customer() {
        super();
        // Constructor protected for FXMLLoader
    }

    // Singleton method to get the controller instance
    public static synchronized Customer getInstance() {
        if (instance == null) {
            try {
                FXMLLoader loader = new FXMLLoader(Customer.class.getResource("/com/example/cs262/CartFrame.fxml"));
                Parent root = loader.load();
                instance = loader.getController();

                // Create and show the stage if needed
                if (cartStage == null) {
                    cartStage = new Stage();
                    cartStage.setScene(new Scene(root));
                    cartStage.setTitle("Your Cart");
                    cartStage.setOnCloseRequest(event -> cartStage = null);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return instance;
    }


    @FXML
    public void initialize() {
        instance = this;
        if (totalPriceofItemsInCart == null) {
            System.err.println("totalPriceofItemsInCart is null during initialization!");
        }
    }


    public VBox getCartBox() {
        return scrollbar;
    }

    // Adds a product to the cart
    public static void addProductToCart(Product product) {
        CartItems existingItem = findCartItemByName(product.getName());
        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + 1);
            updateProductQuantityInCart(existingItem);
        } else {
            CartItems newItem = new CartItems(product.getName(), product.getPrice(), product.getRating(), product.getImageURL());
            newItem.setQuantity(1);
            cartItems.add(newItem);
            addCartItemToUI(newItem);
        }
        Customer.getInstance().updateTotalPrice();
    }

    public static CartItems findCartItemByName(String name) {
        for (CartItems item : cartItems) {
            if (item.getName().equals(name)) {
                return item;
            }
        }
        return null;
    }

    private static void addCartItemToUI(CartItems item) {
        try {
            FXMLLoader loader = new FXMLLoader(Customer.class.getResource("/com/example/cs262/CartBar.fxml"));
            AnchorPane bar = loader.load();

            Product cartProductController = loader.getController();
            cartProductController.setDataofCartItem(item.getName(), item.getPrice(), item.getRating(), item.getImage(), item.getStock());

            TextField quantityField = (TextField) bar.lookup("#quantityField");
            Label priceLabel = (Label) bar.lookup("#productPrice1");

            if (quantityField != null && priceLabel != null) {
                quantityField.setText(String.valueOf(item.getQuantity()));
                priceLabel.setText(String.format("%.2f", item.getQuantity() * item.getPrice()));

                quantityField.textProperty().addListener((observable, oldValue, newValue) -> {
                    try {
                        int newQuantity = Integer.parseInt(newValue);
                        if (newQuantity > 0) {
                            item.setQuantity(newQuantity);
                            updateProductQuantityInCart(item);
                            priceLabel.setText(String.format("₱%.2f", newQuantity * item.getPrice()));
                            Customer.getInstance().updateTotalPrice();
                        } else {
                            quantityField.setText(oldValue);
                        }
                    } catch (NumberFormatException e) {
                        quantityField.setText(oldValue);
                    }
                });
            }

            VBox cartBox = getInstance().getCartBox();
            cartBox.getChildren().add(bar);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Opens the CartFrame.fxml in a new window to display the cart
    public static void viewCart() {
        try {
            if (cartStage != null && cartStage.isShowing()) {
                cartStage.requestFocus();
                return;
            }

            FXMLLoader loader = new FXMLLoader(Customer.class.getResource("/com/example/cs262/CartFrame.fxml"));
            Parent root = loader.load();

            cartStage = new Stage();
            cartStage.setScene(new Scene(root));
            cartStage.initStyle(StageStyle.UNDECORATED);
            cartStage.setOnCloseRequest(event -> cartStage = null);


            Customer cartController = loader.getController();
            VBox cartVBox = cartController.getCartBox();
            if (cartVBox != null) {
                cartVBox.getChildren().clear();
                for (CartItems product : cartItems) {
                    FXMLLoader itemLoader = new FXMLLoader(Customer.class.getResource("/com/example/cs262/CartBar.fxml"));
                    Parent itemRoot = itemLoader.load();

                    Product cartProductController = itemLoader.getController();
                    cartProductController.setDataofCartItem(
                            product.getName(),
                            product.getPrice(),
                            product.getRating(),
                            product.getImage(),
                            product.getStock()
                    );

                    TextField quantityField = (TextField) itemRoot.lookup("#quantityField");
                    Label priceLabel = (Label) itemRoot.lookup("#productPrice1");
                    if (quantityField != null && priceLabel != null) {
                        quantityField.setText(String.valueOf(product.getQuantity()));
                        priceLabel.setText(String.format("₱%.2f", product.getQuantity() * product.getPrice()));

                        quantityField.textProperty().addListener((observable, oldValue, newValue) -> {
                            try {
                                int newQuantity = Integer.parseInt(newValue);
                                if (newQuantity > 0) {
                                    product.setQuantity(newQuantity);
                                    priceLabel.setText(String.format("₱%.2f", newQuantity * product.getPrice()));
                                    updateProductQuantityInCart(product);
                                    Customer.getInstance().updateTotalPrice();
                                } else {
                                    quantityField.setText(oldValue);
                                }
                            } catch (NumberFormatException e) {
                                quantityField.setText(oldValue);
                            }
                        });
                    }

                    Button deleteButton = (Button) itemRoot.lookup("#deleteButton");
                    if (deleteButton != null) {
                        deleteButton.setOnAction(event -> {
                            cartVBox.getChildren().remove(itemRoot);
                            cartItems.remove(product);
                            Customer.getInstance().updateTotalPrice();
                        });
                    }

                    Button increaseButton = (Button) itemRoot.lookup("#increaseQuantity");
                    if (increaseButton != null) {
                        increaseButton.setOnAction(event -> {
                            product.setQuantity(product.getQuantity() + 1);
                            quantityField.setText(String.valueOf(product.getQuantity()));
                            priceLabel.setText(String.format("₱%.2f", product.getQuantity() * product.getPrice()));
                            Customer.getInstance().setTotalPrice("10000");
                            System.out.println("Gana:" + Customer.getInstance().getTotalPrice());
                            updateProductQuantityInCart(product);
                            Customer.getInstance().updateTotalPrice();
                        });
                    }

                    Button decreaseButton = (Button) itemRoot.lookup("#decreaseQuantity");
                    if (decreaseButton != null) {
                        decreaseButton.setOnAction(event -> {
                            if (product.getQuantity() > 1) {
                                product.setQuantity(product.getQuantity() - 1);
                                quantityField.setText(String.valueOf(product.getQuantity()));
                                priceLabel.setText(String.format("₱%.2f", product.getQuantity() * product.getPrice()));
                                updateProductQuantityInCart(product);
                                Customer.getInstance().updateTotalPrice();
                            }
                        });
                    }

                    cartVBox.getChildren().add(itemRoot);
                }
            }

            cartStage.show();


            Platform.runLater(() -> instance.updateTotalPrice());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateTotalPrice() {
        double total = cartItems.stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();

        Platform.runLater(() -> {
            if (totalPriceofItemsInCart != null) {
                totalPriceofItemsInCart.setText(String.format("₱%.2f", total));
                System.out.println("Updated total price in UI: ₱" + total);
            } else {
                System.err.println("TotalPrice label is null during UI update!");
            }
        });

        // Log the updated total price for debugging
        System.out.println("Computed total price: ₱" + total);
    }


    static void updateProductQuantityInCart(CartItems item) {
        VBox cartBox = getInstance().getCartBox();
        boolean found = false; // To track if the item was updated

        for (Node node : cartBox.getChildren()) {
            if (node instanceof AnchorPane) {
                Label productLabel = (Label) node.lookup("#productName1");
                if (productLabel != null && productLabel.getText().equals(item.getName())) {
                    TextField quantityField = (TextField) node.lookup("#quantityField");
                    Label price = (Label) node.lookup("#productPrice1");

                    if (quantityField != null && price != null) {
                        int newQuantity = item.getQuantity();
                        double newPrice = newQuantity * item.getPrice();

                        quantityField.setText(String.valueOf(newQuantity));
                        price.setText(String.format("₱%.2f", newPrice));

                        // Log updates for debugging
                        System.out.println("Updated quantityField and price for: " + item.getName());
                        System.out.println("Quantity: " + newQuantity + ", Price: " + newPrice);

                        found = true; // Mark as updated
                    }
                    break;
                }
            }
        }

        if (!found) {
            System.err.println("Item not found in the cart: " + item.getName());
        }

        // Update the total price after updating the item
        Customer.getInstance().updateTotalPrice();
    }

    public void registerCustomer(String name, String email, String password, String address, String contact_number) {
        String sql = "INSERT INTO customers (username, email, password,address,contact_number) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, name);
            pstmt.setString(2, email);
            pstmt.setString(3, password);
            pstmt.setString(4, address);
            pstmt.setString(5, contact_number);
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Customer registered successfully!");
            } else {
                System.out.println("Registration failed.");
            }

        } catch (SQLException e) {
            System.err.println("SQL error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }

    }


    public boolean userExists(String email) {
        String sql = "SELECT COUNT(*) FROM customers WHERE email = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, email);
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

    // Method to add a customer
    public void addCustomer(String name, String email, String password, String address, String contactNumber) throws Exception {
        if (userExists(email)) {
            // User already exists, clear fields and prompt user
            clearSignupFields(); // Implement this method to clear your fields
            System.out.println("User  already exists in the database. Please try again.");
            return; // Exit the method
        }

        // If user does not exist, proceed to register
        registerCustomer(name, email, password, address, contactNumber);
        BSignUpPageController signUpController = new BSignUpPageController(); // Create instance or get existing instance
        signUpController.goToLoginPage();
        closeSignupWindow();
        // Implement this method to close the signup window
        // Implement this method to open the login window
    }

    // Method to clear fields in the signup window (to be implemented)
    private void clearSignupFields() {
        // Logic to clear the fields in your signup window
        // For example, if you have TextFields for username, email, etc.
        // usernameField.clear();
        // emailField.clear();
        // passwordField.clear();
        // addressField.clear();
        // contactNumberField.clear();
    }

    // Method to close the signup window (to be implemented)
    private void closeSignupWindow() {
        // Logic to close the signup window
        // For example, if you have a reference to the signup stage:
        // signupStage.close();
    }

    // Method to open the login window (to be implemented)
    private void openLoginWindow() {
        // Logic to open the login window
        // For example, you could create a new instance of LoginWindow and show it
        // LoginWindow loginWindow = new LoginWindow();
        // loginWindow.show();
    }

    private void gotoCart() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(""));
    }

    @FXML
    private Button CheckoutBtn;

    @FXML
    private void handleCheckoutBtn(ActionEvent event) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/cs262/payment.fxml"));
            Parent root = loader.load();

            PaymentController paymentController = loader.getController();
            paymentController.setTotalCost(totalPriceofItemsInCart.getText());
            paymentController.setcartItems(cartItems);
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            Stage cartStage = (Stage) CheckoutBtn.getScene().getWindow();
            cartStage.close();
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    // Helper method to create a product object based on category
    private static Product createProduct(String category, String name, double price, String rating, String imageURL, String extraField) {
        switch (category) {
            case "Fruit":
                Fruit fruit = new Fruit(name, price, rating, imageURL, extraField);
                fruit.setSeason(extraField);
                return fruit;
            case "Vegetable":
                Vegetable vegetable = new Vegetable(name, price, rating, imageURL, extraField);
                vegetable.setIsOrganic(extraField);
                return vegetable;
            case "Beverages":
                Beverages beverages = new Beverages(name, price, rating, imageURL, extraField);
                beverages.setSize(extraField);
                return beverages;
            case "MilkAndEggs":
                MilkAndEggs milkAndEggs = new MilkAndEggs(name, price, rating, imageURL, extraField);
                milkAndEggs.setExpirationDate(extraField);
                return milkAndEggs;
            case "Laundry":
                Laundry laundry = new Laundry(name, price, rating, imageURL, extraField);
                laundry.setBrand(extraField);
                return laundry;
            default:
                System.err.println("Unknown category: " + category);
                return null;
        }



    }

}