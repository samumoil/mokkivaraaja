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
            this.name = name;
            this.phoneNumber = phoneNumber;
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

        public void setName(String name) {
            this.name = name.trim();
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber.trim();
        }

}
