package com.example.cs262.model;

import com.example.cs262.gui.Payment2Controller;
import com.example.cs262.products.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.example.cs262.model.Customer.addProductToCart;
import static com.example.cs262.model.Customer.cartItems;

public class Admin extends User {

    private static Admin instance;

    public Admin(String userName, String password, String address, String email, String contactnumber) {
        super(userName, password, address, email, contactnumber);
    }

    public Admin() {
        // Initialization code if needed
        instance = this;
    }

    // Method to add a product to the database
    public static void addProduct(String category, String name, double price, String rating, String imageURL, String extraField) {
        try {
            // Check if the product already exists in the database
            if (!isProductInDatabase(category, name, price, rating, imageURL, extraField)) {
                insertProductIntoDatabase(category, name, price, rating, imageURL, extraField);
            } else {
                System.out.println("Product already exists in the database. Skipping addition.");
            }
        } catch (SQLException e) {
            System.err.println("Error adding product to database: " + e.getMessage());
        }
    }

    // Helper method to check if a product exists in the database
    private static boolean isProductInDatabase(String category, String name, double price, String rating, String imageURL, String extraField) throws SQLException {
        String checkSql = "SELECT COUNT(*) FROM products WHERE category = ? AND name = ? AND price = ? AND rating = ? AND imageURL = ? AND extraField = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(checkSql)) {
            stmt.setString(1, category);
            stmt.setString(2, name);
            stmt.setDouble(3, price);
            stmt.setString(4, rating);
            stmt.setString(5, imageURL);
            stmt.setString(6, extraField);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    // Helper method to insert a product into the database
    private static void insertProductIntoDatabase(String category, String name, double price, String rating, String imageURL, String extraField) throws SQLException {
        String sql = "INSERT INTO products (category, name, price, rating, imageURL, extraField) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, category);
            stmt.setString(2, name);
            stmt.setDouble(3, price);
            stmt.setString(4, rating);
            stmt.setString(5, imageURL);
            stmt.setString(6, extraField);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Product successfully added to the database.");
            }
        }
    }

    public static Admin getInstance() {
        return instance;
    }

    @FXML
    private Button Inventory;

    @FXML
    private void handleInventory(ActionEvent event) {
//        loadView("/com/example/cs262/Inventory.fxml");
    }

    @FXML
    private BorderPane LoaderPane;


    private void loadView(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            ScrollPane newLoadedPane = loader.load();
            LoaderPane.setCenter(newLoadedPane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private Button selectImageButton; // Button to trigger the file chooser

    @FXML
    private TextField imageTextField; // The TextField where the image path will be displayed

    // Method to handle the file chooser for selecting a photo
    @FXML
    public void selectImage() {
        // Create a FileChooser
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif"));

        // Open the file chooser dialog
        File selectedFile = fileChooser.showOpenDialog(selectImageButton.getScene().getWindow());

        // If a file was selected, set the path to the TextField
        if (selectedFile != null) {
            // Get the absolute path
            String absolutePath = selectedFile.getAbsolutePath();

            // Convert the absolute path to a relative resource path
            String relativePath = convertToResourcePath(absolutePath);

            // Set the relative path to the TextField
            imageTextField.setText(relativePath);
        }
    }

    // Helper method to convert the absolute path to a resource path
    private String convertToResourcePath(String absolutePath) {
        // Assuming your project structure is as follows:
        // src/main/resources/com/example/cs262/Fruits/Fruits.png
        // You can adjust the base path according to your project structure
        String basePath = new File("src/main/resources").getAbsolutePath();

        // Replace the base path with an empty string to get the relative path
        String relativePath = absolutePath.replace(basePath, "");

        // Normalize the path to ensure it starts with a '/'
        return relativePath.replace("\\", "/"); // Handle Windows file separators
    }


    @FXML
    private ComboBox<String> comboBox;


    public void setupComboBox() {
        if (comboBox == null) {
            System.err.println("ComboBox is not initialized!");
            return; // Exit if comboBox is null
        }

        ObservableList<String> items = FXCollections.observableArrayList("Fruit", "Vegetable", "Beverages", "MilkAndEggs", "Laundry");
        comboBox.setItems(items);
        comboBox.setOnAction(event -> {
            String selectedItem = comboBox.getSelectionModel().getSelectedItem();
            System.out.println("Selected Item: " + selectedItem);
        });
    }

    @FXML
    private void initialize() {
        setupComboBox();
        Admin.displayAllProducts();
    }

    // Method to display all products from the database in their respective sections
    public static void displayAllProducts() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT * FROM products";
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

                    FXMLLoader loader = new FXMLLoader(Admin.class.getResource("/com/example/cs262/AdminItem.fxml"));
                    AnchorPane item = loader.load();

                    Product controller = loader.getController();
                    controller.setData(name, price, rating, imageURL, stock);

                    // Add item to the appropriate section based on its category
                    switch (category) {
                        case "Fruit":
                            if (Admin.getInstance().getHFruits() != null) {
                                Admin.getInstance().getHFruits().getChildren().add(item);
                            }
                            break;
                        case "Vegetable":
                            if (Admin.getInstance().getVegeBox() != null) {
                                Admin.getInstance().getVegeBox().getChildren().add(item);
                            }
                            break;
                        case "Beverages":
                            if (Admin.getInstance().getBeveragesBox() != null) {
                                Admin.getInstance().getBeveragesBox().getChildren().add(item);
                            }
                            break;
                        case "MilkAndEggs":
                            if (Admin.getInstance().getDairyBox() != null) {
                                Admin.getInstance().getDairyBox().getChildren().add(item);
                            }
                            break;
                        case "Laundry":
                            if (Admin.getInstance().getLaundryBox() != null) {
                                Admin.getInstance().getLaundryBox().getChildren().add(item);
                            }
                            break;
                        default:
                            System.err.println("Unknown category: " + category);
                            break;
                    }

//                    Admin.getInstance().getAdminProductLoad().getChildren().add(item);
                }
            }
        } catch (SQLException | IOException e) {
            System.err.println("Error fetching products from database: " + e.getMessage());
        }
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
    private HBox HFruits;

    public HBox getHFruits() {
        return this.HFruits;
    }

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
    private HBox LaundryBox;
    public HBox getLaundryBox() {
        return LaundryBox;
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


    @FXML
    private Button AddProductButton;

    @FXML
    private void handleAddProduct() {
        try {
            // Load the AddProductForm.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/cs262/AddProductForm.fxml"));
            Parent root = loader.load();

            // Create a new Stage (window) for the pop-up form
            Stage stage = new Stage();
            stage.setTitle("Add Product");
            stage.setScene(new Scene(root));

            // Show the pop-up window
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showError("Failed to open Add Product form.");
        }
    }

    @FXML
    private TextField ProductNameExtraField;

    @FXML
    private TextField ProductNameField;

    @FXML
    private TextField ProductNamePrice;

    @FXML
    private TextField ProductNameRating;

    @FXML
    private Button Addtodatabasebutton;

    @FXML
    private void handleAddtodatabasebutton() {

        String Category = comboBox.getSelectionModel().getSelectedItem();
        String Name = ProductNameField.getText();
        double Price = Double.parseDouble(ProductNamePrice.getText());
        String Rating = ProductNameRating.getText();
        String ImageURL = imageTextField.getText();
        String ExtraField = ProductNameExtraField.getText();

        addProduct(Category, Name, Price, Rating, ImageURL, ExtraField);

    }


    public List<Product> retrieveProductsFromDatabase() {
        List<Product> products = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT * FROM products";
            try (PreparedStatement stmt = conn.prepareStatement(sql);
                 ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    String category = rs.getString("category");
                    String name = rs.getString("name");
                    double price = rs.getDouble("price");
                    String rating = rs.getString("rating");
                    String imageURL = rs.getString("imageURL");
                    String extraField = rs.getString("extraField");

                    // Create a Product object and add it to the list
                    Product product = new Product(name, price, rating, imageURL);
                    product.setCategory(category);
                    product.setExtraField(extraField);
                    products.add(product);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving products from database: " + e.getMessage());
        }

        return products; // Return the list of products
    }

    List<Product> products = retrieveProductsFromDatabase();


    @FXML
    private VBox AdminProductLoad;
    /**
     * Provides access to the VegeBox HBox.
     *
     * @return the VegeBox HBox
     */
    public VBox getAdminProductLoad() {
        return AdminProductLoad;
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void loadproducts() {

        for (Product product : products) {
            try {
                FXMLLoader itemLoader = new FXMLLoader(getClass().getResource("/com/example/cs262/PaymentBar.fxml"));
                Parent itemRoot = itemLoader.load();

                // Get the controller of the PaymentBar.fxml
                Payment2Controller controller = itemLoader.getController();
                controller.setData(
                        product.getName(),
                        product.getPrice(),
                        product.getImageURL(),
                        String.valueOf(0)
                );

                // Add the item to the VBox

                //  AdminProductLoad.getChildren().add(itemRoot);

            } catch (IOException e) {
                System.err.println("Error loading PaymentBar.fxml: " + e.getMessage());
            }
        }
    }


    @FXML
    public void handleCancelButton(ActionEvent actionEvent) {
        // Get the current stage (window) that the button belongs to
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

        // Close the current stage (this is the pop-up window)
        stage.close();

        // If you want to bring the main window back into focus:
        Stage mainStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        mainStage.requestFocus();
    }
}