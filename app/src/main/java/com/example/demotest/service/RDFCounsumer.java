package com.example.demotest.service;

import java.util.ArrayList;
import java.util.List;
import com.example.demotest.modele.City;
import com.example.demotest.modele.School;
import com.example.demotest.modele.Stop;

import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFWriter;
import org.apache.jena.riot.RIOT;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.OutputStream;
import java.util.Scanner;

import javax.inject.Inject;

@Service
public class RDFCounsumer {

    @Inject 
    ResourceFileReader fileReader;
    
    private final String sparqlEndPoint = "http://localhost:3030/test";
    private final String querySelectLocator = "query/triplestore/select/";

    public List<City> fetchCities() {
        List<City> cities = new ArrayList<City>();
        String queryString = getCitiesQuery();
        QueryExecution qexec = QueryExecutionFactory.sparqlService(sparqlEndPoint, queryString);
        try {
            ResultSet results = qexec.execSelect();
            while(results.hasNext()) {
                QuerySolution qs = results.next();
                Resource city = qs.getResource("city");
                Literal lattitude = qs.getLiteral("latitude");
                Literal longitude = qs.getLiteral("longitude");
                Resource url = qs.getResource("url");
                Resource wikidataEntityId = qs.getResource("wikidataEntityId");
                Literal name = qs.getLiteral("name");
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
                Literal lattitude = qs.getLiteral("latitude");
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

    public void describeCity(String cityId, OutputStream out) {
        String cityURI = RDFProducer.BASE_URI + RDFProducer.CITY_BASE_URI + cityId;
        String queryString = "DESCRIBE " + "<"+cityURI+">";
        QueryExecution qexec = QueryExecutionFactory.sparqlService(sparqlEndPoint, queryString);
        try {
            Model model = qexec.execDescribe();
            model.setNsPrefix("schema", RDFProducer.schema);
            model.setNsPrefix("city", RDFProducer.BASE_URI + RDFProducer.CITY_BASE_URI);
            RDFWriter.create()
            .set(RIOT.symTurtleDirectiveStyle, "at")
            .lang(Lang.TTL)
            .source(model)
            .output(out);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public List<School> fetchSchools(String cityURI) {
        List<School> schools = new ArrayList<School>();
        String queryString = getSchoolsByCityQuery(cityURI);
        QueryExecution qexec = QueryExecutionFactory.sparqlService(sparqlEndPoint, queryString);
        try {
            ResultSet results = qexec.execSelect();
            while(results.hasNext()) {
                QuerySolution qs = results.next();
                Resource school = qs.getResource("school");
                Literal lattitude = qs.getLiteral("latitude");
                Literal longitude = qs.getLiteral("longitude");
                Resource url = qs.getResource("url");
                Resource website = qs.getResource("website");
                Literal name = qs.getLiteral("name");
                schools.add(new School(school.getURI(), name.getString(), lattitude.getDouble(), longitude.getDouble(), url.getURI(), website.getURI()));
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        } finally {
            qexec.close();
        }

        return schools; 
    }

    public List<Stop> fetchStops(String cityURI) {
        List<Stop> stops = new ArrayList<Stop>();
        String queryString = getStopsByCityQuery(cityURI);
        QueryExecution qexec = QueryExecutionFactory.sparqlService(sparqlEndPoint, queryString);
        try {
            ResultSet results = qexec.execSelect();
            while(results.hasNext()) {
                QuerySolution qs = results.next();
                Resource stop = qs.getResource("stop");
                Literal lattitude = qs.getLiteral("latitude");
                Literal longitude = qs.getLiteral("longitude");
                Literal name = qs.getLiteral("name");
                Resource url = qs.getResource("url");
                stops.add(new Stop(stop.getURI(), name.getString(), longitude.getString(), lattitude.getString(), url.getURI()));
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        } finally {
            qexec.close();
        }

        return stops; 
    }

    public List<Stop> fetchStopsAroundSchool(String cityURI, int maxDistance) {
        List<Stop> stops = new ArrayList<Stop>();
        String queryString = getStopsByCityAroundSchool(cityURI, maxDistance);
        QueryExecution qexec = QueryExecutionFactory.sparqlService(sparqlEndPoint, queryString);
        try {
            ResultSet results = qexec.execSelect();
            while(results.hasNext()) {
                QuerySolution qs = results.next();
                Resource stop = qs.getResource("stop");
                Literal lattitude = qs.getLiteral("latitude");
                Literal longitude = qs.getLiteral("longitude");
                Literal name = qs.getLiteral("name");
                Literal distanceFromSchool = qs.getLiteral("distance");
                Literal refSchool = qs.getLiteral("school_name");
                Resource url = qs.getResource("url");
                stops.add(new Stop(stop.getURI(), name.getString(), longitude.getString(), lattitude.getString(), url.getURI(), distanceFromSchool.getDouble(), refSchool.getString()));
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        } finally {
            qexec.close();
        }

        return stops; 
    }

    private String getCitiesQuery() {
        return this.getSelectQuery("cities.rq");
    }

    private String getSchoolsByCityQuery(String cityURI) {
        return this.getSelectQuery("schoolsByCity.rq").replace("<city-uri>", "<"+cityURI+">");
    }

    private String getStopsByCityQuery(String cityURI) {
        return this.getSelectQuery("stopsByCity.rq").replace("<city-uri>", "<"+cityURI+">");
    }

    private String getStopsByCityAroundSchool(String cityURI, int maxDistance) {
        return this.getSelectQuery("stopsByCityAroundSchool.rq").replace("<city-uri>", "<"+cityURI+">").replace("<max-dist>", String.valueOf(maxDistance));
    }

    private String getCityByIdQuery(String cityURI) {
        String query = this.getSelectQuery("cityById.rq").replace("<city-uri>", "<"+cityURI+">");
        return query;
    }

    private String getSelectQuery(String filename) {
        String query = "";
        BufferedReader bufferedReader = fileReader.readFile(this.querySelectLocator + filename);
        Scanner reader = new Scanner(bufferedReader);
        while (reader.hasNextLine()) {
            query += reader.nextLine();
        }
        reader.close();

        return query;
    }
    
}
