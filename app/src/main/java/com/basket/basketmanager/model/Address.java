package com.basket.basketmanager.model;


public class Address {
    private String street;
    private int streetNumber;

    public Address(String street, int streetNumber) {
        if (street != null && streetNumber > 0) {
            this.street = street;
            this.streetNumber = streetNumber;
        } else {
            throw new IllegalArgumentException();
        }
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public int getStreetNumber() {
        return streetNumber;
    }

    public void setStreetNumber(int streetNumber) {
        this.streetNumber = streetNumber;
    }

    public String toString() {
        return street + ", " + streetNumber;
    }
}
