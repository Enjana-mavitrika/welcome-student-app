package com.example.demotest.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import com.example.demotest.modele.Stop;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.stereotype.Service;

@Service
public class CsvDataCollector {

	@Inject 
	ResourceFileReader fileReader;

    private final String CSV_DATA_LOCATOR = "data/csv/";
	private final String STAS_STOP_FILE = "stops.csv";

    public List<Stop> collectStasStopsCSV() {
		List<Stop> stops = new ArrayList<Stop>();
		try {
			BufferedReader reader = fileReader.readFile(CSV_DATA_LOCATOR + STAS_STOP_FILE);
			CSVReader csvReader = new CSVReader(reader);
		    // read one record at a time
			String[] record;
			boolean isFirstLine = true;
		    while ((record = csvReader.readNext()) != null) {
				if (isFirstLine) {
					isFirstLine = false;
					continue;
				}
				Stop stop = new Stop(record[0], record[1], record[2], record[3]);
				stops.add(stop);
			}
		    csvReader.close();
			reader.close();
		} catch (IOException ex) {
		    ex.printStackTrace();
		} catch (CsvValidationException e) {
			e.printStackTrace();
		}

		return stops;
	}

}
