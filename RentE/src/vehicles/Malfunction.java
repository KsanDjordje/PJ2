package vehicles;

import java.time.LocalDateTime;

/**
 * Represents a malfunction that occurs in a vehicle.
 * This class contains the vehicle's ID, the time of the malfunction, and a description of the issue.
 */
public class Malfunction {
    private String vehicleId;            // ID of the vehicle that has the malfunction
    private LocalDateTime malfunctionTime; // Time when the malfunction occurred
    private String description;          // Description of the malfunction

    /**
     * Constructor for the Malfunction class.
     * 
     * @param vehicleId The ID of the vehicle that experienced the malfunction.
     * @param malfunctionTime The time the malfunction occurred.
     * @param description A description of the malfunction.
     */
    public Malfunction(String vehicleId, LocalDateTime malfunctionTime, String description) {
        this.vehicleId = vehicleId;
        this.malfunctionTime = malfunctionTime;
        this.description = description;
    }

    /**
     * Gets the ID of the vehicle that had the malfunction.
     * 
     * @return The vehicle's ID.
     */
    public String getVehicleId() {
        return vehicleId;
    }

    /**
     * Gets the time when the malfunction occurred.
     * 
     * @return The time of the malfunction as a LocalDateTime object.
     */
    public LocalDateTime getMalfunctionTime() {
        return malfunctionTime;
    }

    /**
     * Gets the description of the malfunction.
     * 
     * @return The malfunction description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns a string representation of the malfunction.
     * 
     * @return A string containing the vehicle ID, malfunction time, and description.
     */
    @Override
    public String toString() {
        return "(" + this.vehicleId + ", " + this.malfunctionTime + ", " + this.description + ")";
    }
}
