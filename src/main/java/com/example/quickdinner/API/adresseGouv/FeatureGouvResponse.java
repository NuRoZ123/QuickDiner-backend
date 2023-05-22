package com.example.quickdinner.API.adresseGouv;

public class FeatureGouvResponse {
    private String type;
    private GeometyGouvResponse geometry;
    private PropertiesGouvResponse properties;

    public FeatureGouvResponse(String type, GeometyGouvResponse geometry, PropertiesGouvResponse properties) {
        this.type = type;
        this.geometry = geometry;
        this.properties = properties;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public GeometyGouvResponse getGeometry() {
        return geometry;
    }

    public void setGeometry(GeometyGouvResponse geometry) {
        this.geometry = geometry;
    }

    public PropertiesGouvResponse getProperties() {
        return properties;
    }

    public void setProperties(PropertiesGouvResponse properties) {
        this.properties = properties;
    }
}
