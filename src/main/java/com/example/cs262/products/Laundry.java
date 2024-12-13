package com.example.cs262.products;

import com.example.cs262.model.Product;

public class Laundry extends Product {

    private String brand; // Field specific to Laundry products

    // No-argument constructor required by FXML
    public Laundry() {
        super(); // Call the parent no-argument constructor
    }

    // Constructor: Initialize fields for Laundry products
    public Laundry(String name, double price, String rating, String imageURL, String brand) {
        super(name, price, rating, imageURL); // Initialize fields from the parent class
        this.brand = brand; // Set the brand specific to Laundry products
    }

    // Getter for brand (Encapsulation)
    public String getBrand() {
        return brand;
    }

    // Setter for brand (Encapsulation)
    public void setBrand(String brand) {
        this.brand = brand;
    }

    // Override setData to include brand details (Polymorphism)
    @Override
    public void setData(String name, double price, String rating, String imageURL, int stock) {
        super.setData(name, price, rating, imageURL, stock); // Update common fields
        // The brand is set separately if required
    }

    // Method to display laundry-specific details (Abstraction)
    public String displayDetails() {
        return getName() + " - Price: ₱" + String.format("%.2f", getPrice()) +
                " - Rating: " + getRating() +
                " - Brand: " + (brand != null ? brand : "Unknown");
    }

    // Additional functionality: Check if it's a premium brand
    public boolean isPremiumBrand() {
        // Assuming certain brands are classified as premium
        String[] premiumBrands = {"Tide", "Ariel", "Persil"};
        for (String premium : premiumBrands) {
            if (brand != null && brand.equalsIgnoreCase(premium)) {
                return true;
            }
        }
        return false;
    }
}
