package Vehicles;

import java.time.LocalDate;
import java.util.List;

import main.FileLoadData;
import main.MyDateParser;

public class VehicleFunctions {
	public Vehicle findVehicleById(String vehicleId, Vehicle[] vehicleList) {
	    for (Vehicle v : vehicleList) {
	        if (v != null && v.getId().equals(vehicleId)) {
	            return v;
	        }
	    }
	    return null;
	}
	public Vehicle createVehicle(List<String> vehicleData, MyDateParser dp) {
	    String id = vehicleData.get(0);
	    String manu = vehicleData.get(1);
	    String model = vehicleData.get(2);
	    double price = Double.parseDouble(vehicleData.get(4));
	    String desc = vehicleData.get(7);
	    String vehType = vehicleData.get(8);

	    switch (vehType) {
	        case "automobil":
	            LocalDate date = dp.parseLocalDate(vehicleData.get(3));
	            return new Car(id, manu, model, price, 100, date, desc);
	        case "bicikl":
	            double range = Double.parseDouble(vehicleData.get(5));
	            return new Bicycle(id, manu, model, price, 100, range, desc);
	        case "trotinet":
	            float speed = Float.parseFloat(vehicleData.get(6));
	            return new Scooter(id, manu, model, price, 100, speed, desc);
	        default:
	            return null;
	    }
	}
	public Vehicle[] loadVehicles(FileLoadData fileData, MyDateParser dp) {
		VehicleFunctions veh = new VehicleFunctions();
	    Vehicle[] vehicleList = new Vehicle[fileData.getAllVehicles().size()];
	    for (int i = 0; i < fileData.getAllVehicles().size(); i++) {
	        List<String> vehicleData = fileData.getAllVehicles().get(i);
	        vehicleList[i] = veh.createVehicle(vehicleData, dp);
	    }
	    return vehicleList;
	}
}
