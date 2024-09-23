package rental;

import java.io.IOException;
import java.util.Properties;
import vehicles.*;
import java.io.File;
import java.io.FileInputStream;

/**
 * Calculates the rental price for a vehicle based on distance, time, and applicable discounts.
 */
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
    private double fullPrice;
    private double priceTotal;
    private double discountTotal;
    private double discountPromTotal;
    private double priceDiscounted;

    /**
     * Constructs a PriceCalculator with specified vehicle and pricing conditions.
     *
     * @param vehicle       the vehicle for which the price is being calculated
     * @param timeUsed      the duration of the rental in seconds
     * @param isWide        whether the rental is in a wide area
     * @param applyDiscount  whether discounts should be applied
     * @param applyPromotion whether promotions should be applied
     */
    public PriceCalculator(Vehicle vehicle, double timeUsed, Boolean isWide, 
                           Boolean applyDiscount, Boolean applyPromotion) {
        Properties prop = new Properties();
        String path = new File("src/rental.properties").getAbsolutePath();

        try (FileInputStream input = new FileInputStream(path)) {
            prop.load(input);
            this.fullPrice = 0;
            this.priceTotal = 0;
            this.discountTotal = 0;
            this.discountPromTotal = 0;
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Resets the price to zero if there was a malfunction.
     */
    public void applyMalfunction() {
        this.fullPrice = 0.0;
        this.priceTotal = 0.0;
        this.discountTotal = 0.0;
        this.discountPromTotal = 0.0;
        this.priceDiscounted = 0.0;
    }

    /**
     * Calculates the total price based on the vehicle type and distance.
     *
     * @return the total calculated price
     */
    public double calculatePrice() {
        double distance = isWide ? this.timeTraveled * this.distanceWide : 
                                   this.timeTraveled * this.distanceNarrow;

        if (vehicle instanceof Car) {
            this.fullPrice = distance * this.carUnitPrice;
        } else if (vehicle instanceof Bicycle) {
            this.fullPrice = distance * this.bikeUnitPrice;
        } else {
            this.fullPrice = distance * this.scooterUnitPrice;
        }

        priceApplyMalfunction(this.vehicle.getHasMalfunction());
        this.priceTotal = applyDiscount();
        return this.priceTotal;
    }

    /**
     * Applies discounts to the calculated price if applicable.
     *
     * @return the total price after applying discounts
     */
    public double applyDiscount() {
        if (this.applyDiscount) {
            this.discountTotal = this.fullPrice * this.discount;
        }
        if (this.applyPromotion) {
            this.discountPromTotal = this.fullPrice * this.discountPromotion;
        }
        this.priceDiscounted = this.discountTotal + this.discountPromTotal;
        
        return (fullPrice - this.priceDiscounted);
    }

    /**
     * Sets the price to zero if the vehicle has a malfunction.
     *
     * @param malfunction indicates whether there was a malfunction
     */
    public void priceApplyMalfunction(Boolean malfunction) {
        if (malfunction) {
            this.fullPrice = 0;
        }
    }

    /**
     * Gets the total amount discounted from promotions.
     *
     * @return the promotional discount amount
     */
    public double getDiscountedAmmountFromPromotion() {
        return discountPromTotal;
    }

    /**
     * Gets the total amount discounted based on quantity.
     *
     * @return the quantity discount amount
     */
    public double getDiscountedAmmountFromNum() {
        return discountTotal;
    }

    /**
     * Gets the total amount discounted from the price.
     *
     * @return the discounted price
     */
    public double getPriceDiscounted() {
        return priceDiscounted;
    }

    /**
     * Gets the full price before discounts.
     *
     * @return the full price
     */
    public double getFullPrice() {
        return this.fullPrice;
    }

    /**
     * Gets the total price after discounts have been applied.
     *
     * @return the total price
     */
    public double getPrice() {
        return this.priceTotal;
    }
}
