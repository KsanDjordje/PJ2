package SimulationPJ2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
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
		MyDateParser dp = new MyDateParser();
	    List<List<String>> tempSortedRentedList = new ArrayList<>(this.sortedRentedList);
	    
	    for (List<String> record : tempSortedRentedList) {
	        for (List<String> veh : vehicles) {
	            if (record.get(2).equals(veh.get(0))) { 
	                result.add(record);
	                break; // If match is found break inner loop
	            }
	        }
	    }
	    tempSortedRentedList = result;
        Iterator<List<String>> iterator = tempSortedRentedList.iterator();

        while(iterator.hasNext()) {
        	List<String> record = iterator.next();
        	LocalDateTime recordTime = dp.parseLocalDateTime(record.get(0));
        	String recordID = record.get(2);
        	double recordDuration = Double.parseDouble(record.get(7));
        	// this is if we count the time in the rented file list as minutes...
        	LocalDateTime timeFinal = recordTime.plusMinutes((long) recordDuration);
        	boolean shouldRemove = false;
        	
        	for (List<String> otherRecord : sortedRentedList) {
                if (record == otherRecord) continue;
        	
                LocalDateTime otherRecord0 = dp.parseLocalDateTime(otherRecord.get(0));
                String otherRecord2 = otherRecord.get(2);
                double otherRecord8 = Double.parseDouble(otherRecord.get(7));
                
                LocalDateTime otherRecordTimeWithMinutesAdded = otherRecord0.plusMinutes((long) otherRecord8);
                
                if ((((recordTime.equals(otherRecord0) && recordID.equals(otherRecord2)) &&
                    timeFinal.isAfter(otherRecordTimeWithMinutesAdded))) || ((recordTime.equals(otherRecord0) && recordID.equals(otherRecord2)) &&
                    timeFinal.equals(otherRecordTimeWithMinutesAdded))) {
                    shouldRemove = true;
                    break;
                }
            }
            
            if (shouldRemove) {
                iterator.remove();
            }
        }
        
        
	    
	    return tempSortedRentedList;
    }
	
}
