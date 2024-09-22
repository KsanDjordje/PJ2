package myUtility;

/**
 * Class to manage file locations for vehicles and rentals.
 */
public class FileLocation {
    private static String vehicles; // Global variable for vehicles file location
    private static String rentals;   // Global variable for rentals file location

    /**
     * Sets the file locations for vehicles and rentals.
     *
     * @param vehicles the file location for vehicles
     * @param rentals  the file location for rentals
     */
    public static void setFileLocations(String vehicles, String rentals) {
        FileLocation.vehicles = vehicles;
        FileLocation.rentals = rentals;
    }

    /**
     * Gets the file location for vehicles.
     *
     * @return the vehicles file location
     */
    public static String getVehiclesFileLocation() {
        return vehicles;
    }

    /**
     * Gets the file location for rentals.
     *
     * @return the rentals file location
     */
    public static String getRentalsFileLocation() {
        return rentals;
    }

	
}
