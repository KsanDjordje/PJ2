package SimulationPJ2;

import java.time.LocalDateTime;
import java.util.List;

import Vehicles.Vehicle;
import Vehicles.VehicleFunctions;
import main.FileLoadData;
import main.Location;
import main.MyDateParser;
import main.OutOfRadiusException;
import main.Rent;
import main.User;

public class SimulationFunctions {
	public void generateInvoices(Rent[] rentedList) {
	    int i = 0;
	    for (Rent rent : rentedList) {
	        rent.generateInvoice(String.valueOf(i++));
	    }
	}

	public void calculateTotals(Rent[] rentedList) {
	    double totalProfit = 0;
	    double totalPromotion = 0;
	    double totalNumDiscount = 0;
	    double totalDiscount = 0;

	    for (Rent rent : rentedList) {
	        rent.calculatePrice();
	        totalProfit += rent.getTotal();
	        totalPromotion += rent.getPromo();
	        totalNumDiscount += rent.getDiscount();
	        totalDiscount += rent.getTotalDiscount();
	    }

	    System.out.println("Total Profit: " + totalProfit);
	    System.out.println("Total Promotion: " + totalPromotion);
	    System.out.println("Total Number Discount: " + totalNumDiscount);
	    System.out.println("Total Discount: " + totalDiscount);
	}
	public Rent[] loadRents(FileLoadData fileData, MyDateParser dp, Vehicle[] vehicleList) throws OutOfRadiusException {
	    Rent[] rentedList = new Rent[fileData.getSortedList().size()];
	    for (int i = 0; i < fileData.getSortedList().size(); i++) {
	        List<String> data = fileData.getSortedList().get(i);
	        rentedList[i] = createRent(data, dp, vehicleList);
	    }
	    return rentedList;
	}
	public Rent createRent(List<String> data, MyDateParser dp, Vehicle[] vehicleList) throws OutOfRadiusException {
	    LocalDateTime dateTime = dp.parseLocalDateTime(data.get(0));
	    User user = new User(data.get(1));
	    String vehicleId = data.get(2);
	    Location locationStart = new Location(data.get(3), data.get(4));
	    Location locationEnd = new Location(data.get(5), data.get(6));
	    int timeUsed = Integer.parseInt(data.get(7));
	    boolean malf = data.get(8).equals("da");
	    boolean promotion = data.get(9).equals("da");
	    VehicleFunctions veh = new VehicleFunctions();
	    Vehicle vehicle = veh.findVehicleById(vehicleId, vehicleList);
	    if (vehicle != null) {
	        
	        synchronized(vehicle) {
		        return new Rent(user, dateTime, locationStart, locationEnd, timeUsed, vehicle, promotion, malf);

	        }
	    }
	    return null;
	}
}
