package reports;

import java.io.Serializable;

import vehicles.Vehicle;

/**
 * Represents a vehicle and its earnings.
 */
public class VehicleEarned implements Serializable {
	/*** Serialization version identifier */
    private static final long serialVersionUID = 1L;
    /***
     * The vehicle associated with the earnings
     */
    private Vehicle vehicle;     
    /***Total earnings for the vehicle*/
    private Double moneyEarned;    
    
    /**
     * Constructs a VehicleEarned object for a given vehicle.
     *
     * @param veh the vehicle for which earnings are being tracked
     */
    public VehicleEarned(Vehicle veh) {
        this.vehicle = veh;
        this.moneyEarned = 0.0; // Initialize earnings to zero
    }
    
    /**
     * Adds the specified amount to the vehicle's earnings.
     *
     * @param val the amount to be added to the earnings
     */
    void earned(Double val) {
        this.moneyEarned += val;
    }
    
    /**
     * Retrieves the associated vehicle.
     *
     * @return the vehicle
     */
    public Vehicle getVehicle() {
        return vehicle;
    }
    
    /**
     * Retrieves the total money earned by the vehicle.
     *
     * @return the total earnings
     */
    public Double getMoneyEarned() {
        return this.moneyEarned;
    }
}
