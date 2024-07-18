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


public class FileRentedListReader {

	
    String COMMA_DELIMITER = ",";
    private List<List<String>> rentedCars = new ArrayList<>();
    private List<List<String>> rentedBikes = new ArrayList<>();
    private List<List<String>> rentedScooters = new ArrayList<>();
    private List<List<String>> sortedRentedList = new ArrayList<>();

    public FileRentedListReader(String location){
		
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
                this.sortedRentedList.add(record);
                switch (startChar) {
                    case 'A':
                        rentedCars.add(record);
                        break;
                    case 'B':
                        rentedBikes.add(record);
                        break;
                    case 'T':
                        rentedScooters.add(record);
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
            
        };
        for (DateTimeFormatter formatter : formatters) {
            try {
                return LocalDateTime.parse(dateStr, formatter);
            } catch (Exception e) {
                
            }
        }
        throw new IllegalArgumentException("Date format not supported for: " + dateStr);
    }
    
    public List<List<String>> getSortedList(){
    	return sortedRentedList;
    }
	public List<List<String>> getCars() {
		return rentedCars;
	}

	public List<List<String>> getBikes() {
		return rentedBikes;
	}

	public List<List<String>> getScooters() {
		return rentedScooters;
	}

	
}
