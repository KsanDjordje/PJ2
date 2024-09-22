package rental;

import simulationPJ2.SimulateUser;

/**
 * Represents a user of the rental service.
 */
public class User {
    private String name;
    private int timesRented;
    private Boolean isLocal;
    private String userID;
    private Integer driversLicense;

    /**
     * Constructs a User with a specified name, generating user attributes automatically.
     *
     * @param name the name of the user
     */
    public User(String name) {
        SimulateUser sim = new SimulateUser();
        this.name = name;
        this.setIsLocal(sim.generateIsLocal());
        this.setUserID(sim.generateUserID(isLocal));
        this.setDriversLicense(sim.generateDriversLicense());
    }

    /**
     * Constructs a User with specified attributes.
     *
     * @param name           the name of the user
     * @param isLocal        indicates if the user is a local
     * @param userID        the user ID
     * @param driversLicense the driver's license number
     */
    public User(String name, Boolean isLocal, String userID, int driversLicense) {
        this.name = name;
        this.setIsLocal(isLocal);
        this.setUserID(userID);
        this.setDriversLicense(driversLicense);
    }

    /**
     * Gets the name of the user.
     *
     * @return the user's name
     */
    public String getName() {
        return name;
    }

    /**
     * Checks if the user is local.
     *
     * @return true if the user is local; false otherwise
     */
    public Boolean getIsLocal() {
        return isLocal;
    }

    /**
     * Sets whether the user is local.
     *
     * @param isLocal true if the user is local; false otherwise
     */
    public void setIsLocal(Boolean isLocal) {
        this.isLocal = isLocal;
    }

    /**
     * Gets the user ID.
     *
     * @return the user's ID
     */
    public String getUserID() {
        return userID;
    }

    /**
     * Sets the user ID.
     *
     * @param userID the user's ID
     */
    public void setUserID(String userID) {
        this.userID = userID;
    }

    /**
     * Gets the driver's license number.
     *
     * @return the driver's license number
     */
    public Integer getDriversLicense() {
        return driversLicense;
    }

    /**
     * Sets the driver's license number.
     *
     * @param driversLicense the driver's license number
     */
    public void setDriversLicense(Integer driversLicense) {
        this.driversLicense = driversLicense;
    }

    /**
     * Gets the number of times the user has rented a vehicle.
     *
     * @return the number of rentals
     */
    public int getTimesRented() {
        return timesRented;
    }

    /**
     * Increments the number of times the user has rented a vehicle.
     */
    public void updateTimesRented() {
        this.timesRented++;
    }
}
