package Project;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class ParkingReservation extends ParkingBlocks implements UserAuthentication {
    private String studentId;

    // Text fields and buttons for login panel
    private JTextField txtStudentId = new JTextField(10);
    private JPasswordField txtPassword = new JPasswordField(10);
    private JButton btnLogin = new JButton("Login");
    private JButton btnClear = new JButton("Clear");

    // Text fields and buttons for reservation panel
    private JTextField txtFloorNumber = new JTextField(5);
    private JTextField txtSpaceNumber = new JTextField(5);
    private JButton btnReserve = new JButton("Reserve Space");
    private JButton btnCheckSummary = new JButton("Check Summary");
    private JButton btnPrintTicket = new JButton("Print Ticket");
    private JButton btnCheckAvailability = new JButton("Check Availability");
    private JButton btnClearInput = new JButton("Clear Input");
    private JButton btnResetDatabase = new JButton("Reset Database"); // Button to reset the database

    private ReservationDatabase database; // Database instance

    // Constructor to initialize the reservation system
    public ParkingReservation() {
        super("Parking Building", 40); // Initialize with block name and total spaces
        this.database = new ReservationDatabase();

        // Reset the database daily
        resetDatabaseDaily();

        // Login panel setup
        JPanel loginPanel = new JPanel(new GridLayout(3, 2));
        loginPanel.add(new JLabel("Student ID:"));
        loginPanel.add(txtStudentId);
        loginPanel.add(new JLabel("Password:"));
        loginPanel.add(txtPassword);
        loginPanel.add(btnLogin);
        loginPanel.add(btnClear);

        btnLogin.addActionListener(e -> login());
        btnClear.addActionListener(e -> clear());

        JFrame loginFrame = new JFrame("Login");
        loginFrame.add(loginPanel);
        loginFrame.setSize(300, 150);
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setVisible(true);
    }

    // Method to handle login
    private void login() {
        String studentId = txtStudentId.getText();
        String password = new String(txtPassword.getPassword());

        if (authenticate(studentId, password)) {
            this.studentId = studentId;
            showReservationPanel();
        } else {
            JOptionPane.showMessageDialog(null, "Invalid student ID or password", "Login Failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Method to clear login inputs
    private void clear() {
        txtStudentId.setText("");
        txtPassword.setText("");
    }

    // Method to display reservation panel
    private void showReservationPanel() {
        JFrame reservationFrame = new JFrame("Assumption University Parking Reservation System");
        JPanel reservationPanel = new JPanel(new GridLayout(7, 2)); // Increased row count for new button
        reservationPanel.add(new JLabel("Welcome, " + studentId + "!"));
        reservationPanel.add(new JLabel(""));
        reservationPanel.add(new JLabel("Floor Number:"));
        reservationPanel.add(txtFloorNumber);
        reservationPanel.add(new JLabel("Space Number:"));
        reservationPanel.add(txtSpaceNumber);
        reservationPanel.add(btnReserve);
        reservationPanel.add(new JLabel(""));
        reservationPanel.add(btnCheckSummary);
        reservationPanel.add(btnPrintTicket);
        reservationPanel.add(btnCheckAvailability);
        reservationPanel.add(btnClearInput);
        reservationPanel.add(btnResetDatabase); // Added button for resetting the database

        btnReserve.addActionListener(e -> reserveSpace(studentId, Integer.parseInt(txtFloorNumber.getText()), Integer.parseInt(txtSpaceNumber.getText())));
        btnCheckSummary.addActionListener(e -> displayReservationSummary(studentId));
        btnPrintTicket.addActionListener(e -> printTicket());
        btnCheckAvailability.addActionListener(e -> displaySpaces());
        btnClearInput.addActionListener(e -> clearInputs());
        btnResetDatabase.addActionListener(e -> resetDatabase()); // Action listener for reset database button

        reservationFrame.add(reservationPanel);
        reservationFrame.setSize(400, 250); // Increased height for new button
        reservationFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        reservationFrame.setVisible(true);
    }

    // Method to clear input fields
    private void clearInputs() {
        txtFloorNumber.setText("");
        txtSpaceNumber.setText("");
    }

    // Method to update space availability display
    private void updateSpaceAvailabilityDisplay() {
        // Update the display with current space availability
        StringBuilder spacesMessage = new StringBuilder("Space Availability:\n\n");
        for (int i = 0; i < 5; i++) {
            spacesMessage.append("Floor ").append(i + 1).append(": ");
            for (int j = 0; j < getTotalSpaces(); j++) {
                spacesMessage.append(getSpaceAvailability()[i][j] ? (j + 1) + " " : "X ");
            }
            spacesMessage.append("\n");
        }

        JOptionPane.showMessageDialog(null, spacesMessage.toString(), "Space Availability", JOptionPane.INFORMATION_MESSAGE);
    }

    // Method to handle reservation of a parking space
    @Override
    public void reserveSpace(String studentId, int floor, int spaceNumber) {
        if (floor < 1 || floor > 5 || spaceNumber < 1 || spaceNumber > getTotalSpaces()) {
            // Display a message for invalid floor or space number
            JOptionPane.showMessageDialog(null, "Invalid floor or space number", "Reservation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (getSpaceAvailability()[floor - 1][spaceNumber - 1]) {
            setSpaceAvailability(floor, spaceNumber, false);
            // Save reservation to the database
            database.saveReservation(studentId, floor, spaceNumber);
            // Successful reservation message without displaying an error
            JOptionPane.showMessageDialog(null, "Space reserved successfully!", "Reservation Success", JOptionPane.INFORMATION_MESSAGE);
        }

        // Update the space availability display
        updateSpaceAvailabilityDisplay();
    }

    // Method to display space availability
    @Override
    public void displaySpaces() {
        updateSpaceAvailabilityDisplay();
    }

    // Method to display reservation summary for a student
    @Override
    public void displayReservationSummary(String studentId) {
        // Retrieve reservations from the database
        List<String[]> reservations = database.getAllReservations();
        StringBuilder summaryMessage = new StringBuilder("Reservation Summary:\n\n");
        summaryMessage.append("Student ID: ").append(studentId).append("\n");
        summaryMessage.append("Reserved Spaces: ");
        for (String[] reservation : reservations) {
            if (reservation[0].equals(studentId)) {
                summaryMessage.append("Floor ").append(reservation[1]).append(", Space ").append(reservation[2]).append(" | ");
            }
        }

        JOptionPane.showMessageDialog(null, summaryMessage.toString(), "Reservation Summary", JOptionPane.INFORMATION_MESSAGE);
    }

    // Method to print a parking ticket
    private void printTicket() {
        JOptionPane.showMessageDialog(null, "Ticket printed. Cost: 30 baht", "Print Ticket", JOptionPane.INFORMATION_MESSAGE);
    }

    // Method to authenticate user
    @Override
    public boolean authenticate(String studentId, String password) {
        // Simple authentication, replace with your own logic
        return true;
    }

    // Method to reset the database
    private void resetDatabase() {
        database.resetDatabase();
        JOptionPane.showMessageDialog(null, "Database reset successful!", "Reset Database", JOptionPane.INFORMATION_MESSAGE);
    }

    // Method to reset the database daily
    private void resetDatabaseDaily() {
        // Reset the database if it's a new day
        LocalDate today = LocalDate.now();
        LocalDate lastResetDate = database.getLastResetDate();
        if (!today.isEqual(lastResetDate)) {
            database.resetDatabase();
            database.setLastResetDate(today);
        }
    }

    // Main method
    public static void main(String[] args) {
        new ParkingReservation();
    }
}
