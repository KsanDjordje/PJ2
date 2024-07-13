package application;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class MapController {

	@FXML
	private Button logoutButton;
	@FXML
	private AnchorPane scenePane;
	@FXML
	private GridPane gridPane;
	
	private int vehiclePositionX = 0; // Example starting position X
    private int vehiclePositionY = 0; // Example starting position Y
    
	@FXML
	private void initialize() {
        // Example: Simulate movement by changing vehicle position and updating tile color
        
        simulateMovement(0,0,5,15);
        simulateMovement(1,3,5,15);
        simulateMovement(5,0,10,13);

        simulateMovement(0,7,19,19);



    }

	private void simulateMovement(int startX, int startY, int endX, int endY) {
        // Example: Move vehicle from startX, startY to endX, endY
        new Thread(() -> {
            for (int x = startX; x <= endX; x++) {
                for (int y = startY; y <= endY; y++) {
                    final int finalX = x;
                    final int finalY = y;
                    Platform.runLater(() -> moveVehicleTo(finalX, finalY)); // Update UI on JavaFX Application Thread
                    try {
                        Thread.sleep(100); // Example delay for simulation
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    private void moveVehicleTo(int newX, int newY) {
    	
        // Reset previous tile color
        Rectangle previousTile = findTile(vehiclePositionX, vehiclePositionY);
        previousTile.setFill(Color.GREEN); // Reset color to default

        // Update vehicle position
        vehiclePositionX = newX;
        vehiclePositionY = newY;

        // Change color of the new tile
        Rectangle currentTile = findTile(vehiclePositionX, vehiclePositionY);
        currentTile.setFill(Color.RED); // Example color for current position
    }

    private Rectangle findTile(int column, int row) {
        for (int i = 0; i < gridPane.getChildren().size(); i++) {
            Rectangle tile = (Rectangle) gridPane.getChildren().get(i);
            if (GridPane.getColumnIndex(tile) == column && GridPane.getRowIndex(tile) == row) {
                return tile;
            }
        }
        return null;
    }
	
	
	Stage stage;
	
	public void logout(ActionEvent event) {
		
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
}
