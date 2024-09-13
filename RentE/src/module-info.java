module RentE {
	requires javafx.controls;
	requires javafx.graphics;
	requires javafx.fxml;
	requires javafx.base;
	
	opens application to javafx.graphics, javafx.fxml;
	opens Vehicles to javafx.base;
	opens main to javafx.base;
	

    
}
