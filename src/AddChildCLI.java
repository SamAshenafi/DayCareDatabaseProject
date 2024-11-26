import java.sql.*;
import java.util.Scanner;

public class AddChildCLI {
    public static void main(String[] args) {
        String url = "jdbc:sqlite:daycare.db";
        try (Connection conn = DriverManager.getConnection(url)) {
            Scanner scanner = new Scanner(System.in);

            // Input child's details
            System.out.print("Enter child's name: ");
            String name = scanner.nextLine();

            System.out.print("Enter child's age: ");
            int age = scanner.nextInt();

            System.out.print("Enter parent ID: ");
            int parentId = scanner.nextInt();
            scanner.nextLine(); // Consume the newline

            System.out.print("Enter child's allergies (or None): ");
            String allergies = scanner.nextLine();

            System.out.print("Enter child's medical conditions (or None): ");
            String medicalConditions = scanner.nextLine();

            System.out.print("Enter class group ID: ");
            int groupId = scanner.nextInt();

            // Insert data into the database
            String sql = "INSERT INTO Children (Name, Age, ParentID, Allergies, MedicalConditions, GroupID) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, name);
                pstmt.setInt(2, age);
                pstmt.setInt(3, parentId);
                pstmt.setString(4, allergies);
                pstmt.setString(5, medicalConditions);
                pstmt.setInt(6, groupId);
                pstmt.executeUpdate();
                System.out.println("Child added successfully.");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
