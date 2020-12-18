package com.example.demotest.service;

import com.example.demotest.modele.City;
import com.example.demotest.modele.Meteo;
import com.example.demotest.modele.School;
import com.example.demotest.modele.Stop;
import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdfconnection.RDFConnection;
import org.apache.jena.rdfconnection.RDFConnectionFactory;
import org.springframework.stereotype.Service;

@Service
public class RDFProducer {
    // Entity URIs
    public static String STOP_BASE_URI = "/stop/";
    public static String CITY_BASE_URI = "/city/";
    public static String SCHOOL_BASE_URI = "/school/";
    public static String BASE_URI = "http://localhost:8080";
    // RDF PREFIXES
    public static final String rdf = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
    public static final String schema = "https://schema.org/";
    public static final String wd = "http://www.wikidata.org/entity/";
    public static final String geo = "http://www.opengis.net/ont/geosparql#";
    public static final String xsd = "http://www.w3.org/2001/XMLSchema#";
    // SPARQL API
    private final String datasetURL = "http://localhost:3030/test";
    private final String sparqlEndpoint = datasetURL + "/sparql";

    public void sendToTripleStore(Meteo meteo, String cityId) {
        Model model = this.meteoToRDF(meteo, cityId);
        this.updateRDF(model);
    }

    public void sendToTripleStore(School school, String cityId) {
        Model model = this.schoolToRDF(school, cityId);
        this.updateRDF(model);
    }
    

    public void sendToTripleStore(Stop stop, String cityId) {
        Model model = this.stopToRDF(stop, cityId);
        this.updateRDF(model);
    }

    public void sendToTripleStore(City city) {
        Model model = this.cityToRDF(city);
        this.updateRDF(model);
    }

    private void updateRDF(Model model) {
        String sparqlUpdate = datasetURL + "/update";
        String graphStore = datasetURL + "/data";
        RDFConnection conneg = RDFConnectionFactory.connect(sparqlEndpoint,sparqlUpdate,graphStore);
        conneg.load(model); // add the content of model to the triplestore
    }

    private Model schoolToRDF(School school, String cityId) {
        Model model = ModelFactory.createDefaultModel();
        Property a = model.createProperty(rdf + "type");
        Property label = model.createProperty(schema + "name");
        Property lati = model.createProperty(schema + "latitude");
        Property longi = model.createProperty(schema + "longitude");
        Property location = model.createProperty(schema + "containedInPlace");
        Property identifier = model.createProperty(schema + "identifier");
        Property url = model.createProperty(schema + "url");
        Property geoAsWKT = model.createProperty(geo, "asWKT");
        Property hasGeometry = model.createProperty(geo, "hasGeometry");
        String id = BASE_URI + SCHOOL_BASE_URI + school.qid;
        Resource city = model.createResource(BASE_URI + CITY_BASE_URI + cityId);
        Resource page = model.createResource(BASE_URI + "/page" + SCHOOL_BASE_URI + school.qid);
        Literal name = model.createLiteral(school.name, "fr");
        Literal geoAsWKTvalue = model.createTypedLiteral(school.geo, geo+"wktLiteral");
        String longitude = school.longitude + "";
        String latitude = school.latitude + "";
        Resource schoolRDF = model.createResource(id);
        Resource schoolSchema = model.createResource(schema + "School");
        Resource geometry = model.createResource(geo + "Geometry");
        Resource schoolGeomRDF = model.createResource(id + "Geom");
        model.setNsPrefix("schema", schema);
        model.setNsPrefix("school", BASE_URI + SCHOOL_BASE_URI);
        model.setNsPrefix("geo", geo);
        model.setNsPrefix("a", rdf);
        schoolGeomRDF.addProperty(a, geometry);
        schoolGeomRDF.addProperty(geoAsWKT, geoAsWKTvalue);
        schoolRDF.addProperty(a, schoolSchema);
        schoolRDF.addProperty(label, name);
        schoolRDF.addProperty(lati, latitude, XSDDatatype.XSDdecimal);
        schoolRDF.addProperty(longi, longitude, XSDDatatype.XSDdecimal);
        schoolRDF.addProperty(location, city);
        schoolRDF.addProperty(url, page);
        schoolRDF.addProperty(identifier, model.createResource(school.website));
        schoolRDF.addProperty(hasGeometry, schoolGeomRDF);
        
        return model;
    }

