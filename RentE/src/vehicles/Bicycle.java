package vehicles;

/**
 * The Bicycle class represents a bicycle that extends the Vehicle class.
 * It includes specific properties like the range of the bicycle.
 */
public class Bicycle extends Vehicle {
    // The maximum range the bicycle can travel
    private double range;

    /**
     * Constructor to initialize a Bicycle object with the given parameters.
     *
     * @param id The unique identifier of the bicycle.
     * @param manufacturer The manufacturer of the bicycle.
     * @param model The model name of the bicycle.
     * @param purchasePrice The purchase price of the bicycle.
     * @param currentBatteryLevel The current battery level of the bicycle.
     * @param range The maximum range the bicycle can travel on a full charge.
     * @param description A description of the bicycle.
     */
    public Bicycle(String id, String manufacturer, String model, double purchasePrice, int currentBatteryLevel, double range, String description) {
        super(id, manufacturer, model, purchasePrice, currentBatteryLevel, description);
        this.range = range;
    }

    /**
     * Gets the maximum range the bicycle can travel.
     *
     * @return The range of the bicycle.
     */
    public double getRange() {
        return range;
    }
}
