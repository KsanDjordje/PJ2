package Vehicles;


public class Bicycle extends Vehicle {
	private double range;
	
	public Bicycle(String id, String manufacturer, String model, double purchasePrice, int currentBatteryLevel, double range) {
		super(id, manufacturer, model, purchasePrice, currentBatteryLevel);
		this.range = range;
	}
	public double getRange() {
		return range;
	}
	
}
