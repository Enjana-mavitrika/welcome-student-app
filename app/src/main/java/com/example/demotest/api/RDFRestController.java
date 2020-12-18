package com.example.demotest.api;

import java.util.List;
import javax.inject.Inject;
import com.example.demotest.modele.City;
import com.example.demotest.modele.School;
import com.example.demotest.modele.Stop;
import com.example.demotest.service.RDFCounsumer;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RDFRestController {

    @Inject
    RDFCounsumer rdfCounsumer;

    @GetMapping("/api/v1/cities")
    public List<City> getCities() {
        List<City> cities = rdfCounsumer.fetchCities();
        return cities;
    }

    @GetMapping("/api/v1/city/{id}/schools")
    public List<School> getSchoolsByCity(@PathVariable String id) {
        // fetch schools in the city
        List<School> schools = rdfCounsumer.fetchSchools(id);

        return schools;
    }

    @GetMapping("/api/v1/city/{id}/stops")
    public List<Stop> getStopsByCity(@PathVariable String id) {
        // fetch stops in the city
        List<Stop> stops = rdfCounsumer.fetchStops(id);

        return stops;
    }

    @GetMapping("/api/v1/city/{id}/stops/{distMax}")
    public List<Stop> getStopsByCityAroundSchool(@PathVariable String id, @PathVariable int distMax) {
        // fetch stops in the city around schools with distMax
        List<Stop> stopsAround = rdfCounsumer.fetchStopsAroundSchool(id, distMax);

        return stopsAround;
    }

    @GetMapping("/api/v1/school/{idSchool}/stops")
    public List<Stop> getStopsBySchool(@PathVariable String idSchool) {
        // fetch stops around
        List<Stop> stopsAround = rdfCounsumer.fetchStopsAroundSchool(idSchool, 200);

        return stopsAround;
    }

    @GetMapping("/api/v1/school/{idSchool}/schools")
    public List<School> getSchoolsBySchool(@PathVariable String idSchool) {
        // fetch schools around
        List<School> schoolsAround = rdfCounsumer.fetchSchoolAroundSchool(idSchool, 2000) ;

        return schoolsAround;
    }


}
