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
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

@Service
public class WikidataCollector {

    private final String sparqlEndPoint = "https://query.wikidata.org/sparql";
    private final String queryLocator = "src/main/resources/query/wikidata/";

    public List<School> fetchSchool(String cityId) {
        List<School> schools = new ArrayList<School>();
        String queryString = getSchoolQuery(cityId);
        QueryExecution qexec = QueryExecutionFactory.sparqlService(sparqlEndPoint, queryString);
        try {
            ResultSet results = qexec.execSelect();
            System.out.println("row number : " + results.getRowNumber());
            while(results.hasNext()) {
                QuerySolution qs = results.next();
                Resource item = qs.getResource("item");
                Literal label = qs.getLiteral("itemLabel");
                Resource website = qs.getResource("website");
                Literal geo = qs.getLiteral("geo");
                schools.add(new School(label.getString(), geo.getString(), website.getURI(), item.getURI()));
                System.out.println("item : " + item + " " + label +  " " + website + " " + geo);
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
        try {
            File file = new File(this.queryLocator + "school.rq");
            Scanner reader = new Scanner(file);
            while (reader.hasNextLine()) {
              query += reader.nextLine();
            }
            reader.close();
            query = query.replaceAll("<city-id>", cityId);
          } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
          }

        return query;
    }
    
}
