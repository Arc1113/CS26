package com.example.cs262.model;

public abstract class User {
    private static User currentUser ; // Static variable to hold the current user

    // Private fields for user details
    private String userName;
    private String password;
    private String address;
    private String email;
    private String contactnumber;

    public User(String userName, String password, String address, String email, String contactnumber) {
        this.userName = userName;
        this.password = password;
        this.address = address;
        this.email = email;
        this.contactnumber = contactnumber;
    }

    public User() {
    }

    // Getters and setters for user details
    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getAddress() {
        return address;
    }

    public String getEmail() {
        return email;
    }

    public String getContactnumber() {
        return contactnumber;
    }

    // Static methods to manage the current user
    public static void setCurrentUser (User user) {
        currentUser  = user;
    }

    public static User getCurrentUser () {
        return currentUser ;
    }

    public static void logout() {
        currentUser  = null; // Clear the current user
    }


}