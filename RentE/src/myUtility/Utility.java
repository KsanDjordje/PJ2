package myUtility;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

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
