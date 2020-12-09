package com.example.demotest.modele;

import lombok.Data;

@Data
public class StasStop {

    public String id;
    public String name;
    public double longitude;
    public double latitude;

    public StasStop() {}

    public StasStop(String id, String name, String longitude, String latitude) {
        this.id = id;
        this.name = name;
        this.longitude = Double.parseDouble(longitude);
        this.latitude = Double.parseDouble(latitude);
    }
}
