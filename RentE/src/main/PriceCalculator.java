package main;
import Vehicles.*;
import java.io.IOException;
import java.util.Properties;
import java.io.File;
import java.io.FileInputStream;
public class PriceCalculator {
	private Vehicle vehicle;
	private double distanceNarrow;
	private double distanceWide;
    private double discount;
    private double discountPromotion;
    private double carUnitPrice;
    private double bikeUnitPrice;
    private double scooterUnitPrice;
    private double timeTraveled;
    private Boolean isWide;
    private Boolean applyDiscount;
    
    private double price;
    private double priceTotal;
    private double discountTotal;
    private double discountPromTotal;
    //TODO
	private boolean applyPromotion;
    
	public PriceCalculator(Rent rented, Boolean isWide, Boolean applyDiscount, Boolean applyPromotion) {
		Properties prop = new Properties();
		//String basePath = new File("").getAbsolutePath();
	    //System.out.println(basePath);

	    String path = new File("src/main/rental.properties").getAbsolutePath();
	    //System.out.println(path);
	    
		try(FileInputStream input = new FileInputStream(path)){
			prop.load(input);
			
			System.out.println(prop.getProperty("version"));
			this.price = 0;
		    this.priceTotal = 0;
		    this.discountTotal = 0;
		    this.discountPromTotal = 0 ;
			this.isWide = isWide;
			this.applyDiscount = applyDiscount;
			this.applyPromotion = applyPromotion;
			this.timeTraveled = rented.getTimeUsed();
			this.vehicle = rented.getVehicle();
			this.distanceNarrow = Double.parseDouble(prop.getProperty("DISTANCE_NARROW"));
            this.distanceWide = Double.parseDouble(prop.getProperty("DISTANCE_WIDE"));
            this.discount = Double.parseDouble(prop.getProperty("DISCOUNT")) / 100;
            this.discountPromotion = Double.parseDouble(prop.getProperty("DISCOUNT_PROM")) / 100;
            this.carUnitPrice = Double.parseDouble(prop.getProperty("CAR_UNIT_PRICE"));
            this.bikeUnitPrice = Double.parseDouble(prop.getProperty("BIKE_UNIT_PRICE"));
            this.scooterUnitPrice = Double.parseDouble(prop.getProperty("SCOOTER_UNIT_PRICE"));
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public double calculatePrice() {
		double result = 0.0;
		double distance = 0.0;
		
		if(isWide) {
			distance = this.timeTraveled * this.distanceWide;
		}else {
			distance = this.timeTraveled * this.distanceNarrow;	
		}
		
		
		
		if(vehicle instanceof Car) {
			result = distance * this.carUnitPrice;
		}else if(vehicle instanceof Bicycle) {
			result = distance * this.bikeUnitPrice;
		}else {
			result = distance * this.scooterUnitPrice;
		}
		
		
		this.price = result;
		this.priceApplyMalfunction(this.vehicle.hasMalfunction());
		return applyDiscount(result);
		//return applyDiscount(result);
	}
	public double applyDiscount(double price) {
		double result = 0;
		if(this.applyDiscount) {
			this.discountTotal = price * this.discount;
		}
		result = price - this.discountTotal;

		if(this.applyPromotion) {
			this.discountPromTotal = price * this.discountPromotion;
		}
		return (result - this.discountPromTotal);
	}
	public void priceApplyMalfunction(Boolean malfunction) {
		if(malfunction == true) {
			this.price = 0;
		}
	}
}
