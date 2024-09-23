package vehicles;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import myUtility.RandomFunctions;

/**
 * Abstract class representing a vehicle with attributes of ID, manufacturer, model, purchase price, 
 * battery level, and the ability to report malfunctions. It includes methods for charging and discharging 
 * the battery, reporting and repairing malfunctions.
 */
public abstract class Vehicle implements Serializable {
    private static final long serialVersionUID = 1L; // Serialization version identifier
    /** Unique identifier for the vehicle. */
    protected String id;
    
    /** Manufacturer of the vehicle. */
    protected String manufacturer;
    
    /** Model of the vehicle. */
    protected String model;
    
    /** Purchase price of the vehicle. */
    protected double purchasePrice;
    
    /** Indicates if the vehicle has a malfunction. */
    private Boolean hasMalfunction;
    
    /** Description of the malfunction, if any. */
    private String malfunctionDescription;
    
    /** Time when the malfunction occurred. */
    private LocalDateTime malfunctionTime;
    
    /** Current battery level of the vehicle, represented as a percentage. */
    protected float currentBatteryLevel;
    
    /** Additional description of the vehicle. */
    protected String description;
    
    /**
     * Constructor to initialize the vehicle object with its basic attributes.
     * 
     * @param id                  Unique identifier for the vehicle.
     * @param manufacturer        Manufacturer of the vehicle.
     * @param model               Model of the vehicle.
     * @param purchasePrice       Price at which the vehicle was purchased.
     * @param currentBatteryLevel Initial battery level of the vehicle.
     * @param description         Description of the vehicle.
     */
    public Vehicle(String id, String manufacturer, String model, double purchasePrice, float currentBatteryLevel, String description) {
        this.id = id;
        this.manufacturer = manufacturer;
        this.model = model;
        this.purchasePrice = purchasePrice;
        this.currentBatteryLevel = currentBatteryLevel;
        this.hasMalfunction = false;
        this.description = description.isBlank() ? " " : description;
    }
    
    /**
     * Gets the vehicle's unique identifier.
     * 
     * @return The vehicle's ID.
     */
    public String getId() {
        return id;
    }

    /**
     * Gets the manufacturer of the vehicle.
     * 
     * @return The manufacturer's name.
     */
    public String getManufacturer() {
        return manufacturer;
    }
    /**
     * Gets the model name of the vehicle.
     * 
     * @return The vehicle's model.
     */
    public String getModel() {
        return model;
    }

    /**
     * Gets the purchase price of the vehicle.
     * 
     * @return The vehicle's purchase price.
     */
    public double getPurchasePrice() {
        return purchasePrice;
    }

    /**
     * Gets the current battery level of the vehicle.
     * 
     * @return The current battery level as a percentage.
     */
    public float getCurrentBatteryLevel() {
        return currentBatteryLevel;
    }

    /**
     * Gets the description of the vehicle.
     * 
     * @return The vehicle's description.
     */
    public String getDescription() {
        return description;
    }

    // Battery-related methods

    /**
     * Charges the vehicle's battery by a specified amount. The battery level cannot exceed 100%.
     * 
     * @param amount The amount to charge the battery.
     * @return True if the battery is not fully charged yet, false if it's fully charged.
     */
    public boolean chargeBattery(float amount) {
        this.currentBatteryLevel = Math.min(100, this.currentBatteryLevel + amount);
        System.out.println(this.getId() + " battery charged to: " + this.currentBatteryLevel);
        return this.currentBatteryLevel < 100;
    }

    /**
     * Discharges the vehicle's battery by a specified amount. The battery level cannot drop below 0%.
     * 
     * @param amount The amount to discharge the battery.
     */
    public void dischargeBattery(float amount) {
        this.currentBatteryLevel = Math.max(0, this.currentBatteryLevel - amount);
    }

    // Malfunction-related methods

    /**
     * Gets the description of the malfunction if the vehicle has a malfunction.
     * 
     * @return The malfunction description, or a message indicating no malfunction.
     */
    public String getMalfunctionDescription() {
        if(hasMalfunction) {
            return malfunctionDescription;
        }
        return "There is no malfunction.";
    }

    /**
     * Gets the time when the malfunction occurred.
     * 
     * @return The malfunction time, or null if no malfunction occurred.
     */
    public LocalDateTime getMalfunctionTime() {
        if(hasMalfunction) {
            return malfunctionTime;
        }
        return null;
    }

    /**
     * Reports a malfunction for the vehicle at the specified time. Generates a random malfunction description.
     * 
     * @param malfunctionTime The time when the malfunction occurred.
     */
    public void reportMalfunction(LocalDateTime malfunctionTime) {
        // Array of possible malfunction messages
        String[] malfunctionMessages = {
            "Engine overheating - coolant levels are low.",
            "Battery malfunction - unable to start the engine.",
            "Brake system failure - emergency stop required.",
            "Transmission slipping - unusual noises during acceleration.",
            "Flat tire - replace or repair the tire immediately.",
            "Suspension issues - uneven ride and excessive bouncing.",
            "Power steering failure - difficulty in steering the vehicle.",
            "Fuel leak detected - potential fire hazard.",
            "Electrical system failure - dashboard lights not functioning.",
            "ABS malfunction - anti-lock braking system not operational.",
            "Exhaust system problem - increased noise and reduced performance.",
            "Cooling system leak - possible radiator issue.",
            "Headlight failure - visibility issues during night driving.",
            "Wiper blades malfunction - poor visibility in rainy conditions.",
            "Engine knocking - unusual sounds from the engine compartment."
        };

        RandomFunctions gen = new RandomFunctions();

        this.hasMalfunction = true;
        this.malfunctionDescription = malfunctionMessages[gen.generateRandomNumber(0, malfunctionMessages.length)];
        this.malfunctionTime = malfunctionTime;

        LocalDate date = malfunctionTime.toLocalDate();
        String dateString = date.format(DateTimeFormatter.ISO_LOCAL_DATE);

        File file = new File("malfunctionList.txt");

        synchronized (file) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
                writer.write("Date: " + dateString + "\n");
                writer.write("Vehicle: " + this.getId() + "\n");
                writer.write("Malfunction Time: " + malfunctionTime + "\n");
                writer.write("Description: " + this.malfunctionDescription + "\n");
                writer.write("---------------------------\n");

                this.sendVehicleToRepair();
            } catch (IOException e) {
                System.err.println("Error writing to malfunction list file: " + e.getMessage());
            }
        }
    }

    /**
     * Resets the malfunction status of the vehicle and prepares it for further use.
     */
    public void sendVehicleToRepair() {
        this.hasMalfunction = false;
        this.malfunctionDescription = "";
        this.malfunctionTime = null;
    }

    /**
     * Checks if the vehicle currently has a malfunction.
     * 
     * @return True if the vehicle has a malfunction, false otherwise.
     */
    public Boolean getHasMalfunction() {
        return this.hasMalfunction;
    }
}
