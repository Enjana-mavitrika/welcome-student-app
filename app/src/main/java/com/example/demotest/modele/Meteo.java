package com.example.demotest.modele;
import lombok.Data;

@Data
public class Meteo {
	public String date;
    public long tmax;
    public long tmin;

    public Meteo(String datetime, long tmin, long tmax) {
        this.date = datetime.split("T")[0];
        this.tmin = tmin;
        this.tmax = tmax;
	}

}
