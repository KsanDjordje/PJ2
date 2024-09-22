package vehicles;

import java.time.LocalDate;
import java.util.List;

import myUtility.MyDateParser;
import reports.FileLoadData;

/**
 * A utility class that provides helpful functions for vehicle management,
 * including finding vehicles, creating vehicle instances from data, and loading 
 * vehicles from a data source.
 */
public class VehicleFunctions {
    
    /**
     * Finds a vehicle by its ID from an array of vehicles.
     * 
     * @param vehicleId The ID of the vehicle to search for.
     * @param vehicleList The array of vehicles to search through.
     * @return The vehicle if found, or null if not found.
     */
    public Vehicle findVehicleById(String vehicleId, Vehicle[] vehicleList) {
        for (Vehicle v : vehicleList) {
            if (v != null && v.getId().equals(vehicleId)) {
                return v; // Return the vehicle if the ID matches
            }
        }
        return null; // Return null if no matching vehicle is found
    }

    /**
     * Creates a vehicle instance based on the provided vehicle data.
     * 
     * @param vehicleData A list of strings containing vehicle data.
     * @param dp An instance of MyDateParser for parsing dates.
     * @return A Vehicle object created from the provided data, or null if the type is unknown.
     */
    public Vehicle createVehicle(List<String> vehicleData, MyDateParser dp) {
        String id = vehicleData.get(0);
        String manu = vehicleData.get(1);
        String model = vehicleData.get(2);
        double price = Double.parseDouble(vehicleData.get(4));
        String desc = vehicleData.get(7);
        String vehType = vehicleData.get(8);

        switch (vehType) {
            case "automobil": // For cars
                LocalDate date = dp.parseLocalDate(vehicleData.get(3));
                return new Car(id, manu, model, price, 100, date, desc);
            case "bicikl": // For bicycles
                double range = Double.parseDouble(vehicleData.get(5));
                return new Bicycle(id, manu, model, price, 100, range, desc);
            case "trotinet": // For scooters
                float speed = Float.parseFloat(vehicleData.get(6));
                return new Scooter(id, manu, model, price, 100, speed, desc);
            default:
                return null; // Return null for unknown vehicle types
        }
    }

    /**
     * Loads vehicles from a data source and creates an array of Vehicle objects.
     * 
     * @param fileData An instance of FileLoadData containing vehicle data.
     * @param dp An instance of MyDateParser for parsing dates.
     * @return An array of Vehicle objects created from the loaded data.
     */
    public Vehicle[] loadVehicles(FileLoadData fileData, MyDateParser dp) {
        VehicleFunctions veh = new VehicleFunctions();
        Vehicle[] vehicleList = new Vehicle[fileData.getAllVehicles().size()];

        for (int i = 0; i < fileData.getAllVehicles().size(); i++) {
            List<String> vehicleData = fileData.getAllVehicles().get(i);
            vehicleList[i] = veh.createVehicle(vehicleData, dp); // Create vehicle from data
        }
        return vehicleList; // Return the populated array of vehicles
    }
}
