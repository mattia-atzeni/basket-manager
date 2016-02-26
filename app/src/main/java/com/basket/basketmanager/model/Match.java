package com.basket.basketmanager.model;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class Match implements Comparable<Match> {
    private long id;
    private String homeTeam;
    private String visitingTeam;
    private GregorianCalendar calendar;
    private Referee firstReferee;
    private Referee secondReferee;
    private String place;
    private String category;
    private Address address;
    private List<StopOver> plan;
    private String score;

    public Match() {
        calendar = new GregorianCalendar();
    }

    public String getHomeTeam() {
        return homeTeam;
    }

    public void setHomeTeam(String homeTeam) {
        this.homeTeam = homeTeam;
    }

    public String getVisitingTeam() {
        return visitingTeam;
    }

    public void setVisitingTeam(String visitingTeam) {
        this.visitingTeam = visitingTeam;
    }

    public String getDate() {
        return DateFormatter.formatDate(calendar);
    }

    public String getTime() {
        return DateFormatter.formatTime(calendar);
    }

    public String getDateTime() {
        return DateFormatter.formatDateTime(calendar);
    }

    public void setDateTime(String date) {
        this.calendar = DateFormatter.parseDateTime(date);
    }

    public Referee getFirstReferee() {
        return firstReferee;
    }

    public void setFirstReferee(Referee firstReferee) {
        this.firstReferee = firstReferee;
    }

    public Referee getSecondReferee() {
        return secondReferee;
    }

    public void setSecondReferee(Referee secondReferee) {
        this.secondReferee = secondReferee;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public void setCategory(String category) { this.category = category; }

    public String getCategory() { return category; }

    public Address getAddress() { return address; }

    public void setAddress(String street, int streetNumber) {
        this.address = new Address(street, streetNumber);
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Calendar getCalendar() {
        return this.calendar;
    }

    @Override
    public int compareTo(@NonNull Match another) {
        return this.calendar.getTime().compareTo(another.calendar.getTime());
    }

    public List<StopOver> getPlan() {
        return plan;
    }

    public void setPlan(List<StopOver> plan) {
        this.plan = plan;
    }

    public String getDateExtended() {
        return DateFormatter.formatDateExtended(calendar);
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }
}
