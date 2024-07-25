package application;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import Vehicles.Bicycle;
import Vehicles.Car;
import Vehicles.Scooter;
import Vehicles.Vehicle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import main.FileLoadData;
import main.Location;
import main.MyDateParser;
import main.OutOfRadiusException;
import main.Rent;
import main.User;

public class Controller {

	@FXML
	private Button logoutButton;
	@FXML
	private AnchorPane scenePane;
	
	private Stage stage;
	private Scene scene;
	private Parent root;
	
	
	
	public void login(ActionEvent event) throws IOException, OutOfRadiusException {
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("Map.fxml"));
		FileLoadData fileData = new FileLoadData("ovo.csv","rented.csv");
		root = loader.load();
		
		MapController mapController = loader.getController();
		
		MyDateParser dp = new MyDateParser();

		
		Vehicle[] vehicleList = new Vehicle[fileData.getAllVehicles().size()];
		for(int i = 0; i < fileData.getAllVehicles().size(); i++) {
			List<String> vehicleData = fileData.getAllVehicles().get(i);
				
			String id = vehicleData.get(0);
			String manu = vehicleData.get(1);
			String model = vehicleData.get(2);
			

			
			double price = Double.parseDouble(vehicleData.get(4));
			String desc = vehicleData.get(7);
			String vehType = vehicleData.get(8);
			if(vehType.equals("automobil")) {
				LocalDate date = dp.parseLocalDate(vehicleData.get(3));
				vehicleList[i] = new Car(id, manu, model, price, 100, date, desc);
			}else if(vehType.equals("bicikl")) {
				double range = Double.parseDouble(vehicleData.get(5));
				vehicleList[i] = new Bicycle(id, manu, model, price, 100, range, desc);
			}else if(vehType.equals("trotinet")) {
				float speed = Float.parseFloat(vehicleData.get(6));
				vehicleList[i] = new Scooter(id, manu, model, price, 100, speed, desc);
			}else {
				// ne znam :/
			}
		}
		
		Rent[] rentedList = new Rent[fileData.getSortedList().size()];
		for(int i = 0; i < fileData.getSortedList().size(); i++) {
			List<String> data = fileData.getSortedList().get(i);
			LocalDateTime dateTime = dp.parseLocalDateTime(data.get(0));

			
			User user = new User(data.get(1));
			String vehicleId = data.get(2);
			Location locationStart = new Location(data.get(3),data.get(4));
			Location locationEnd = new Location(data.get(5),data.get(6));
			int timeUsed = Integer.parseInt(data.get(7));
			Boolean malf = false;
			if(data.get(8).equals("da")) {
				malf = true;
			}
			Boolean promotion = false;
			if(data.get(9).equals("da")) {
				promotion = true;
			}
			Vehicle vehicle = null;
			for(Vehicle v : vehicleList) {
				if(v != null && v.getId().equals(vehicleId)) {
					vehicle = v;
					break;
				}
			}
			if(vehicle != null) {
				if(malf == true) {
					vehicle.reportMalfunction(dateTime);
				}
					
				rentedList[i] = new Rent(user, dateTime, locationStart, locationEnd, timeUsed, vehicle, promotion);
			}
			
		}
		double totalProfit = 0;
		double totalPromotion = 0;
		double totalNumDiscount = 0;
		double totalDiscount = 0;
		
		
		
		{
			int i = 0;
			for(Rent rent : rentedList) {
				
				rent.generateInvoice(String.valueOf(i++));
			}
		}
		
		for(Rent rent : rentedList){
			rent.calculatePrice();
			totalProfit += rent.getTotal();
			totalPromotion += rent.getPromo();
			totalNumDiscount += rent.getDiscount();
			totalDiscount += rent.getTotalDiscount();
		}
		

		
		
		
		
		
		for(List<String> temp : fileData.getSortedList()) {
			System.out.println(temp);
		}
		mapController = loader.getController();
        
        
        
        
        mapController.loadData(fileData, vehicleList, rentedList);
        System.out.println("Total Profit: " + totalProfit);
		System.out.println("Total Promotion: " + totalPromotion);
		System.out.println("Total Number Discount: " + totalNumDiscount);
		System.out.println("Total Discount: " + totalDiscount);
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root, 1280, 720);
		stage.setScene(scene);
		stage.show();
	}
	public void closeProgram(ActionEvent event) {
		
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Logout");
		alert.setHeaderText(null);
		alert.setContentText("Are you sure you want to logout?");
		if(alert.showAndWait().get() == ButtonType.OK) {
			stage = (Stage) scenePane.getScene().getWindow();
			System.out.println("logout");
			stage.close();
		}
		
		
	}
}
