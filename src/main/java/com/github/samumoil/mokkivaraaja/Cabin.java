package com.github.samumoil.mokkivaraaja;

public class Cabin {

        int id;
        String name;
        int beds;
        String streetAddress;
        String postalCode;
        String city;
        float pricePerNight;

        public Cabin() {
            this.id = 0;
            this.name = "Mallimökki";
            this.beds = 4;
            this.streetAddress = "Mökkitiemalli 150";
            this.postalCode = "70100";
            this.city = "Kuopio";
            this.pricePerNight = 84.95F;
        } // Constructor for creating dummy cabins. Needed for testing.

        public Cabin(int id, String name, int beds, String streetAddress,
                     String postalCode, String city, float pricePerNight) {
            this.id = id;
            this.name = name;
            this.beds = beds;
            this.streetAddress = streetAddress;
            this.postalCode = postalCode;
            this.city = city;
            this.pricePerNight = pricePerNight;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public int getBeds() {
            return beds;
        }

        public String getStreetAddress() {
            return streetAddress;
        }

        public String getPostalCode() {
            return postalCode;
        }

        public String getCity() {
            return city;
        }

        public float getPricePerNight() {
            return pricePerNight;
        }

        /*
        public void setName(String name) {
            this.name = name.trim();
        }

        public void setBeds(int beds) {
            if (beds >= 0) this.beds = beds;
        }

        public void setStreetAddress(String streetAddress) {
            this.streetAddress = streetAddress.trim();
        }

        public void setPostalCode(String postalCode) {
            this.postalCode = postalCode.trim();
        }

        public void setCity(String city) {
            this.city = city.trim();
        }

        public void setPricePerNight(float pricePerNight) {
            if (pricePerNight >= 0) this.pricePerNight = pricePerNight;
        }
         */


}

