package application;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;

import SimulationPJ2.SimulationFunctions;
import Vehicles.Bicycle;
import Vehicles.Car;
import Vehicles.Scooter;
import Vehicles.Vehicle;
import Vehicles.VehicleFunctions;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import main.BusinessResultsReport;
import main.FileDeleter;
import main.FileLoadData;
import main.MyDateParser;
import main.OutOfRadiusException;
import main.Rent;

public class VehicleViewController {
	@FXML
	AnchorPane pane;
	@FXML
	private AnchorPane scenePane;
	@FXML
	private TableView<Car> carTableView;
    @FXML
    private TableColumn<Car, String> idColumn;
    @FXML
    private TableColumn<Car, String> manufacturerColumn;
    @FXML
    private TableColumn<Car, String> modelColumn;
    @FXML
    private TableColumn<Car, Double> purchasePriceColumn;
    @FXML
    private TableColumn<Car, Boolean> hasMalfunctionColumn;
    @FXML
    private TableColumn<Car, String> malfunctionDescriptionColumn;
    @FXML
    private TableColumn<Car, LocalDateTime> malfunctionTimeColumn;
    @FXML
    private TableColumn<Car, Float> batteryLevelColumn;
    @FXML
    private TableColumn<Car, LocalDate> purchaseDateColumn;
    @FXML
    private TableColumn<Car, Integer> capacityColumn;
    @FXML
    private TableColumn<Car, String> descriptionColumn;
    
	private MapController mapController;

    private Stage stage;
	private Scene scene;
	private Parent root;
    
	@FXML
	public void goBackToMap(ActionEvent event)throws IOException, OutOfRadiusException{
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
	@FXML
	private void initialize() {
	    // Create the TableViews
	    TableView<Car> carTable = new TableView<>();
	    TableView<Bicycle> bikeTable = new TableView<>();
	    TableView<Scooter> scooterTable = new TableView<>();
	    // Create columns for properties
	    TableColumn<Car, String> idColumn = new TableColumn<>("ID");
	    idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

	    TableColumn<Car, String> manufacturerColumn = new TableColumn<>("Manufacturer");
	    manufacturerColumn.setCellValueFactory(new PropertyValueFactory<>("manufacturer"));

	    TableColumn<Car, String> modelColumn = new TableColumn<>("Model");
	    modelColumn.setCellValueFactory(new PropertyValueFactory<>("model"));

	    TableColumn<Car, Double> purchasePriceColumn = new TableColumn<>("Purchase Price");
	    purchasePriceColumn.setCellValueFactory(new PropertyValueFactory<>("purchasePrice"));

	    TableColumn<Car, Boolean> hasMalfunctionColumn = new TableColumn<>("Has Malfunction");
	    hasMalfunctionColumn.setCellValueFactory(new PropertyValueFactory<Car,Boolean>("hasMalfunction"));

	    TableColumn<Car, String> malfunctionDescriptionColumn = new TableColumn<>("Malfunction Description");
	    malfunctionDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("malfunctionDescription"));

	    TableColumn<Car, LocalDateTime> malfunctionTimeColumn = new TableColumn<>("Malfunction Time");
	    malfunctionTimeColumn.setCellValueFactory(new PropertyValueFactory<>("malfunctionTime"));

	    TableColumn<Car, Float> batteryLevelColumn = new TableColumn<>("Battery Level");
	    batteryLevelColumn.setCellValueFactory(new PropertyValueFactory<>("currentBatteryLevel"));

	    TableColumn<Car, LocalDate> purchaseDateColumn = new TableColumn<>("Purchase Date");
	    purchaseDateColumn.setCellValueFactory(new PropertyValueFactory<>("purchaseDate"));

	    TableColumn<Car, Integer> capacityColumn = new TableColumn<>("Capacity");
	    capacityColumn.setCellValueFactory(new PropertyValueFactory<>("capacity"));

	    TableColumn<Car, String> descriptionColumn = new TableColumn<>("Description");
	    descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

	    // Add columns to the carTable
	    carTable.getColumns().addAll(
	        idColumn, manufacturerColumn, modelColumn, purchasePriceColumn, 
	        hasMalfunctionColumn, malfunctionDescriptionColumn, malfunctionTimeColumn, 
	        batteryLevelColumn, purchaseDateColumn, capacityColumn, descriptionColumn
	    );
	 // Bicycle Table Columns
	    TableColumn<Bicycle, String> bikeIdColumn = new TableColumn<>("ID");
	    bikeIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

	    TableColumn<Bicycle, String> bikeManufacturerColumn = new TableColumn<>("Manufacturer");
	    bikeManufacturerColumn.setCellValueFactory(new PropertyValueFactory<>("manufacturer"));

	    TableColumn<Bicycle, String> bikeModelColumn = new TableColumn<>("Model");
	    bikeModelColumn.setCellValueFactory(new PropertyValueFactory<>("model"));

	    TableColumn<Bicycle, Double> bikePurchasePriceColumn = new TableColumn<>("Purchase Price");
	    bikePurchasePriceColumn.setCellValueFactory(new PropertyValueFactory<>("purchasePrice"));

	    TableColumn<Bicycle, Boolean> bikeHasMalfunctionColumn = new TableColumn<>("Has Malfunction");
	    bikeHasMalfunctionColumn.setCellValueFactory(new PropertyValueFactory<>("hasMalfunction"));

	    TableColumn<Bicycle, String> bikeMalfunctionDescriptionColumn = new TableColumn<>("Malfunction Description");
	    bikeMalfunctionDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("malfunctionDescription"));

