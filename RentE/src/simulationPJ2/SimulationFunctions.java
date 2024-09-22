package simulationPJ2;

import java.time.LocalDateTime;
import java.util.List;

import location.Location;
import location.OutOfRadiusException;
import myUtility.MyDateParser;
import rental.Rent;
import rental.User;
import reports.FileLoadData;
import vehicles.Vehicle;
import vehicles.VehicleFunctions;

/**
 * This class contains various utility methods used in the vehicle rental simulation, such as generating invoices,
 * calculating totals, loading rents from files, and creating rent instances.
 */
public class SimulationFunctions {

    /**
     * Generates invoices for each rent in the provided list of rentals.
     * 
     * @param rentedList An array of Rent objects for which invoices need to be generated.
     */
    public void generateInvoices(Rent[] rentedList) {
        int i = 0;
        for (Rent rent : rentedList) {
            rent.generateInvoice(String.valueOf(i++));
        }
    }

    /**
     * Calculates and prints total profit, promotions, number of discounts, and total discount from the list of rents.
     * 
     * @param rentedList An array of Rent objects for which totals need to be calculated.
     */
    public void calculateTotals(Rent[] rentedList) {
        double totalProfit = 0;
        double totalPromotion = 0;
        double totalNumDiscount = 0;
        double totalDiscount = 0;

        for (Rent rent : rentedList) {
            rent.calculatePrice();
            totalProfit += rent.getTotal();
            totalPromotion += rent.getPromo();
            totalNumDiscount += rent.getDiscount();
            totalDiscount += rent.getTotalDiscount();
        }

        System.out.println("Total Profit: " + totalProfit);
        System.out.println("Total Promotion: " + totalPromotion);
        System.out.println("Total Number Discount: " + totalNumDiscount);
        System.out.println("Total Discount: " + totalDiscount);
    }

    /**
     * Loads the rent data from a FileLoadData instance and creates an array of Rent objects.
     * 
     * @param fileData    A FileLoadData instance containing sorted rental data.
     * @param dp          An instance of MyDateParser to parse date and time information.
     * @param vehicleList An array of available vehicles for the rental process.
     * @return An array of Rent objects created from the loaded file data.
     * @throws OutOfRadiusException if the rental location is out of the allowed radius.
     */
    public Rent[] loadRents(FileLoadData fileData, MyDateParser dp, Vehicle[] vehicleList) throws OutOfRadiusException {
        Rent[] rentedList = new Rent[fileData.getSortedList().size()];
        for (int i = 0; i < fileData.getSortedList().size(); i++) {
            List<String> data = fileData.getSortedList().get(i);
            rentedList[i] = createRent(data, dp, vehicleList);
        }
        return rentedList;
    }

    /**
     * Creates a Rent object from the provided rental data.
     * 
     * @param data        A list of strings containing rental information.
     * @param dp          An instance of MyDateParser to parse date and time information.
     * @param vehicleList An array of vehicles to match with the rental.
     * @return A Rent object created from the data provided, or null if the vehicle is not found.
     * @throws OutOfRadiusException if the rental location is out of the allowed radius.
     */
    public Rent createRent(List<String> data, MyDateParser dp, Vehicle[] vehicleList) throws OutOfRadiusException {
        LocalDateTime dateTime = dp.parseLocalDateTime(data.get(0));
        User user = new User(data.get(1));
        String vehicleId = data.get(2);
        Location locationStart = new Location(data.get(3), data.get(4));
        Location locationEnd = new Location(data.get(5), data.get(6));
        int timeUsed = Integer.parseInt(data.get(7));
        boolean malf = data.get(8).equals("da");
        boolean promotion = data.get(9).equals("da");

        VehicleFunctions veh = new VehicleFunctions();
        Vehicle vehicle = veh.findVehicleById(vehicleId, vehicleList);

        if (vehicle != null) {
            synchronized (vehicle) {
                return new Rent(user, dateTime, locationStart, locationEnd, timeUsed, vehicle, promotion, malf);
            }
        }
        return null;
    }
}
