package rental;

import java.io.PrintWriter;
import java.time.LocalDateTime;

import location.Location;
import location.OutOfRadiusException;
import location.PathFinder;
import simulationPJ2.*;
import vehicles.*;

/**
 * Represents a rental transaction involving a user and a vehicle.
 */
public class Rent {
    private User user;
    private LocalDateTime dateTime;
    private Location locationStart;
    private Location locationEnd;
    private double timeUsed;
    private Vehicle vehicle;
    private Boolean promotion;
    private PriceCalculator price;
    private Boolean hadMalfunction;
    private Boolean isInnerCity;

    /**
     * Constructs a Rent object with the specified details.
     *
     * @param user         the user renting the vehicle
     * @param dateTime     the date and time of the rental
     * @param locationStart the starting location of the rental
     * @param locationEnd   the ending location of the rental
     * @param timeUsed      the duration of the rental in seconds
     * @param vehicle       the vehicle being rented
     * @param promotion     whether a promotion is applied
     * @param hadMalfunction whether the vehicle had a malfunction during the rental
     */
    public Rent(User user, LocalDateTime dateTime, Location locationStart, Location locationEnd, 
                int timeUsed, Vehicle vehicle, Boolean promotion, Boolean hadMalfunction) {

        if (vehicle instanceof Car) {
            if ((user.getIsLocal() != null) && (user.getUserID() != null) && (user.getDriversLicense() != null)) {
                // User already has necessary information
            } else {
                SimulateUser sim = new SimulateUser();
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
        this.hadMalfunction = hadMalfunction;
        this.isInnerCity = true;

        if (hadMalfunction) {
            this.vehicle.reportMalfunction(dateTime);
        }
        calculatePrice();
        vehicle.sendVehicleToRepair();
    }

    /**
     * Calculates the price of the rental based on various factors.
     */
    public void calculatePrice() {
        PathFinder path = new PathFinder(locationStart, locationEnd);
        try {
            path.getPathDijkstra();
        } catch (OutOfRadiusException e) {
            e.printStackTrace();
        }
        if (path.isWide()) {
            this.isInnerCity = false; 
        }
        PriceCalculator cal = new PriceCalculator(vehicle, timeUsed, path.isWide(), 
                                                   user.getTimesRented() % 10 == 0, promotion);
        if (this.hadMalfunction) {
            cal.applyMalfunction();
        }
        this.price = cal;
        price.calculatePrice();
    }

    /**
     * Generates an invoice for the rental and saves it to a file.
     *
     * @param fileName the name of the file to save the invoice
     */
    public void generateInvoice(String fileName) {
        String vType;
        if (vehicle instanceof Car) {
            vType = "Car";
        } else if (vehicle instanceof Scooter) {
            vType = "Scooter";
        } else if (vehicle instanceof Bicycle) {
            vType = "Bicycle";
        } else {
            vType = "Unknown Vehicle Type";
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

        // Save to file
        try (PrintWriter writer = new PrintWriter(fileName + ".txt")) {
            writer.println(invoice);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the full price of the rental.
     *
     * @return the full price
     */
    public double getFullPrice() {
        return price.getFullPrice();
    }

    /**
     * Returns the total price of the rental without discounts.
     *
     * @return the total price
     */
    public double getTotal() {
        return price.getPrice();
    }

    /**
     * Returns the amount discounted from promotions.
     *
     * @return the promotional discount amount
     */
    public double getPromo() {
        return price.getDiscountedAmmountFromPromotion();
    }

    /**
     * Returns the discount amount based on the number of rentals.
     *
     * @return the discount amount based on quantity
     */
    public double getDiscount() {
        return price.getDiscountedAmmountFromNum();
    }

    /**
     * Returns the total discounted price.
     *
     * @return the total discounted price
     */
    public double getTotalDiscount() {
        return price.getPriceDiscounted();
    }

    /**
     * Returns the user associated with the rental.
     *
     * @return the user
     */
    public User getUser() {
        return user;
    }

    /**
     * Returns whether the rental is within the inner city.
     *
     * @return true if the rental is inner city, false otherwise
     */
    public Boolean getIsInnerCity() {
        return isInnerCity;
    }

    /**
     * Returns the date and time when the rental was made.
     *
     * @return the date and time of the rental
     */
    public LocalDateTime getDateTime() {
        return dateTime;
    }

    /**
     * Returns the ending location of the rental.
     *
     * @return the ending location
     */
    public Location getLocationEnd() {
        return locationEnd;
    }

    /**
     * Returns the starting location of the rental.
     *
     * @return the starting location
     */
    public Location getLocationStart() {
        return locationStart;
    }

    /**
     * Returns whether the vehicle had a malfunction during the rental.
     *
     * @return true if there was a malfunction, false otherwise
     */
    public Boolean getHadMalfunction() {
        return hadMalfunction;
    }

    /**
     * Returns the total time used for the rental.
     *
     * @return the time used in seconds
     */
    public double getTimeUsed() {
        return timeUsed;
    }

    /**
     * Returns the vehicle associated with the rental.
     *
     * @return the vehicle
     */
    public Vehicle getVehicle() {
        return this.vehicle;
    }

    /**
     * Sets the vehicle for the rental.
     *
     * @param vehicle the vehicle to set
     */
    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }
}