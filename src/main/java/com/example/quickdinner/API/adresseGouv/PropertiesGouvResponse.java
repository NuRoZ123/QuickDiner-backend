package com.example.quickdinner.API.adresseGouv;

public class PropertiesGouvResponse {
    private String label;
    private double score;
    private String housenumber;
    private String id;
    private String name;
    private String postcode;
    private String citycode;
    private double x;
    private double y;
    private String city;
    private String context;
    private String type;
    private double importance;
    private String street;

    public PropertiesGouvResponse(String label, double score, String housenumber, String id, String name, String postcode, String citycode, double x, double y, String city, String context, String type, double importance, String street) {
        this.label = label;
        this.score = score;
        this.housenumber = housenumber;
        this.id = id;
        this.name = name;
        this.postcode = postcode;
        this.citycode = citycode;
        this.x = x;
        this.y = y;
        this.city = city;
        this.context = context;
        this.type = type;
        this.importance = importance;
        this.street = street;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public String getHousenumber() {
        return housenumber;
    }

    public void setHousenumber(String housenumber) {
        this.housenumber = housenumber;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getCitycode() {
        return citycode;
    }

    public void setCitycode(String citycode) {
        this.citycode = citycode;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getImportance() {
        return importance;
    }

    public void setImportance(double importance) {
        this.importance = importance;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }
}
