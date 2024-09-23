package reports;

import java.util.ArrayList;
import java.util.List;

import myUtility.MyDateParser;
import vehicles.Bicycle;
import vehicles.Car;
import vehicles.Scooter;
import vehicles.Vehicle;
import vehicles.VehicleFunctions;

/**
 * Class to manage and track vehicle earnings for different types of vehicles.
 */
public class VehicleEarnings {
    
    private static VehicleEarned bestCar;
    private static VehicleEarned bestBike;
    private static VehicleEarned bestScooter;  

    List<VehicleEarned> carEarnings = new ArrayList<>();
    List<VehicleEarned> bikeEarnings = new ArrayList<>(); 
    List<VehicleEarned> scooterEarnings = new ArrayList<>();
    
    /**
     * Constructs a VehicleEarnings object and loads vehicle data.
     *
     * @param data the data to load vehicle information from
     */
    public VehicleEarnings(FileLoadData data) {
        MyDateParser dp = new MyDateParser();
        VehicleFunctions veh = new VehicleFunctions();
        Vehicle[] vehicleList = veh.loadVehicles(data, dp);

        for (int i = 0; i < vehicleList.length; i++) {
            if (vehicleList[i] instanceof Car) {
                carEarnings.add(new VehicleEarned(vehicleList[i]));
            } else if (vehicleList[i] instanceof Bicycle) {
                bikeEarnings.add(new VehicleEarned(vehicleList[i]));
            } else if (vehicleList[i] instanceof Scooter) {
                scooterEarnings.add(new VehicleEarned(vehicleList[i]));
            }
        }
    }
    
    /**
     * Updates the earnings of a specific vehicle.
     *
     * @param veh the vehicle to update
     * @param value the amount earned
     */
    public void vehicleEarned(Vehicle veh, Double value) {
        String type = veh.getClass().getSimpleName();

        if (veh instanceof Car) {
            for (VehicleEarned ve : carEarnings) {
                if (ve.getVehicle().getId().equals(veh.getId()))
                    ve.earned(value);
            }
        } else if (veh instanceof Bicycle) {
            for (VehicleEarned ve : bikeEarnings) {
                if (ve.getVehicle().getId().equals(veh.getId()))
                    ve.earned(value);
            }
        } else if (veh instanceof Scooter) {
            for (VehicleEarned ve : scooterEarnings) {
                if (ve.getVehicle().getId().equals(veh.getId()))
                    ve.earned(value);
            }
        } else {
            System.out.println("Unknown vehicle type.");
        }
    }
    
    /**
     * Retrieves the best earning vehicles across all types.
     *
     * @return a list of the best earning vehicles, or null if no earnings exist
     */
    public List<VehicleEarned> getBestEarningVehicles() {
        List<VehicleEarned> result = new ArrayList<>();
        VehicleEarned car = null;
        Double carBest = 0.0;
        VehicleEarned bike = null;
        Double bikeBest = 0.0;
        VehicleEarned scooter = null;
        Double scooterBest = 0.0;

        for (VehicleEarned ve : carEarnings) {
            if (ve.getMoneyEarned() > carBest) {
                carBest = ve.getMoneyEarned();
                car = ve;
            }
        }

        for (VehicleEarned ve : bikeEarnings) {
            if (ve.getMoneyEarned() > bikeBest) {
                bikeBest = ve.getMoneyEarned();
                bike = ve;
            }
        }

        for (VehicleEarned ve : scooterEarnings) {
            if (ve.getMoneyEarned() > scooterBest) {
                scooterBest = ve.getMoneyEarned();
                scooter = ve;
            }
        }
        
        if (car != null && bike != null && scooter != null) {
            this.bestCar = car;
            result.add(car);
            this.bestBike = bike;
            result.add(bike);
            this.bestScooter = scooter;
            result.add(scooter);
            return result;
        } else {
            System.out.println("Vehicle Earned list is null");
            return null;
        }
    }

    /**
     * Gets the best earning car.
     *
     * @return the best earning car
     */
    public static VehicleEarned getBestCar() {
        return bestCar;
    }

    /**
     * Gets the best earning bicycle.
     *
     * @return the best earning bicycle
     */
    public static VehicleEarned getBestBike() {
        return bestBike;
    }

    /**
     * Gets the best earning scooter.
     *
     * @return the best earning scooter
     */
    public static VehicleEarned getBestScooter() {
        return bestScooter;
    }
}
