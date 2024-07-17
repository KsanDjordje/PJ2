package SimulationPJ2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class FileRentedList {

	
    String COMMA_DELIMITER = ",";
    private List<List<String>> cars = new ArrayList<>();
    private List<List<String>> bikes = new ArrayList<>();
    private List<List<String>> scooters = new ArrayList<>();
    

    public FileRentedList(String location){
		
        List<List<String>> records = new ArrayList<>();
        Boolean isFirstRow = true;
        try (BufferedReader br = new BufferedReader(new FileReader(location))) {
            String line;
            while ((line = br.readLine()) != null) {
            	if (isFirstRow) {
                    isFirstRow = false; // Skip header row
                    continue;
                }
                String[] values = line.split(",");
                List<String> record = new ArrayList<>();
                for (String value : values) {
                    record.add(value.trim());
                }
                records.add(record);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        records.sort(Comparator.comparing(record -> parseDate(record.get(0))));
       
        for (List<String> record : records) {
            if (!record.isEmpty()) {
                char startChar = record.get(2).charAt(0);
                switch (startChar) {
                    case 'A':
                        cars.add(record);
                        break;
                    case 'B':
                        bikes.add(record);
                        break;
                    case 'T':
                        scooters.add(record);
                        break;
                    default:
                        break;
                }
            }
        }

        
    }
    
    private static LocalDateTime parseDate(String dateStr) {
        DateTimeFormatter[] formatters = {
            DateTimeFormatter.ofPattern("d.M.yyyy HH:mm"),
            DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"),
            DateTimeFormatter.ofPattern("d.M.yyyy HH:mm"),
            DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")
        };
        for (DateTimeFormatter formatter : formatters) {
            try {
                return LocalDateTime.parse(dateStr, formatter);
            } catch (Exception e) {
                
            }
        }
        throw new IllegalArgumentException("Date format not supported for: " + dateStr);
    }

	public List<List<String>> getCars() {
		return cars;
	}

	public List<List<String>> getBikes() {
		return bikes;
	}

	public List<List<String>> getScooters() {
		return scooters;
	}
}
