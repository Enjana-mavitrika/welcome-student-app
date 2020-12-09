package com.example.demotest.service;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.example.demotest.modele.StasStop;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import org.springframework.stereotype.Service;

@Service
public class CsvDataCollector {

    private final String CSV_DATA_LOCATOR = "src/main/resources/data/csv/";
	private final String STAS_STOP_FILE = "stops.csv";

    public List<StasStop> collectStasStopsCSV() {
		List<StasStop> stops = new ArrayList<StasStop>();
		try {
            Reader reader = Files.newBufferedReader(Paths.get(CSV_DATA_LOCATOR + STAS_STOP_FILE));
		    CSVReader csvReader = new CSVReader(reader);
		    // read one record at a time
			String[] record;
			boolean isFirstLine = true;
		    while ((record = csvReader.readNext()) != null) {
				if (isFirstLine) {
					isFirstLine = false;
					continue;
				}
				StasStop stop = new StasStop(record[0], record[1], record[3], record[4]);
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
