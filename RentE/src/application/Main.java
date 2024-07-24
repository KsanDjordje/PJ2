package application;
	
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.List;

import SimulationPJ2.FileRentedListReader;
import Vehicles.Bicycle;
import Vehicles.Car;
import Vehicles.FileVehicleReader;
import Vehicles.Scooter;
import Vehicles.Vehicle;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import main.FileLoadData;
import main.Location;
import main.MyDateParser;
import main.OutOfRadiusException;
import main.PathFinder;
import main.PriceCalculator;
import main.Rent;
import main.User;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;



public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			
			FXMLLoader loader = new FXMLLoader(getClass().getResource("Map.fxml"));
			
			FileLoadData fileData = new FileLoadData("ovo.csv","rented.csv");
			
			
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
            Parent root = loader.load();
            MapController mapController = loader.getController();
            
            
            
            
            mapController.loadData(fileData, vehicleList, rentedList);
			//Parent root = FXMLLoader.load(getClass().getResource("Map.fxml"));
            
            
            
            
            
            
            
            
            
            
            
            
			User user = new User("NIGG");
			LocalDateTime start = LocalDateTime.of(2024, Month.JULY, 4, 0, 0);
			
			Location loc = new Location(2,5);
			Location locc = new Location(10,10);
			
			LocalDate purchaseDate = LocalDate.of(2024, Month.JULY, 4);

			Car auto = new Car("a", "a", "a", 50, 2, purchaseDate, "opis");
			Rent rent = new Rent(user,start,loc,locc,1,auto,true);
			
			
			
			// path test
			PathFinder path = new PathFinder(rent.getLocationStart(), rent.getLocationEnd());
			Location[] putanja = path.getPathDijkstra();
			
			
			Rent rent2 = new Rent(user,start,new Location (0,0),new  Location (19,19),1,auto,true);
			
			PathFinder path2 = new PathFinder(rent2.getLocationStart(), rent2.getLocationEnd());
			Location[] putanja2 = path2.getPathDijkstra();
			
			Rent rent3 = new Rent(user,start,new Location (16,15),new  Location (10,2),1,auto,true);
			
			PathFinder path3 = new PathFinder(rent3.getLocationStart(), rent3.getLocationEnd());
			Location[] putanja3 = path3.getPathDijkstra();
			
			Rent rent4 = new Rent(user,start,new Location (3,7),new  Location (0,19),1,auto,true);
			
			PathFinder path4 = new PathFinder(rent4.getLocationStart(), rent4.getLocationEnd());
			Location[] putanja4 = path4.getPathDijkstra();
			
			
			
			// path test
			for(int i = 0; i < putanja.length;i++) {
				System.out.println(putanja[i]);
			}
			
			rent.generateInvoice("invoice");
			
			
			PriceCalculator p = new PriceCalculator((Vehicle)auto, 10.0, path.isWide(), rent.getUser().getTimesRented() % 10 == 0, true);
			//auto.reportMalfunction(start);
			// price test
			System.out.println(p.calculatePrice());
			System.out.println(path.isWide());
			System.out.println(p.getDiscountedAmmountFromNum());
			System.out.println(p.getDiscountedAmmountFromPromotion());
			
			System.out.println(p.getPriceDiscounted());
			System.out.println(p.getFullPrice());
			System.out.println(p.getPrice());
			System.out.println(auto.getMalfunctionDescription());
			
			// temp movement test
			mapController.initializeExecutorService(10);
			mapController.simulateMovement(putanja);
			mapController.simulateMovement(putanja2);
			mapController.simulateMovement(putanja3);
			mapController.simulateMovement(putanja4);
				
				
				
			
			
			
			
			System.out.println("Total Profit: " + totalProfit);
			System.out.println("Total Promotion: " + totalPromotion);
			System.out.println("Total Number Discount: " + totalNumDiscount);
			System.out.println("Total Discount: " + totalDiscount);
			
			
			Scene scene = new Scene(root, 1280 , 720);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
			
			primaryStage.setOnCloseRequest(event -> {
				event.consume();
				logout(primaryStage);
			});
			
		
		}catch(OutOfRadiusException e) {
			System.out.println("Invalid location");
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
public void logout(Stage stage) {
		
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Logout");
		alert.setHeaderText(null);
		alert.setContentText("Are you sure you want to logout?");
		
		if(alert.showAndWait().get() == ButtonType.OK) {
			System.out.println("logout");
			stage.close();
		}
		
		
	}
	public static void main(String[] args) {
		launch(args);
	}
}
