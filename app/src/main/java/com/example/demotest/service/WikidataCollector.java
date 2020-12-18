package com.example.demotest.service;

import java.util.ArrayList;
import java.util.List;
import com.example.demotest.modele.School;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Resource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.util.Scanner;

import javax.inject.Inject;

@Service
public class WikidataCollector {

    @Inject 
    ResourceFileReader fileReader;
    
    private final String sparqlEndPoint = "https://query.wikidata.org/sparql";
    private final String queryLocator = "query/wikidata/";

    public List<School> fetchSchool(String cityId) {
        List<School> schools = new ArrayList<School>();
        String queryString = getSchoolQuery(cityId);
        QueryExecution qexec = QueryExecutionFactory.sparqlService(sparqlEndPoint, queryString);
        try {
            ResultSet results = qexec.execSelect();
            while(results.hasNext()) {
                QuerySolution qs = results.next();
                Resource item = qs.getResource("item");
                Literal label = qs.getLiteral("itemLabel");
                Resource website = qs.getResource("website");
                Literal geo = qs.getLiteral("geo");
                schools.add(new School(label.getString(), geo.getString(), website.getURI(), item.getURI()));
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        } finally {
            qexec.close();
        }

        return schools;
    }

    /**
     * Build a query that will fetch all school in a given city from Wikidata
     * @param cityId
     * @return
     */
    private String getSchoolQuery(String cityId) {
        String query = "";
        BufferedReader bufferedReader = fileReader.readFile(this.queryLocator + "school.rq");
        Scanner reader = new Scanner(bufferedReader);
        while (reader.hasNextLine()) {
            query += reader.nextLine();
        }
        reader.close();
        query = query.replaceAll("<city-id>", cityId);

        return query;
    }
    
}
