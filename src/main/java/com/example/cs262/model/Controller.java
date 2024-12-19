package com.example.cs262.model;

import com.example.cs262.products.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.example.cs262.model.Customer.addProductToCart;
import static com.example.cs262.model.Customer.cartItems;

/**
 * Controller class responsible for managing the interaction between the GUI components
 * and the application logic.
 */
public class Controller {

    private static Controller instance;
    public VBox CartScrollPane;

    @FXML
    private HBox BeveragesBox;
    public HBox getBeveragesBox() {
        return BeveragesBox;
    }


    @FXML
    private HBox DairyBox;
    public HBox getDairyBox() {
        return DairyBox;
    }


    @FXML
    private Button HomeButton;

    @FXML
    private Label CurrentCustomerAddress;

    @FXML
    private HBox LaundryBox;
    public HBox getLaundryBox() {
        return LaundryBox;
    }

    @FXML
    private Button Fruitbtn;

    @FXML
    private Button LaundryBtn;

    @FXML
    private Button vegetableBtn;

    @FXML
    private Button BeveragesBtn;

    @FXML
    private Button DairyBtn;

    /**
     * Default constructor. Saves the instance when the controller is initialized.
     */
    public Controller() {
        instance = this; // Save the instance when the controller is initialized
    }

    /**
     * Retrieves the singleton instance of the controller.
     *
     * @return the controller instance
     */
    public static Controller getInstance() {
        return instance;
    }

    @FXML
    private ImageView BanerImage;

    @FXML
    private ScrollPane MainScrollPane;

    @FXML
    private HBox HFruits;

    public HBox getHFruits() {
        return this.HFruits;
    }


    @FXML
    private ImageView FruitImage;

    @FXML
    private StackPane productBar;


    public void setMenuImages(String URL){
        Image image = new Image(getClass().getResourceAsStream(URL));
        FruitImage.setImage(image);
    }
    public StackPane getproductBar() {
        return productBar;
    }

    @FXML
    private HBox VegeBox;

    /**
     * Provides access to the VegeBox HBox.
     *
     * @return the VegeBox HBox
     */
    public HBox getVegeBox() {
        return VegeBox;
    }

    @FXML
    void handleHomeButtonClick(ActionEvent event) {
        System.out.println("HI");
    }

    @FXML
    private Button Favorite;

    @FXML
    void handleFavoriteClick(ActionEvent event) {
        System.out.println("Hello");
    }

    @FXML
    Button BtnCart;

    /**
     * Initializes the controller and sets up the initial application state.
     */
    @FXML
    public void initialize() {

        displayAllProducts();
        // Setting click event for HFruits HBox
//        HFruits.setOnMouseClicked(event -> handleClick());

//        setMenuImages("/com/example/cs262/Fruits/Fruits.png");
        MainScrollPane.setVvalue(0);

        Image image = new Image(getClass().getResourceAsStream("/com/example/cs262/Fruits/ScreenShot.png"));
        BanerImage.setImage(image);

       Customer currentCustomer = UserSession.getCurrentCustomer();

        // Display the customer's information
        if (currentCustomer != null) {
            CurrentCustomerAddress.setText(currentCustomer.getAddress());

    }
    }

