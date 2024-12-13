package com.example.cs262.products;

import com.example.cs262.model.Product;

public class MilkAndEggs extends Product {

    private String expirationDate; // Field specific to Milk and Eggs category

    // No-argument constructor required by FXML
    public MilkAndEggs() {
        super(); // Call the parent no-argument constructor
    }

    // Constructor: Initializes fields for Milk and Eggs category
    public MilkAndEggs(String name, double price, String rating, String imageURL, String expirationDate) {
        super(name, price, rating, imageURL); // Initialize parent class fields
        this.expirationDate = expirationDate; // Set expiration date
    }

    // Getter for expirationDate (Encapsulation)
    public String getExpirationDate() {
        return expirationDate;
    }

    // Setter for expirationDate (Encapsulation)
    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    // Override setData to include expiration date (Polymorphism)
    @Override
    public void setData(String name, double price, String rating, String imageURL, int stock) {
        super.setData(name, price, rating, imageURL, stock); // Update common fields
        // Expiration date can be set separately if needed
    }

    // Additional functionality: Check if the product is expired
    public boolean isExpired(String currentDate) {
        // Assuming the dates are in "yyyy-MM-dd" format
        return expirationDate != null && currentDate.compareTo(expirationDate) > 0;
    }

    // Method to display milk and eggs-specific details (Abstraction)
    public String displayDetails() {
        return getName() + " - Price: ₱" + String.format("%.2f", getPrice()) +
                " - Rating: " + getRating() +
                " - Expiration Date: " + (expirationDate != null ? expirationDate : "Unknown");
    }
}
