/**
 * The RentE module.
 * 
 * This module requires JavaFX libraries for building the user interface
 * and handling graphics.
 */
module RentE {
    /**
     * Requires the JavaFX controls library for UI controls.
     */
    requires javafx.controls;

    /**
     * Requires the JavaFX graphics library for rendering graphics.
     */
    requires javafx.graphics;

    /**
     * Requires the JavaFX FXML library for loading FXML files.
     */
    requires javafx.fxml;

    /**
     * Requires the JavaFX base library for core JavaFX functionalities.
     */
    requires javafx.base;

    /**
     * Opens the application package to JavaFX graphics and FXML,
     * allowing these frameworks to access its classes and methods.
     */
    opens application to javafx.graphics, javafx.fxml;

    /**
     * Opens the vehicles package to the JavaFX base module,
     * allowing reflection on its classes and methods.
     */
    opens vehicles to javafx.base;

    /**
     * Opens the reports package to the JavaFX base module,
     * allowing reflection on its classes and methods.
     */
    opens reports to javafx.base;
}
