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
import javafx.scene.control.DateCell;
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
import location.Location;
import location.OutOfRadiusException;
import location.PathFinder;
import myUtility.MyDateParser;
import myUtility.RandomFunctions;
import myUtility.Utility;
import rental.Rent;
import reports.BusinessResultsReport;
import reports.FileLoadData;
import reports.TableResultsData;
import vehicles.Car;
import vehicles.Vehicle;

/**
 * Controller class for managing the map scene in the application.
 * Handles interactions with the UI components, including the date picker,
 * simulation buttons, and vehicle management.
 */
public class MapController implements Initializable {

    @FXML
    private Button serializerButton;
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
    private BusinessResultsReport report;

    private Stage stage;
    private Scene scene;
    private FileLoadData data;
    private String currentDateTime;
    private Vehicle[] vehicleList;
    private Rent[] rentedList;

    private TreeItem<String> rootTree = new TreeItem<>("Vehicles");

    private final Object treeLock = new Object();

    /**
     * Initializes the DatePicker with available dates and sets its action handler.
     */
    private void loadDatePicker() {
        List<LocalDate> availableDates = this.getAvailableDates();
        myDatePicker = new DatePicker();
        myDatePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                if (date == null || !availableDates.contains(date)) {
                    setDisable(true);
                    setStyle("-fx-background-color: #ffc0cb;");
                }
            }
        });
        myDatePicker.setPromptText("Pick a date");

        myDatePicker.setOnAction(event -> getDate());

        AnchorPane.setLeftAnchor(myDatePicker, 61.0);
        AnchorPane.setTopAnchor(myDatePicker, 38.0);
        
        scenePane.getChildren().add(myDatePicker);
    }

    /**
     * Resets the map to its default state in case of any errors.
     * This method updates the colors of the grid based on their locations.
     */
    @FXML
    private void resetMap() {
        Platform.runLater(() -> {
            try {
                List<Location> locations = new ArrayList<>();

                for (int x = 0; x < 20; x++) {
                    for (int y = 0; y < 20; y++) {
                        locations.add(new Location(x, y));
                    }
                }
                for (Location loc : locations) {
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

    /**
     * Retrieves a list of available dates for the DatePicker.
     * 
     * @return a list of LocalDate objects representing available dates.
     */
    private List<LocalDate> getAvailableDates() {
        List<TableResultsData> dataList = loadData();
        
        List<LocalDate> list = new ArrayList<>();
        
        for (TableResultsData res : dataList) {
            list.add(res.getDate());
        }
        
        return list;
    }

    /**
     * Loads data from the BusinessResultsReport to create a list of TableResultsData.
     * 
     * @return a list of TableResultsData objects.
     */
    private List<TableResultsData> loadData() {
        List<TableResultsData> dataList = new ArrayList<>();
        
        for (int j = 0; j < report.getDailyEarnings().size(); j++) {
            for (int i = 0; i < report.getDailyEarnings().get(j).getEarningsList().size(); i++) {
                dataList.add(new TableResultsData(report.getDailyEarnings().get(j).getDate(),
                        report.getDailyEarnings().get(j).getEarningsList().get(i),
                        report.getDailyDiscount().get(j).getEarningsList().get(i),
                        report.getDailyDiscountPromotion().get(j).getEarningsList().get(i),
                        report.getDailyDiscountQuantity().get(j).getEarningsList().get(i),
                        report.getDailyMaintenancePrice().get(j).getEarningsList().get(i),
                        report.getDailyRepairPrice().get(j).getEarningsList().get(i)));
            }
        }
        return dataList;
    }

    /**
     * Updates the battery level and location of the given vehicle in the TreeView.
     * This method runs on the JavaFX Application Thread and synchronizes access
     * to the TreeItem structure to prevent concurrent modification issues.
     *
     * @param vehicle the Vehicle object whose information is to be updated
     * @param loc the Location object representing the current location of the vehicle
     */
    private void updateBatAndLoc(Vehicle vehicle, Location loc) {
        RandomFunctions func = new RandomFunctions();
        String threadName = Thread.currentThread().getName();
        Color threadColor = func.generateColorFromThreadIdentifier(vehicle.getId());
        
        String className = vehicle.getClass().getSimpleName();
        String cutClassName = className + "s"; 

        Platform.runLater(() -> {
            synchronized (treeLock) {
                TreeItem<String> vehicleItem = getOrCreateBranch(rootTree, cutClassName);

                HBox idBox = new HBox();
                
                Rectangle colorRect = new Rectangle(10, 10);
                colorRect.setFill(threadColor);

                Label idLabel = new Label(vehicle.getId());

                idBox.getChildren().addAll(colorRect);

                TreeItem<String> idItem = getOrCreateBranch(vehicleItem, vehicle.getId());
                
                idItem.setGraphic(idBox);

                idItem.getChildren().clear(); 

                Float batteryLevel = vehicle.getCurrentBatteryLevel();
                String formattedBatteryLevel = String.format("%.1f", batteryLevel);
                TreeItem<String> batteryItem = new TreeItem<>(formattedBatteryLevel + "%");        
                TreeItem<String> locationItem = new TreeItem<>(loc.toString());
                
                idItem.getChildren().addAll(batteryItem, locationItem);
                idItem.setExpanded(true);
            }
        });
    }

    /**
     * Moves the vehicle along the specified path for a given duration.
     * Updates the vehicle's location on the map and discharges its battery
     * during the movement. This method also updates the UI elements with
     * the vehicle's current status.
     *
     * @param path an array of Location objects representing the path to be followed
     * @param time the total time taken to move along the path in seconds
     * @param vehicle the Vehicle object to be moved
     * @return the updated Vehicle object after movement
     */
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
                String className = vehicle.getClass().getSimpleName();
                String cutClassName = className + "s";
                
                updateBatAndLoc(vehicle, loc);

                // Ensure UI updates happen on the JavaFX Application Thread
                Platform.runLater(() -> {
                    try {
                        rect.setFill(threadColor);
                        System.out.println(vehicle.getId() + " " + vehicle.getCurrentBatteryLevel());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });

                vehicle.dischargeBattery((float) timePerField);
                Thread.sleep((long) (timePerField * 1000));

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

    /**
     * Retrieves the Rectangle at the specified Location on the grid.
     * This method assumes that the GridPane contains a Rectangle for each cell.
     *
     * @param loc the Location object representing the grid position
     * @return the Rectangle located at the specified location
     */
    private Rectangle getRectangleAtLocation(Location loc) {
        // Assuming your GridPane has a Rectangle at each cell
        return (Rectangle) gridPane.getChildren().get(loc.getY() * gridPane.getColumnCount() + loc.getX());
    }

    /**
     * Handles the selection of an item in the rented TreeView.
     * Prints the value of the selected item to the console.
     */
    public void selectItem() {
        TreeItem<String> item = rentedTreeView.getSelectionModel().getSelectedItem();
        
        if (item != null) {
            System.out.println(item.getValue());
        }
    }

    /**
     * Gets the selected date from the DatePicker and updates the TreeView.
     *
     * @param event the ActionEvent triggered by the user action
     */
    public void getDate(ActionEvent event) {
        updateTree();
        System.out.println(getDate());
    }

    /**
     * Retrieves the selected date from the DatePicker.
     * 
     * @return a formatted string representing the selected date in "dd.MM.yyyy" format
     */
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

    /**
     * Starts the vehicle movement simulation based on the selected date.
     * Creates threads for each vehicle movement and handles day changes
     * with appropriate resets and battery recharges.
     *
     * @throws OutOfRadiusException if any vehicle moves out of the defined area
     */
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

                        thread.start();
                        threads.add(thread);
                        
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

                            thread.start();
                            threads.add(thread); 
                        }
                    }

                    // Wait for all threads to finish
                    for (Thread thread : threads) {
                        try {
                            thread.join();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();  
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

    /**
     * Closes the program when the close action is triggered.
     *
     * @param event the ActionEvent triggered by the close action
     */
    public void closeProgram(ActionEvent event) {
        Utility.closeProgram(stage, scenePane);
    }

    /**
     * Opens the vehicle view when the corresponding action is triggered.
     *
     * @param event the ActionEvent triggered by the user action
     * @throws IOException if the FXML file cannot be loaded
     */
    @FXML
    private void vehicleView(ActionEvent event) throws IOException {
        updateCurrentWindow(event, "vehicleView.fxml", 1280, 720);
    }

    @FXML
    private void serializerView(ActionEvent event) throws IOException{
        updateCurrentWindow(event, "serializerView.fxml", 1280, 720);

    }
    /**
     * Opens the malfunction view when the corresponding action is triggered.
     *
     * @param event the ActionEvent triggered by the user action
     * @throws IOException if the FXML file cannot be loaded
     */
    @FXML
    private void malfunctionView(ActionEvent event) throws IOException {
        updateCurrentWindow(event, "malfunctionView.fxml", 1280, 720);
    }

    /**
     * Opens the results view when the corresponding action is triggered.
     *
     * @param event the ActionEvent triggered by the user action
     * @throws IOException if the FXML file cannot be loaded
     */
    @FXML
    private void resultView(ActionEvent event) throws IOException {
        updateCurrentWindow(event, "resultView.fxml", 1280, 720);
    }

    /**
     * Opens a new window with the specified FXML file and dimensions.
     *
     * @param fxmlFileName the name of the FXML file to be loaded
     * @param width the width of the new window
     * @param height the height of the new window
     * @throws IOException if the FXML file cannot be loaded
     */
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

    /**
     * Updates the current window with a new scene based on the specified FXML file.
     *
     * @param event the ActionEvent triggered by the user action
     * @param fxmlFileName the name of the FXML file to be loaded
     * @param width the width of the new scene
     * @param height the height of the new scene
     * @throws IOException if the FXML file cannot be loaded
     */
    private void updateCurrentWindow(ActionEvent event, String fxmlFileName, double width, double height) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFileName));
        Parent parent = loader.load();
        if (fxmlFileName.equals("vehicleView.fxml")) {
            VehicleViewController controller = loader.getController();
        } else if (fxmlFileName.equals("malfunctionView.fxml")) {
            MalfunctionController controller = loader.getController();
        } else if (fxmlFileName.equals("resultView.fxml")) {
            ResultsController controller = loader.getController();
            controller.setReport(this.report);
            controller.open();
        }else if (fxmlFileName.equals("serializerView")) {
        	SerializerController controller = loader.getController();
        }

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setX(0);
        stage.setY(0);
        Scene scene = new Scene(parent, width, height);
        stage.setScene(scene);
    }

    /**
     * Logs out the user when the logout action is triggered.
     * Displays a confirmation dialog before logging out.
     *
     * @param event the ActionEvent triggered by the user action
     * @throws IOException if the main FXML file cannot be loaded
     */
    public void logout(ActionEvent event) throws IOException {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Logout");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to logout?");
        if (alert.showAndWait().get() == ButtonType.OK) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Main.fxml"));
            root = loader.load();

            Controller controller = loader.getController();
            
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            System.out.println("logout");
        }
    }

    /**
     * Updates the TreeView with vehicle data based on the selected date.
     */
    public void updateTree() {
        String targetDate = getDate();
        rootTree.getChildren().clear();
        TreeItem<String> rootItem = new TreeItem<>("Vehicles");
        rootItem.setExpanded(true);

        TreeItem<String> carBranchItem = getOrCreateBranch(rootItem, "Cars");
        updateVehicleBranch(carBranchItem, data.getRentedCars(), targetDate, true);
        carBranchItem.setExpanded(true);

        TreeItem<String> bikeBranchItem = getOrCreateBranch(rootItem, "Bicycles");
        updateVehicleBranch(bikeBranchItem, data.getRentedBikes(), targetDate, false);
        bikeBranchItem.setExpanded(true);

        TreeItem<String> scooterBranchItem = getOrCreateBranch(rootItem, "Scooters");
        updateVehicleBranch(scooterBranchItem, data.getRentedScooters(), targetDate, false);
        scooterBranchItem.setExpanded(true);

        rootTree = rootItem;
        rentedTreeView.setRoot(rootItem);
    }

    /**
     * Retrieves or creates a branch in the TreeItem structure.
     *
     * @param root the root TreeItem to search in
     * @param branchName the name of the branch to retrieve or create
     * @return the existing or newly created TreeItem branch
     */
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

    /**
     * Updates the specified vehicle branch with data from rented vehicles.
     *
     * @param branch the TreeItem branch to be updated
     * @param rentedVehicles the list of rented vehicles data
     * @param targetDate the target date for filtering rented vehicles
     * @param isCar a boolean indicating if the branch is for cars
     */
    private void updateVehicleBranch(TreeItem<String> branch, List<List<String>> rentedVehicles, String targetDate, boolean isCar) {
        branch.getChildren().clear();

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

    /**
     * Loads data for vehicles and rentals into the controller.
     *
     * @param data the FileLoadData object containing vehicle data
     * @param veh the array of Vehicle objects
     * @param rented the array of Rent objects
     * @param report the BusinessResultsReport object containing report data
     */
    public void loadData(FileLoadData data, Vehicle[] veh, Rent[] rented, BusinessResultsReport report) {
        this.data = data;
        this.vehicleList = veh;
        this.rentedList = rented;
        this.report = report;
        loadDatePicker();
        this.updateTree();
    }

    /**
     * Initializes the controller after its root element has been processed.
     *
     * @param arg0 the URL location
     * @param arg1 the ResourceBundle
     */
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        
    }
}