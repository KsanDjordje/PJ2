package main;

import SimulationPJ2.SimulateUser;

public class User {
	private String name;
	private int timesRented;
	private Boolean isLocal;
	private String userID;
	private Integer driversLicense;

	public  User(String name) {
		SimulateUser sim = new SimulateUser();
		this.name = name;
		this.setIsLocal(sim.generateIsLocal());
		this.setUserID(sim.generateUserID(isLocal));
		this.setDriversLicense(sim.generateDriversLicense());
	}
	public  User(String name, Boolean isLocal, String userID, int driversLicense) {
		this.name = name;
		this.setIsLocal(isLocal);
		this.setUserID(userID);
		this.setDriversLicense(driversLicense);
	}
	
	public String getName() {
		return name;
	}

	public Boolean getIsLocal() {
		return isLocal;
	}

	public void setIsLocal(Boolean isLocal) {
		this.isLocal = isLocal;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public Integer getDriversLicense() {
		return driversLicense;
	}

	public void setDriversLicense(Integer driversLicense) {
		this.driversLicense = driversLicense;
	}
	public int getTimesRented() {
		return timesRented;
	}
	public void updateTimesRented() {
		this.timesRented++;
	}
	
	
	
}
