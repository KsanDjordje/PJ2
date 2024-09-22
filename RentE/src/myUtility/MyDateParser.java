package myUtility;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Utility class for parsing date and time strings into LocalDate and LocalDateTime objects.
 */
public class MyDateParser {

    /**
     * Parses a string representation of date and time into a LocalDateTime object.
     *
     * @param dateStr the string representation of the date and time
     * @return the parsed LocalDateTime object
     * @throws IllegalArgumentException if the date format is not supported
     */
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
                // Ignore and try the next formatter
            }
        }
        throw new IllegalArgumentException("Date format not supported for: " + dateStr);
    }

    /**
     * Parses a string representation of a date into a LocalDate object.
     *
     * @param dateStr the string representation of the date
     * @return the parsed LocalDate object
     * @throws IllegalArgumentException if the date format is not supported
     */
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
                // Ignore and try the next formatter
            }
        }
        throw new IllegalArgumentException("Date format not supported for: " + dateStr);
    }
}
