package main;

public class User {
	private String name;
	private int timesRented;
	private Boolean isLocal;
	private String userID;
	private Integer driversLicense;

	public  User(String name) {
		this.name = name;
		this.setIsLocal(null);
		this.setUserID(null);
		this.setDriversLicense(null);
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
	
	/**
	 * Helper functions to 
	 * 
	 */
	
}
