package com.example.quickdinner.API.adresseGouv;

public class AdresseGouvResponse {
    private String type;
    private String version;
    private FeatureGouvResponse[] features;
    private String attribution;
    private String licence;
    private String query;
    private int limit;

    public AdresseGouvResponse(String type, String version, FeatureGouvResponse[] features, String attribution, String licence, String query, int limit) {
        this.type = type;
        this.version = version;
        this.features = features;
        this.attribution = attribution;
        this.licence = licence;
        this.query = query;
        this.limit = limit;
    }

    public AdresseGouvResponse() {
    }

    public String getType() {
        return type;
    }

    public String getVersion() {
        return version;
    }

    public FeatureGouvResponse[] getFeatures() {
        return features;
    }

    public String getAttribution() {
        return attribution;
    }

    public String getLicence() {
        return licence;
    }

    public String getQuery() {
        return query;
    }

    public int getLimit() {
        return limit;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void setFeatures(FeatureGouvResponse[] features) {
        this.features = features;
    }

    public void setAttribution(String attribution) {
        this.attribution = attribution;
    }

    public void setLicence(String licence) {
        this.licence = licence;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }
}
