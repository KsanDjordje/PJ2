package reports;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import vehicles.Bicycle;
import vehicles.Car;
import vehicles.Scooter;
import vehicles.Vehicle;

/**
 * This class represents a business report that tracks various financial metrics such as 
 * total earnings, discounts, maintenance, repairs, expenses, and taxes.
 * It also tracks the number of inner-city and outer-city rentals.
 */
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
    private List<DailyEarnings> dailyDiscount;
    private List<DailyEarnings> dailyDiscountQuantity;
    private List<DailyEarnings> dailyMaintenancePrice;
    private List<DailyEarnings> dailyRepairPrice;

    /**
     * Constructs an empty BusinessResultsReport with all metrics initialized to default values.
     */
    public BusinessResultsReport() {
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

    /**
     * Adds a daily report entry for a specific date, recording rental price, discounts, promotions,
     * and other financial aspects, based on vehicle type and rental details.
     *
     * @param date        The date of the rental.
     * @param price       The rental price.
     * @param discount    The discount applied.
     * @param promo       The promotion discount applied.
     * @param qDiscount   The quantity discount applied.
     * @param innerCity   Whether the rental took place in the inner city.
     * @param malfunction Whether the vehicle experienced a malfunction.
     * @param vehicle     The rented vehicle.
     */
    public void dailyReport(LocalDate date, Double price, Double discount, Double promo, Double qDiscount, 
                            Boolean innerCity, Boolean malfunction, Vehicle vehicle) {
        synchronized(this) {
            if (!malfunction) {
                addEarningsToList(dailyEarnings, date, price);
                addEarningsToList(dailyDiscountPromotion, date, promo);
                addEarningsToList(dailyDiscount, date, discount);
                addEarningsToList(dailyDiscountQuantity, date, qDiscount);
            }

            if (innerCity) {
                this.cityInnerRentals++;
            } else {
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
            }
            addEarningsToList(dailyRepairPrice, date, repairCost);

            totalEarnings += price;
            totalDiscount += discount;
            totalDiscountPromotion += promo;
            totalDiscountQuantity += qDiscount;
            totalMaintenancePrice += maintenanceCost;
            totalRepairPrice += repairCost;
            totalExpenses = totalEarnings * 0.2; // 20% of total earnings
            totalTax = (totalEarnings - totalMaintenancePrice - totalRepairPrice - totalExpenses) * 0.10; // 10% tax
        }
    }

    /**
     * Helper method to add earnings to a specific list for a given date.
     * If there is no entry for the date, a new entry is created.
     *
     * @param earningsList The list to which the earnings should be added.
     * @param date         The date of the earnings.
     * @param price        The earnings amount.
     */
    private void addEarningsToList(List<DailyEarnings> earningsList, LocalDate date, double price) {
        DailyEarnings entry = findEarningsByDate(earningsList, date);
        if (entry == null) {
            entry = new DailyEarnings(date);
            earningsList.add(entry);
        }
        entry.addEarnings(price);
    }

    /**
     * Finds an earnings entry by date from the provided list.
     *
     * @param earningsList The list of earnings.
     * @param date         The date to search for.
     * @return The found DailyEarnings object, or null if none is found.
     */
    private DailyEarnings findEarningsByDate(List<DailyEarnings> earningsList, LocalDate date) {
        for (DailyEarnings dailyEarning : earningsList) {
            if (dailyEarning.getDate().equals(date)) {
                return dailyEarning;
            }
        }
        return null;
    }

    /**
     * Provides a string representation of the business results report.
     * 
     * @return A formatted string summarizing the financial metrics and daily entries.
     */
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

        // Append daily earnings and other details
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

    /**
     * Retrieves the total earnings.
     *
     * @return the total earnings as a Double.
     */
    public Double getTotalEarnings() {
        return totalEarnings;
    }

    /**
     * Retrieves the total discount amount.
     *
     * @return the total discount as a Double.
     */
    public Double getTotalDiscount() {
        return totalDiscount;
    }

    /**
     * Retrieves the total discount amount from promotions.
     *
     * @return the total discount from promotions as a Double.
     */
    public Double getTotalDiscountPromotion() {
        return totalDiscountPromotion;
    }

    /**
     * Retrieves the total discount amount based on quantity.
     *
     * @return the total discount quantity as a Double.
     */
    public Double getTotalDiscountQuantity() {
        return totalDiscountQuantity;
    }

    /**
     * Retrieves the total maintenance price.
     *
     * @return the total maintenance price as a Double.
     */
    public Double getTotalMaintenancePrice() {
        return totalMaintenancePrice;
    }

    /**
     * Retrieves the total repair price.
     *
     * @return the total repair price as a Double.
     */
    public Double getTotalRepairPrice() {
        return totalRepairPrice;
    }

    /**
     * Retrieves the total expenses incurred.
     *
     * @return the total expenses as a Double.
     */
    public Double getTotalExpenses() {
        return totalExpenses;
    }

    /**
     * Retrieves the total tax calculated.
     *
     * @return the total tax as a Double.
     */
    public Double getTotalTax() {
        return totalTax;
    }

    /**
     * Retrieves a list of daily earnings.
     *
     * @return a List of DailyEarnings objects.
     */
    public List<DailyEarnings> getDailyEarnings() {
        return dailyEarnings;
    }

    /**
     * Retrieves a list of daily discount promotions.
     *
     * @return a List of DailyEarnings objects representing discounts from promotions.
     */
    public List<DailyEarnings> getDailyDiscountPromotion() {
        return dailyDiscountPromotion;
    }

    /**
     * Retrieves a list of daily discounts.
     *
     * @return a List of DailyEarnings objects representing daily discounts.
     */
    public List<DailyEarnings> getDailyDiscount() {
        return dailyDiscount;
    }

    /**
     * Retrieves a list of daily discounts based on quantity.
     *
     * @return a List of DailyEarnings objects representing daily discounts based on quantity.
     */
    public List<DailyEarnings> getDailyDiscountQuantity() {
        return dailyDiscountQuantity;
    }

    /**
     * Retrieves the count of inner city rentals.
     *
     * @return the number of inner city rentals as an Integer.
     */
    public Integer getCityInnerRentals() {
        return cityInnerRentals;
    }

    /**
     * Retrieves the count of outer city rentals.
     *
     * @return the number of outer city rentals as an Integer.
     */
    public Integer getCityOuterRentals() {
        return cityOuterRentals;
    }

    /**
     * Retrieves a list of daily maintenance prices.
     *
     * @return a List of DailyEarnings objects representing daily maintenance prices.
     */
    public List<DailyEarnings> getDailyMaintenancePrice() {
        return dailyMaintenancePrice;
    }

    /**
     * Retrieves a list of daily repair prices.
     *
     * @return a List of DailyEarnings objects representing daily repair prices.
     */
    public List<DailyEarnings> getDailyRepairPrice() {
        return dailyRepairPrice;
    }
}