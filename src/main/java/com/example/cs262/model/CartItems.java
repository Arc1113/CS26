package com.example.cs262.model;

public class CartItems {

    private int id;
    private int quantity; // Instance variable for quantity
    private int stock; // Instance variable for quantity
    private double price;
    private String name;
    private String rating;
    private String image;
    private String description;


    // Constructor
    public CartItems( String name, double price, String rating, String image) {
        this.name = name;
        this.price = price;
        this.rating = rating;
        this.image = image;
        this.quantity = 1; // Default quantity
    }


    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        if (quantity > 0) {
            this.quantity = quantity; // Ensure valid quantity
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
