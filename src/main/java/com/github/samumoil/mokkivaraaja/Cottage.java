package com.github.samumoil.mokkivaraaja;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Cottage {
    private int id;
    private String name;
    private String description;
    private String location;
    private int capacity;
    private LocalDateTime createdAt;
    private int ownerId;
    private float pricePerNight;

    public Cottage() {
        this.id = 0;
        this.name = "";
        this.description = "";
        this.location = "";
        this.capacity = 0;
        this.createdAt = LocalDateTime.now();
        this.ownerId = 0;
        this.pricePerNight = 0.0f;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getLocation() { return location; }
    public int getCapacity() { return capacity; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public int getOwnerId() { return ownerId; }
    public float getPricePerNight() { return pricePerNight; }

    public void setId(int id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description; }
    public void setLocation(String location) { this.location = location; }
    public void setCapacity(int capacity) { this.capacity = capacity; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public void setOwnerId(int ownerId) { this.ownerId = ownerId; }
    public void setPricePerNight(float pricePerNight) { this.pricePerNight = pricePerNight; }

    public String getAddress() {
        return location;
    }

    public String getAge() {
        long years = ChronoUnit.YEARS.between(createdAt, LocalDateTime.now());
        return String.valueOf(years);
    }

    public String getSize() {
        return String.valueOf(capacity);
    }

    public String getNumber() {
        return String.valueOf(id);
    }

    public String getCottageNumber() {
        return String.valueOf(id);
    }

    public void setAddress(String address) {
        this.location = address;
    }

    public void setAge(int age) {
        this.createdAt = LocalDateTime.now().minusYears(age);
    }

    public void setSize(int size) {
        this.capacity = size;
    }

    public void setNumber(String trim) {
        try {
            this.id = Integer.parseInt(trim);
        } catch (NumberFormatException e) {
            this.id = 0;
        }
    }
}
