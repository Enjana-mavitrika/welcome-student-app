package com.example.demotest.controller;

import java.io.IOException;
import java.util.List;
import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

import com.example.demotest.modele.City;
import com.example.demotest.modele.School;
import com.example.demotest.modele.Stop;
import com.example.demotest.service.CsvDataCollector;
import com.example.demotest.service.JsonDataCollector;
import com.example.demotest.service.RDFCounsumer;
import com.example.demotest.service.RDFProducer;
import com.example.demotest.service.WikidataCollector;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@Controller
public class IndexController {

    @Inject
    CsvDataCollector csvDataCollector;

    @Inject
    RDFProducer rdfProducer;

    @Inject
    RDFCounsumer rdfCounsumer;

    @Inject
    WikidataCollector wikidataCollector;

    @Inject
    JsonDataCollector jsonDataCollector;

    @GetMapping("/")
    public String indexAction(Model model) {
        // load cities from json file
        List<City> cities = jsonDataCollector.collectCities();
        // populate triplestore with loaded cities
        for (City city : cities) {
            rdfProducer.sendToTripleStore(city);
        }
        // fetch cities from triplestore and send to model view
        cities = rdfCounsumer.fetchCities();
        model.addAttribute("cities", cities);
        // load bus stops from csv file
        List<Stop> stops = csvDataCollector.collectStasStopsCSV();
        // populate triplestore with loaded stops
        for (Stop stop : stops) {
            rdfProducer.sendToTripleStore(stop, "Q42716");
        }

        return "index";
    }

    @GetMapping("/page/city/{id}")
    public String cityAction(Model model, @PathVariable String id) {
        // load schools in city from wikidata
        List<School> schools = wikidataCollector.fetchSchool(id);
        // populate triplestore with loaded schools
        for (School school : schools) {
            rdfProducer.sendToTripleStore(school, id);
        }
        // fetch city
        City city = rdfCounsumer.fetchCity(id);
        // fetch schools in the city
        schools = rdfCounsumer.fetchSchools(city.qid);
        // fetch stops with distance less than 200m of at least one school in the city 
        List<Stop> stopsAround = rdfCounsumer.fetchStopsAroundSchool(city.qid, 200);
        // fetch stops in the city
        List<Stop> stops = rdfCounsumer.fetchStops(city.qid);
        // send data to model view
        model.addAttribute("city", city);
        model.addAttribute("schools", schools);
        model.addAttribute("stops", stops);
        model.addAttribute("stopsAround", stopsAround);

        return "city";
    }

    @GetMapping("/city/{id}")
    public Object cityTurtle(@RequestHeader(value = "Content-Type", required = false) String contentType,
            @PathVariable String id, HttpServletResponse response) {
        System.out.println("Content-Type = " + contentType);
        if (contentType != null && contentType.equals("text/turtle")) {
            try {
                rdfCounsumer.describeCity(id, response.getOutputStream());
                response.setContentType("text/turtle");
                response.setHeader("Content-Disposition", "attachment; filename=\"" + id + ".ttl\"");
                response.flushBuffer();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            return null;
        } else {
            return "redirect:/page/city/" + id;
        }
    }

}
