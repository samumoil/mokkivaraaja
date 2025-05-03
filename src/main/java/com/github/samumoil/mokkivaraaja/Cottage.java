package com.github.samumoil.mokkivaraaja;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * The Cottage class represents a cottage with its details such as name, description, location, capacity, etc.
 */
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

    public Cottage(
            int id,
            String name,
            String description,
            String location,
            int capacity,
            LocalDateTime createdAt,
            int ownerId
    ) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.location = location;
        this.capacity = capacity;
        this.createdAt = createdAt;
        this.ownerId = ownerId;
        this.pricePerNight = 0.0f;
    }

    public Cottage(
            int id,
            String name,
            String description,
            String location,
            int capacity,
            LocalDateTime createdAt,
            int ownerId,
            float pricePerNight
    ) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.location = location;
        this.capacity = capacity;
        this.createdAt = createdAt;
        this.ownerId = ownerId;
        this.pricePerNight = pricePerNight;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getLocation() { return location; }
    public int getCapacity() { return capacity; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public int getOwnerId() { return ownerId; }
    public float getPricePerNight() { return pricePerNight; }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public void setPricePerNight(float pricePerNight) {
        this.pricePerNight = pricePerNight;
    }

    /**
     * Returns the address of the cottage.
     * @return The location of the cottage.
     */
    public String getAddress() {
        return location; // Assuming 'location' is the address of the cottage
    }

    /**
     * Calculates and returns the age of the cottage in years.
     * @return The age of the cottage as a string.
     */
    public String getAge() {
        long years = ChronoUnit.YEARS.between(createdAt, LocalDateTime.now());
        return String.valueOf(years);
    }

    /**
     * Returns the size of the cottage, which is represented by the capacity.
     * @return The capacity of the cottage as a string.
     */
    public String getSize() {
        return String.valueOf(capacity); // Assuming capacity reflects the size
    }

    /**
     * Returns the number (ID) of the cottage.
     * @return The ID of the cottage as a string.
     */
    public String getNumber() {
        return String.valueOf(id); // ID as the number of the cottage
    }

    /**
     * Returns the cottage number, which is the ID of the cottage.
     * @return The ID of the cottage as a string.
     */
    public String getCottageNumber() {
        return String.valueOf(id); // ID as the cottage number
    }
}
