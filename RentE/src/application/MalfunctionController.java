package application;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import location.OutOfRadiusException;
import myUtility.FileDeleter;
import myUtility.FileLocation;
import myUtility.MyDateParser;
import myUtility.Utility;
import rental.Rent;
import reports.BusinessResultsReport;
import reports.FileLoadData;
import simulationPJ2.SimulationFunctions;
import vehicles.Malfunction;
import vehicles.Vehicle;
import vehicles.VehicleFunctions;

/**
 * Controller for managing the Malfunction view in the application.
 * This class handles loading malfunctions, displaying them in a table, 
 * and navigating back to the map view.
 */
public class MalfunctionController {
    @FXML
    AnchorPane pane;
    @FXML
    private AnchorPane scenePane;
    @FXML
    Button backButton;
    private MapController mapController;

    private Stage stage;
    private Scene scene;
    private Parent root;

    /**
     * Initializes the Malfunction view by loading malfunctions from a file
     * and displaying them in a TableView.
     */
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
            for (Malfunction malf : loadMalfunctions("malfunctionList.txt")) {
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

    /**
     * Loads malfunctions from a specified file path.
     *
     * @param filePath the path to the file containing malfunction data
     * @return a list of Malfunction objects
     * @throws FileNotFoundException if the specified file cannot be found
     * @throws IOException if an I/O error occurs while reading the file
     */
    public static List<Malfunction> loadMalfunctions(String filePath) throws FileNotFoundException, IOException {
        List<Malfunction> malfunctions = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            String id = null;
            LocalDateTime malfDate = null;
            String desc = null;

            String idFormat = "Vehicle: ";
            String dateFormat = "Malfunction Time: ";
            String descFormat = "Description: ";

            while ((line = reader.readLine()) != null) {
                if (line.startsWith(idFormat)) {
                    id = line.substring(idFormat.length());
                } else if (line.startsWith(dateFormat)) {
                    malfDate = LocalDateTime.parse(line.substring(dateFormat.length()));
                } else if (line.startsWith(descFormat)) {
                    desc = line.substring(descFormat.length());
                } else {
                    if (id != null && malfDate != null && desc != null) {
                        malfunctions.add(new Malfunction(id, malfDate, desc));
                        id = null;
                        malfDate = null;
                        desc = null;
                    }
                }
            }
            if (id != null && malfDate != null && desc != null) {
                malfunctions.add(new Malfunction(id, malfDate, desc));
            }
        }

        return malfunctions;
    }

    /**
     * Navigates back to the map view, cleaning up any necessary data.
     *
     * @param event the ActionEvent triggered by the user action
     * @throws IOException if an I/O error occurs while loading the map
     * @throws OutOfRadiusException if an out-of-radius error occurs
     */
    @FXML
    public void goBackToMap(ActionEvent event) throws IOException, OutOfRadiusException {
        FileDeleter.deleteFileIfExists("malfunctionList.txt");

        initializeMap(event);

        MyDateParser dp = new MyDateParser();

        FileLoadData fileData = new FileLoadData(FileLocation.getVehiclesFileLocation(), FileLocation.getRentalsFileLocation());

        SimulationFunctions sim = new SimulationFunctions();

        VehicleFunctions veh = new VehicleFunctions();
        Vehicle[] vehicleList = veh.loadVehicles(fileData, dp);
        Rent[] rentedList = sim.loadRents(fileData, dp, vehicleList);
        BusinessResultsReport rep = new BusinessResultsReport();
        for (int i = 0; i < rentedList.length; i++) {
            rep.dailyReport(rentedList[i].getDateTime().toLocalDate(), rentedList[i].getFullPrice(), rentedList[i].getTotalDiscount(), rentedList[i].getPromo(), rentedList[i].getDiscount(), rentedList[i].getIsInnerCity(), rentedList[i].getHadMalfunction(), rentedList[i].getVehicle());
        }
        System.out.println(rep);
        sim.generateInvoices(rentedList);

        mapController.loadData(fileData, vehicleList, rentedList, rep);
    }

    /**
     * Initializes the map view and restores the stage's size and position.
     *
     * @param event the ActionEvent triggered by the user action
     * @throws IOException if the FXML file for the map cannot be loaded
     */
    private void initializeMap(ActionEvent event) throws IOException {
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        // Save current position and size of the stage
        double currentX = stage.getX();
        double currentY = stage.getY();
        double currentWidth = stage.getWidth();
        double currentHeight = stage.getHeight();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("Map.fxml"));
        root = loader.load();

        mapController = loader.getController();

        scene = new Scene(root, currentWidth, currentHeight); // Keep the current size
        stage.setScene(scene);

        // Restore the position
        stage.setX(currentX);
        stage.setY(currentY);

        stage.show();
    }

    /**
     * Closes the application when the close action is triggered.
     *
     * @param event the ActionEvent triggered by the close action
     */
    public void closeProgram(ActionEvent event) {
        Utility.closeProgram(stage, scenePane);
    }
}
