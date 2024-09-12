package application;

import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import SimulationPJ2.RandomFunctions;
import Vehicles.Car;
import Vehicles.Vehicle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
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
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import main.FileLoadData;
import main.Location;
import main.MyDateParser;
import main.OutOfRadiusException;
import main.PathFinder;
import main.Rent;

public class MapController implements Initializable{

	@FXML
	private Button testButton;
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
	
	@FXML
	private Button vehicleWindowButton;
	@FXML
	private Button malfunctionWindowButton;
	@FXML
	private Button resultWindowButton;
	
	private Stage stage;
	private Scene scene;
	private FileLoadData data;
	private String currentDateTime;
	Vehicle[] vehicleList;
	Rent[] rentedList;

	private TreeItem<String> rootTree = new TreeItem<>("Vehicles");
	
	private final Object treeLock = new Object(); // Lock object for synchronizing access to TreeItem
	@FXML
	private void initialize() {
        
    }
	

	// helping function to reset the map to its default state in case of any errors
	@FXML
	private void resetMap() {
		Platform.runLater(() ->{
			try {
				
				List<Location> locations = new ArrayList<>();  // Create a list to hold Location objects

	            // Populate the list with Locations from (0,0) to (19,19)
	            for (int x = 0; x < 20; x++) {
	                for (int y = 0; y < 20; y++) {
	                    locations.add(new Location(x, y));  // Add each Location to the list
	                }
	            }
	            for(Location loc : locations) {
	            	Rectangle rect = getRectangleAtLocation(loc);
	        		if (loc.getX() > 14 || loc.getX() < 5 || loc.getY() > 14 || loc.getY() < 5) {
	                    rect.setStyle("-fx-fill: green;");
	                } else {
	                    rect.setStyle("-fx-fill: dodgerblue;");
	                }
	        	}
	        } catch (OutOfRadiusException e) {
	            System.out.println("Location out of bounds!");
	        }
		});
		
		
	}
	
    
    private void updateBatAndLoc(Vehicle vehicle, Location loc) {
    	
    	RandomFunctions func = new RandomFunctions();
        String threadName = Thread.currentThread().getName();
        Color threadColor = func.generateColorFromThreadIdentifier(vehicle.getId());
        
        String className = vehicle.getClass().getSimpleName(); // Use getSimpleName() to avoid package info
        String cutClassName = className + "s"; 
    	
    	Platform.runLater(()-> {synchronized (treeLock) {
            TreeItem<String> vehicleItem = getOrCreateBranch(rootTree, cutClassName);

            // Create an HBox to hold the colored rectangle and vehicle ID
            HBox idBox = new HBox();
            
            // Create a colored rectangle with thread-specific color
            Rectangle colorRect = new Rectangle(10, 10);  // 10x10 rectangle
            colorRect.setFill(threadColor);

            // Create a label for the vehicle ID
            Label idLabel = new Label(vehicle.getId());

            // Add the rectangle and label to the HBox
            idBox.getChildren().addAll(colorRect);
            
            // Wrap the HBox in a TreeItem
            TreeItem<String> idItem = getOrCreateBranch(vehicleItem, vehicle.getId());
            
            idItem.setGraphic(idBox);  // Set the HBox as the graphic for this TreeItem

            // Clear existing children and update with battery level and location
            idItem.getChildren().clear(); 

            Float batteryLevel = vehicle.getCurrentBatteryLevel();
            String formattedBatteryLevel = String.format("%.1f", batteryLevel); // Round to 1 decimal place
            TreeItem<String> batteryItem = new TreeItem<>(formattedBatteryLevel + "%");        
            TreeItem<String> locationItem = new TreeItem<>(loc.toString());
            
            idItem.getChildren().addAll(batteryItem, locationItem);
            idItem.setExpanded(true);
        }});
        
    }

