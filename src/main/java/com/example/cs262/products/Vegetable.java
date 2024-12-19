package com.example.cs262.products;

import com.example.cs262.model.Product;

public class Vegetable extends Product {

    private String isOrganic; // Field to indicate if the vegetable is organic (String)

    // No-argument constructor required by FXML
    public Vegetable() {
        super();  // Call the parent class no-argument constructor
    }

    // Constructor to initialize both Product fields and Vegetable-specific fields
    public Vegetable(String name, double price, String rating, String imageURL, String isOrganic, int stock) {
        super(name, price, rating, imageURL, stock); // Initialize parent class fields
        this.isOrganic = isOrganic; // Set organic status
    }

    // Getter for isOrganic (Encapsulation)
    public String getIsOrganic() {
        return isOrganic;
    }

    // Setter for isOrganic (Encapsulation)
    public void setIsOrganic(String isOrganic) {
        this.isOrganic = isOrganic;
    }

    // Override setData method to include organic details (Polymorphism)
    @Override
    public void setData(String name, double price, String rating, String imageURL, int stock) {
        super.setData(name, price, rating, imageURL, stock); // Update common product fields
        // Organic status can be updated separately if needed
    }

    // Method to display vegetable-specific details (Abstraction)
    public String displayDetails() {
        return getName() + " - Price: ₱" + String.format("%.2f", getPrice()) +
                " - Rating: " + getRating() +
                " - Organic: " + (isOrganic != null ? isOrganic : "Unknown");
    }
}
