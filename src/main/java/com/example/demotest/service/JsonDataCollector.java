package com.example.demotest.service;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.example.demotest.modele.City;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;

@Service
public class JsonDataCollector {
    
    private final String jsonDataLocator = "src/main/resources/data/json/";
    
    public List<City> collectCities() {
        List<City> cities = new ArrayList<City>();
        JSONParser jsonParser = new JSONParser();
        try (FileReader reader = new FileReader(this.jsonDataLocator + "cities.json"))
        {
            Object obj = jsonParser.parse(reader); 
            JSONArray cityList = (JSONArray) obj;
            for(Object cityObj : cityList) {
                JSONObject city = (JSONObject) cityObj;
                String cityId = "Q" + ((String) city.get("item")).split("Q")[1];
                String cityName = (String) city.get("itemLabel");
                double longitude = Double.parseDouble(((String)city.get("gps")).split(" ")[0].split("\\(")[1]);
                double latitude = Double.parseDouble(((String)city.get("gps")).split(" ")[1].split("\\)")[0]);
                cities.add(new City(cityId, cityName, latitude, longitude));
                // System.out.println(cityId + " " + cityName + " " + latitude + " " + longitude);
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
