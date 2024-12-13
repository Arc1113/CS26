package com.example.cs262.products;

import com.example.cs262.model.Product;

public class Fruit extends Product {

    private String season; // Field specific to the Fruit category

    // No-argument constructor required by FXML
    public Fruit() {
        super(); // Call the parent no-argument constructor
    }

    // Constructor: Initialize fields for the Fruit category
    public Fruit(String name, double price, String rating, String imageURL, String season) {
        super(name, price, rating, imageURL); // Initialize fields from the parent class
        this.season = season;
    }

    // Getter for season (Encapsulation)
    public String getSeason() {
        return season;
    }

    // Setter for season (Encapsulation)
    public void setSeason(String season) {
        this.season = season;
    }

    // Override setData to include season details (Polymorphism)
    @Override
    public void setData(String name, double price, String rating, String imageURL, int stock) {
        super.setData(name, price, rating, imageURL, stock); // Update common fields
        // No need to set the season here as it is handled in the constructor or separately
    }

    // Additional functionality: Check if the fruit is in season
    public boolean isInSeason(String currentSeason) {
        // Assume season values like "Winter", "Summer", "All Year"
        return season != null && (season.equalsIgnoreCase("All Year") || season.equalsIgnoreCase(currentSeason));
    }
}
