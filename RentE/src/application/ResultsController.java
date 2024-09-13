package application;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import SimulationPJ2.SimulationFunctions;
import Vehicles.Vehicle;
import Vehicles.VehicleFunctions;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import main.BusinessResultsReport;
import main.DailyEarnings;
import main.FileDeleter;
import main.FileLoadData;
import main.MyDateParser;
import main.OutOfRadiusException;
import main.Rent;
import main.TableResultsData;

public class ResultsController {
	@FXML
	AnchorPane pane;
	@FXML
	private AnchorPane scenePane;
	@FXML Button backButton;
	private MapController mapController;
	BusinessResultsReport report;
    private Stage stage;
	private Scene scene;
	private Parent root;
	private DatePicker datePicker;
	private TableView<TableResultsData> tableView;
	public void setReport(BusinessResultsReport report) {
		this.report = report;
	}
	
	public void open() {

		
		// Create TextField instances
        TextField textTotalEarnings = new TextField("Total Earnings: " + report.getTotalEarnings());
        TextField textTotalDiscount = new TextField("Total Discount: " + report.getTotalDiscount());
        TextField textTotalPromotionDiscount = new TextField("Total Promotion Discount: " + report.getTotalDiscountPromotion());
        TextField textTotalMaintenance = new TextField("Total Maintenance: " + report.getTotalMaintenancePrice());
        TextField textTotalRepair = new TextField("Total Repair: " + report.getTotalRepairPrice());
        TextField textTotalExpenses = new TextField("Total Expenses: " + report.getTotalExpenses());
        TextField textTotalTax = new TextField("Total Tax: " + report.getTotalTax());
        TextField textInner = new TextField("Inner City Travel: " + report.getCityInnerRentals());
        TextField textOuter = new TextField("Outer City Travel: " + report.getCityOuterRentals());

        // Set editable property to false for displaying text only
        textTotalEarnings.setEditable(false);
        textTotalDiscount.setEditable(false);
        textTotalPromotionDiscount.setEditable(false);
        textTotalMaintenance.setEditable(false);
        textTotalRepair.setEditable(false);
        textTotalExpenses.setEditable(false);
        textTotalTax.setEditable(false);
        textInner.setEditable(false);
        textOuter.setEditable(false);

        // Create a VBox and add TextField instances
        VBox vbox = new VBox(10); // 10px spacing between children
        vbox.setPadding(new Insets(15)); // Padding around VBox
        vbox.getChildren().addAll(
            textTotalEarnings,
            textTotalDiscount,
            textTotalPromotionDiscount,
            textTotalMaintenance,
            textTotalRepair,
            textTotalExpenses,
            textTotalTax,
            textInner,
            textOuter
        );
        AnchorPane.setTopAnchor(vbox, 20.0);
	    AnchorPane.setLeftAnchor(vbox, 20.0);
	    AnchorPane.setRightAnchor(vbox, 1025.0);
	    AnchorPane.setBottomAnchor(vbox, 80.0);
	    
	    List<TableResultsData> dataList = new ArrayList<>();
	    for(int j = 0; j < report.getDailyEarnings().size(); j++) {
		    for(int i = 0; i < report.getDailyEarnings().get(j).getEarningsList().size(); i++) {
		    	dataList.add(new TableResultsData(report.getDailyEarnings().get(j).getDate(),report.getDailyEarnings().get(j).getEarningsList().get(i),
		    			report.getDailyDiscount().get(j).getEarningsList().get(i),
		    			report.getDailyDiscountPromotion().get(j).getEarningsList().get(i),
		    			report.getDailyDiscountQuantity().get(j).getEarningsList().get(i),
		    			report.getDailyMaintenancePrice().get(j).getEarningsList().get(i),
		    			report.getDailyRepairPrice().get(j).getEarningsList().get(i)));
		    	System.out.println(report.getDailyRepairPrice().get(j).getEarningsList().get(i));
		    }
	    
	    }
	    
//	    TableView<TableResultsData> tableView = new TableView<>();
//
//        TableColumn<TableResultsData, LocalDate> dateColumn = new TableColumn<>("Date");
//        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
//
//        TableColumn<TableResultsData, Double> earningsColumn = new TableColumn<>("Earnings");
//        earningsColumn.setCellValueFactory(new PropertyValueFactory<>("dailyEarnings"));
//
//        TableColumn<TableResultsData, Double> discountColumn = new TableColumn<>("Discount");
//        discountColumn.setCellValueFactory(new PropertyValueFactory<>("dailyDiscount"));
//
//        TableColumn<TableResultsData, Double> discountPromotionColumn = new TableColumn<>("Discount Promotion");
//        discountPromotionColumn.setCellValueFactory(new PropertyValueFactory<>("dailyDiscountPromotion"));
//
//        TableColumn<TableResultsData, Double> discountQuantityColumn = new TableColumn<>("Discount Quantity");
//        discountQuantityColumn.setCellValueFactory(new PropertyValueFactory<>("dailyDiscountQuantity"));
//
//        TableColumn<TableResultsData, Double> maintenancePriceColumn = new TableColumn<>("Maintenance Price");
//        maintenancePriceColumn.setCellValueFactory(new PropertyValueFactory<>("dailyMaintenancePrice"));
//
//        TableColumn<TableResultsData, Double> repairPriceColumn = new TableColumn<>("Repair Price");
//        repairPriceColumn.setCellValueFactory(new PropertyValueFactory<>("dailyRepairPrice"));
//
//        // Add columns to TableView
//        tableView.getColumns().addAll(dateColumn, earningsColumn, discountColumn, discountPromotionColumn, discountQuantityColumn, maintenancePriceColumn, repairPriceColumn);
//	    
//        ObservableList<TableResultsData> observableDataList = FXCollections.observableArrayList(dataList);
//
//        // Set the data to the TableView
//        tableView.setItems(observableDataList);
//	    
//        VBox newVbox = new VBox(tableView);
//        AnchorPane.setTopAnchor(newVbox, 20.0);
//	    AnchorPane.setLeftAnchor(newVbox, 350.0);
//	    AnchorPane.setRightAnchor(newVbox, 20.0);
//	    AnchorPane.setBottomAnchor(newVbox, 80.0);
	    
	    
	    scenePane.getChildren().addAll(vbox);
	    this.showDailyTable();
		
	}
	public void showDailyTable() {
		datePicker = new DatePicker();
		List<LocalDate> availableDates = this.getAvailableDates();
		
		datePicker.setDayCellFactory(picker -> new DateCell() {
			
			@Override
			public void updateItem(LocalDate date, boolean empty) {
				super.updateItem(date, empty);
				if(date == null || !availableDates.contains(date)) {
					setDisable(true);
					setStyle("-fx-background-color: #ffc0cb;");
				}
			}
		});
		
		datePicker.setOnAction(e -> updateTableView());
		tableView = new TableView<>();
		setupTableColumns();
		
        VBox vbox = new VBox(datePicker, tableView);
        vbox.setPadding(new Insets(10));
        vbox.setSpacing(8);
        AnchorPane.setTopAnchor(vbox, 20.0);
	    AnchorPane.setLeftAnchor(vbox, 350.0);
	    AnchorPane.setRightAnchor(vbox, 20.0);
	    AnchorPane.setBottomAnchor(vbox, 80.0);
	    scenePane.getChildren().addAll(vbox);

		
	}
	private void setupTableColumns() {
	    TableColumn<TableResultsData, Double> earningsColumn = new TableColumn<>("Daily Earnings");
	    earningsColumn.setCellValueFactory(new PropertyValueFactory<>("dailyEarnings"));

	    TableColumn<TableResultsData, Double> discountPromotionColumn = new TableColumn<>("Discount Promotion");
	    discountPromotionColumn.setCellValueFactory(new PropertyValueFactory<>("dailyDiscountPromotion"));

	    TableColumn<TableResultsData, Double> discountColumn = new TableColumn<>("Discount");
	    discountColumn.setCellValueFactory(new PropertyValueFactory<>("dailyDiscount"));

	    TableColumn<TableResultsData, Double> discountQuantityColumn = new TableColumn<>("Discount Quantity");
	    discountQuantityColumn.setCellValueFactory(new PropertyValueFactory<>("dailyDiscountQuantity"));

	    TableColumn<TableResultsData, Double> maintenancePriceColumn = new TableColumn<>("Maintenance Price");
	    maintenancePriceColumn.setCellValueFactory(new PropertyValueFactory<>("dailyMaintenancePrice"));

	    TableColumn<TableResultsData, Double> repairPriceColumn = new TableColumn<>("Repair Price");
	    repairPriceColumn.setCellValueFactory(new PropertyValueFactory<>("dailyRepairPrice"));

	    tableView.getColumns().addAll(earningsColumn, discountPromotionColumn, discountColumn, 
	                                  discountQuantityColumn, maintenancePriceColumn, repairPriceColumn);
	}

