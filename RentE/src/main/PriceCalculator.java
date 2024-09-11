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
	private boolean applyPromotion;
    private double price;
    private double priceTotal;
    private double discountTotal;
    private double discountPromTotal;
    private double priceDiscounted;
    
	public PriceCalculator(Vehicle vehicle, double timeUsed, Boolean isWide, Boolean applyDiscount, Boolean applyPromotion) {
		Properties prop = new Properties();
		//String basePath = new File("").getAbsolutePath();
	    //System.out.println(basePath);

	    String path = new File("src/main/rental.properties").getAbsolutePath();
	    //System.out.println(path);
	    
		try(FileInputStream input = new FileInputStream(path)){
			prop.load(input);
			
			//System.out.println(prop.getProperty("version"));
			this.price = 0;
		    this.priceTotal = 0;
		    this.discountTotal = 0;
		    this.discountPromTotal = 0 ;
		    this.priceDiscounted = 0;
			this.isWide = isWide;
			this.applyDiscount = applyDiscount;
			this.applyPromotion = applyPromotion;
			this.timeTraveled = timeUsed;
			this.vehicle = vehicle;
			this.distanceNarrow = Double.parseDouble(prop.getProperty("DISTANCE_NARROW"));
            this.distanceWide = Double.parseDouble(prop.getProperty("DISTANCE_WIDE"));
            this.discount = Double.parseDouble(prop.getProperty("DISCOUNT"));
            this.discountPromotion = Double.parseDouble(prop.getProperty("DISCOUNT_PROM"));
            this.carUnitPrice = Double.parseDouble(prop.getProperty("CAR_UNIT_PRICE"));
            this.bikeUnitPrice = Double.parseDouble(prop.getProperty("BIKE_UNIT_PRICE"));
            this.scooterUnitPrice = Double.parseDouble(prop.getProperty("SCOOTER_UNIT_PRICE"));
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public double calculatePrice() {
		double distance = 0.0;
		
		if(isWide) {
			distance = this.timeTraveled * this.distanceWide;
		}else {
			distance = this.timeTraveled * this.distanceNarrow;	
		}
		
		
		
		if(vehicle instanceof Car) {
			this.price = distance * this.carUnitPrice;
		}else if(vehicle instanceof Bicycle) {
			this.price = distance * this.bikeUnitPrice;
		}else {
			this.price = distance * this.scooterUnitPrice;
		}
		
		
		this.priceApplyMalfunction(this.vehicle.getHasMalfunction());
		this.priceTotal = applyDiscount();
		return this.priceTotal;
		//return applyDiscount(result);
	}
	public double applyDiscount() {
		if(this.applyDiscount) {
			this.discountTotal = this.price * this.discount;
		}
		if(this.applyPromotion) {
			this.discountPromTotal = this.price * this.discountPromotion;
		}
		this.priceDiscounted = this.discountTotal + this.discountPromTotal;
		
		return (price - this.priceDiscounted);
	}
	
	public void priceApplyMalfunction(Boolean malfunction) {
		if(malfunction == true) {
			this.price = 0;
		}
	}
	
	public double getDiscountedAmmountFromPromotion() {
		return discountPromTotal;
	}
	
	public double getDiscountedAmmountFromNum() {
		return discountTotal;
	}
	
	public double getPriceDiscounted() {
		return priceDiscounted;
	}
	
	public double getFullPrice() {
		return this.price;
	}
	
	public double getPrice() {
		return this.priceTotal;
	}
}
