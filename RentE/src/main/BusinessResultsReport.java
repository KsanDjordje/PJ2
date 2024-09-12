package main;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import Vehicles.Bicycle;
import Vehicles.Car;
import Vehicles.Scooter;
import Vehicles.Vehicle;

public class BusinessResultsReport {
	private Double totalEarnings;
	private Double totalDiscount;
	private Double totalDiscountPromotion;
	private Double totalDiscountQuantity;
	private Double totalMaintenancePrice;
	private Double totalRepairPrice;
	private Double totalExpenses;
	private Double totalTax;
	
	private Integer cityInnerRentals;
	private Integer cityOuterRentals;
	
	private List<DailyEarnings> dailyEarnings;
	private List<DailyEarnings> dailyDiscountPromotion;
	private List<DailyEarnings>  dailyDiscount;
	private List<DailyEarnings>  dailyDiscountQuantity;
	private List<DailyEarnings>  dailyMaintenancePrice;
	private List<DailyEarnings>  dailyRepairPrice;
	
                                 
	public BusinessResultsReport(){
		this.totalEarnings = 0.0;         
        this.totalDiscount = 0.0;         
        this.totalDiscountPromotion = 0.0;
        this.totalDiscountQuantity = 0.0; 
        this.totalMaintenancePrice = 0.0;
        this.totalRepairPrice = 0.0;     
        this.totalExpenses = 0.0;        
        this.totalTax = 0.0;
        this.cityInnerRentals = 0;
    	this.cityOuterRentals = 0;
        
        this.dailyEarnings = new ArrayList<>();
        this.dailyDiscountPromotion = new ArrayList<>();
        this.dailyDiscount = new ArrayList<>();
        this.dailyDiscountQuantity = new ArrayList<>();
        this.dailyMaintenancePrice = new ArrayList<>();
        this.dailyRepairPrice = new ArrayList<>();            

	}
	public void dailyReport(LocalDate date, Double price, Double discount, Double promo, Double qDiscount, Boolean innerCity, Boolean malfunction, Vehicle vehicle) {
        synchronized(this) {
        	// Add earnings to each list
        	if(!malfunction) {
        		addEarningsToList(dailyEarnings, date, price);
                addEarningsToList(dailyDiscountPromotion, date, promo);
                addEarningsToList(dailyDiscount, date, discount);
                addEarningsToList(dailyDiscountQuantity, date, qDiscount);
        	}
            
            
            if(innerCity) {
            	this.cityInnerRentals++;
            }else {
            	this.cityOuterRentals++;
            }
            
            double maintenanceCost = price * 0.2;
            addEarningsToList(dailyMaintenancePrice, date, maintenanceCost);    
            double repairCost = 0.0;
            if (malfunction) {
                
                if (vehicle instanceof Car) {
                    repairCost = vehicle.getPurchasePrice() * 0.07;
                } else if (vehicle instanceof Bicycle) {
                    repairCost = vehicle.getPurchasePrice() * 0.04;
                } else if (vehicle instanceof Scooter) {
                    repairCost = vehicle.getPurchasePrice() * 0.02;
                }
                addEarningsToList(dailyRepairPrice, date, repairCost);
            }
            totalEarnings += price;
            totalDiscount += discount;
            totalDiscountPromotion += promo;
            totalDiscountQuantity += qDiscount;
            totalMaintenancePrice += maintenanceCost;
            totalRepairPrice += repairCost;
            totalExpenses = totalEarnings * 0.2; // 20% of totalEarnings
            totalTax = (totalEarnings - totalMaintenancePrice - totalRepairPrice - totalExpenses) * 0.10; // 10% of the remaining amount
        }
		
    }
	
