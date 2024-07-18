package application;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
import main.Location;

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
    	LocalDate myDate = myDatePicker.getValue();
    	//TODO
    	System.out.println(myDate.toString());
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
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		TreeItem<String> rootItem = new TreeItem<>("Vehicles");
		TreeItem<String> carBrantchItem = new TreeItem<>("Cars");
		TreeItem<String> bikeBrantchItem = new TreeItem<>("Bikes");
		TreeItem<String> scooterBrantchItem = new TreeItem<>("Scooters");
		
		TreeItem<String> leafItem1 = new TreeItem<>("picture1");
		TreeItem<String> leafItem2 = new TreeItem<>("picture2");	
		TreeItem<String> leafItem3 = new TreeItem<>("video1");
		TreeItem<String> leafItem4 = new TreeItem<>("video2");	
		TreeItem<String> leafItem5 = new TreeItem<>("music1");
		TreeItem<String> leafItem6 = new TreeItem<>("music2");
		
		carBrantchItem.getChildren().addAll(leafItem1, leafItem2);
		bikeBrantchItem.getChildren().addAll(leafItem3, leafItem4);
		scooterBrantchItem.getChildren().addAll(leafItem5, leafItem6);
		
		
		
		rootItem.getChildren().addAll(carBrantchItem,bikeBrantchItem,scooterBrantchItem);
		rentedTreeView.setRoot(rootItem);
		
	}
}
