package com.example.demotest.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

import com.example.demotest.modele.City;
import com.example.demotest.modele.Meteo;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

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
    public String indexAction() {
        return "loader";
    }

    @GetMapping("/home")
    public String homeAction(Model model) {
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

    @GetMapping("/page/city/{id}/fetchSchoolsOnWikidata")
    public String fetchSchoolsOnWikidataAction(@PathVariable String id) {
        //load schools in city from wikidata
        List<School> schools = wikidataCollector.fetchSchool(id);
        // populate triplestore with loaded schools
        for (School school : schools) {
            rdfProducer.sendToTripleStore(school, id);
        }

        return "redirect:/page/city/" + id;
    }

    @RequestMapping(value="/page/city/{id}/meteo", method = RequestMethod.POST, consumes = { "text/plain" })
    //@PostMapping("/page/city/{id}/meteo")
    public String cityMeteoPostAction(@PathVariable String id, @RequestBody String meteoData) {
        //System.out.println(meteoData);
        List<Meteo> meteos = jsonDataCollector.collectCityMeteo(meteoData);
        for (Meteo meteo : meteos) {
            System.out.println(meteo.date + " " + meteo.tmax + " " + meteo.tmin);
        }
        
        return "redirect:/page/city/" + id;
    }

    @GetMapping("/page/city/{id}")
    public String cityAction(Model model, @PathVariable String id, @RequestParam(required = false) Integer distance) {
        // //load schools in city from wikidata
        // List<School> schoolsFetched = wikidataCollector.fetchSchool(id);
        // // populate triplestore with loaded schools
        // for (School school : schoolsFetched) {
        //     rdfProducer.sendToTripleStore(school, id);
        // }
        // fetch city
        if (distance == null) {
            distance = 200;
        }
        City city = rdfCounsumer.fetchCity(id);
        // describe city as JSON-LD
        String jsonLd = rdfCounsumer.describeCity(id);
        // fetch schools in the city
        List<School> schools = rdfCounsumer.fetchSchools(city.qid);
        // fetch stops with distance less than 200m of at least one school in the city 
        List<Stop> stopsAround = rdfCounsumer.fetchStopsByCityAroundSchool(city.qid, distance);
        // fetch stops in the city
        List<Stop> stops = rdfCounsumer.fetchStops(city.qid);
        List<Meteo> meteos = new ArrayList<Meteo>();
        // send data to model view
        model.addAttribute("city", city);
        model.addAttribute("schools", schools);
        model.addAttribute("stops", stops);
        model.addAttribute("stopsAround", stopsAround);
        model.addAttribute("jsonLD", jsonLd);
        model.addAttribute("distance", distance);
        model.addAttribute("meteos", meteos);

        return "city";
    }

    @GetMapping("/city/{id}")
    public Object cityTurtle(@RequestHeader(value = "Content-Type", required = false) String contentType,
            @PathVariable String id, HttpServletResponse response) {
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

    @GetMapping("/school/{id}")
    public Object schoolTurtle(@RequestHeader(value = "Content-Type", required = false) String contentType,
            @PathVariable String id, HttpServletResponse response) {
        if (contentType != null && contentType.equals("text/turtle")) {
            try {
                rdfCounsumer.describeSchool(id, response.getOutputStream());
                response.setContentType("text/turtle");
                response.setHeader("Content-Disposition", "attachment; filename=\"" + id + ".ttl\"");
                response.flushBuffer();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            return null;
        } else {
            return "redirect:/page/school/" + id;
        }
    }

    @GetMapping("/page/school/{id}")
    public String schoolAction(Model model, @PathVariable String id, @RequestParam(required = false) Integer distanceSchool,  @RequestParam(required = false) Integer distanceStop) {
        if (distanceSchool == null) {
            distanceSchool = 200;
        }
        if (distanceStop == null) {
            distanceStop = 2000;
        }
        // fetch school
        School school = rdfCounsumer.fetchSchool(id);
        // describe school as JSON-LD
        String jsonLd = rdfCounsumer.describeSchool(id);
        // fetch stops around
        List<Stop> stopsAround = rdfCounsumer.fetchStopsAroundSchool(school.qid, distanceStop);
        // fetch schools around
        List<School> schoolsAround = rdfCounsumer.fetchSchoolAroundSchool(school.qid, distanceSchool) ;
        // send data to model view
        model.addAttribute("school", school);
        model.addAttribute("schoolsAround", schoolsAround);
        model.addAttribute("stopsAround", stopsAround);
        model.addAttribute("jsonLD", jsonLd);
        model.addAttribute("distanceStop", distanceStop);
        model.addAttribute("distanceSchool", distanceSchool);

        return "school";
    }

}
