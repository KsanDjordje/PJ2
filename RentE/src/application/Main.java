package application;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import location.Location;
import location.OutOfRadiusException;
import myUtility.FileLocation;
import rental.Rent;
import rental.User;
import vehicles.Car;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.control.Alert.AlertType;

/**
 * The main application class that initializes and starts the JavaFX application.
 * This class sets up the primary stage and loads the initial FXML layout.
 */
public class Main extends Application {

    /**
     * Starts the JavaFX application by loading the main scene and setting up
     * the primary stage with the necessary properties.
     *
     * @param primaryStage the primary stage for this application
     */
    @Override
    public void start(Stage primaryStage) {
        try {
            // Set file locations for vehicle and rental data
            FileLocation.setFileLocations("ovo.csv", "iznamljivanja.csv");

            FXMLLoader loader = new FXMLLoader(getClass().getResource("Main.fxml"));
            Parent root = loader.load();
            Controller controller = loader.getController();

            // Create a test user and rental data
            User user = new User("test");
            LocalDateTime start = LocalDateTime.of(2024, Month.JULY, 4, 0, 0);
            Location loc = new Location(2, 5);
            Location locc = new Location(10, 10);
            LocalDate purchaseDate = LocalDate.of(2024, Month.JULY, 4);
            Car auto = new Car("a", "a", "a", 50, 2, purchaseDate, "opis");
            Rent rent = new Rent(user, start, loc, locc, 1, auto, true, false);

            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
            primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("newLogo.jpg")));
            primaryStage.setTitle("E-mobility");
            primaryStage.setScene(scene);
            primaryStage.show();

            // Handle application close request
            primaryStage.setOnCloseRequest(event -> {
                event.consume();
                logout(primaryStage);
            });

        } catch (OutOfRadiusException e) {
            System.out.println("Invalid location");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Prompts the user for confirmation before logging out of the application.
     *
     * @param stage the primary stage of the application
     */
    public void logout(Stage stage) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Logout");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to logout?");

        if (alert.showAndWait().get() == ButtonType.OK) {
            System.out.println("logout");
            stage.close();
        }
    }

    /**
     * The entry point for the JavaFX application.
     *
     * @param args command line arguments for the application
     */
    public static void main(String[] args) {
        launch(args);
    }
}
