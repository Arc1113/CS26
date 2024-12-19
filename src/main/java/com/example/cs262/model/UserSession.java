package com.example.cs262.model;

public class UserSession {
    private static Customer currentCustomer;

    public static void setCurrentCustomer(Customer customer) {
        currentCustomer = customer;
    }

    public static Customer getCurrentCustomer() {
        return currentCustomer;
    }

    public static void clearSession() {
        currentCustomer = null;
    }
}
