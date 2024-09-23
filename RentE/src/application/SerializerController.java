package application;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
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
import reports.VehicleEarned;
import reports.VehicleEarnings;
import simulationPJ2.SimulationFunctions;
import vehicles.Vehicle;
import vehicles.VehicleFunctions;

/**
* Controller for managing serialization and deserialization of vehicle earnings data.
*/
public class SerializerController {
   
   @FXML
   private AnchorPane scenePane;  // Pane to hold the scene elements

   private MapController mapController;  // Controller for the map (not used in this context)

   private Stage stage;  // Stage for the application
   private Scene scene;  // Scene for the application
   private Parent root;  // Root node for the scene
   
   private VehicleEarned car;      // VehicleEarned object for car
   private VehicleEarned bike;     // VehicleEarned object for bike
   private VehicleEarned scooter;   // VehicleEarned object for scooter
   
   /**
    * Initializes the GUI components for serializing and deserializing vehicle data.
    * This method is called after the FXML file has been loaded.
    */
   @FXML
   private void initialize() {
       VBox vbox = new VBox(50);
       Button serializeButton = new Button();
       Button deserializeButton = new Button();

       TextField carText = new TextField();
       carText.setPrefWidth(350);
       carText.setText("Car that has earned the most: " + VehicleEarnings.getBestCar().getVehicle().getId() + " = " + VehicleEarnings.getBestCar().getMoneyEarned());
       carText.setEditable(false);

       TextField bikeText = new TextField();
       bikeText.setPrefWidth(350);
       bikeText.setText("Bicycle that has earned the most: " + VehicleEarnings.getBestBike().getVehicle().getId() + " = " + VehicleEarnings.getBestBike().getMoneyEarned());
       bikeText.setEditable(false);

       TextField scooterText = new TextField();
       scooterText.setPrefWidth(350);
       scooterText.setText("Scooter that has earned the most: " + VehicleEarnings.getBestScooter().getVehicle().getId() + " = " + VehicleEarnings.getBestScooter().getMoneyEarned());
       scooterText.setEditable(false);

       HBox hbox = new HBox(10);
       hbox.getChildren().addAll(carText, bikeText, scooterText);
       
       serializeButton.setText("Serialize");
       serializeButton.setOnAction(event -> {
           serializeVehicle(VehicleEarnings.getBestCar(), "bestCar.dat");
           serializeVehicle(VehicleEarnings.getBestBike(), "bestBike.dat");
           serializeVehicle(VehicleEarnings.getBestScooter(), "bestScooter.dat");
       });
       
       TextField carTextt = new TextField();
       carTextt.setPrefWidth(350);
       TextField bikeTextt = new TextField();
       bikeTextt.setPrefWidth(350);
       TextField scooterTextt = new TextField();
       scooterTextt.setPrefWidth(350);
       
       deserializeButton.setText("Deserialize");
       deserializeButton.setOnAction(event -> {
           car = deserializeVehicle("bestCar.dat");
           bike = deserializeVehicle("bestBike.dat");
           scooter = deserializeVehicle("bestScooter.dat");

           if (car != null) {
               carTextt.setText("Car that has earned the most: " + car.getVehicle().getId() + " = " + car.getMoneyEarned());
           } else {
               System.out.println("Failed to deserialize Car.");
           }

           if (bike != null) {
               bikeTextt.setText("Bicycle that has earned the most: " + bike.getVehicle().getId() + " = " + bike.getMoneyEarned());
           } else {
               System.out.println("Failed to deserialize Bicycle.");
           }

           if (scooter != null) {
               scooterTextt.setText("Scooter that has earned the most: " + scooter.getVehicle().getId() + " = " + scooter.getMoneyEarned());
           } else {
               System.out.println("Failed to deserialize Scooter.");
           }
       });
       
       if (car != null) {
           carTextt.setText("Car that has earned the most: " + car.getVehicle().getId() + " = " + car.getMoneyEarned());
       } else {
           carTextt.setText("No car data available.");
       }
       carTextt.setEditable(false);

       if (bike != null) {
           bikeTextt.setText("Bicycle that has earned the most: " + bike.getVehicle().getId() + " = " + bike.getMoneyEarned());
       } else {
           bikeTextt.setText("No bike data available.");
       }
       bikeTextt.setEditable(false);

       if (scooter != null) {
           scooterTextt.setText("Scooter that has earned the most: " + scooter.getVehicle().getId() + " = " + scooter.getMoneyEarned());
       } else {
           scooterTextt.setText("No scooter data available.");
       }
       scooterTextt.setEditable(false);

       HBox hbox2 = new HBox(10);
       hbox2.getChildren().addAll(carTextt, bikeTextt, scooterTextt);
       vbox.getChildren().addAll(hbox, serializeButton, hbox2, deserializeButton);
       AnchorPane.setTopAnchor(vbox, 20.0);
       AnchorPane.setLeftAnchor(vbox, 20.0);
       AnchorPane.setRightAnchor(vbox, 20.0);
       AnchorPane.setBottomAnchor(vbox, 80.0);
       scenePane.getChildren().add(vbox);
   }

