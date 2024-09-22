package Vehicles;

import java.time.LocalDate;


public class Car extends Vehicle{
	private LocalDate purchaseDate;
	private int capacity;
	
	private static final int DEFAULT_CAPACITY = 2;
	
	
	public Car(String id, String manufacturer, String model, double purchasePrice, int currentBatteryLevel, LocalDate purchaseDate, int capacity, String description) {
		super(id, manufacturer, model, purchasePrice, currentBatteryLevel, description);
		this.purchaseDate = purchaseDate;
		this.capacity = capacity;
	}
	public Car(String id, String manufacturer, String model, double purchasePrice, int currentBatteryLevel, LocalDate purchaseDate, String description) {
		this(id, manufacturer, model, purchasePrice, currentBatteryLevel, purchaseDate, DEFAULT_CAPACITY, description);		
	}
	public LocalDate getPurchaseDate() {
    	return purchaseDate;
    }
	
	public int getCapacity() {
		return capacity;
	}
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
