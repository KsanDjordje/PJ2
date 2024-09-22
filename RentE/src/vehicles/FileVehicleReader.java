package Vehicles;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileVehicleReader {
	private static final String COMMA_DELIMITER = ",";
    private List<List<String>> cars = new ArrayList<>();
    private List<List<String>> bikes = new ArrayList<>();
    private List<List<String>> scooters = new ArrayList<>();
    private List<List<String>> allVehicles = new ArrayList<>();

    public FileVehicleReader(String location) {
        try (BufferedReader br = new BufferedReader(new FileReader(location))) {
            String line;
            Boolean isFirstRow = true;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(COMMA_DELIMITER);
                List<String> record = new ArrayList<>(Arrays.asList(values));
                if (isFirstRow) {
                    isFirstRow = false; // Skip header row
                    continue;
                }
                this.allVehicles.add(record);
                char firstChar = values[0].charAt(0);
                switch (firstChar) {
                    case 'A':
                        this.cars.add(record);
                        break;
                    case 'B':
                        this.bikes.add(record);
                        break;
                    case 'T':
                        this.scooters.add(record);
                        break;
                    default:
                        break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    public List<List<String>> getAllVehicles() {
        return allVehicles;
    }
}
