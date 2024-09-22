package reports;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * The {@code DailyEarnings} class represents the earnings for a particular date.
 * It keeps track of individual earnings for that date and calculates the total earnings.
 */
public class DailyEarnings {

    /**
     * The date for which the earnings are recorded.
     */
    private LocalDate date;

    /**
     * The list of earnings values for the given date.
     */
    private List<Double> earningsList;

    /**
     * The total sum of earnings for the given date.
     */
    private double totalEarnings;

    /**
     * Constructs a {@code DailyEarnings} object with a specific date.
     * Initializes the list of earnings and the total earnings to zero.
     *
     * @param date the date for which earnings are recorded
     */
    public DailyEarnings(LocalDate date) {
        this.date = date;
        this.earningsList = new ArrayList<>();
        this.totalEarnings = 0.0;
    }

    /**
     * Adds a new earning value to the earnings list and updates the total earnings.
     *
     * @param value the earning value to be added
     */
    public void addEarnings(double value) {
        earningsList.add(value);
        totalEarnings += value;
    }

    /**
     * Returns the date for which the earnings are recorded.
     *
     * @return the date of the earnings
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Returns the list of individual earnings recorded for the date.
     *
     * @return the list of earnings
     */
    public List<Double> getEarningsList() {
        return earningsList;
    }

    /**
     * Returns the total sum of earnings for the date.
     *
     * @return the total earnings
     */
    public double getTotalEarnings() {
        return totalEarnings;
    }

    /**
     * Returns a string representation of the {@code DailyEarnings} object.
     *
     * @return a string in the format "Date: {date}, Total Earnings: {totalEarnings}, Earnings List: {earningsList}"
     */
    @Override
    public String toString() {
        return "Date: " + date + ", Total Earnings: " + totalEarnings + ", Earnings List: " + earningsList;
    }
}
