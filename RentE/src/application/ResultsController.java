package application;

import java.io.IOException;

import SimulationPJ2.SimulationFunctions;
import Vehicles.Vehicle;
import Vehicles.VehicleFunctions;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import main.BusinessResultsReport;
import main.FileDeleter;
import main.FileLoadData;
import main.MyDateParser;
import main.OutOfRadiusException;
import main.Rent;

public class ResultsController {
	@FXML
	AnchorPane pane;
	@FXML
	private AnchorPane scenePane;
	@FXML Button backButton;
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
	
	public void closeProgram(ActionEvent event) {
		Utility.closeProgram(stage,scenePane);
	}
	
}
