package reports;

import java.time.LocalDate;

/**
 * The {@code TableResultsData} class represents the daily results data for a business, 
 * including earnings, discounts, maintenance, and repair costs for a specific date.
 */
public class TableResultsData {
    
    /**
     * The date for which the results are recorded.
     */
    private final LocalDate date;
    
    /**
     * The total earnings for the day.
     */
    private final Double dailyEarnings;           
    
    /**
     * The total amount from discount promotions for the day.
     */
    private final Double dailyDiscountPromotion;  
    
    /**
     * The total amount of discounts applied for the day.
     */
    private final Double dailyDiscount;          
    
    /**
     * The total quantity of discounts applied for the day.
     */
    private final Double dailyDiscountQuantity;  
    
    /**
     * The total maintenance costs for the day.
     */
    private final Double dailyMaintenancePrice;  
    
    /**
     * The total repair costs for the day.
     */
    private final Double dailyRepairPrice;

    /**
     * Constructs a {@code TableResultsData} object with the specified values.
     *
     * @param date                     the date of the results
     * @param dailyEarnings            the total earnings for the day
     * @param dailyDiscount            the total discounts applied for the day
     * @param dailyDiscountPromotion    the total discount promotion amount for the day
     * @param dailyDiscountQuantity    the total quantity of discounts applied for the day
     * @param dailyMaintenancePrice    the total maintenance costs for the day
     * @param dailyRepairPrice         the total repair costs for the day
     */
    public TableResultsData(LocalDate date, Double dailyEarnings, Double dailyDiscount, 
                            Double dailyDiscountPromotion, Double dailyDiscountQuantity, 
                            Double dailyMaintenancePrice, Double dailyRepairPrice) {
        this.date = date;           		                
        this.dailyEarnings = dailyEarnings;            
        this.dailyDiscountPromotion = dailyDiscountPromotion;
        this.dailyDiscount = dailyDiscount;
        this.dailyDiscountQuantity = dailyDiscountQuantity;
        this.dailyMaintenancePrice = dailyMaintenancePrice;
        this.dailyRepairPrice = dailyRepairPrice;
    }

    /**
     * Returns the date for the results.
     *
     * @return the date of the results
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Returns the total earnings for the day.
     *
     * @return the daily earnings
     */
    public Double getDailyEarnings() {
        return dailyEarnings;
    }

    /**
     * Returns the total amount from discount promotions for the day.
     *
     * @return the daily discount promotion amount
     */
    public Double getDailyDiscountPromotion() {
        return dailyDiscountPromotion;
    }

    /**
     * Returns the total amount of discounts applied for the day.
     *
     * @return the daily discount amount
     */
    public Double getDailyDiscount() {
        return dailyDiscount;
    }

    /**
     * Returns the total quantity of discounts applied for the day.
     *
     * @return the daily discount quantity
     */
    public Double getDailyDiscountQuantity() {
        return dailyDiscountQuantity;
    }

    /**
     * Returns the total maintenance costs for the day.
     *
     * @return the daily maintenance price
     */
    public Double getDailyMaintenancePrice() {
        return dailyMaintenancePrice;
    }

    /**
     * Returns the total repair costs for the day.
     *
     * @return the daily repair price
     */
    public Double getDailyRepairPrice() {
        return dailyRepairPrice;
    }
}
