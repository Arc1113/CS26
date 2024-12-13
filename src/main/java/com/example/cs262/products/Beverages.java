package com.example.cs262.products;

import com.example.cs262.model.Product;

public class Beverages extends Product {

    private String size; // Field specific to the Beverages category (e.g., "Small", "Medium", "Large")

    // Constructor: Initialize fields for Beverages
    public Beverages(String name, double price, String rating, String imageURL, String size) {
        super(name, price, rating, imageURL); // Call the parent constructor to initialize product fields
        this.size = (size != null && !size.isEmpty()) ? size : "Unknown"; // Set size, default to "Unknown" if null or empty
    }

    // No-argument constructor required by FXML
    public Beverages() {
        super(); // Call the parent no-argument constructor
        this.size = "Unknown"; // Provide a default size to avoid null
    }

    // Getter for size (Encapsulation)
    public String getSize() {
        return size;
    }

    // Setter for size (Encapsulation)
    public void setSize(String size) {
        this.size = (size != null && !size.isEmpty()) ? size : "Unknown"; // Set size and handle null/empty
    }

    // Override setData to include size details (Polymorphism)
    @Override
    public void setData(String name, double price, String rating, String imageURL, int stock) {
        super.setData(name, price, rating, imageURL, stock); // Call the parent class method to set product data
        // The size field can be updated separately through the setSize() method
    }

    // Additional functionality: Check if the beverage is a large size
    public boolean isLargeSize() {
        return size != null && size.equalsIgnoreCase("Large");
    }

    // Method to display beverage-specific details (Abstraction)
    public String displayDetails() {
        return getName() + " - Price: ₱" + String.format("%.2f", getPrice()) +
                " - Rating: " + getRating() +
                " - Size: " + (size != null ? size : "Unknown");
    }
}
