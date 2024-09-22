package Vehicles;

import java.time.LocalDateTime;

public class Malfunction {
	private String vehicleId;
	private LocalDateTime malfunctionTime;
	private String description;
	
	public Malfunction(String vehicleId, LocalDateTime malfunctionTime, String description) {
        this.vehicleId = vehicleId;
        this.malfunctionTime = malfunctionTime;
        this.description = description;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public LocalDateTime getMalfunctionTime() {
        return malfunctionTime;
    }

    public String getDescription() {
        return description;
    }
    @Override
	public String toString() {
		return "(" + this.vehicleId + ", " + this.malfunctionTime + ", "+ this.description +")";
    }
}
