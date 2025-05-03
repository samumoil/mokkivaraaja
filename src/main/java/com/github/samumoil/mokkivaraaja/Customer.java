package com.github.samumoil.mokkivaraaja;

public class Customer {

    int id;
    String name;
    String email;
    String phoneNumber;
    String address;

    public Customer() {
        this.id = 0;
        this.name = "Malli Mallikas";
        this.email = "example@example.com";
        this.phoneNumber = "0441234567";
        this.address = "Mallikatu 1, Mallikaupunki";
    }

    public Customer(int id, String name, String email, String phoneNumber, String address) {
        this.id = id;
        this.name = name.trim();
        this.email = email.trim();
        this.phoneNumber = phoneNumber.trim();
        this.address = address.trim();
    }

    public int getId() {
        return id;
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

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setAddress(String address) {
        this.address = address;
    }

}
