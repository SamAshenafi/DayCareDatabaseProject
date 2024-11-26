import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Date;
import java.sql.Time;
import java.util.Random;

public class PopulateDatabase {
    public static void main(String[] args) {
        String url = "jdbc:sqlite:daycare.db";
        Random random = new Random();

        // Example data for random generation
        String[] firstNames = {
                "Ava", "Liam", "Olivia", "Noah", "Sophia", "Jackson", "Isabella", "Aiden",
                "Emma", "Lucas", "Mia", "Elijah", "Amelia", "Ethan", "Charlotte", "Logan",
                "Luna", "Sebastian", "Aria", "James", "Zoe", "Henry", "Ellie", "Alexander", "Grace"
        };
        String[] lastNames = {
                "Smith", "Johnson", "Williams", "Brown", "Jones", "Garcia", "Martinez",
                "Davis", "Rodriguez", "Lopez", "Wilson", "Anderson", "Thomas", "Taylor",
                "Moore", "Hernandez", "Martin", "Jackson", "Lee", "Perez", "Clark", "Lewis"
        };
        String[] relationships = { "Mother", "Father", "Guardian" };
        String[] roles = { "Teacher", "Assistant" };
        String[] allergies = { "None", "Peanuts", "Dairy", "Eggs", "Soy", "Wheat", "Shellfish", "Tree nuts" };
        String[] medicalConditions = {
                "None", "Asthma", "Diabetes", "Epilepsy", "ADHD", "Autism", "Eczema", "Seasonal Allergies",
                "Heart Murmur", "Anemia", "Hearing Loss"
        };

        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                System.out.println("Connected to SQLite database!");

                // Insert 200 random parents
                String insertParentSQL = "INSERT INTO Parents (Name, Phone, Email, Relationship) VALUES (?, ?, ?, ?)";
                try (PreparedStatement pstmt = conn.prepareStatement(insertParentSQL)) {
                    for (int i = 0; i < 200; i++) {
                        String firstName = firstNames[random.nextInt(firstNames.length)];
                        String lastName = lastNames[random.nextInt(lastNames.length)];
                        String name = firstName + " " + lastName;
                        String phone = String.format("(%03d) %03d-%04d", random.nextInt(800) + 200, random.nextInt(900),
                                random.nextInt(10000)); // (XXX) XXX-XXXX
                        String email = firstName.toLowerCase() + "." + lastName.toLowerCase() + "@example.com";
                        String relationship = relationships[random.nextInt(relationships.length)];
                        pstmt.setString(1, name);
                        pstmt.setString(2, phone);
                        pstmt.setString(3, email);
                        pstmt.setString(4, relationship);
                        pstmt.executeUpdate();
                    }
                    System.out.println("200 Parents inserted.");
                }

                // Insert 5 random staff members
                String insertStaffSQL = "INSERT INTO Staff (Name, Role, Phone, AssignedGroupID) VALUES (?, ?, ?, ?)";
                try (PreparedStatement pstmt = conn.prepareStatement(insertStaffSQL)) {
                    for (int i = 0; i < 5; i++) {
                        String firstName = firstNames[random.nextInt(firstNames.length)];
                        String lastName = lastNames[random.nextInt(lastNames.length)];
                        String name = firstName + " " + lastName;
                        String role = roles[random.nextInt(roles.length)];
                        String phone = String.format("(%03d) %03d-%04d", random.nextInt(800) + 200, random.nextInt(900),
                                random.nextInt(10000)); // (XXX) XXX-XXXX
                        int groupID = random.nextInt(3) + 1; // Assign group ID (1 to 3)
                        pstmt.setString(1, name);
                        pstmt.setString(2, role);
                        pstmt.setString(3, phone);
                        pstmt.setInt(4, groupID);
                        pstmt.executeUpdate();
                    }
                    System.out.println("5 Staff members inserted.");
                }

                // Insert 3 classes/groups
                String insertClassSQL = "INSERT INTO Classes (Name, AgeRange, AssignedStaffID) VALUES (?, ?, ?)";
                try (PreparedStatement pstmt = conn.prepareStatement(insertClassSQL)) {
                    String[] classNames = { "Toddlers", "Preschool", "Kindergarten" };
                    String[] ageRanges = { "2-3 years", "3-4 years", "4-5 years" };
                    for (int i = 0; i < 3; i++) {
                        pstmt.setString(1, classNames[i]);
                        pstmt.setString(2, ageRanges[i]);
                        pstmt.setInt(3, i + 1); // Assign staff IDs sequentially
                        pstmt.executeUpdate();
                    }
                    System.out.println("3 Classes inserted.");
                }

                // Insert 100 random children
                String insertChildSQL = "INSERT INTO Children (Name, Age, ParentID, Allergies, MedicalConditions, GroupID) VALUES (?, ?, ?, ?, ?, ?)";
                try (PreparedStatement pstmt = conn.prepareStatement(insertChildSQL)) {
                    for (int i = 0; i < 100; i++) {
                        String firstName = firstNames[random.nextInt(firstNames.length)];
                        String lastName = lastNames[random.nextInt(lastNames.length)];
                        String name = firstName + " " + lastName;
                        int age = 2 + random.nextInt(4); // Ages between 2 and 5
                        int parentID = 1 + random.nextInt(200); // Random ParentID between 1 and 200
                        String allergy = allergies[random.nextInt(allergies.length)];
                        String medicalCondition = medicalConditions[random.nextInt(medicalConditions.length)];
                        int groupID = age - 2 + 1; // Assign group based on age
                        pstmt.setString(1, name);
                        pstmt.setInt(2, age);
                        pstmt.setInt(3, parentID);
                        pstmt.setString(4, allergy);
                        pstmt.setString(5, medicalCondition);
                        pstmt.setInt(6, groupID);
                        pstmt.executeUpdate();
                    }
                    System.out.println("100 Children inserted.");
                }

                // Insert 300 random attendance records
                String insertAttendanceSQL = "INSERT INTO Attendance (ChildID, Date, ArrivalTime, DepartureTime) VALUES (?, ?, ?, ?)";
                try (PreparedStatement pstmt = conn.prepareStatement(insertAttendanceSQL)) {
                    for (int i = 0; i < 300; i++) {
                        int childID = 1 + random.nextInt(100); // Random ChildID between 1 and 100
                
                        // Generate a random date within the last 30 days
                        java.time.LocalDate date = java.time.LocalDate.now().minusDays(random.nextInt(30));
                
                        // Generate random arrival and departure times in "HH:mm:ss" format
                        java.time.LocalTime arrivalTime = java.time.LocalTime.of(8 + random.nextInt(2), random.nextInt(60));
                        java.time.LocalTime departureTime = java.time.LocalTime.of(15 + random.nextInt(3), random.nextInt(60));
                
                        pstmt.setInt(1, childID);
                        pstmt.setString(2, date.toString()); // Insert date as a string (e.g., "2024-11-25")
                        pstmt.setString(3, arrivalTime.toString()); // Insert arrival time as a string (e.g., "08:45:00")
                        pstmt.setString(4, departureTime.toString()); // Insert departure time as a string (e.g., "17:30:00")
                        pstmt.executeUpdate();
                    }
                    System.out.println("300 Attendance records inserted.");
                }
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
