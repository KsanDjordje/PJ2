package reports;

import java.util.List;
import vehicles.FileVehicleReader;

/**
 * The {@code FileLoadData} class is responsible for loading and processing data 
 * from vehicle and rental list files. It reads vehicle data and filtered lists of rented vehicles.
 */
public class FileLoadData {
    
    /**
     * The object responsible for reading rented list data.
     */
    private FileRentedListReader rented;
    
    /**
     * The object responsible for reading vehicle list data.
     */
    private FileVehicleReader vehicleList;

    /**
     * Constructs a {@code FileLoadData} object that initializes both the vehicle list 
     * and rented list by reading data from the specified file locations.
     * The rented list is filtered to remove any incorrect data based on the vehicle list.
     *
     * @param vehicleListLocation  the file location of the vehicle list
     * @param rentedListLocation   the file location of the rented list
     */
    public FileLoadData(String vehicleListLocation, String rentedListLocation) {
        FileRentedListReader rented = new FileRentedListReader(rentedListLocation);
        this.vehicleList = new FileVehicleReader(vehicleListLocation);
        rented.setSortedList(rented.removeIncorrectData(vehicleList.getAllVehicles()));
        rented.setCars(rented.removeIncorrectData(vehicleList.getCars()));
        rented.setBikes(rented.removeIncorrectData(vehicleList.getBikes()));
        rented.setScooters(rented.removeIncorrectData(vehicleList.getScooters()));
        this.rented = rented;
    }

    /**
     * Returns a sorted list of rented vehicles after filtering out incorrect data.
     *
     * @return a list of lists representing the sorted rented vehicle data
     */
    public List<List<String>> getSortedList() {
        return rented.getSortedList();
    }

    /**
     * Returns a list of rented cars after filtering out incorrect data.
     *
     * @return a list of lists representing rented car data
     */
    public List<List<String>> getRentedCars() {
        return rented.getCars();
    }

    /**
     * Returns a list of rented bicycles after filtering out incorrect data.
     *
     * @return a list of lists representing rented bicycle data
     */
    public List<List<String>> getRentedBikes() {
        return rented.getBikes();
    }

    /**
     * Returns a list of rented scooters after filtering out incorrect data.
     *
     * @return a list of lists representing rented scooter data
     */
    public List<List<String>> getRentedScooters() {
        return rented.getScooters();
    }

    /**
     * Returns a list of all cars available in the vehicle list.
     *
     * @return a list of lists representing all car data
     */
    public List<List<String>> getCars() {
        return vehicleList.getCars();
    }

    /**
     * Returns a list of all bicycles available in the vehicle list.
     *
     * @return a list of lists representing all bicycle data
     */
    public List<List<String>> getBikes() {
        return vehicleList.getBikes();
    }

    /**
     * Returns a list of all scooters available in the vehicle list.
     *
     * @return a list of lists representing all scooter data
     */
    public List<List<String>> getScooters() {
        return vehicleList.getScooters();
    }

    /**
     * Returns a list of all vehicles available in the vehicle list.
     *
     * @return a list of lists representing all vehicle data
     */
    public List<List<String>> getAllVehicles() {
        return vehicleList.getAllVehicles();
    }
}
