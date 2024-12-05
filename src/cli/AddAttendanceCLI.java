package cli;
import java.sql.*;
import java.util.Scanner;

public class AddAttendanceCLI {
    public static void main(String[] args) {
        String url = "jdbc:sqlite:daycare.db";
        try (Connection conn = DriverManager.getConnection(url)) {
            Scanner scanner = new Scanner(System.in);

            // Input attendance details
            System.out.print("Enter ChildID: ");
            int childId = scanner.nextInt();

            System.out.print("Enter date (YYYY-MM-DD): ");
            String date = scanner.next();

            System.out.print("Enter arrival time (HH:mm): ");
            String arrivalTime = scanner.next();

            System.out.print("Enter departure time (HH:mm): ");
            String departureTime = scanner.next();

            // Insert into database
            String sql = "INSERT INTO Attendance (ChildID, Date, ArrivalTime, DepartureTime) VALUES (?, ?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, childId);
                pstmt.setString(2, date);
                pstmt.setString(3, arrivalTime);
                pstmt.setString(4, departureTime);
                pstmt.executeUpdate();
                System.out.println("Attendance record added successfully.");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
