package main;
import Vehicles.*;
import java.io.IOException;
import java.util.Properties;
import java.io.File;
import java.io.FileInputStream;
public class PriceCalculator {
	private Vehicle vehicle;
	public double distanceNarrow;
    public double distanceWide;
    public double discount;
    public double discountPromotion;
    public double carUnitPrice;
    public double bikeUnitPrice;
    public double scooterUnitPrice;
	public PriceCalculator(Rent rented) {
		Properties prop = new Properties();
		//String basePath = new File("").getAbsolutePath();
	    //System.out.println(basePath);

	    String path = new File("src/main/rental.properties").getAbsolutePath();
	    //System.out.println(path);
	    
		try(FileInputStream input = new FileInputStream(path)){
			prop.load(input);
			
			System.out.println(prop.getProperty("version"));
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
	public double distanceTraveled() {
		double result = 0.0;
		
		
		
		return result;
	}
	public double calculatePrice() {
		double result = 0.0;
		double distance = 0.0;
		
//		if() {
//			
//		}else {
//			
//		}
//		
		if(vehicle instanceof Car) {
			result = distance * this.carUnitPrice;
		}else if(vehicle instanceof Bicycle) {
			result = distance * this.bikeUnitPrice;
		}else {
			result = distance * this.scooterUnitPrice;
		}
		
		return applyDiscount(result);
	}
	public double applyDiscount(double price) {
		double result = 0;
		result = price - (price * this.discount);
		return (result - (price * this.discountPromotion));
	}
}
