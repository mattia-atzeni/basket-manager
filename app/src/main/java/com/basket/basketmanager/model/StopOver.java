package com.basket.basketmanager.model;


public class StopOver implements Cloneable {
    private String place;
    private int transport = -1;
    private boolean driver = false;

    public StopOver(String place) {
        setPlace(place);
    }

    public StopOver(String place, int transport) {
        setTransport(transport);
        setPlace(place);
    }

    public StopOver(String place, int transport, boolean driver) {
        setTransport(transport);
        setPlace(place);
        setDriver(driver);
    }

    public StopOver(StopOver other) {
        this(other.place, other.transport, other.driver);
    }


    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        if (place == null) {
            throw new IllegalArgumentException();
        }
        this.place = place;
    }

    public int getTransport() {
        return transport;
    }

    public void setTransport(int transport) {
        this.transport = transport;
    }

    public boolean isDriver() {
        return driver;
    }

    public void setDriver(boolean flagDriver) {
        this.driver = flagDriver;
    }


    public boolean isTransportSet() {
        return transport >= 0;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof StopOver)) return false;

        StopOver stopOver = (StopOver) other;

        return place.equals(stopOver.place) &&
                transport == stopOver.transport &&
                driver ==  stopOver.driver;
    }

    @Override
    public int hashCode() {
        int result = place != null ? place.hashCode() : 0;
        result = 31 * result + transport;
        result = 31 * result + (driver ? 1 : 0);
        return result;
    }

    @Override
    public StopOver clone() {
        try {
            StopOver clone = (StopOver) super.clone();
            clone.setPlace(this.place);
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}
