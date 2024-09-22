package vehicles;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The FileVehicleReader class reads vehicle data from a CSV file and categorizes them into
 * cars, bikes, and scooters based on their identifiers.
 */
public class FileVehicleReader {
    // Delimiter used in the CSV file
    private static final String COMMA_DELIMITER = ",";

    // Lists to store vehicle data
    private List<List<String>> cars = new ArrayList<>();
    private List<List<String>> bikes = new ArrayList<>();
    private List<List<String>> scooters = new ArrayList<>();
    private List<List<String>> allVehicles = new ArrayList<>();

    /**
     * Constructor that reads vehicle data from the provided file location.
     * Vehicles are categorized into cars, bikes, and scooters based on their IDs.
     *
     * @param location The path of the CSV file containing vehicle data.
     */
    public FileVehicleReader(String location) {
        try (BufferedReader br = new BufferedReader(new FileReader(location))) {
            String line;
            Boolean isFirstRow = true;

            // Read each line from the file
            while ((line = br.readLine()) != null) {
                String[] values = line.split(COMMA_DELIMITER);  // Split the line by commas
                List<String> record = new ArrayList<>(Arrays.asList(values));

                // Skip the header row
                if (isFirstRow) {
                    isFirstRow = false;
                    continue;
                }

                // Add the vehicle to the allVehicles list
                this.allVehicles.add(record);

                // Categorize the vehicle based on the first character of the ID
                char firstChar = values[0].charAt(0);
                switch (firstChar) {
                    case 'A':  // IDs starting with 'A' are cars
                        this.cars.add(record);
                        break;
                    case 'B':  // IDs starting with 'B' are bikes
                        this.bikes.add(record);
                        break;
                    case 'T':  // IDs starting with 'T' are scooters
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

    /**
     * Gets the list of cars from the file.
     *
     * @return A list of car records.
     */
    public List<List<String>> getCars() {
        return cars;
    }

    /**
     * Gets the list of bikes from the file.
     *
     * @return A list of bike records.
     */
    public List<List<String>> getBikes() {
        return bikes;
    }

    /**
     * Gets the list of scooters from the file.
     *
     * @return A list of scooter records.
     */
    public List<List<String>> getScooters() {
        return scooters;
    }

    /**
     * Gets the list of all vehicles from the file.
     *
     * @return A list of all vehicle records.
     */
    public List<List<String>> getAllVehicles() {
        return allVehicles;
    }
}
