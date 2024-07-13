package Vehicles;

public class Scooter extends Vehicle {
	private float maxSpeed;
	
	public Scooter(String id, String manufacturer, String model, double purchasePrice, int currentBatteryLevel, float maxSpeed) {
		super(id, manufacturer, model, purchasePrice, currentBatteryLevel);
		this.maxSpeed = maxSpeed;
	}

	public float getMaxSpeed() {
		return maxSpeed;
	}
}
