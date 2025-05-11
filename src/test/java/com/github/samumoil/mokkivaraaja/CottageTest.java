package com.github.samumoil.mokkivaraaja; // Same package as the class under test

import com.github.samumoil.mokkivaraaja.domain.object.Cottage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.List;


import static org.junit.jupiter.api.Assertions.*; // Using JUnit 5 Assertions

@DisplayName("Cottage Class Tests")
class CottageTest {

    // Define common test data as constants
    private final int TEST_ID = 1;
    private final String TEST_NAME = "Test Cabin";
    private final String TEST_DESC = "A nice test cabin";
    private final String TEST_LOC = "Test Location, Test City";
    private final int TEST_CAPACITY = 4;
    // Use a fixed date/time for predictable testing, truncate nanos as DB might not store them
    private final LocalDateTime TEST_CREATED_AT = LocalDateTime.of(2023, Month.MAY, 5, 10, 30, 0).truncatedTo(ChronoUnit.SECONDS);
    private final int TEST_OWNER_ID = 100;
    private final float TEST_PRICE = 99.95f;

    private Cottage cottage; // Test instance

    @BeforeEach
    void setUp() {
        // Create a fresh cottage object before each test using the full constructor
        cottage = new Cottage();
        cottage.setId(TEST_ID);
        cottage.setName(TEST_NAME);
        cottage.setDescription(TEST_DESC);
        cottage.setLocation(TEST_LOC);
        cottage.setCapacity(TEST_CAPACITY);
        cottage.setCreatedAt(TEST_CREATED_AT);
        cottage.setOwnerId(TEST_OWNER_ID);
    }



    @Nested
    @DisplayName("Getter and Setter Tests")
    class GetterSetterTests {

        @Test
        @DisplayName("setName and getName")
        void testNameGetterSetter() {
            String newName = "Updated Cabin Name";
            cottage.setName(newName);
            assertEquals(newName, cottage.getName());
        }

        @Test
        @DisplayName("setCapacity and getCapacity")
        void testCapacityGetterSetter() {
            int newCapacity = 6;
            cottage.setCapacity(newCapacity);
            assertEquals(newCapacity, cottage.getCapacity());
        }

        @Test
        @DisplayName("setPricePerNight and getPricePerNight")
        void testPriceGetterSetter() {
            float newPrice = 120.50f;
            cottage.setPricePerNight(newPrice);
            assertEquals(newPrice, cottage.getPricePerNight());
        }

        // --- Add similar tests for other getters/setters ---
        // setId/getId, setDescription/getDescription, setLocation/getLocation,
        // setCreatedAt/getCreatedAt, setOwnerId/getOwnerId
        @Test
        @DisplayName("setLocation and getLocation")
        void testLocationGetterSetter() {
            String newLocation = "New Location Rd";
            cottage.setLocation(newLocation);
            assertEquals(newLocation, cottage.getLocation());
        }
    }

    @Nested
    @DisplayName("Derived Getter Logic Tests")
    class DerivedGetterTests {

        @Test
        @DisplayName("getAge should calculate years correctly")
        void getAge_CalculatesYears() {
            // To make test independent of exact run time, set createdAt relative to a fixed point
            LocalDateTime fixedNow = LocalDateTime.of(2025, Month.MAY, 5, 14, 0, 0); // A fixed reference "now"

            // Test case 1: Exactly 2 years old relative to fixedNow
            cottage.setCreatedAt(fixedNow.minusYears(2));
            // We need to compare against fixedNow, the getAge() uses LocalDateTime.now()
            // This highlights a testability issue - getAge() isn't easily testable with exact values.
            // A better approach would be cottage.getAge(LocalDateTime referenceTime)
            // For now, we test the logic approximately:
            long years = ChronoUnit.YEARS.between(cottage.getCreatedAt(), LocalDateTime.now());
            assertEquals(String.valueOf(years), cottage.getAge(), "Age calculation relative to actual now");
            // Let's assert it's likely 2 if run soon after fixedNow was defined
            assertTrue(List.of("1", "2", "3").contains(cottage.getAge()), "Age should be around 2 years");


            // Test case 2: Less than 1 year old
            cottage.setCreatedAt(fixedNow.minusMonths(6));
            years = ChronoUnit.YEARS.between(cottage.getCreatedAt(), LocalDateTime.now());
            assertEquals(String.valueOf(years), cottage.getAge());
            assertTrue(List.of("0", "1").contains(cottage.getAge()), "Age should be around 0 years");
        }

        @Test
        @DisplayName("getAddress should return location")
        void getAddress_ReturnsLocation() {
            assertEquals(TEST_LOC, cottage.getAddress());
            cottage.setLocation("New Place");
            assertEquals("New Place", cottage.getAddress());
        }

        @Test
        @DisplayName("getSize should return capacity as String")
        void getSize_ReturnsCapacityString() {
            assertEquals(String.valueOf(TEST_CAPACITY), cottage.getSize());
            cottage.setCapacity(8);
            assertEquals("8", cottage.getSize());
        }

        @Test
        @DisplayName("getNumber/getCottageNumber should return id as String")
        void getNumber_ReturnsIdString() {
            assertEquals(String.valueOf(TEST_ID), cottage.getNumber());
            assertEquals(String.valueOf(TEST_ID), cottage.getCottageNumber());
            cottage.setId(123);
            assertEquals("123", cottage.getNumber());
            assertEquals("123", cottage.getCottageNumber());
        }
    }

    @Nested
    @DisplayName("Anomalous Setter Method Tests")
    class AnomalousSetterTests {

        // These tests verify the current (no-op) behavior
        @Test
        void setAddress_shouldDoNothingCurrently() {
            String initialLocation = cottage.getLocation();
            cottage.setAddress("This should have no effect");
            assertEquals(initialLocation, cottage.getLocation(), "setAddress currently does nothing");
            // RECOMMENDATION: Implement setAddress to call setLocation, or remove setAddress.
        }

        @Test
        void setAge_shouldDoNothingCurrently() {
            LocalDateTime initialDate = cottage.getCreatedAt();
            cottage.setAge(10); // This call does nothing
            assertEquals(initialDate, cottage.getCreatedAt(), "setAge currently does nothing");
            // RECOMMENDATION: Remove setAge method.
        }

        @Test
        void setSize_shouldDoNothingCurrently() {
            int initialCapacity = cottage.getCapacity();
            cottage.setSize(20); // This call does nothing
            assertEquals(initialCapacity, cottage.getCapacity(), "setSize currently does nothing");
            // RECOMMENDATION: Implement setSize to call setCapacity, or remove setSize.
        }
    }
}