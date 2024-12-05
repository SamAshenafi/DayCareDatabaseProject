package cli;
import java.sql.*;
import java.util.Scanner;

public class DeleteChildCLI {
    public static void main(String[] args) {
        String url = "jdbc:sqlite:daycare.db";
        try (Connection conn = DriverManager.getConnection(url)) {
            Scanner scanner = new Scanner(System.in);

            //Ask for ChildID
            System.out.print("Enter the ChildID of the child to delete: ");
            int childId = scanner.nextInt();

            // Confirm The Deletions
            System.out.print("Are you sure you want to delete this child? (y/n): ");
            String confirm = scanner.next();
            if (!confirm.equalsIgnoreCase("y")) {
                System.out.println("Operation canceled.");
                return;
            }

            // SQL to delete child
            String sql = "DELETE FROM Children WHERE ChildID = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, childId);
                pstmt.executeUpdate();
                System.out.println("Child deleted successfully.");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
