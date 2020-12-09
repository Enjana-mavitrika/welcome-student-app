package com.example.demotest.api;

import java.util.List;
import javax.inject.Inject;
import com.example.demotest.modele.City;
import com.example.demotest.service.RDFCounsumer;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CityRestController {

    @Inject
    RDFCounsumer rdfCounsumer;

    @GetMapping("/cities")
    public List<City> getCities() {
        List<City> cities = rdfCounsumer.fetchCities();
        return cities;
    }

}