    private Model meteoToRDF(Meteo meteo, String cityId) {
        Model model = ModelFactory.createDefaultModel();
        Property a = model.createProperty(rdf + "type");
        Property hasMeteo = model.createProperty(BASE_URI + "/property/" + "hasMeteo");
        Property hasTmin = model.createProperty(BASE_URI + "/property/" + "hasTmin");
        Property hasTmax = model.createProperty(BASE_URI + "/property/" + "hasTmax");
        Property isPredicted = model.createProperty(BASE_URI + "/property" + "isPredictedAt");
        Literal date = model.createTypedLiteral(meteo.date, xsd+"date");
        Resource city = model.createResource(BASE_URI + CITY_BASE_URI + cityId);
        String id = BASE_URI + "/class/meteo/" + cityId + meteo.date;
        Resource meteoRDF = model.createResource(id);
        meteoRDF.addProperty(a, BASE_URI + "/class/Meteo");
        meteoRDF.addProperty(hasTmax, Double.toString(meteo.tmax), XSDDatatype.XSDdecimal);
        meteoRDF.addProperty(hasTmin, Double.toString(meteo.tmin), XSDDatatype.XSDdecimal);
        meteoRDF.addProperty(isPredicted, date);
        city.addProperty(hasMeteo, meteoRDF);

        return model;
    }

    private Model stopToRDF(Stop stop, String cityId) {
        Model model = ModelFactory.createDefaultModel();
        Property a = model.createProperty(rdf + "type");
        Property label = model.createProperty(schema + "name");
        Property lati = model.createProperty(schema + "latitude");
        Property longi = model.createProperty(schema + "longitude");
        Property location = model.createProperty(schema + "containedInPlace");
        Property geoAsWKT = model.createProperty(geo, "asWKT");
        Property hasGeometry = model.createProperty(geo, "hasGeometry");
        Property url = model.createProperty(schema + "url");
        String id = BASE_URI + STOP_BASE_URI + stop.id;
        Resource city = model.createResource(BASE_URI + CITY_BASE_URI + cityId);
        Resource page = model.createResource(BASE_URI + "/page" + CITY_BASE_URI + stop.id);
        Literal name = model.createLiteral(stop.name, "fr");
        Literal geoAsWKTvalue = model.createTypedLiteral(stop.geo, geo+"wktLiteral");
        String longitude = stop.longitude + "";
        String latitude = stop.latitude + "";
        Resource stopRDF = model.createResource(id);
        Resource busStopSchema = model.createResource(schema + "BusStop");
        Resource geometry = model.createResource(geo + "Geometry");
        Resource stopGeomRDF = model.createResource(id + "Geom");
        model.setNsPrefix("schema", schema);
        model.setNsPrefix("stop", BASE_URI + STOP_BASE_URI);
        model.setNsPrefix("geo", geo);
        model.setNsPrefix("a", rdf);
        stopGeomRDF.addProperty(a, geometry);
        stopGeomRDF.addProperty(geoAsWKT, geoAsWKTvalue);
        stopRDF.addProperty(a, busStopSchema);
        stopRDF.addProperty(label, name);
        stopRDF.addProperty(lati, latitude, XSDDatatype.XSDdecimal);
        stopRDF.addProperty(longi, longitude, XSDDatatype.XSDdecimal);
        stopRDF.addProperty(location, city);
        stopRDF.addProperty(url, page);
        stopRDF.addProperty(hasGeometry, stopGeomRDF);
        
        return model;
    }

    private Model cityToRDF(City city) {
        Model model = ModelFactory.createDefaultModel();
        Property a = model.createProperty(rdf + "type");
        Property label = model.createProperty(schema + "name");
        Property lati = model.createProperty(schema + "latitude");
        Property longi = model.createProperty(schema + "longitude");
        Property url = model.createProperty(schema + "url");
        Property identifier = model.createProperty(schema + "identifier");
        String uri = BASE_URI + CITY_BASE_URI + city.qid;
        Resource cityUrl = model.createResource(BASE_URI + "/page" + CITY_BASE_URI + city.qid);
        Literal name = model.createLiteral(city.name, "fr");
        String longitude = city.longitude + "";
        String latitude = city.latitude + "";
        Resource wikidataIdentifier = model.createResource(wd + city.qid);
        Resource cityRDF = model.createResource(uri);
        Resource schemaCity = model.createResource(schema+"City");
        model.setNsPrefix("schema", schema);
        model.setNsPrefix("city", BASE_URI + CITY_BASE_URI);
        cityRDF.addProperty(a, schemaCity);
        cityRDF.addProperty(label, name);
        cityRDF.addProperty(lati, latitude, XSDDatatype.XSDdecimal);
        cityRDF.addProperty(longi, longitude, XSDDatatype.XSDdecimal);
        cityRDF.addProperty(url, cityUrl);
        cityRDF.addProperty(identifier, wikidataIdentifier);

        return model;
    }
    
}
