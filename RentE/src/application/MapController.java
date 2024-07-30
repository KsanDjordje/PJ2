package application;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import Vehicles.Car;
import Vehicles.Vehicle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import main.FileLoadData;
import main.Location;
import main.Rent;

public class MapController implements Initializable{

	@FXML
	private Button logoutButton;
	@FXML
	private AnchorPane scenePane;
	@FXML
	private GridPane gridPane;
	@FXML
	private DatePicker myDatePicker;
	@FXML
	private TreeView<String> rentedTreeView;
	@FXML
	private Parent root;
	@FXML
	private Button simulationButton;
	private Stage stage;
	private Scene scene;
	private FileLoadData data;
	private LocalDate currentDate;
	Vehicle[] vehicleList;
	Rent[] rentedList;
    private ExecutorService executorService; // or however many threads you need

	
    
	@FXML
	private void initialize() {
        
    }
	public void initializeExecutorService(int nThreads) {
        executorService = Executors.newFixedThreadPool(nThreads);
    }

    public void simulateMovement(Location[] path) {
        Runnable task = () -> {
            for (Location loc : path) {
                // Simulate the movement by updating the UI on the JavaFX Application Thread
                javafx.application.Platform.runLater(() -> {
                    Rectangle rect = getRectangleAtLocation(loc);
                    // Update rectangle color or any other property
                    rect.setStyle("-fx-fill: red;");
                    
                });
                try {
                    Thread.sleep(400); // Adjust the sleep time as needed
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        };
        executorService.submit(task);
    }

    private Rectangle getRectangleAtLocation(Location loc) {
        // Assuming your GridPane has a Rectangle at each cell
        return (Rectangle) gridPane.getChildren().get(loc.getY() * gridPane.getColumnCount() + loc.getX());
    }

    public void shutdown() {
        executorService.shutdown();
    }

	public void selectItem() {
		TreeItem<String> item = rentedTreeView.getSelectionModel().getSelectedItem();
		
		if(item != null) {
			System.out.println(item.getValue());
			
		}
	}
	
    public void getDate(ActionEvent event) {
    	
    	updateTree();
    	System.out.println(getDate());
    }
    public String getDate() {
        LocalDate myDate = myDatePicker.getValue();
        if (myDate != null) {
            // Format the date to "dd.MM.yyyy"
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d.M.yyyy");
            return myDate.format(formatter);
        } else {
            return ""; // Return an empty string if no date is selected
        }
    }
    
    public void startSimulation() {
    	
    	String targetDate = getDate();
    	System.out.println(targetDate);
    	if(targetDate == "") {
    		System.out.println("jesam");
    		for(Rent rented : rentedList) {
    			System.out.println(rented.getDateTime());
    		}
    	}
    	
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
	
	public void logout(ActionEvent event) throws IOException {
		
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Logout");
		alert.setHeaderText(null);
		alert.setContentText("Are you sure you want to logout?");
		if(alert.showAndWait().get() == ButtonType.OK) {
			
			
			FXMLLoader loader = new FXMLLoader(getClass().getResource("Main.fxml"));
			root = loader.load();
			
			Controller controller= loader.getController();
			
			stage = (Stage)((Node)event.getSource()).getScene().getWindow();
			scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
			System.out.println("logout");
		}
		
	}
	
	
	public void updateTree() {
		
		String targetDate = getDate();
	    TreeItem<String> rootItem = new TreeItem<>("Vehicles");

	    // Update or create the Car branch
	    TreeItem<String> carBranchItem = getOrCreateBranch(rootItem, "Cars");
	    updateVehicleBranch(carBranchItem, data.getRentedCars(), targetDate, true);

	    // Update or create the Bike branch
	    TreeItem<String> bikeBranchItem = getOrCreateBranch(rootItem, "Bikes");
	    updateVehicleBranch(bikeBranchItem, data.getRentedBikes(), targetDate, false);

	    // Update or create the Scooter branch
	    TreeItem<String> scooterBranchItem = getOrCreateBranch(rootItem, "Scooters");
	    updateVehicleBranch(scooterBranchItem, data.getRentedScooters(), targetDate, false);

	    // Set the root item to the TreeView
	    rentedTreeView.setRoot(rootItem);
	}
	private TreeItem<String> getOrCreateBranch(TreeItem<String> root, String branchName) {
	    for (TreeItem<String> child : root.getChildren()) {
	        if (child.getValue().equals(branchName)) {
	            return child;
	        }
	    }
	    TreeItem<String> newBranch = new TreeItem<>(branchName);
	    root.getChildren().add(newBranch);
	    return newBranch;
	}

	private void updateVehicleBranch(TreeItem<String> branch, List<List<String>> rentedVehicles, String targetDate, boolean isCar) {
	    branch.getChildren().clear();  // Clear existing items

	    for (List<String> val : rentedVehicles) {
	        String[] temp = val.get(0).split(" ");
	        if (temp[0].equals(targetDate)) {
	            TreeItem<String> vehicleItem = new TreeItem<>(val.get(2));

	            if (isCar) {
	                for (Vehicle veh : vehicleList) {
	                    if (veh instanceof Car) {
	                        Car car = (Car) veh;
	                        if (car.getId().equals(val.get(2))) {
	                            TreeItem<String> batteryItem = new TreeItem<>("Battery: " + String.valueOf(car.getCurrentBatteryLevel()));
	                            vehicleItem.getChildren().add(batteryItem);
	                        }
	                    }
	                }
	            }

	            branch.getChildren().add(vehicleItem);
	        }
	    }
	}
	public void loadData(FileLoadData data, Vehicle[] veh,Rent[] rented) {
		 this.data = data;
		 this.vehicleList = veh;
		 this.rentedList = rented;
		 
		 this.updateTree();
	    
	}
	
	
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		

		
		
	}
}
