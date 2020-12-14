package com.example.demotest.modele;

import lombok.Data;

@Data
public class Stop {

    public String id;
    public String name;
    public double longitude;
    public double latitude;
    public String geo;
    public String url;
    public double distanceMinFromSchool;
    public String refSchool;

    public Stop() {}

    public Stop(String id, String name, String longitude, String latitude) {
        this.id = id;
        this.name = name;
        this.longitude = Double.parseDouble(longitude);
        this.latitude = Double.parseDouble(latitude);
        this.geo = "Point(" + longitude + " " + latitude + ")";
    }

    public Stop(String id, String name, String longitude, String latitude, String url) {
        this(id, name, longitude, latitude);
        this.url = url;
    }

    public Stop(String id, String name, String longitude, String latitude, String url, double distFromSchool, String refSchool) {
        this(id, name, longitude, latitude, url);
        this.distanceMinFromSchool = distFromSchool;
        this.refSchool = refSchool; 
    }
}
