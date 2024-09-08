package main;
import SimulationPJ2.*;

import java.io.PrintWriter;
import java.time.LocalDateTime;

import Vehicles.*;
public class Rent {
	private User user;
	private LocalDateTime dateTime;
	private Location locationStart;
	private Location locationEnd;
	private double timeUsed;
	private Vehicle vehicle;
	private Boolean promotion;
	PriceCalculator price;
	public Rent(User user, LocalDateTime dateTime, Location locationStart, Location locationEnd, int timeUsed, Vehicle vehicle, Boolean promotion) {
	
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
		this.promotion = promotion;
		
		calculatePrice();
	}
	public void calculatePrice() {
		PathFinder path = new PathFinder(locationStart, locationEnd);
		this.price = new PriceCalculator(vehicle,timeUsed, path.isWide(),user.getTimesRented() % 10 == 0, promotion);
		price.calculatePrice();
	}


	public void generateInvoice(String fileName) {
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
		double fullPrice = price.getFullPrice();
	    double discountFromPromotion = price.getDiscountedAmmountFromPromotion();
	    double discountFromNum = price.getDiscountedAmmountFromNum();
	    double priceDiscounted = price.getPriceDiscounted();
	    double priceTotal = price.getPrice();

	    String invoice = "Invoice\n" +
	            "-------\n" +
	            "User: " + user.getName() + "\n" +
	            "Vehicle Type: " + vType + "\n" +
	            "Start Location: " + this.locationStart + "\n" +
	            "End Location: " + this.locationEnd + "\n" +
	            "Duration: " + this.timeUsed + " seconds\n" +
	            "Full Price: $" + String.format("%.2f", fullPrice) + "\n" +
	            "Discount from Promotion: -$" + String.format("%.2f", discountFromPromotion) + "\n" +
	            "Discount from Quantity: -$" + String.format("%.2f", discountFromNum) + "\n" +
	            "Discounted Price: $" + String.format("%.2f", priceDiscounted) + "\n" +
	            "Total Price: $" + String.format("%.2f", priceTotal) + "\n";

	    if (vehicle instanceof Car) {
	        invoice += "ID Document: " + user.getUserID() + "\n" +
	                "Driving License: " + user.getDriversLicense() + "\n";
	    }

        //System.out.println(invoice);

        // Save to file
        try (PrintWriter writer = new PrintWriter(fileName+".txt")) {
            writer.println(invoice);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	
	public double getTotal() {
        return price.getPrice();
    }

    public double getPromo() {
        return price.getDiscountedAmmountFromPromotion();
    }

    public double getDiscount() {
        return price.getDiscountedAmmountFromNum();
    }

    public double getTotalDiscount() {
        return price.getPriceDiscounted();
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


	public double getTimeUsed() {
		return timeUsed;
	}
	public Vehicle getVehicle() {
		return this.vehicle;
	}
	public void setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
	}
	
	
}
