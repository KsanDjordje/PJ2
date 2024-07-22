package main;

import java.util.List;

import SimulationPJ2.FileRentedListReader;
import Vehicles.FileVehicleReader;

public class FileLoadData {
	private FileRentedListReader rented;
	private FileVehicleReader vehicleList;
	
	public FileLoadData(String vehicleListLocation, String rentedListLocation){
		FileRentedListReader rented = new FileRentedListReader(rentedListLocation);
		FileVehicleReader vehicleList = new FileVehicleReader(vehicleListLocation);
		rented.setSortedList(rented.removeIncorrectData(vehicleList.getAllVehicles()));
		rented.setCars(rented.removeIncorrectData(vehicleList.getCars()));
		rented.setBikes(rented.removeIncorrectData(vehicleList.getBikes()));
		rented.setScooters(rented.removeIncorrectData(vehicleList.getScooters()));
		this.rented = rented;
		this.vehicleList = vehicleList;
		
		
		
	}
	
	
	public List<List<String>> getSortedList(){
    	return rented.getSortedList();
    }
	public List<List<String>> getRentedCars() {
		return rented.getCars();
	}

	public List<List<String>> getRentedBikes() {
		return rented.getBikes();
	}

	public List<List<String>> getRentedScooters() {
		return rented.getScooters();
	}
	
	public List<List<String>> getCars() {
		return vehicleList.getCars();
	}

	public List<List<String>> getBikes() {
		return vehicleList.getBikes();
	}

	public List<List<String>> getScooters() {
		return vehicleList.getScooters();
	}

	
}
