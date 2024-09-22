package main;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MyDateParser {

	public LocalDateTime parseLocalDateTime(String dateStr) {
		if (dateStr.endsWith(".")) {
	        dateStr = dateStr.substring(0, dateStr.length() - 1);
	    }
        DateTimeFormatter[] formatters = {
            DateTimeFormatter.ofPattern("d.M.yyyy HH:mm"),
            DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"),
            
        };
        for (DateTimeFormatter formatter : formatters) {
            try {
                return LocalDateTime.parse(dateStr, formatter);
            } catch (Exception e) {
                
            }
        }
        throw new IllegalArgumentException("Date format not supported for: " + dateStr);
    }
	public LocalDate parseLocalDate(String dateStr) {
		if (dateStr.endsWith(".")) {
	        dateStr = dateStr.substring(0, dateStr.length() - 1);
	    }
        DateTimeFormatter[] formatters = {
            DateTimeFormatter.ofPattern("d.M.yyyy"),
            DateTimeFormatter.ofPattern("dd.MM.yyyy"),
            
        };
        for (DateTimeFormatter formatter : formatters) {
            try {
                return LocalDate.parse(dateStr, formatter);
            } catch (Exception e) {
                
            }
        }
        throw new IllegalArgumentException("Date format not supported for: " + dateStr);
    }
}
