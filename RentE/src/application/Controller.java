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
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
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
	AnchorPane pane;
	@FXML
	private Button loginButton;
	@FXML
	private Button logoutButton;
	@FXML
	private AnchorPane scenePane;
	@FXML
	private TextField usernameField;
	@FXML
	private PasswordField passwordField;
	private MapController mapController;

	private Stage stage;
	private Scene scene;
	private Parent root;
	
	
	
	//temp
	private String secretUsername = "admin";
	private String secretPassword = "admin";
	@FXML
	public void login(ActionEvent event) throws IOException, OutOfRadiusException{
	    String username = usernameField.getText();
	    String password = passwordField.getText();
	    
	    if (isLoginValid(username, password)) {
	    	
	        initializeMap(event);

	        MyDateParser dp = new MyDateParser();
	        
	        FileLoadData fileData = new FileLoadData("ovo.csv", "rented.csv");

	        
	        
	        
	        Vehicle[] vehicleList = loadVehicles(fileData, dp);
	        Rent[] rentedList = loadRents(fileData, dp, vehicleList);
	        
	        generateInvoices(rentedList);
	        calculateTotals(rentedList);
	        
	        
	        mapController.loadData(fileData, vehicleList, rentedList);
	        
	        mapController.initializeExecutorService(50);
	    } else {
	        System.out.println("Incorrect Login");
	    }
	    
	}

	private boolean isLoginValid(String username, String password) {
	    return username.equals(secretUsername) && password.equals(secretPassword);
	}

	private void initializeMap(ActionEvent event) throws IOException {
	    FXMLLoader loader = new FXMLLoader(getClass().getResource("Map.fxml"));
	    root = loader.load();

	    mapController = loader.getController();
	    stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
	    stage.setX(0);
	    stage.setY(0);
	    scene = new Scene(root, 1280, 720);
	    stage.setScene(scene);
	    stage.show();
	}

	private Vehicle[] loadVehicles(FileLoadData fileData, MyDateParser dp) {
	    Vehicle[] vehicleList = new Vehicle[fileData.getAllVehicles().size()];
	    for (int i = 0; i < fileData.getAllVehicles().size(); i++) {
	        List<String> vehicleData = fileData.getAllVehicles().get(i);
	        vehicleList[i] = createVehicle(vehicleData, dp);
	    }
	    return vehicleList;
	}

	private Vehicle createVehicle(List<String> vehicleData, MyDateParser dp) {
	    String id = vehicleData.get(0);
	    String manu = vehicleData.get(1);
	    String model = vehicleData.get(2);
	    double price = Double.parseDouble(vehicleData.get(4));
	    String desc = vehicleData.get(7);
	    String vehType = vehicleData.get(8);

	    switch (vehType) {
	        case "automobil":
	            LocalDate date = dp.parseLocalDate(vehicleData.get(3));
	            return new Car(id, manu, model, price, 100, date, desc);
	        case "bicikl":
	            double range = Double.parseDouble(vehicleData.get(5));
	            return new Bicycle(id, manu, model, price, 100, range, desc);
	        case "trotinet":
	            float speed = Float.parseFloat(vehicleData.get(6));
	            return new Scooter(id, manu, model, price, 100, speed, desc);
	        default:
	            return null;
	    }
	}

	private Rent[] loadRents(FileLoadData fileData, MyDateParser dp, Vehicle[] vehicleList) throws OutOfRadiusException {
	    Rent[] rentedList = new Rent[fileData.getSortedList().size()];
	    for (int i = 0; i < fileData.getSortedList().size(); i++) {
	        List<String> data = fileData.getSortedList().get(i);
	        rentedList[i] = createRent(data, dp, vehicleList);
	    }
	    return rentedList;
	}

	private Rent createRent(List<String> data, MyDateParser dp, Vehicle[] vehicleList) throws OutOfRadiusException {
	    LocalDateTime dateTime = dp.parseLocalDateTime(data.get(0));
	    User user = new User(data.get(1));
	    String vehicleId = data.get(2);
	    Location locationStart = new Location(data.get(3), data.get(4));
	    Location locationEnd = new Location(data.get(5), data.get(6));
	    int timeUsed = Integer.parseInt(data.get(7));
	    boolean malf = data.get(8).equals("da");
	    boolean promotion = data.get(9).equals("da");

	    Vehicle vehicle = findVehicleById(vehicleId, vehicleList);
	    if (vehicle != null) {
	        if (malf) {
	            vehicle.reportMalfunction(dateTime);
	        }
	        return new Rent(user, dateTime, locationStart, locationEnd, timeUsed, vehicle, promotion);
	    }
	    return null;
	}

	private Vehicle findVehicleById(String vehicleId, Vehicle[] vehicleList) {
	    for (Vehicle v : vehicleList) {
	        if (v != null && v.getId().equals(vehicleId)) {
	            return v;
	        }
	    }
	    return null;
	}

	private void generateInvoices(Rent[] rentedList) {
	    int i = 0;
	    for (Rent rent : rentedList) {
	        rent.generateInvoice(String.valueOf(i++));
	    }
	}

	private void calculateTotals(Rent[] rentedList) {
	    double totalProfit = 0;
	    double totalPromotion = 0;
	    double totalNumDiscount = 0;
	    double totalDiscount = 0;

	    for (Rent rent : rentedList) {
	        rent.calculatePrice();
	        totalProfit += rent.getTotal();
	        totalPromotion += rent.getPromo();
	        totalNumDiscount += rent.getDiscount();
	        totalDiscount += rent.getTotalDiscount();
	    }

	    System.out.println("Total Profit: " + totalProfit);
	    System.out.println("Total Promotion: " + totalPromotion);
	    System.out.println("Total Number Discount: " + totalNumDiscount);
	    System.out.println("Total Discount: " + totalDiscount);
	}
	public void closeProgram(ActionEvent event) {
		
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Close Program");
		alert.setHeaderText(null);
		alert.setContentText("Are you sure you want to close the program?");
		if(alert.showAndWait().get() == ButtonType.OK) {
			stage = (Stage) scenePane.getScene().getWindow();
			System.out.println("closed");
			stage.close();
		}
		
	}
	

}
