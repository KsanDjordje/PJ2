package main;

import java.time.LocalDate;
import java.util.List;

public class TableResultsData {
	private final LocalDate date;
	private final Double dailyEarnings;           
   	private final Double dailyDiscountPromotion;  
   	private final Double  dailyDiscount;          
   	private final Double  dailyDiscountQuantity;  
   	private final Double  dailyMaintenancePrice;  
   	private final Double dailyRepairPrice;
   	
   	public TableResultsData(LocalDate date, Double dailyEarnings, Double dailyDiscount, Double dailyDiscountPromotion,
 Double dailyDiscountQuantity, Double dailyMaintenancePrice, Double dailyRepairPrice) {
   		this.date = date;   		                
	    this.dailyEarnings = dailyEarnings;            
	    this.dailyDiscountPromotion = dailyDiscountPromotion;
	    this.dailyDiscount =  dailyDiscount;
	    this.dailyDiscountQuantity = dailyDiscountQuantity;
	    this.dailyMaintenancePrice = dailyMaintenancePrice;
	    this.dailyRepairPrice = dailyRepairPrice;
      	}

	public LocalDate getDate() {
		return date;
	}

	public Double getDailyEarnings() {
		return dailyEarnings;
	}

	public Double getDailyDiscountPromotion() {
		return dailyDiscountPromotion;
	}

	public Double getDailyDiscount() {
		return dailyDiscount;
	}

	public Double getDailyDiscountQuantity() {
		return dailyDiscountQuantity;
	}

	public Double getDailyMaintenancePrice() {
		return dailyMaintenancePrice;
	}

	public Double getDailyRepairPrice() {
		return dailyRepairPrice;
	}

	 
   	
   	
   	
   	
  }     
                                             
                                             