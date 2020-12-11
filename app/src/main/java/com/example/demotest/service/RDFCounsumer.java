package com.example.demotest.service;

import java.util.ArrayList;
import java.util.List;
import com.example.demotest.modele.City;
import com.example.demotest.modele.School;

import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Resource;
import org.springframework.stereotype.Service;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

@Service
public class RDFCounsumer {

    private final String sparqlEndPoint = "http://localhost:3030/test";
    private final String querySelectLocator = "src/main/resources/query/triplestore/";

    public List<City> fetchCities() {
        List<City> cities = new ArrayList<City>();
        String queryString = getCitiesQuery();
        QueryExecution qexec = QueryExecutionFactory.sparqlService(sparqlEndPoint, queryString);
        try {
            ResultSet results = qexec.execSelect();
            System.out.println("row number : " + results.getRowNumber());
            while(results.hasNext()) {
                QuerySolution qs = results.next();
                Resource city = qs.getResource("city");
                Literal lattitude = qs.getLiteral("lattitude");
                Literal longitude = qs.getLiteral("longitude");
                Resource url = qs.getResource("url");
                Resource wikidataEntityId = qs.getResource("wikidataEntityId");
                Literal name = qs.getLiteral("name");
                System.out.println(city + " " + name +  " " + lattitude + " " + longitude + " " + wikidataEntityId + " " + url);
                cities.add(new City(city.getURI(), name.getString(), lattitude.getDouble(), longitude.getDouble(), url.getURI(), wikidataEntityId.getURI()));
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        } finally {
            qexec.close();
        }

        return cities;
    }

    public City fetchCity(String cityId) {
        City city = new City();
        String cityURI = RDFProducer.BASE_URI + RDFProducer.CITY_BASE_URI + cityId;
        String queryString = getCityByIdQuery(cityURI);
        QueryExecution qexec = QueryExecutionFactory.sparqlService(sparqlEndPoint, queryString);
        try {
            ResultSet results = qexec.execSelect();
            while(results.hasNext()) {
                QuerySolution qs = results.next();
                Literal lattitude = qs.getLiteral("lattitude");
                Literal longitude = qs.getLiteral("longitude");
                Resource url = qs.getResource("url");
                Resource wikidataEntityId = qs.getResource("wikidataEntityId");
                Literal name = qs.getLiteral("name");
                city = new City(cityURI, name.getString(), lattitude.getDouble(), longitude.getDouble(), url.getURI(), wikidataEntityId.getURI());
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        } finally {
            qexec.close();
        }

        return city;
    }

    public List<School> fetchSchools(String cityURI) {
        List<School> schools = new ArrayList<School>();
        String queryString = getSchoolsByCityQuery(cityURI);
        QueryExecution qexec = QueryExecutionFactory.sparqlService(sparqlEndPoint, queryString);
        try {
            ResultSet results = qexec.execSelect();
            System.out.println("row number : " + results.getRowNumber());
            while(results.hasNext()) {
                QuerySolution qs = results.next();
                Resource school = qs.getResource("school");
                Literal lattitude = qs.getLiteral("lattitude");
                Literal longitude = qs.getLiteral("longitude");
                Resource url = qs.getResource("url");
                Resource website = qs.getResource("website");
                Literal name = qs.getLiteral("name");
                System.out.println(school + " " + name +  " " + lattitude + " " + longitude + " " + website + " " + url);
                schools.add(new School(school.getURI(), name.getString(), lattitude.getDouble(), longitude.getDouble(), url.getURI(), website.getURI()));
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        } finally {
            qexec.close();
        }

        return schools; 
    }

    private String getCitiesQuery() {
        return this.getSelectQuery("cities.rq");
    }

    private String getSchoolsByCityQuery(String cityURI) {
        return this.getSelectQuery("schoolsByCity.rq").replace("<city-uri>", "<"+cityURI+">");
    }

    private String getCityByIdQuery(String cityURI) {
        String query = this.getSelectQuery("cityById.rq").replace("<city-uri>", "<"+cityURI+">");
        return query;
    }

    private String getSelectQuery(String filename) {
        String query = "";
        try {
            File file = new File(this.querySelectLocator + filename);
            Scanner reader = new Scanner(file);
            while (reader.hasNextLine()) {
              query += reader.nextLine();
            }
            reader.close();
          } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
          }

        return query;
    }
    
}
