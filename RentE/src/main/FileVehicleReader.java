package main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileVehicleReader {
    String COMMA_DELIMITER = ",";
    private List<String[]> cars = new ArrayList<>();
    private List<String[]> bikes = new ArrayList<>();
    private List<String[]> scooters = new ArrayList<>();

    public FileVehicleReader(String location){
		List<List<String>> records = new ArrayList<>();
		BufferedReader br = null;
		try  {

			
			br = new BufferedReader(new FileReader(location));
		    String line;
		    while ((line = br.readLine()) != null) {
		        String[] values = line.split(COMMA_DELIMITER);
		        char[] temp = new char[1];
		        values[0].getChars(0, 1, temp, 0);
		        if(temp[0] == 'A') {
		            this.cars.add(Arrays.copyOf(values, values.length));
		        }else if(temp[0] == 'B') {
		            this.bikes.add(Arrays.copyOf(values, values.length));
		        }else if(temp[0]== 'T') {
		            this.scooters.add(Arrays.copyOf(values, values.length));

		        }
		        records.add(Arrays.asList(values));
		    }

		}catch(Exception e) {
			e.printStackTrace();
		}
    }

	public List<String[]> getCars() {
		return cars;
	}

	public List<String[]> getBikes() {
		return bikes;
	}

	public List<String[]> getScooters() {
		return scooters;
	}
}
