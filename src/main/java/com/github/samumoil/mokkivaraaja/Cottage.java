package com.github.samumoil.mokkivaraaja;

import java.time.LocalDateTime;

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
}
