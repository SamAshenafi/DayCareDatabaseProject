import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class Main {
    public static void main(String[] args) {
        String url = "jdbc:sqlite:daycare.db";

        try {
            // Explicitly load the SQLite JDBC driver
            Class.forName("org.sqlite.JDBC");

            // Establish a connection to the database
            try (Connection conn = DriverManager.getConnection(url)) {
                if (conn != null) {
                    System.out.println("Connected to SQLite database!");

                    // SQL to create the Parents table
                    String createParentsTable = """
                        CREATE TABLE IF NOT EXISTS Parents (
                            ParentID INTEGER PRIMARY KEY AUTOINCREMENT,
                            Name TEXT NOT NULL,
                            Phone TEXT NOT NULL,
                            Email TEXT,
                            Relationship TEXT
                        );
                    """;

                    // SQL to create the Children table
                    String createChildrenTable = """
                        CREATE TABLE IF NOT EXISTS Children (
                            ChildID INTEGER PRIMARY KEY AUTOINCREMENT,
                            Name TEXT NOT NULL,
                            Age INTEGER NOT NULL,
                            ParentID INTEGER NOT NULL,
                            Allergies TEXT,
                            MedicalConditions TEXT,
                            GroupID INTEGER,
                            FOREIGN KEY (ParentID) REFERENCES Parents(ParentID),
                            FOREIGN KEY (GroupID) REFERENCES Classes(GroupID)
                        );
                    """;

                    // SQL to create the Staff table
                    String createStaffTable = """
                        CREATE TABLE IF NOT EXISTS Staff (
                            StaffID INTEGER PRIMARY KEY AUTOINCREMENT,
                            Name TEXT NOT NULL,
                            Role TEXT NOT NULL,
                            Phone TEXT NOT NULL,
                            AssignedGroupID INTEGER,
                            FOREIGN KEY (AssignedGroupID) REFERENCES Classes(GroupID)
                        );
                    """;

                    // SQL to create the Classes table
                    String createClassesTable = """
                        CREATE TABLE IF NOT EXISTS Classes (
                            GroupID INTEGER PRIMARY KEY AUTOINCREMENT,
                            Name TEXT NOT NULL,
                            AgeRange TEXT NOT NULL,
                            AssignedStaffID INTEGER,
                            FOREIGN KEY (AssignedStaffID) REFERENCES Staff(StaffID)
                        );
                    """;

                    // SQL to create the Attendance table
                    String createAttendanceTable = """
                        CREATE TABLE IF NOT EXISTS Attendance (
                            AttendanceID INTEGER PRIMARY KEY AUTOINCREMENT,
                            ChildID INTEGER NOT NULL,
                            Date DATE NOT NULL,
                            ArrivalTime TIME NOT NULL,
                            DepartureTime TIME NOT NULL,
                            FOREIGN KEY (ChildID) REFERENCES Children(ChildID)
                        );
                    """;

                    // Execute SQL commands to create the tables
                    try (Statement stmt = conn.createStatement()) {
                        stmt.execute(createParentsTable);
                        stmt.execute(createChildrenTable);
                        stmt.execute(createStaffTable);
                        stmt.execute(createClassesTable);
                        stmt.execute(createAttendanceTable);
                        System.out.println("Tables created successfully.");
                    }
                }
            }
        } catch (ClassNotFoundException e) {
            System.out.println("SQLite JDBC driver not found: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
