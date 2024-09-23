package application;

import java.io.IOException;
import java.util.List;

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
import location.OutOfRadiusException;
import myUtility.FileDeleter;
import myUtility.FileLocation;
import myUtility.MyDateParser;
import rental.Rent;
import rental.User;
import reports.BusinessResultsReport;
import reports.FileLoadData;
import reports.VehicleEarned;
import reports.VehicleEarnings;
import simulationPJ2.SimulationFunctions;
import vehicles.Vehicle;
import vehicles.VehicleFunctions;

/**
 * The main controller for handling user interactions and application flow.
 * This class manages the login process, initializes the map, and controls the program's closure.
 */
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

    // Temporary credentials for login validation
    private String secretUsername = "admin";
    private String secretPassword = "admin";

    /**
     * Handles the login action when the login button is pressed.
     * Validates the username and password, then initializes the map and loads data if valid.
     *
     * @param event the action event triggered by the login button
     * @throws IOException if an error occurs while loading the map
     * @throws OutOfRadiusException if a location is out of the valid radius
     */
    @FXML
    public void login(ActionEvent event) throws IOException, OutOfRadiusException {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (isLoginValid(username, password)) {
            FileDeleter.deleteFileIfExists("malfunctionList.txt");
            initializeMap(event);

            MyDateParser dp = new MyDateParser();
            FileLoadData fileData = new FileLoadData(FileLocation.getVehiclesFileLocation(), FileLocation.getRentalsFileLocation());
            SimulationFunctions sim = new SimulationFunctions();
            VehicleFunctions veh = new VehicleFunctions();
            Vehicle[] vehicleList = veh.loadVehicles(fileData, dp);
            Rent[] rentedList = sim.loadRents(fileData, dp, vehicleList);
            BusinessResultsReport rep = new BusinessResultsReport();
            VehicleEarnings ve = new VehicleEarnings(fileData);
            for (Rent rent : rentedList) {
            	ve.vehicleEarned(rent.getVehicle(), rent.getFullPrice());
                rep.dailyReport(rent.getDateTime().toLocalDate(), rent.getFullPrice(), rent.getTotalDiscount(),
                        rent.getPromo(), rent.getDiscount(), rent.getIsInnerCity(), rent.getHadMalfunction(), rent.getVehicle());
            }
            
            
            for(VehicleEarned list : ve.getBestEarningVehicles()) {
            	System.out.println(list.getVehicle().getId());
            	System.out.println(list.getMoneyEarned());
            }
            
            System.out.println(rep);
            sim.generateInvoices(rentedList);

            mapController.loadData(fileData, vehicleList, rentedList, rep);
        } else {
            System.out.println("Incorrect Login");
        }
    }

    /**
     * Validates the provided username and password against the predefined credentials.
     *
     * @param username the username entered by the user
     * @param password the password entered by the user
     * @return true if the credentials match, false otherwise
     */
    private boolean isLoginValid(String username, String password) {
        return username.equals(secretUsername) && password.equals(secretPassword);
    }

    /**
     * Initializes the map scene and sets it as the current scene.
     *
     * @param event the action event triggered by the login process
     * @throws IOException if an error occurs while loading the map
     */
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

    /**
     * Prompts the user for confirmation before closing the application.
     *
     * @param event the action event triggered by the close button
     */
    public void closeProgram(ActionEvent event) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Close Program");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to close the program?");
        if (alert.showAndWait().get() == ButtonType.OK) {
            stage = (Stage) scenePane.getScene().getWindow();
            System.out.println("closed");
            stage.close();
        }
    }
}