	    TableColumn<Bicycle, LocalDateTime> bikeMalfunctionTimeColumn = new TableColumn<>("Malfunction Time");
	    bikeMalfunctionTimeColumn.setCellValueFactory(new PropertyValueFactory<>("malfunctionTime"));

	    TableColumn<Bicycle, Float> bikeBatteryLevelColumn = new TableColumn<>("Battery Level");
	    bikeBatteryLevelColumn.setCellValueFactory(new PropertyValueFactory<>("currentBatteryLevel"));

	    TableColumn<Bicycle, String> bikeDescriptionColumn = new TableColumn<>("Description");
	    bikeDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

	    // Add columns to the bikeTable
	    bikeTable.getColumns().addAll(
	        bikeIdColumn, bikeManufacturerColumn, bikeModelColumn, bikePurchasePriceColumn,
	        bikeHasMalfunctionColumn, bikeMalfunctionDescriptionColumn, bikeMalfunctionTimeColumn,
	        bikeBatteryLevelColumn, bikeDescriptionColumn
	    );

	    // Scooter Table Columns
	    TableColumn<Scooter, String> scooterIdColumn = new TableColumn<>("ID");
	    scooterIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

	    TableColumn<Scooter, String> scooterManufacturerColumn = new TableColumn<>("Manufacturer");
	    scooterManufacturerColumn.setCellValueFactory(new PropertyValueFactory<>("manufacturer"));

	    TableColumn<Scooter, String> scooterModelColumn = new TableColumn<>("Model");
	    scooterModelColumn.setCellValueFactory(new PropertyValueFactory<>("model"));

	    TableColumn<Scooter, Double> scooterPurchasePriceColumn = new TableColumn<>("Purchase Price");
	    scooterPurchasePriceColumn.setCellValueFactory(new PropertyValueFactory<>("purchasePrice"));

	    TableColumn<Scooter, Boolean> scooterHasMalfunctionColumn = new TableColumn<>("Has Malfunction");
	    scooterHasMalfunctionColumn.setCellValueFactory(new PropertyValueFactory<>("hasMalfunction"));

	    TableColumn<Scooter, String> scooterMalfunctionDescriptionColumn = new TableColumn<>("Malfunction Description");
	    scooterMalfunctionDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("malfunctionDescription"));

	    TableColumn<Scooter, LocalDateTime> scooterMalfunctionTimeColumn = new TableColumn<>("Malfunction Time");
	    scooterMalfunctionTimeColumn.setCellValueFactory(new PropertyValueFactory<>("malfunctionTime"));

	    TableColumn<Scooter, Float> scooterBatteryLevelColumn = new TableColumn<>("Battery Level");
	    scooterBatteryLevelColumn.setCellValueFactory(new PropertyValueFactory<>("currentBatteryLevel"));

	    TableColumn<Scooter, String> scooterDescriptionColumn = new TableColumn<>("Description");
	    scooterDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

	    // Add columns to the scooterTable
	    scooterTable.getColumns().addAll(
	        scooterIdColumn, scooterManufacturerColumn, scooterModelColumn, scooterPurchasePriceColumn,
	        scooterHasMalfunctionColumn, scooterMalfunctionDescriptionColumn, scooterMalfunctionTimeColumn,
	        scooterBatteryLevelColumn, scooterDescriptionColumn
	    );
	    // Load the vehicle data (assuming the method returns an array of Vehicles)
	    MyDateParser dp = new MyDateParser();
	    FileLoadData fileData = new FileLoadData("ovo.csv", "rented.csv");
	    VehicleFunctions veh = new VehicleFunctions();
	    Vehicle[] vehicleList = veh.loadVehicles(fileData, dp);

	    // Populate the carTable with Car data
	    for (Vehicle vehicle : vehicleList) {
	        if (vehicle instanceof Car) {
	            Car car = (Car) vehicle;
	            carTable.getItems().add(car); // Add car to the TableView
	        }else if(vehicle instanceof Bicycle) {
	        	Bicycle bike = (Bicycle) vehicle;
	        	bikeTable.getItems().add(bike);
	        }else if(vehicle instanceof Scooter) {
	        	Scooter scooter = (Scooter)vehicle;
	        	scooterTable.getItems().add(scooter);
	        }
	    }
	    
	    carTable.setPrefSize(550, 300);
	    bikeTable.setPrefSize(550, 300);
	    scooterTable.setPrefSize(550, 300);

	    // Create a VBox to arrange the tables vertically
	    VBox vbox = new VBox(10); // 10 is the spacing between the tables

	    // Add all the tables to the VBox
	    vbox.getChildren().addAll(carTable, bikeTable, scooterTable);
	    AnchorPane.setTopAnchor(vbox, 20.0);
	    AnchorPane.setLeftAnchor(vbox, 20.0);
	    AnchorPane.setRightAnchor(vbox, 20.0);
	    AnchorPane.setBottomAnchor(vbox, 80.0);
	    
	    scenePane.getChildren().addAll(vbox);
	}
	
	public void closeProgram(ActionEvent event) {
		Utility.closeProgram(stage,scenePane);
	}
    
}
