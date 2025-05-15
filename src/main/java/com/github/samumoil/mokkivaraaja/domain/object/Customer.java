package com.github.samumoil.mokkivaraaja.domain.object;

/**
 * The Customer class represents a customer entity and encapsulates details
 * including their unique identification, associated user ID, name, email,
 * phone number, and address. It provides getter and setter methods for
 * managing these details and additional utility methods for handling names.
 */
public class Customer {
    private int id;
    private int userId;
    private String name;
    private String email;
    private String phoneNumber;
    private String address;

    public Customer() {
        this.id = 0;
        this.userId = 0;
        this.name = "Malli Mallikas";
        this.email = "example@example.com";
        this.phoneNumber = "0441234567";
        this.address = "Mallikatu 1, Mallikaupunki";
    }

    public Customer(int id, int userId, String name, String email, String phoneNumber, String address) {
        this.id = id;
        this.userId = userId;
        this.name = name.trim();
        this.email = email.trim();
        this.phoneNumber = phoneNumber.trim();
        this.address = address.trim();
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setId(int id) {
        this.id = id;
    }

    /**
     * setter for the new userId field
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setName(String name) {
        this.name = name.trim();
    }

    public void setEmail(String email) {
        this.email = email.trim();
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber.trim();
    }

    public void setAddress(String address) {
        this.address = address.trim();
    }

    public String getFirstName() {
        if (name != null && name.contains(" ")) {
            return name.split(" ")[0];
        }
        return name;
    }

    public String getLastName() {
        if (name != null && name.contains(" ")) {
            String[] parts = name.split(" ");
            return parts[parts.length - 1];
        }
        return "";
    }

    public void setCottageId(int i) {
    }

    public void setFirstName(String part) {
    }

    public void setLastName(String part) {
    }

    public String getPhone() {
        return phoneNumber;
    }
}
