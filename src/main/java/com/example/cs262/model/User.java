package com.example.cs262.model;

/**
 * Abstract base class representing a user with encapsulated details.
 */
abstract class User {

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

    /**
     * Gets the username of the user.
     *
     * @return the username
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Sets the username of the user.
     *
     * @param userName the username to set
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * Gets the password of the user.
     *
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password of the user.
     *
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gets the address of the user.
     *
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * Sets the address of the user.
     *
     * @param address the address to set
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Gets the email of the user.
     *
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email of the user.
     *
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    public String getContactnumber() {
        return contactnumber;
    }
    public void setContactnumber(String contactnumber) {
        this.contactnumber=contactnumber;
    }
}
