package cli;

import java.sql.*;
import java.util.Scanner;

public class ViewAttendanceCLI {
    public static void main(String[] args) {
        String url = "jdbc:sqlite:daycare.db";

        try (Connection conn = DriverManager.getConnection(url)) {
            Scanner scanner = new Scanner(System.in);

            // Filter Specific ChildID
            System.out.print("Enter ChildID to view attendance (or 0 to skip): ");
            int childId = scanner.nextInt();
            scanner.nextLine();

            System.out.print("Enter date to view attendance (YYYY-MM-DD, or leave blank to see all): ");
            String date = scanner.nextLine();

            // SQL to fetch attendance 
            String sql = "SELECT * FROM Attendance WHERE 1=1";
            if (childId > 0) sql += " AND ChildID = ?";
            if (!date.isEmpty()) sql += " AND Date = ?";

            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                int paramIndex = 1;
                if (childId > 0) {
                    pstmt.setInt(paramIndex++, childId);
                }
                if (!date.isEmpty()) {
                    pstmt.setString(paramIndex, date);
                }

                try (ResultSet rs = pstmt.executeQuery()) {
                    if (!rs.isBeforeFirst()) {
                        System.out.println("No attendance records found for the given criteria.");
                    } else {
                        while (rs.next()) {
                            System.out.printf("AttendanceID: %d, ChildID: %d, Date: %s, Arrival: %s, Departure: %s%n",
                                    rs.getInt("AttendanceID"), rs.getInt("ChildID"), rs.getString("Date"),
                                    rs.getString("ArrivalTime"), rs.getString("DepartureTime"));
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
