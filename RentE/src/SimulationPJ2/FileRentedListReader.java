package SimulationPJ2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import main.MyDateParser;


public class FileRentedListReader {

	
    String COMMA_DELIMITER = ",";
    private List<List<String>> rentedCars = new ArrayList<>();
    private List<List<String>> rentedBikes = new ArrayList<>();
    private List<List<String>> rentedScooters = new ArrayList<>();
    private List<List<String>> sortedRentedList = new ArrayList<>();

    public FileRentedListReader(String location){
		MyDateParser dp = new MyDateParser();
        List<List<String>> records = new ArrayList<>();
        Boolean isFirstRow = true;
        try (BufferedReader br = new BufferedReader(new FileReader(location))) {
            String line;
            while ((line = br.readLine()) != null) {
            	if (isFirstRow) {
                    isFirstRow = false; // Skip header row
                    continue;
                }
                String[] values = line.split(",");
                List<String> record = new ArrayList<>();
                for (String value : values) {
                    record.add(value.trim());
                }
                records.add(record);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        records.sort(Comparator.comparing(record -> dp.parseLocalDateTime(record.get(0))));
        for (List<String> record : records) {
            if (!record.isEmpty()) {
                char startChar = record.get(2).charAt(0);
                this.sortedRentedList.add(record);
                switch (startChar) {
                    case 'A':
                        rentedCars.add(record);
                        break;
                    case 'B':
                        rentedBikes.add(record);
                        break;
                    case 'T':
                        rentedScooters.add(record);
                        break;
                    default:
                        break;
                }
            }
        }

        
    }
    
    
    
    public List<List<String>> getSortedList(){
    	return sortedRentedList;
    }
	public List<List<String>> getCars() {
		return rentedCars;
	}

	public List<List<String>> getBikes() {
		return rentedBikes;
	}

	public List<List<String>> getScooters() {
		return rentedScooters;
	}
	public void setSortedList(List<List<String>> list){
    	this.sortedRentedList = list ;
    }
	public void setCars(List<List<String>> list) {
		this.rentedCars = list;
	}

	public void setBikes(List<List<String>> list) {
		this.rentedBikes = list ;
	}

	public  void setScooters(List<List<String>> list) {
		this.rentedScooters = list;
	}
	
	public List<List<String>> removeIncorrectData(List<List<String>> vehicles){
		List<List<String>> result = new ArrayList<>();
	    
	    List<List<String>> tempSortedRentedList = this.sortedRentedList;

	    for (List<String> record : tempSortedRentedList) {
	        for (List<String> veh : vehicles) {
	            if (record.get(2).equals(veh.get(0))) { 
	                result.add(record);
	                break; // If match is found break inner loop
	            }
	        }
	    }

	    return result;
    }
	
}