    private Vehicle moveVehicle(Location[] path, double time, Vehicle vehicle) {
        try {
            RandomFunctions func = new RandomFunctions();
            String threadName = Thread.currentThread().getName();
            Color threadColor = func.generateColorFromThreadIdentifier(vehicle.getId());
            int numberOfFields = path.length;
            double timePerField = time / numberOfFields;

            for (Location loc : path) {
                System.out.println(Thread.currentThread().getName() + " moved");
                Rectangle rect = getRectangleAtLocation(loc);
                String className = vehicle.getClass().getSimpleName(); // Use getSimpleName() to avoid package info
                String cutClassName = className + "s"; // Assumes you are using plural form for class items
                
                // Synchronize access to tree structure updates
                updateBatAndLoc(vehicle,loc);

                // Ensure UI updates happen on the JavaFX Application Thread
                Platform.runLater(() -> {
                    try {
                        rect.setFill(threadColor);
                        System.out.println(vehicle.getId() + " " + vehicle.getCurrentBatteryLevel());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });

                // Battery discharge and sleep happen outside the UI update block
                vehicle.dischargeBattery((float)timePerField);
                Thread.sleep((long)(timePerField * 1000));

                // Update the rectangle's color based on location
                Platform.runLater(() -> {
                    try {
                        if (loc.getX() > 14 || loc.getX() < 5 || loc.getY() > 14 || loc.getY() < 5) {
                            rect.setFill(Color.GREEN);
                        } else {
                            rect.setFill(Color.DODGERBLUE);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        } catch (InterruptedException e) {
            System.out.println("Interrupted");
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vehicle;
    }

    private Rectangle getRectangleAtLocation(Location loc) {
        // Assuming your GridPane has a Rectangle at each cell
        return (Rectangle) gridPane.getChildren().get(loc.getY() * gridPane.getColumnCount() + loc.getX());
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
    
    public void startSimulation() throws OutOfRadiusException {
    	
    	Thread sim = new Thread(() -> {
            try {
                String targetDate = getDate();
                System.out.println(targetDate);
                List<Thread> threads = new ArrayList<>();  // To hold all threads
                
                if (targetDate.isBlank()) {
                    System.out.println("jesam");
                    
                    LocalDateTime previousDateTime = null;

                    for (Rent rented : rentedList) {
                        LocalDateTime rentedDateTime = rented.getDateTime();
                        
                        // Check for day change
                        if (previousDateTime != null && rentedDateTime.getDayOfMonth() != previousDateTime.getDayOfMonth()) {
                            try {
                                // Wait for all threads of the previous day to complete
                                for (Thread thread : threads) {
                                    thread.join();
                                }

                                System.out.println("Day changed. Sleeping for 5 seconds...");
                                Thread.sleep(2500);
                                for (Vehicle veh : vehicleList) {
                                    while (veh.chargeBattery(1)) ;
                                }
                                resetMap();
                                Platform.runLater(() -> updateTree());
                                Thread.sleep(2500);
                                

                                // Clear the threads list for the new day
                                threads.clear();
                            } catch (InterruptedException e) {
                                System.out.println("Interrupted while waiting.");
                                Thread.currentThread().interrupt();
                            }
                        }

                        this.currentDateTime = rentedDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                        
                        // Create a new thread for the vehicle movement simulation
                        Thread thread = new Thread(() -> {
                            try {
                                for (Vehicle veh : vehicleList) {
                                    if (rented.getVehicle().getId().equals(veh.getId())) {
                                        rented.setVehicle(veh);
                                    }
                                }

                                PathFinder path = new PathFinder(rented.getLocationStart(), rented.getLocationEnd());
                                rented.setVehicle(moveVehicle(path.getPathDijkstra(), (long) rented.getTimeUsed(), rented.getVehicle()));
                            } catch (OutOfRadiusException e) {
                                e.printStackTrace();
                            }
                        });

                        thread.start();  // Start the thread
                        threads.add(thread);  // Add the thread to the list
                        
                        previousDateTime = rentedDateTime;
                    }

                } else {
                    System.out.println("nisam");

                    for (Rent rented : rentedList) {
                        MyDateParser parser = new MyDateParser();
                        System.out.println(rented.getDateTime().toLocalDate());

                        // Process only rents matching the target date
                        if (rented.getDateTime().toLocalDate().equals(parser.parseLocalDate(targetDate))) {
                            LocalDateTime rentedDateTime = rented.getDateTime();

                            this.currentDateTime = rentedDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                            
                            // Create a new thread for each rent
                            Thread thread = new Thread(() -> {
                                try {
                                    System.out.println("Task for vehicle " + rented.getVehicle().getId() + " submitted.");

                                    for (Vehicle veh : vehicleList) {
                                        if (rented.getVehicle().getId().equals(veh.getId())) {
                                            rented.setVehicle(veh);
                                        }
                                    }

                                    PathFinder path = new PathFinder(rented.getLocationStart(), rented.getLocationEnd());
                                    rented.setVehicle(moveVehicle(path.getPathDijkstra(), (long) rented.getTimeUsed(), rented.getVehicle()));
                                } catch (OutOfRadiusException e) {
                                    e.printStackTrace();
                                }
                            });

                            thread.start();  // Start the thread
                            threads.add(thread);  // Add the thread to the list
                        }
                    }

                    // Wait for all threads to finish
                    for (Thread thread : threads) {
                        try {
                            thread.join();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();  // Handle thread interruption
                        }
                    }
                    System.out.println("Day Finished. Recharging...");
                    Thread.sleep(2500);
                    for (Vehicle veh : vehicleList) {
                        while (veh.chargeBattery(1)) ;
                    }
                    resetMap();
                    Platform.runLater(() -> updateTree());
                    Thread.sleep(2500);
                    

                    // Clear the threads list for the new day
                    threads.clear();
                    resetMap();  // Reset the map once all threads are done
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        sim.start();
    	
    	
    }

    public void closeProgram(ActionEvent event) {
    	Utility.closeProgram(stage,scenePane);
	}
    
    @FXML
    private void vehicleView(ActionEvent event) throws IOException {
    	updateCurrentWindow(event, "vehicleView.fxml", 1280, 720);
    	
	}
	
    @FXML
    private void malfunctionView(ActionEvent event) throws IOException {
    	updateCurrentWindow(event, "malfunctionView.fxml", 1280, 720);
	}
    @FXML
    private void resultView(ActionEvent event) throws IOException {
    	updateCurrentWindow(event, "resultView.fxml", 1280, 720);
	}
    private void openWindow(String fxmlFileName, double width, double height) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFileName));
        Parent parent = loader.load();

        
        Controller controller = loader.getController();

        Stage stage = new Stage();
        stage.setX(0);
        stage.setY(0);
        Scene scene = new Scene(parent, width, height);
        stage.setScene(scene);
        stage.show();
    }
    
    private void updateCurrentWindow(ActionEvent event, String fxmlFileName, double width, double height) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFileName));
        Parent parent = loader.load();
        if(fxmlFileName.equals("vehicleView.fxml")) {
            VehicleViewController controller = loader.getController();
        }else if(fxmlFileName.equals("malfunctionView.fxmll")) {
        	MalfunctionController controller = loader.getController();

        }else if(fxmlFileName.equals("resultView.fxml")) {
            ResultsController controller = loader.getController();

        }else {
        	
        }

        // Get the current stage from the event source
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setX(0);
        stage.setY(0);
        Scene scene = new Scene(parent, width, height);
        stage.setScene(scene);
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
		rootTree.getChildren().clear();
	    TreeItem<String> rootItem = new TreeItem<>("Vehicles");
	    rootItem.setExpanded(true);
	    
	    
	    // Update or create the Car branch
	    TreeItem<String> carBranchItem = getOrCreateBranch(rootItem, "Cars");
	    updateVehicleBranch(carBranchItem, data.getRentedCars(), targetDate, true);
	    carBranchItem.setExpanded(true);
	    
	    
	 // Update or create the Bike branch
	    TreeItem<String> bikeBranchItem = getOrCreateBranch(rootItem, "Bicycles");
	    updateVehicleBranch(bikeBranchItem, data.getRentedBikes(), targetDate, false);
	    bikeBranchItem.setExpanded(true);
	    
	    
	    // Update or create the Scooter branch
	    TreeItem<String> scooterBranchItem = getOrCreateBranch(rootItem, "Scooters");
	    updateVehicleBranch(scooterBranchItem, data.getRentedScooters(), targetDate, false);
	    scooterBranchItem.setExpanded(true);
	    
	    
	    
	    rootTree = rootItem;
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
	            vehicleItem.setExpanded(true);
	            if (isCar) {
	                for (Vehicle veh : vehicleList) {
	                    if (veh instanceof Car) {
	                        Car car = (Car) veh;
	                        if (car.getId().equals(val.get(2))) {
	                            TreeItem<String> batteryItem = new TreeItem<>("Battery: " + String.valueOf(car.getCurrentBatteryLevel()));
	                            vehicleItem.getChildren().add(batteryItem);
	                            vehicleItem.setExpanded(true);
	                        }
	                    }
	                }
	            }
	            vehicleItem.setExpanded(true);
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
