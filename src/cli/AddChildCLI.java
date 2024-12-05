package cli;
import java.sql.*;
import java.util.Scanner;

public class AddChildCLI {
    public static void main(String[] args) {
        String url = "jdbc:sqlite:daycare.db";

        try (Connection conn = DriverManager.getConnection(url)) {
            Scanner scanner = new Scanner(System.in);

            // Input child's name
            System.out.print("Enter child's name: ");
            String name = scanner.nextLine();

            // Input and validate child's age
            int age;
            while (true) {
                System.out.print("Enter child's age (must be between 2 and 5): ");
                age = scanner.nextInt();
                if (age >= 2 && age <= 5) {
                    break;
                }
                System.out.println("Invalid age. Please enter an age between 2 and 5.");
            }

            // Parent selection or registration
            int parentId = 0;
            System.out.println("Would you like to select an existing parent or register a new one?");
            System.out.print("Enter 'list' to view existing parents, or leave blank to register a new parent: ");
            scanner.nextLine(); // Consume newline
            String choice = scanner.nextLine();

            if (choice.equalsIgnoreCase("list")) {
                // List existing parents
                System.out.println("Available parents:");
                String fetchParentsSQL = "SELECT ParentID, Name, Phone FROM Parents";
                try (Statement stmt = conn.createStatement();
                     ResultSet rs = stmt.executeQuery(fetchParentsSQL)) {
                    while (rs.next()) {
                        System.out.printf("ParentID: %d, Name: %s, Phone: %s%n",
                                rs.getInt("ParentID"), rs.getString("Name"), rs.getString("Phone"));
                    }
                }

                // Prompt user to select a ParentID
                while (true) {
                    System.out.print("Enter ParentID: ");
                    parentId = scanner.nextInt();
                    scanner.nextLine(); // Consume newline

                    // Validate selected ParentID exists
                    String validateParentSQL = "SELECT COUNT(*) AS Count FROM Parents WHERE ParentID = ?";
                    try (PreparedStatement pstmt = conn.prepareStatement(validateParentSQL)) {
                        pstmt.setInt(1, parentId);
                        try (ResultSet rs = pstmt.executeQuery()) {
                            if (rs.getInt("Count") > 0) {
                                break; // Valid ParentID
                            }
                        }
                    }
                    System.out.println("Invalid ParentID. Please choose an ID from the list.");
                }
            } else {
                // Register a new parent
                System.out.println("Registering a new parent...");
                System.out.print("Enter parent's name: ");
                String parentName = scanner.nextLine();

                // Handle phone number formatting
                String parentPhone;
                while (true) {
                    System.out.print("Enter parent's phone number (10 digits or format: (123) 456-7890): ");
                    parentPhone = scanner.nextLine();
                    parentPhone = formatPhoneNumber(parentPhone);
                    if (parentPhone != null) {
                        break;
                    }
                    System.out.println("Invalid phone number. Please enter exactly 10 digits.");
                }

                System.out.print("Enter parent's email (or leave blank): ");
                String parentEmail = scanner.nextLine();

                System.out.print("Enter parent's relationship to child (e.g., Mother, Father, Guardian): ");
                String relationship = scanner.nextLine();

                // Insert new parent into database
                String insertParentSQL = "INSERT INTO Parents (Name, Phone, Email, Relationship) VALUES (?, ?, ?, ?)";
                try (PreparedStatement pstmt = conn.prepareStatement(insertParentSQL, Statement.RETURN_GENERATED_KEYS)) {
                    pstmt.setString(1, parentName);
                    pstmt.setString(2, parentPhone);
                    pstmt.setString(3, parentEmail.isEmpty() ? null : parentEmail);
                    pstmt.setString(4, relationship);
                    pstmt.executeUpdate();

                    // Retrieve the newly generated ParentID
                    try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            parentId = generatedKeys.getInt(1);
                            System.out.println("New parent registered successfully with ParentID: " + parentId);
                        } else {
                            System.out.println("Error: Unable to retrieve new ParentID.");
                            return;
                        }
                    }
                }
            }

            // Automatically assign Class Group ID based on age
            int groupId = 0;
            String fetchGroupIdSQL = "SELECT GroupID FROM Classes WHERE AgeRange = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(fetchGroupIdSQL)) {
                pstmt.setString(1, age + " years"); // Match exact age
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        groupId = rs.getInt("GroupID");
                        System.out.println("Assigned GroupID: " + groupId);
                    } else {
                        System.out.println("No class found for the given age. Please configure classes properly.");
                        return;
                    }
                }
            }

            // Insert child into database
            String insertChildSQL = "INSERT INTO Children (Name, Age, ParentID, Allergies, MedicalConditions, GroupID) VALUES (?, ?, ?, ?, ?, ?)";
            System.out.println("Enter child's allergies (or None): ");
            String allergies = scanner.nextLine();
            System.out.println("Enter child's medical conditions (or None): ");
            String medicalConditions = scanner.nextLine();

            try (PreparedStatement pstmt = conn.prepareStatement(insertChildSQL)) {
                pstmt.setString(1, name);
                pstmt.setInt(2, age);
                pstmt.setInt(3, parentId);
                pstmt.setString(4, allergies.isEmpty() || allergies.equalsIgnoreCase("None") ? "None" : allergies);
                pstmt.setString(5, medicalConditions.isEmpty() || medicalConditions.equalsIgnoreCase("None") ? "None" : medicalConditions);
                pstmt.setInt(6, groupId);
                pstmt.executeUpdate();
                System.out.println("Child added successfully.");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Helper method to format phone numbers
    public static String formatPhoneNumber(String input) {
        // Remove all non-digit characters
        String digits = input.replaceAll("\\D", "");
        if (digits.length() == 10) {
            // Format as (XXX) XXX-XXXX
            return String.format("(%s) %s-%s", digits.substring(0, 3), digits.substring(3, 6), digits.substring(6));
        }
        return null; // Invalid phone number
    }
}
