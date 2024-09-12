package main;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DailyEarnings {
    private LocalDate date;
    private List<Double> earningsList;
    private double totalEarnings;

    public DailyEarnings(LocalDate date) {
        this.date = date;
        this.earningsList = new ArrayList<>();
        this.totalEarnings = 0.0;
    }

    // Method to add earnings and update the total sum
    public void addEarnings(double value) {
        earningsList.add(value);
        totalEarnings += value;
    }

    public LocalDate getDate() {
        return date;
    }

    public List<Double> getEarningsList() {
        return earningsList;
    }

    public double getTotalEarnings() {
        return totalEarnings;
    }

    @Override
    public String toString() {
        return "Date: " + date + ", Total Earnings: " + totalEarnings + ", Earnings List: " + earningsList;
    }
}