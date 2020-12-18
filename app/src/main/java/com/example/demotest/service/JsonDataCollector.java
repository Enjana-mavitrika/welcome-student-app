package com.example.demotest.service;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import com.example.demotest.modele.City;
import com.example.demotest.modele.Meteo;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;

@Service
public class JsonDataCollector {

    @Inject 
    ResourceFileReader fileReader;
    
    private final String jsonDataLocator = "data/json/";

    public List<Meteo> collectCityMeteo(String meteoData) {
        List<Meteo> meteos = new ArrayList<Meteo>();
        JSONParser jsonParser = new JSONParser();
        try {
            Object obj = jsonParser.parse(meteoData);
            JSONObject meteoJson = (JSONObject) obj;
            JSONArray previsions = (JSONArray) meteoJson.get("prevision");
            for (Object previsionObj : previsions) {
                JSONObject prevision = (JSONObject) previsionObj;
                String date = (String) prevision.get("datetime");
                long tmin = (long) prevision.get("tmin");
                long tmax = (long) prevision.get("tmax");
                meteos.add(new Meteo(date, tmin, tmax));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return meteos;
    }

    public List<City> collectCities() {
        List<City> cities = new ArrayList<City>();
        JSONParser jsonParser = new JSONParser();
        try {
            BufferedReader reader = fileReader.readFile(jsonDataLocator + "cities.json");
            Object obj = jsonParser.parse(reader);
            JSONArray cityList = (JSONArray) obj;
            for (Object cityObj : cityList) {
                JSONObject city = (JSONObject) cityObj;
                String cityId = "Q" + ((String) city.get("item")).split("Q")[1];
                String cityName = (String) city.get("itemLabel");
                double longitude = Double.parseDouble(((String) city.get("gps")).split(" ")[0].split("\\(")[1]);
                double latitude = Double.parseDouble(((String) city.get("gps")).split(" ")[1].split("\\)")[0]);
                cities.add(new City(cityId, cityName, latitude, longitude));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return cities;
    }
}
