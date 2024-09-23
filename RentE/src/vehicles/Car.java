package vehicles;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Represents a car which extends the Vehicle class and includes additional attributes such as 
 * purchase date and seating capacity.
 */
public class Car extends Vehicle implements Serializable{
	private static final long serialVersionUID = 1L; // Serialization version identifier

    /** Purchase date of the car. */
    private LocalDate purchaseDate;

    /** Seating capacity of the car. */
    private int capacity;

    /** Default seating capacity for a car. */
    private static final int DEFAULT_CAPACITY = 2;
    
    /**
     * Constructs a Car object with the specified attributes, including a custom seating capacity.
     * 
     * @param id                 Unique identifier for the car.
     * @param manufacturer       Manufacturer of the car.
     * @param model              Model of the car.
     * @param purchasePrice      Price at which the car was purchased.
     * @param currentBatteryLevel Current battery level of the car.
     * @param purchaseDate       Date when the car was purchased.
     * @param capacity           Number of seats available in the car.
     * @param description        Additional description or details about the car.
     */
    public Car(String id, String manufacturer, String model, double purchasePrice, int currentBatteryLevel, LocalDate purchaseDate, int capacity, String description) {
        super(id, manufacturer, model, purchasePrice, currentBatteryLevel, description);
        this.purchaseDate = purchaseDate;
        this.capacity = capacity;
    }

    /**
     * Constructs a Car object with the specified attributes, using a default seating capacity of 2.
     * 
     * @param id                 Unique identifier for the car.
     * @param manufacturer       Manufacturer of the car.
     * @param model              Model of the car.
     * @param purchasePrice      Price at which the car was purchased.
     * @param currentBatteryLevel Current battery level of the car.
     * @param purchaseDate       Date when the car was purchased.
     * @param description        Additional description or details about the car.
     */
    public Car(String id, String manufacturer, String model, double purchasePrice, int currentBatteryLevel, LocalDate purchaseDate, String description) {
        this(id, manufacturer, model, purchasePrice, currentBatteryLevel, purchaseDate, DEFAULT_CAPACITY, description);
    }

    /**
     * Gets the purchase date of the car.
     * 
     * @return The date the car was purchased.
     */
    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }

    /**
     * Gets the seating capacity of the car.
     * 
     * @return The number of seats available in the car.
     */
    public int getCapacity() {
        return capacity;
    }

    /**
     * Returns a string representation of the Car object, including its attributes.
     * 
     * @return A string containing details about the car.
     */
    @Override
    public String toString() {
        return "Car{" +
                "id='" + id + '\'' +
                ", manufacturer='" + manufacturer + '\'' +
                ", model='" + model + '\'' +
                ", purchasePrice=" + purchasePrice +
                ", currentBatteryLevel=" + currentBatteryLevel +
                ", purchaseDate=" + purchaseDate +
                ", capacity=" + capacity +
                '}';
    }
}
