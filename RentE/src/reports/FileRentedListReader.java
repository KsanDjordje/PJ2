package reports;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import myUtility.MyDateParser;

/**
 * This class reads and processes rental data from a CSV file. It sorts the data,
 * categorizes it by vehicle type (cars, bikes, scooters), and provides methods to
 * retrieve and clean the data.
 */
public class FileRentedListReader {

    private String COMMA_DELIMITER = ",";
    private List<List<String>> rentedCars = new ArrayList<>();
    private List<List<String>> rentedBikes = new ArrayList<>();
    private List<List<String>> rentedScooters = new ArrayList<>();
    private List<List<String>> sortedRentedList = new ArrayList<>();

    /**
     * Constructor that reads the rental data from the specified file, sorts it by date and time,
     * and categorizes the records into cars, bikes, and scooters based on the vehicle identifier.
     *
     * @param location The file path to the CSV file containing rental records.
     */
    public FileRentedListReader(String location) {
        MyDateParser dp = new MyDateParser();
        List<List<String>> records = new ArrayList<>();
        Boolean isFirstRow = true;

        try (BufferedReader br = new BufferedReader(new FileReader(location))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (isFirstRow) {
                    isFirstRow = false; // Skip header row
                    continue;
                }
                String[] values = line.split(COMMA_DELIMITER);
                List<String> record = new ArrayList<>();
                for (String value : values) {
                    record.add(value.trim());
                }
                records.add(record);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Sort records by date and time using MyDateParser to parse the first column
        records.sort(Comparator.comparing(record -> dp.parseLocalDateTime(record.get(0))));

        // Categorize records into cars, bikes, and scooters
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

    /**
     * Returns the sorted list of rental records.
     *
     * @return A list of sorted rental records.
     */
    public List<List<String>> getSortedList() {
        return sortedRentedList;
    }

    /**
     * Returns the list of car rental records.
     *
     * @return A list of car rental records.
     */
    public List<List<String>> getCars() {
        return rentedCars;
    }

    /**
     * Returns the list of bike rental records.
     *
     * @return A list of bike rental records.
     */
    public List<List<String>> getBikes() {
        return rentedBikes;
    }

    /**
     * Returns the list of scooter rental records.
     *
     * @return A list of scooter rental records.
     */
    public List<List<String>> getScooters() {
        return rentedScooters;
    }

    /**
     * Sets the sorted rental list.
     *
     * @param list A list of sorted rental records to set.
     */
    public void setSortedList(List<List<String>> list) {
        this.sortedRentedList = list;
    }

    /**
     * Sets the list of car rental records.
     *
     * @param list A list of car rental records to set.
     */
    public void setCars(List<List<String>> list) {
        this.rentedCars = list;
    }

    /**
     * Sets the list of bike rental records.
     *
     * @param list A list of bike rental records to set.
     */
    public void setBikes(List<List<String>> list) {
        this.rentedBikes = list;
    }

    /**
     * Sets the list of scooter rental records.
     *
     * @param list A list of scooter rental records to set.
     */
    public void setScooters(List<List<String>> list) {
        this.rentedScooters = list;
    }

    /**
     * Removes records with incorrect data based on overlapping rental times for the same vehicle.
     * This method checks if two records for the same vehicle have overlapping rental times
     * and removes the ones that should not be there.
     *
     * @param vehicles A list of vehicles to filter the records by.
     * @return A filtered list of rental records without incorrect data.
     */
    public List<List<String>> removeIncorrectData(List<List<String>> vehicles) {
        List<List<String>> result = new ArrayList<>();
        MyDateParser dp = new MyDateParser();
        List<List<String>> tempSortedRentedList = new ArrayList<>(this.sortedRentedList);

        for (List<String> record : tempSortedRentedList) {
            for (List<String> veh : vehicles) {
                if (record.get(2).equals(veh.get(0))) {
                    result.add(record);
                    break; // If match is found, break inner loop
                }
            }
        }
        tempSortedRentedList = result;
        Iterator<List<String>> iterator = tempSortedRentedList.iterator();

        while (iterator.hasNext()) {
            List<String> record = iterator.next();
            LocalDateTime recordTime = dp.parseLocalDateTime(record.get(0));
            String recordID = record.get(2);
            double recordDuration = Double.parseDouble(record.get(7));

            // Calculate final time by adding duration (in minutes) to the start time
            LocalDateTime timeFinal = recordTime.plusMinutes((long) recordDuration);
            boolean shouldRemove = false;

            for (List<String> otherRecord : sortedRentedList) {
                if (record == otherRecord) continue;

                LocalDateTime otherRecordTime = dp.parseLocalDateTime(otherRecord.get(0));
                String otherRecordID = otherRecord.get(2);
                double otherRecordDuration = Double.parseDouble(otherRecord.get(7));

                LocalDateTime otherRecordEndTime = otherRecordTime.plusMinutes((long) otherRecordDuration);

                // Check for overlap in rental times for the same vehicle
                if (((recordTime.equals(otherRecordTime) && recordID.equals(otherRecordID)) &&
                        timeFinal.isAfter(otherRecordEndTime)) ||
                        (recordTime.equals(otherRecordTime) && recordID.equals(otherRecordID) &&
                                timeFinal.equals(otherRecordEndTime))) {
                    shouldRemove = true;
                    break;
                }
            }

            if (shouldRemove) {
                iterator.remove();
            }
        }

        return tempSortedRentedList;
    }
}
