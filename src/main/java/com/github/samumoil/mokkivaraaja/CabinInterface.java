package com.github.samumoil.mokkivaraaja;

public interface CabinInterface {

        // Remember to override every parameter and toString method!
        int id = 0;
        String name = "Mallimökki";
        int beds = 4;
        String streetAddress = "Mökkitiemalli 150";
        String postalCode = "70100";
        String city = "Kuopio";
        float pricePerNight = 84.95F;

        default String getName(int id) {
            return name;
        }

        default int getBeds(int id) {
            return beds;
        }

        default String getStreetAddress(int id) {
            return streetAddress;
        }

        default String getPostalCode(int id) {
            return postalCode;
        }

        default String getCity(int id) {
            return city;
        }

        default float getPricePerNight(int id) {
            return pricePerNight;
        }

        String toString(); // Must implement custom toString for debugging purposes.
}
