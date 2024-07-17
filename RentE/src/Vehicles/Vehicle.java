package Vehicles;

import java.time.LocalDateTime;


public abstract class Vehicle {
	private String id;
	private String manufacturer;
	private String model;
	private double purchasePrice;
	private Boolean hasMalfunction;
	private String malfunctionDescription;
	private LocalDateTime malfunctionTime;
	private float currentBatteryLevel;
	
    public Vehicle(String id, String manufacturer, String model, double purchasePrice, float currentBatteryLevel) {
        this.id = id;
        this.manufacturer = manufacturer;
        this.model = model;
        this.purchasePrice = purchasePrice;
        this.currentBatteryLevel = currentBatteryLevel;
        this.hasMalfunction = false;
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
    
    public void chargeBattery(float amount) {
        this.currentBatteryLevel = Math.min(100, this.currentBatteryLevel + amount);
    }

    public void dischargeBattery(float amount) {
        this.currentBatteryLevel = Math.max(0, this.currentBatteryLevel - amount);
    }
	
    public String getMalfunctionDescription() {
    	if(hasMalfunction) {
    		return malfunctionDescription;
    	}
        return null;
    }
    
    public LocalDateTime getMalfunctionTime() {
    	if(hasMalfunction) {
    		return malfunctionTime;
    	}
    	return null;
    }
    
    public void reportMalfunction(String description, LocalDateTime malfunctionTime) {
    	this.hasMalfunction = true;
    	this.malfunctionDescription = description;
    	this.malfunctionTime = malfunctionTime;
    }
    public Boolean hasMalfunction() {
    	return this.hasMalfunction;
    }
	
    
}
