package com.basket.basketmanager.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

public class Referee {

    private String id;
    private String firstName;
    private String lastName;
    private String birthDate;
    private String email;
    private String place;
    private String address;
    private String phone;
    private String category;

    public Referee(String id, String firstName, String lastName) {
        setFirstName(firstName);
        setLastName(lastName);
        setId(id);
    }

    public String getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public String getEmail() {
        return email;
    }

    public String getCategory() {
        return category;
    }

    public String getPlace() {
        return place;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    public Referee setId(String id) {
        this.id = id;
        return this;
    }

    public Referee setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public Referee setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public Referee setPlace(String place) {
        this.place = place;
        return this;
    }

    public Referee setAddress(String address) {
        this.address = address;
        return this;
    }

    public Referee setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public Referee setCategory(String category) {
        this.category = category;
        return this;
    }

    public Referee setBirthDate(String birthDate) {
        SimpleDateFormat formatterIT = new SimpleDateFormat("dd/MM/yyyy");
        try {
            formatterIT.parse(birthDate);
            this.birthDate = birthDate;
        } catch (ParseException e) {
            throw new IllegalArgumentException();
        }
        return this;
    }

    public Referee setEmail(String email) {
        try {
            new InternetAddress(email).validate();
            this.email = email;
        } catch (AddressException e) {
            throw new IllegalArgumentException();
        }
        return this;
    }

    public String toString() {
        return firstName + " " + lastName;
    }
}
