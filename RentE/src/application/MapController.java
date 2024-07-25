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
	private Stage stage;
	private Scene scene;
	private FileLoadData data;
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
    

    
	
	
	
	public void logout(ActionEvent event) throws IOException {
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("Main.fxml"));
		root = loader.load();
		
		Controller controller= loader.getController();
		
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
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
	public void updateTree() {
		
		String targetDate = getDate();
		TreeItem<String> rootItem = new TreeItem<>("Vehicles");
        if (data.getRentedCars().size() > 0) {
            TreeItem<String> carBranchItem = new TreeItem<>("Cars");
            for(List<String> val : data.getRentedCars()) {
            	
            	String[] temp = val.get(0).split(" ");
            	if(temp[0].equals(targetDate)) {
            		
            		TreeItem<String> carItem = new TreeItem<>(val.get(2));
            		for(Vehicle veh : vehicleList) {
            			if(veh instanceof Car) {
            				Car car = (Car)veh;
            				if(car.getId().equals(val.get(2))) {
            					TreeItem<String> carBat = new TreeItem<>("Battery: " + String.valueOf(car.getCurrentBatteryLevel()));
            					
            					carItem.getChildren().addAll(carBat);
            				}
            			}
            			
            		}
                    carBranchItem.getChildren().add(carItem);
            	}
            	
            }
            
            rootItem.getChildren().add(carBranchItem);
        }
        if (data.getRentedBikes().size() > 0) {
            TreeItem<String> bikeBranchItem = new TreeItem<>("Bikes");
            
            for(List<String> val : data.getRentedBikes()) {
            	
            	String[] temp = val.get(0).split(" ");
            	if(temp[0].equals(targetDate)) {
            		
            		TreeItem<String> carItem = new TreeItem<>(val.get(2));
            		bikeBranchItem.getChildren().add(carItem);
            	}
            	
            }
            
            rootItem.getChildren().add(bikeBranchItem);
            
            
        }
        if (data.getRentedScooters().size() > 0) {
            TreeItem<String> scooterBranchItem = new TreeItem<>("Scooters");
            
            for(List<String> val : data.getRentedScooters()) {
            	
            	String[] temp = val.get(0).split(" ");
            	if(temp[0].equals(targetDate)) {
            		
            		TreeItem<String> carItem = new TreeItem<>(val.get(2));
            		scooterBranchItem.getChildren().add(carItem);
            	}
            	
            }
            
            
            
            rootItem.getChildren().add(scooterBranchItem);
        }

        // Set the root item to the TreeView
        rentedTreeView.setRoot(rootItem);
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