   /**
    * Deserializes a VehicleEarned object from the specified filename.
    *
    * @param filename the name of the file to read from
    * @return the deserialized VehicleEarned object, or null if an error occurs
    */
   private VehicleEarned deserializeVehicle(String filename) {
       VehicleEarned vehicle = null;
       try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename))) {
           vehicle = (VehicleEarned) in.readObject();
       } catch (IOException | ClassNotFoundException e) {
           e.printStackTrace();
       }
       return vehicle;
   }

   /**
    * Serializes a VehicleEarned object to the specified filename.
    *
    * @param vehicle  the VehicleEarned object to serialize
    * @param filename the name of the file to write to
    */
   private void serializeVehicle(VehicleEarned vehicle, String filename) {
       try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename))) {
           out.writeObject(vehicle);
       } catch (IOException e) {
           e.printStackTrace();
       }
   }
    
    /**
     * Navigates back to the map view and loads vehicle and rental data.
     *
     * @param event The action event that triggered this method.
     * @throws IOException If there is an error loading the map.
     * @throws OutOfRadiusException If the vehicle is out of the allowed radius.
     */
    @FXML
    public void goBackToMap(ActionEvent event) throws IOException, OutOfRadiusException {
        FileDeleter.deleteFileIfExists("malfunctionList.txt");
        
        initializeMap(event);

        MyDateParser dp = new MyDateParser();
        
        FileLoadData fileData = new FileLoadData(FileLocation.getVehiclesFileLocation(), FileLocation.getRentalsFileLocation());
        
        SimulationFunctions sim  = new SimulationFunctions();

        VehicleFunctions veh = new VehicleFunctions();
        Vehicle[] vehicleList = veh.loadVehicles(fileData, dp);
        Rent[] rentedList = sim.loadRents(fileData, dp, vehicleList);
        BusinessResultsReport rep  = new BusinessResultsReport();
        for (int i = 0; i < rentedList.length; i++) {
            rep.dailyReport(rentedList[i].getDateTime().toLocalDate(), rentedList[i].getFullPrice(), 
                            rentedList[i].getTotalDiscount(), rentedList[i].getPromo(), 
                            rentedList[i].getDiscount(), rentedList[i].getIsInnerCity(), 
                            rentedList[i].getHadMalfunction(), rentedList[i].getVehicle());
        }
        System.out.println(rep);
        sim.generateInvoices(rentedList);
        
        mapController.loadData(fileData, vehicleList, rentedList, rep);
    }
    
    
    /**
     * Initializes the map view by loading the FXML file and setting the current stage.
     *
     * @param event The action event that triggered this method.
     * @throws IOException If there is an error loading the FXML file.
     */
    private void initializeMap(ActionEvent event) throws IOException {
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        double currentX = stage.getX();
        double currentY = stage.getY();
        double currentWidth = stage.getWidth();
        double currentHeight = stage.getHeight();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("Map.fxml"));
        root = loader.load();

        mapController = loader.getController();

        scene = new Scene(root, currentWidth, currentHeight);
        stage.setScene(scene);

        stage.setX(currentX);
        stage.setY(currentY);

        stage.show();
    }
    /**
     * Closes the application program.
     *
     * @param event The action event that triggered this method.
     */
    public void closeProgram(ActionEvent event) {
        Utility.closeProgram(stage, scenePane);
    }
    
}
