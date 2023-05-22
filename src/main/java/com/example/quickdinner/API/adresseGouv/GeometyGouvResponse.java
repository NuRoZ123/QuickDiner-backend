package com.example.quickdinner.API.adresseGouv;

public class GeometyGouvResponse {
    private String type;
    private double[] coordinates;

    public GeometyGouvResponse(String type, double[] coordinates) {
        this.type = type;
        this.coordinates = coordinates;
    }

    public GeometyGouvResponse() {
    }

    public String getType() {
        return type;
    }

    public double[] getCoordinates() {
        return coordinates;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setCoordinates(double[] coordinates) {
        this.coordinates = coordinates;
    }
}