	// General method to find or add earnings for a specific list
    private void addEarningsToList(List<DailyEarnings> earningsList, LocalDate date, double price) {
        DailyEarnings entry = findEarningsByDate(earningsList, date);
        if (entry == null) {
            entry = new DailyEarnings(date); // Create a new entry if not found
            earningsList.add(entry);
        }
        entry.addEarnings(price); 
    }
    // Helper method to find a DailyEarnings object by date
    private DailyEarnings findEarningsByDate(List<DailyEarnings> earningsList, LocalDate date) {
        for (DailyEarnings dailyEarning : earningsList) {
            if (dailyEarning.getDate().equals(date)) {
                return dailyEarning; 
            }
        }
        return null;
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Business Results Report:\n");
        sb.append(String.format("Total Earnings: %.2f\n", totalEarnings));
        sb.append(String.format("Total Discount: %.2f\n", totalDiscount));
        sb.append(String.format("Total Discount Promotion: %.2f\n", totalDiscountPromotion));
        sb.append(String.format("Total Discount Quantity: %.2f\n", totalDiscountQuantity));
        sb.append(String.format("Total Maintenance Price: %.2f\n", totalMaintenancePrice));
        sb.append(String.format("Total Repair Price: %.2f\n", totalRepairPrice));
        sb.append(String.format("Total Expenses: %.2f\n", totalExpenses));
        sb.append(String.format("Total Tax: %.2f\n", totalTax));
        sb.append(String.format("City Inner Rentals: %d\n", cityInnerRentals));
        sb.append(String.format("City Outer Rentals: %d\n", cityOuterRentals));

        sb.append("Daily Earnings:\n");
        for (DailyEarnings de : dailyEarnings) {
            sb.append(de.toString()).append("\n");
        }

        sb.append("Daily Discount Promotion:\n");
        for (DailyEarnings de : dailyDiscountPromotion) {
            sb.append(de.toString()).append("\n");
        }

        sb.append("Daily Discount:\n");
        for (DailyEarnings de : dailyDiscount) {
            sb.append(de.toString()).append("\n");
        }

        sb.append("Daily Discount Quantity:\n");
        for (DailyEarnings de : dailyDiscountQuantity) {
            sb.append(de.toString()).append("\n");
        }

        sb.append("Daily Maintenance Price:\n");
        for (DailyEarnings de : dailyMaintenancePrice) {
            sb.append(de.toString()).append("\n");
        }

        sb.append("Daily Repair Price:\n");
        for (DailyEarnings de : dailyRepairPrice) {
            sb.append(de.toString()).append("\n");
        }

        return sb.toString();
    }
    private void addDailyEarnings(LocalDate date, double price) {
        addEarningsToList(dailyEarnings, date, price);
    }

	private void addDailyDiscountPromotion(LocalDate date, double price) {
        addEarningsToList(dailyDiscountPromotion, date, price);
    }

    private void addDailyDiscount(LocalDate date, double price) {
        addEarningsToList(dailyDiscount, date, price);
    }

    private void addDailyDiscountQuantity(LocalDate date, double price) {
        addEarningsToList(dailyDiscountQuantity, date, price);
    }


    private void addDailyMaintenancePrice(LocalDate date, double price) {
        addEarningsToList(dailyMaintenancePrice, date, price);
    }

    private void addDailyRepairPrice(LocalDate date, double price) {
        addEarningsToList(dailyRepairPrice, date, price);
    }
    public Double getTotalEarnings() {
		return totalEarnings;
	}
	public Double getTotalDiscount() {
		return totalDiscount;
	}
	public Double getTotalDiscountPromotion() {
		return totalDiscountPromotion;
	}
	public Double getTotalDiscountQuantity() {
		return totalDiscountQuantity;
	}
	public Double getTotalMaintenancePrice() {
		return totalMaintenancePrice;
	}
	public Double getTotalRepairPrice() {
		return totalRepairPrice;
	}
	public Double getTotalExpenses() {
		return totalExpenses;
	}
	public Double getTotalTax() {
		return totalTax;
	}
	public List<DailyEarnings> getDailyEarnings() {
		return dailyEarnings;
	}
	public List<DailyEarnings> getDailyDiscountPromotion() {
		return dailyDiscountPromotion;
	}
	public List<DailyEarnings> getDailyDiscount() {
		return dailyDiscount;
	}
	public List<DailyEarnings> getDailyDiscountQuantity() {
		return dailyDiscountQuantity;
	}
	public Integer getCityInnerRentals() {
		return cityInnerRentals;
	}
	public Integer getCityOuterRentals() {
		return cityOuterRentals;
	}
	public List<DailyEarnings> getDailyMaintenancePrice() {
		return dailyMaintenancePrice;
	}
	public List<DailyEarnings> getDailyRepairPrice() {
		return dailyRepairPrice;
	}
	

}
