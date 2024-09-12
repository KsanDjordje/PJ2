package application;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import SimulationPJ2.SimulationFunctions;
import Vehicles.Malfunction;
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
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import main.BusinessResultsReport;
import main.FileDeleter;
import main.FileLoadData;
import main.MyDateParser;
import main.OutOfRadiusException;
import main.Rent;

public class MalfunctionController {
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
	private void initialize() {
		TableView<Malfunction> tableView = new TableView<>();
        TableColumn<Malfunction, String> vehicleIdColumn = new TableColumn<>("Vehicle ID");
        TableColumn<Malfunction, LocalDateTime> malfunctionTimeColumn = new TableColumn<>("Malfunction Time");
        TableColumn<Malfunction, String> descriptionColumn = new TableColumn<>("Description");

        vehicleIdColumn.setCellValueFactory(new PropertyValueFactory<>("vehicleId"));
        malfunctionTimeColumn.setCellValueFactory(new PropertyValueFactory<>("malfunctionTime"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        
        tableView.getColumns().addAll(vehicleIdColumn, malfunctionTimeColumn, descriptionColumn);

        try {
			for(Malfunction malf :  loadMalfunctions("malfunctionList.txt")) {
				tableView.getItems().add(malf);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
        tableView.setPrefSize(600, 600);
        VBox vbox = new VBox(tableView);
        AnchorPane.setTopAnchor(vbox, 20.0);
	    AnchorPane.setLeftAnchor(vbox, 20.0);
	    AnchorPane.setRightAnchor(vbox, 20.0);
	    AnchorPane.setBottomAnchor(vbox, 80.0);
        scenePane.getChildren().add(vbox);

	}

	public static List<Malfunction> loadMalfunctions(String filePath) throws FileNotFoundException, IOException{
		List<Malfunction> malfunctions = new ArrayList<>();
		
		//DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
		
		try(BufferedReader reader = new BufferedReader(new FileReader(filePath))){
			String line;
			String id = null;
			LocalDateTime malfDate = null;
			String desc = null;
			
			String idFormat = "Vehicle: ";
			String dateFormat = "Malfunction Time: ";
			String descFormat = "Description: ";
			
			while((line = reader.readLine()) != null) {
				if(line.startsWith(idFormat)) {
					id = line.substring(idFormat.length());
				}else if(line.startsWith(dateFormat)) {
					malfDate = LocalDateTime.parse(line.substring(dateFormat.length()));
				}else if(line.startsWith(descFormat)) {
					desc = line.substring(descFormat.length());
				}else {
                    if (id != null && malfDate != null && desc!= null) {
                        malfunctions.add(new Malfunction(id, malfDate, desc));
                        id = null;
                        malfDate = null;
                        desc = null;
                    } 
                }
			}
			if (id != null && malfDate!= null && desc != null) {
                malfunctions.add(new Malfunction(id, malfDate, desc));
            }
		}	
		
		return malfunctions;
	}
	
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
