package Vehicles;

import java.time.LocalDate;


public class Car extends Vehicle{
	private LocalDate purchaseDate;
	private int capacity;
	
	private static final int DEFAULT_CAPACITY = 2;
	
	
	public Car(String id, String manufacturer, String model, double purchasePrice, int currentBatteryLevel, LocalDate purchaseDate, int capacity) {
		super(id, manufacturer, model, purchasePrice, currentBatteryLevel);
		this.purchaseDate = purchaseDate;
		this.capacity = capacity;
	}
	public Car(String id, String manufacturer, String model, double purchasePrice, int currentBatteryLevel, LocalDate purchaseDate) {
		this(id, manufacturer, model, purchasePrice, currentBatteryLevel, purchaseDate, DEFAULT_CAPACITY);		
	}
	public LocalDate getPurchaseDate() {
    	return purchaseDate;
    }
	
	public int getCapacity() {
		return capacity;
	}

}
