package myUtility;

import java.io.IOException;

import application.MapController;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import location.OutOfRadiusException;
import rental.Rent;
import reports.BusinessResultsReport;
import reports.FileLoadData;
import simulationPJ2.SimulationFunctions;
import vehicles.Vehicle;
import vehicles.VehicleFunctions;

/**
 * Utility class providing common functionality for the application.
 */
public class Utility {

    /**
     * Prompts the user for confirmation before closing the program.
     *
     * @param stage     the current stage of the application
     * @param scenePane the main pane of the current scene
     */
    public static void closeProgram(Stage stage, AnchorPane scenePane) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Close Program");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to close the program?");
        
        // Show the confirmation dialog and check the user's response
        if (alert.showAndWait().get() == ButtonType.OK) {
            stage = (Stage) scenePane.getScene().getWindow();
            System.out.println("closed");
            stage.close();
        }
    }
    

}
