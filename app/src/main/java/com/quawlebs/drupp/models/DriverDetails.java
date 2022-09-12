package com.quawlebs.drupp.models;

public class DriverDetails {

    private int id, availability;
    private Integer type;
    private String name;
    private double latitude, longitude;
    private double bearing;

    public DriverDetails() {

    }

    public DriverDetails(int id, String name, double latitude, double longitude,double bearing) {
        this.id = id;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.bearing = bearing;
    }

    public DriverDetails(int id, int availability, String name, double latitude, double longitude,double bearing) {
        this.id = id;
        this.availability = availability;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.bearing = bearing;
    }

    public double getBearing() {
        return bearing;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getAvailability() {
        return availability;
    }

    public void setAvailability(int availability) {
        this.availability = availability;
    }
}
