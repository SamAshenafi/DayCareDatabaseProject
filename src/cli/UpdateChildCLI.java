package cli;

import java.sql.*;
import java.util.Scanner;

public class UpdateChildCLI {
    public static void main(String[] args) {
        String url = "jdbc:sqlite:daycare.db";
        try (Connection conn = DriverManager.getConnection(url)) {
            Scanner scanner = new Scanner(System.in);

            // Child ID
            System.out.print("Enter the ChildID of the child to update: ");
            int childId = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            // New Info
            System.out.print("Enter new name (or leave blank to keep current): ");
            String name = scanner.nextLine();
            // New Age
            System.out.print("Enter new age (or 0 to keep current): ");
            int age = scanner.nextInt();
            scanner.nextLine();
            // New Parent ID
            System.out.print("Enter new parent ID (or 0 to keep current): ");
            int parentId = scanner.nextInt();
            scanner.nextLine();
            // New Allergies
            System.out.print("Enter new allergies (or leave blank to keep current): ");
            String allergies = scanner.nextLine();
            // New Medical Conditions
            System.out.print("Enter new medical conditions (or leave blank to keep current): ");
            String medicalConditions = scanner.nextLine();

            // Update child information
            String sql = "UPDATE Children SET Name = COALESCE(?, Name), "
                       + "Age = COALESCE(NULLIF(?, 0), Age), "
                       + "ParentID = COALESCE(NULLIF(?, 0), ParentID), "
                       + "Allergies = COALESCE(?, Allergies), "
                       + "MedicalConditions = COALESCE(?, MedicalConditions) "
                       + "WHERE ChildID = ?";

            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, name.isEmpty() ? null : name); 
                pstmt.setInt(2, age); 
                pstmt.setInt(3, parentId);
                pstmt.setString(4, allergies.isEmpty() ? null : allergies); 
                pstmt.setString(5, medicalConditions.isEmpty() ? null : medicalConditions); 
                pstmt.setInt(6, childId);
                int rowsUpdated = pstmt.executeUpdate();

                if (rowsUpdated > 0) {
                    System.out.println("Child information updated successfully.");
                } else {
                    System.out.println("No child found with the specified ChildID.");
                }
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
