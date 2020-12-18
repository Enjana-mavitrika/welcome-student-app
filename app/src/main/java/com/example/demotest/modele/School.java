package com.example.demotest.modele;

import lombok.Data;

@Data
public class School {
    public String qid;
    public String name;
    public double latitude;
    public double longitude;
    public String url;
    public String website;
    public String geo;
    public double distanceMinFromSchool;

    public School() {

    }

    public School(String qid, String name, double latitude, double longitude, String url, String website) {
        this.qid = qid;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.url = url;
        this.website = website;
    }

    public School(String name, String geo, String website, String wikidataEntity) {
        this.qid = this.extractQid(wikidataEntity);
        this.name = name;
        this.latitude = this.getLatitude(geo);
        this.longitude = this.getLongitude(geo);
        this.geo = "Point(" + latitude + " " + longitude + ")";
        this.website = website;
    }
    
    public School(String qid, String name, double longitude, double lattitude, String url, double distanceFromSchool) {
        this(qid, name, lattitude, longitude, url, "");
        this.distanceMinFromSchool = distanceFromSchool;
    }

	private String extractQid(String entityUri) {
        return "Q" + entityUri.split("Q")[1];
    }

    private double getLatitude(String geo) {
        return Double.parseDouble(geo.split(" ")[1].split("\\)")[0]);
    }

    private double getLongitude(String geo) {
        return Double.parseDouble(geo.split(" ")[0].split("\\(")[1]);
    }
    
}
