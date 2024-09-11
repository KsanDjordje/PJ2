package Vehicles;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import SimulationPJ2.RandomFunctions;


public abstract class Vehicle {
	protected String id;
	protected String manufacturer;
	protected String model;
	protected double purchasePrice;
	private Boolean hasMalfunction;
	private String malfunctionDescription;
	private LocalDateTime malfunctionTime;
	protected float currentBatteryLevel;
	protected String description;
	
    public Vehicle(String id, String manufacturer, String model, double purchasePrice, float currentBatteryLevel, String description) {
        this.id = id;
        this.manufacturer = manufacturer;
        this.model = model;
        this.purchasePrice = purchasePrice;
        this.currentBatteryLevel = currentBatteryLevel;
        this.hasMalfunction = false;
        if(description.isBlank()) {
        	this.description =  " ";
        }else {
            this.description = description;
        }
    }
    
    
    
    public String getId() {
        return id;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public String getModel() {
        return model;
    }

    public double getPurchasePrice() {
        return purchasePrice;
    }
    
    public float getCurrentBatteryLevel() {
		return currentBatteryLevel;
	}
    public String getDescription() {
    	return description;
    }
    public boolean chargeBattery(float amount) {
        this.currentBatteryLevel = Math.min(100, this.currentBatteryLevel + amount);
        System.out.println(this.getId() + "battery charged to: " + this.currentBatteryLevel);
        if (this.currentBatteryLevel < 100) 
        	return true;
        else 
        	return false;
    }

    public void dischargeBattery(float amount) {
        this.currentBatteryLevel = Math.max(0, this.currentBatteryLevel - amount);
    }
	
    public String getMalfunctionDescription() {
    	if(hasMalfunction) {
    		return malfunctionDescription;
    	}
        return "There is no malfunction.";
    }
    
    public LocalDateTime getMalfunctionTime() {
    	if(hasMalfunction) {
    		return malfunctionTime;
    	}
    	return null;
    }
    
    public void reportMalfunction(LocalDateTime malfunctionTime) {
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
    	
    	
    	// Format the malfunction date for grouping
        LocalDate date = malfunctionTime.toLocalDate();
        String dateString = date.format(DateTimeFormatter.ISO_LOCAL_DATE);
        
        // File for storing malfunctions
        File file = new File("malfunctionList.txt");

        // Use synchronized block to ensure only one thread writes to the file at a time
        synchronized (file) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
                // Write malfunction information to the file, grouped by date
                writer.write("Date: " + dateString + "\n");
                writer.write("Vehicle: " + this.getId()+ "\n");

                writer.write("Malfunction Time: " + malfunctionTime + "\n");
                writer.write("Description: " + this.malfunctionDescription + "\n");
                writer.write("---------------------------\n");
                this.sendVehicleToRepair();
            } catch (IOException e) {
                System.err.println("Error writing to malfunction list file: " + e.getMessage());
            }
        }
    }
    public void sendVehicleToRepair() {
    	this.hasMalfunction = false;
    	this.malfunctionDescription = "" ;
    	this.malfunctionTime = null;
    }
    public Boolean getHasMalfunction() {
    	return this.hasMalfunction;
    }
	
    
}
