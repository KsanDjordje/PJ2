package Vehicles;

public class Scooter extends Vehicle {
	private float maxSpeed;
	
	public Scooter(String id, String manufacturer, String model, double purchasePrice, int currentBatteryLevel, float maxSpeed, String description) {
		super(id, manufacturer, model, purchasePrice, currentBatteryLevel, description);
		this.maxSpeed = maxSpeed;
	}

	public float getMaxSpeed() {
		return maxSpeed;
	}
}
