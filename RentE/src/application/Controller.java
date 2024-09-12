package application;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import SimulationPJ2.SimulationFunctions;
import Vehicles.Bicycle;
import Vehicles.Car;
import Vehicles.Scooter;
import Vehicles.Vehicle;
import Vehicles.VehicleFunctions;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import main.BusinessResultsReport;
import main.FileDeleter;
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
	private Button backButton;
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
			FileDeleter.deleteFileIfExists("malfunctionList.txt");

	        initializeMap(event);

	        MyDateParser dp = new MyDateParser();
	        
	        FileLoadData fileData = new FileLoadData("ovo.csv", "rented.csv");
	        
	        SimulationFunctions sim  = new SimulationFunctions();

	        VehicleFunctions veh = new VehicleFunctions();
	        Vehicle[] vehicleList = veh.loadVehicles(fileData, dp);
	        Rent[] rentedList = sim.loadRents(fileData, dp, vehicleList);
	        BusinessResultsReport rep  = new BusinessResultsReport();
	        for(int i = 0; i <   rentedList.length; i++) {
	        	rep.dailyReport(rentedList[i].getDateTime().toLocalDate(), rentedList[i].getFullPrice(), rentedList[i].getTotalDiscount(), rentedList[i].getPromo(), rentedList[i].getDiscount(), rentedList[i].getIsInnerCity(), rentedList[i].getHadMalfunction(), rentedList[i].getVehicle());
	        }
	        System.out.println(rep);
	        sim.generateInvoices(rentedList);
	        sim.calculateTotals(rentedList);
	        
	        
	        mapController.loadData(fileData, vehicleList, rentedList);
	        
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
