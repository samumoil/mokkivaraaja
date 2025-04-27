package com.github.samumoil.mokkivaraaja;

public class Customer {

        int id;
        String name;
        String phoneNumber;

        public Customer() {
            this.id = 0;
            this.name = "Malli Mallikas";
            this.phoneNumber = "0441234567";
        }

        public Customer(int id, String name, String phoneNumber) {
            this.id = id;
            this.name = name.trim();
            this.phoneNumber = phoneNumber.trim();
        }

        public int getId() {
                return id;
            }

        public String getName() {
            return name;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

}
