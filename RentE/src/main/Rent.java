package main;
import SimulationPJ2.*;

import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;

import Vehicles.*;
public class Rent {
	private User user;
	private LocalDateTime dateTime;
	private Location locationStart;
	private Location locationEnd;
	private long timeUsed;
	private Vehicle vehicle;
	
	public Rent(User user, LocalDateTime dateTime, Location locationStart, Location locationEnd, int timeUsed, Vehicle vehicle) {
	
		if(vehicle instanceof Car) {
			if((user.getIsLocal() != null) && (user.getUserID() != null) && (user.getDriversLicense() != null)) {
				
			}else {
				SimulateUser sim =  new SimulateUser();
				user.setIsLocal(sim.generateIsLocal());
				user.setUserID(sim.generateUserID(user.getIsLocal()));
				user.setDriversLicense(sim.generateDriversLicense());
			}
			
		}
		this.user = user;
		this.dateTime = dateTime;
		this.locationStart = locationStart;
		this.locationEnd = locationEnd;
		this.timeUsed = timeUsed;
		this.vehicle = vehicle;
		
	}
	

//	public Rent(User user, LocalDateTime dateTime, Location locationStart, Location locationEnd, int timeUsed, Vehicle vehicle, Boolean isLocal) {
//		this.user = user;
//		this.dateTime = dateTime;
//		this.locationStart = locationStart;
//		this.locationEnd = locationEnd;
//		this.timeUsed = timeUsed;
//		
//	}

	public static void main(String[] args) {
		
		User user = new User("Iskra");
		LocalDateTime start = LocalDateTime.of(2024, Month.JULY, 4, 0, 0);
		try {
			Location loc = new Location(2,3);
			
			
			Location locc = new Location(2,3);
			LocalDate purchaseDate = LocalDate.of(2024, Month.JULY, 4);

			Car auto = new Car("a", "a", "a", 50, 2, purchaseDate);
			Rent rent = new Rent(user,start,loc,locc,3,auto);
			rent.generateInvoice();
			PriceCalculator p = new PriceCalculator(rent);
		}catch(OutOfRadiusException e) {
			System.out.println("Invalid location");
		}
		
		
		
				
		
	}
	public void generateInvoice() {
		String vType = null;
		if(vehicle instanceof Car) {
			vType = "Car";
        }else if(vehicle instanceof Scooter) {
        	vType = "Scooter";
        }else if(vehicle instanceof Bicycle) {
        	vType = "Bicycle";
        }else {
        	vType = "Ne znam kako smo do ovdje dosli...";
        }
        String invoice = "Invoice\n" +
                "-------\n" +
                "User: " + user.getName() + "\n" +
                "Vehicle Type: " + vType + "\n" +
                "Start Location: " + this.locationStart + "\n" +
                "End Location: " + this.locationEnd + "\n" +
                "Duration: " + this.timeUsed + " seconds\n";

        if (vehicle instanceof Car) {
            invoice += "ID Document: " + user.getUserID() + "\n" +
                    "Driving License: " + user.getDriversLicense() + "\n";
        }

        System.out.println(invoice);

        // Save to file
        try (PrintWriter writer = new PrintWriter("invoice.txt")) {
            writer.println(invoice);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	public User getUser() {
		return user;
	}


	public LocalDateTime getDateTime() {
		return dateTime;
	}


	public Location getLocationEnd() {
		return locationEnd;
	}


	public Location getLocationStart() {
		return locationStart;
	}


	public long getTimeUsed() {
		return timeUsed;
	}
	public Vehicle getVehicle() {
		return this.vehicle;
	}

	
	
}
