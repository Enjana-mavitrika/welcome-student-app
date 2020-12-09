package com.example.demotest.modele;

import lombok.Data;

@Data
public class City {

	public String name;
    public String qid;
    public double latitude;
    public double longitude;
    public String url;
    public String wikidataEntityUri;

    public City() {
    }

    public City(String id, String name) {
        this.qid = id;
        this.name = name;
    }

    public City(String qid, String name, double latitude, double longitude) {
        this(qid, name);
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public City(String qid, String name, double latitude, double longitude, String url, String wikidata) {
        this(qid, name, latitude, longitude);
        this.url = url;
        this.wikidataEntityUri = wikidata;
    }
}
