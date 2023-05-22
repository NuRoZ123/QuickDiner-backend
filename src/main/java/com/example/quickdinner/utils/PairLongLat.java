package com.example.quickdinner.utils;

public class PairLongLat {
    private double longitude;
    private double latitude;

    public PairLongLat(double longitude, double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public double getLongitude() {
        return this.longitude;
    }

    public double getLatitude() {
        return this.latitude;
    }
}
