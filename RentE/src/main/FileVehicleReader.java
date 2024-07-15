package main;


import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileVehicleReader {
    String COMMA_DELIMITER = ",";
    public FileVehicleReader(String location){
		List<List<String>> records = new ArrayList<>();
		BufferedReader br = null;
		try  {
			List<String> cars = new ArrayList<>();
			br = new BufferedReader(new FileReader(location));
		    String line;
		    while ((line = br.readLine()) != null) {
		        String[] values = line.split(COMMA_DELIMITER);
		        if()
		        records.add(Arrays.asList(values));
		    }
		    
		    System.out.println(records);
		}catch(Exception e) {
			e.printStackTrace();
		}
    }
}
