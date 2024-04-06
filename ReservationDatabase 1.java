package Project;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReservationDatabase {
    private static final String DATABASE_FILE = "reservations.txt";
    private static final String LAST_RESET_DATE_FILE = "last_reset_date.txt";

    // Method to save reservation to the database file
    public void saveReservation(String studentId, int floor, int spaceNumber) {
        try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(DATABASE_FILE, true)))) {
            writer.println(studentId + "," + floor + "," + spaceNumber);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to retrieve all reservations from the database file
    public List<String[]> getAllReservations() {
        List<String[]> reservations = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(DATABASE_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                reservations.add(parts);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return reservations;
    }

    // Method to reset the database file
    public void resetDatabase() {
        try {
            new PrintWriter(DATABASE_FILE).close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    // Method to get the last reset date from file
    public LocalDate getLastResetDate() {
        try (BufferedReader reader = new BufferedReader(new FileReader(LAST_RESET_DATE_FILE))) {
            String dateString = reader.readLine();
            return LocalDate.parse(dateString);
        } catch (IOException e) {
            // Return a default value if file doesn't exist or there's an error reading it
            return LocalDate.MIN;
        }
    }

    // Method to set the last reset date to file
    public void setLastResetDate(LocalDate date) {
        try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(LAST_RESET_DATE_FILE)))) {
            writer.println(date.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