    // Method to display all products from the database in their respective sections
    public static void displayAllProducts() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT * FROM products WHERE stock > 0"; // Only fetch products with stock > 0
            assert conn != null;
            try (PreparedStatement stmt = conn.prepareStatement(sql);
                 ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    String category = rs.getString("category");
                    String name = rs.getString("name");
                    double price = rs.getDouble("price");
                    String rating = rs.getString("rating");
                    String imageURL = rs.getString("imageURL");
                    String extraField = rs.getString("extraField");
                    int stock = rs.getInt("stock");

                    // Proceed only if the stock is greater than 0 (This check is now redundant due to SQL query)
                    if (stock > 0) {
                        FXMLLoader loader = new FXMLLoader(Admin.class.getResource("/com/example/cs262/Cart.fxml"));
                        AnchorPane item = loader.load();

                        Product controller = loader.getController();
                        controller.setData(name, price, rating, imageURL, stock);
                        System.out.println("The stock here is " + stock);


                        Button addToCartButton = (Button) item.lookup("#addButton");
                        addToCartButton.setOnAction(event -> {
                            Product product = createProduct(category, name, price, rating, imageURL, extraField,stock);
                            if (!cartItems.contains(product)) {
                                addProductToCart(product);
                            }
                        });

                        // Add item to the appropriate section based on its category
                        switch (category) {
                            case "Fruit":
                                Controller.getInstance().getHFruits().getChildren().add(item);
                                break;
                            case "Vegetable":
                                Controller.getInstance().getVegeBox().getChildren().add(item);
                                break;
                            case "Beverages":
                                Controller.getInstance().getBeveragesBox().getChildren().add(item);
                                break;
                            case "MilkAndEggs":
                                Controller.getInstance().getDairyBox().getChildren().add(item);
                                break;
                            case "Laundry":
                                Controller.getInstance().getLaundryBox().getChildren().add(item);
                                break;
                            default:
                                System.err.println("Unknown category: " + category);
                                break;
                        }
                    }
                }
            }
        } catch (SQLException | IOException e) {
            System.err.println("Error fetching products from database: " + e.getMessage());
        }
    }


    // Helper method to create a product object based on category
    private static Product createProduct(String category, String name, double price, String rating, String imageURL, String extraField, int stock) {
        switch (category) {
            case "Fruit":
                Fruit fruit = new Fruit(name, price, rating, imageURL, extraField,stock);
                fruit.setSeason(extraField);
                return fruit;
            case "Vegetable":
                Vegetable vegetable = new Vegetable(name, price, rating, imageURL, extraField,stock);
                vegetable.setIsOrganic(extraField);
                return vegetable;
            case "Beverages":
                Beverages beverages = new Beverages(name, price, rating, imageURL, extraField,stock);
                beverages.setSize(extraField);
                return beverages;
            case "MilkAndEggs":
                MilkAndEggs milkAndEggs = new MilkAndEggs(name, price, rating, imageURL, extraField,stock);
                milkAndEggs.setExpirationDate(extraField);
                return milkAndEggs;
            case "Laundry":
                Laundry laundry = new Laundry(name, price, rating, imageURL, extraField,stock);
                laundry.setBrand(extraField);
                return laundry;
            default:
                System.err.println("Unknown category: " + category);
                return null;
        }
    }

    // Method to scroll to Fruits category
    @FXML
    public void handleFruitButtonClick(ActionEvent event) {
        scrollToCategory(HFruits);
    }

    // Method to scroll to Vegetables category
    @FXML
    public void handleVegetableButtonClick(ActionEvent event) {
        scrollToCategory(VegeBox);
    }

    // Method to scroll to Beverages category
    @FXML
    public void handleBeveragesButtonClick(ActionEvent event) {
        scrollToCategory(BeveragesBox);
    }

    // Method to scroll to Laundry category
    @FXML
    public void handleLaundryButtonClick(ActionEvent event) {
        scrollToCategory(LaundryBox);
    }

    @FXML
    public void handleDairyButtonClick(ActionEvent event) {

        System.out.println("Dairy");
        scrollToCategory(DairyBox);
    }

    // Helper method to scroll the page to the specified HBox
    private void scrollToCategory(HBox categoryBox) {
        // Calculate the scroll position based on the HBox location
        double scrollPosition = categoryBox.getLayoutY() / MainScrollPane.getContent().getBoundsInParent().getHeight();
        MainScrollPane.setVvalue(scrollPosition);
    }

    /**
     * Handles the click event for the HFruits HBox.
     */
    @FXML
    static void handleClick() {

    }

    /**
     * Handles the cart button click event.
     *
     * @param event the ActionEvent triggered when the cart button is clicked
     */
    @FXML
    public void handleBtnCartClicked(ActionEvent event) {
        System.out.println("ambuttt aniii niyaaaa");
        Customer.viewCart();
    }

    public void handleInventory(ActionEvent event) {
    }
}