	private void updateTableView() {
		
		LocalDate  selectedDate = datePicker.getValue();
		if(selectedDate != null) {
			ObservableList<TableResultsData> data = getResultDataForDate(selectedDate);
			tableView.setItems(data);
		}
	}

	private ObservableList<TableResultsData> getResultDataForDate(LocalDate selectedDate) {
		List<TableResultsData> dataList = loadData();
		// Filter data based on the selected date
	    List<TableResultsData> filteredData = new ArrayList<>();
	    for (TableResultsData res : dataList) {
	        if (res.getDate().equals(selectedDate)) {
	            filteredData.add(res);
	        }
	    }

	    // Return the filtered data as an ObservableList
	    return FXCollections.observableArrayList(filteredData);
				
	}
	private List<LocalDate> getAvailableDates(){
		
		List<TableResultsData> dataList = loadData();
		
		List<LocalDate> list = new ArrayList<>();
		
		for (TableResultsData res : dataList) {
	        list.add(res.getDate());
	    }
		
		return list;
	}
	
	private List<TableResultsData> loadData(){
		List<TableResultsData> dataList = new ArrayList<>();
	    
		for(int j = 0; j < report.getDailyEarnings().size(); j++) {
		    for(int i = 0; i < report.getDailyEarnings().get(j).getEarningsList().size(); i++) {
		    	dataList.add(new TableResultsData(report.getDailyEarnings().get(j).getDate(),report.getDailyEarnings().get(j).getEarningsList().get(i),
		    			report.getDailyDiscount().get(j).getEarningsList().get(i),
		    			report.getDailyDiscountPromotion().get(j).getEarningsList().get(i),
		    			report.getDailyDiscountQuantity().get(j).getEarningsList().get(i),
		    			report.getDailyMaintenancePrice().get(j).getEarningsList().get(i),
		    			report.getDailyRepairPrice().get(j).getEarningsList().get(i)));
		    }
	    }
		return dataList;
	}
	@FXML
	public void goBackToMap(ActionEvent event)throws IOException, OutOfRadiusException{
		FileDeleter.deleteFileIfExists("malfunctionList.txt");
		
        initializeMap(event);

        MyDateParser dp = new MyDateParser();
        
        FileLoadData fileData = new FileLoadData("ovo.csv", "rented.csv");
        
        SimulationFunctions sim  = new SimulationFunctions();

        VehicleFunctions veh = new VehicleFunctions();
        Vehicle[] vehicleList = veh.loadVehicles(fileData, dp);
        Rent[] rentedList = sim.loadRents(fileData, dp, vehicleList);
        BusinessResultsReport rep  = new BusinessResultsReport();
        for(int i = 0; i <   rentedList.length; i++) {
        	rep.dailyReport(rentedList[i].getDateTime().toLocalDate(), rentedList[i].getFullPrice(), rentedList[i].getTotalDiscount(), rentedList[i].getPromo(), rentedList[i].getDiscount(), rentedList[i].getIsInnerCity(), rentedList[i].getHadMalfunction(), rentedList[i].getVehicle());
        }
        System.out.println(rep);
        sim.generateInvoices(rentedList);
        //sim.calculateTotals(rentedList);
        
        
        mapController.loadData(fileData, vehicleList, rentedList, rep);
	}

	

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
	
	public void closeProgram(ActionEvent event) {
		Utility.closeProgram(stage,scenePane);
	}
	
}
