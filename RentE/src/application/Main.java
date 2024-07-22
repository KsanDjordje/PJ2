package application;
	
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import SimulationPJ2.FileRentedListReader;
import Vehicles.Car;
import Vehicles.FileVehicleReader;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import main.FileLoadData;
import main.Location;
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
			
			FileLoadData data = new FileLoadData("ovo.csv","rented.csv");
			for(List<String> temp : data.getSortedList()) {
				System.out.println(temp);
			}
            Parent root = loader.load();
            MapController mapController = loader.getController();
            mapController.loadData(data);
			//Parent root = FXMLLoader.load(getClass().getResource("Map.fxml"));
			User user = new User("NIGG");
			LocalDateTime start = LocalDateTime.of(2024, Month.JULY, 4, 0, 0);
			
			Location loc = new Location(2,5);
			Location locc = new Location(10,10);
			
			LocalDate purchaseDate = LocalDate.of(2024, Month.JULY, 4);

			Car auto = new Car("a", "a", "a", 50, 2, purchaseDate);
			Rent rent = new Rent(user,start,loc,locc,1,auto);
			
			PathFinder path = new PathFinder(rent.getLocationStart(), rent.getLocationEnd());
			Location[] putanja = path.getPathDijkstra();
			
			
			Rent rent2 = new Rent(user,start,new Location (0,0),new  Location (19,19),1,auto);
			
			PathFinder path2 = new PathFinder(rent2.getLocationStart(), rent2.getLocationEnd());
			Location[] putanja2 = path2.getPathDijkstra();
			
			Rent rent3 = new Rent(user,start,new Location (16,15),new  Location (10,2),1,auto);
			
			PathFinder path3 = new PathFinder(rent3.getLocationStart(), rent3.getLocationEnd());
			Location[] putanja3 = path3.getPathDijkstra();
			
			Rent rent4 = new Rent(user,start,new Location (3,7),new  Location (0,19),1,auto);
			
			PathFinder path4 = new PathFinder(rent4.getLocationStart(), rent4.getLocationEnd());
			Location[] putanja4 = path4.getPathDijkstra();
			
			
			for(int i = 0; i < putanja.length;i++) {
				System.out.println(putanja[i]);
			}
			rent.generateInvoice();
			PriceCalculator p = new PriceCalculator(rent, path.isWide(), rent.getUser().getTimesRented() % 10 == 0, true);
			System.out.println(p.calculatePrice());

			System.out.println(path.isWide());
			
			
			mapController.initializeExecutorService(10);
			mapController.simulateMovement(putanja);
			mapController.simulateMovement(putanja2);
			mapController.simulateMovement(putanja3);
			mapController.simulateMovement(putanja4);
				
				
				
			
			
			
			
			
			
			
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
