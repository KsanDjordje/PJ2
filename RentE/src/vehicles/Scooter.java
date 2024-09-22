package vehicles;

/**
 * The Scooter class represents a scooter that extends the Vehicle class.
 * It includes specific properties like the maximum speed of the scooter.
 */
public class Scooter extends Vehicle {
    // The maximum speed of the scooter
    private float maxSpeed;

    /**
     * Constructor to initialize a Scooter object with the given parameters.
     *
     * @param id The unique identifier of the scooter.
     * @param manufacturer The manufacturer of the scooter.
     * @param model The model name of the scooter.
     * @param purchasePrice The purchase price of the scooter.
     * @param currentBatteryLevel The current battery level of the scooter.
     * @param maxSpeed The maximum speed the scooter can achieve.
     * @param description A description of the scooter.
     */
    public Scooter(String id, String manufacturer, String model, double purchasePrice, int currentBatteryLevel, float maxSpeed, String description) {
        super(id, manufacturer, model, purchasePrice, currentBatteryLevel, description);
        this.maxSpeed = maxSpeed;
    }

    /**
     * Gets the maximum speed of the scooter.
     *
     * @return The maximum speed of the scooter.
     */
    public float getMaxSpeed() {
        return maxSpeed;
    }
}
