#!/bin/bash

# Step 1: Compile Main and PopulateDatabase
echo "Compiling Main.java and PopulateDatabase.java..."
javac -cp "lib/sqlite-jdbc-3.47.1.0.jar" src/Main.java -d bin
javac -cp "lib/sqlite-jdbc-3.47.1.0.jar" src/PopulateDatabase.java -d bin

# Step 2: Compile CLI programs
echo "Compiling CLI programs..."
javac -cp "lib/sqlite-jdbc-3.47.1.0.jar" src/cli/*.java -d bin

# Step 3: Check if database exists
if [ -f "daycare.db" ]; then
    echo "Database file exists. Checking if it is populated..."
    java -cp "bin:lib/sqlite-jdbc-3.47.1.0.jar" PopulateDatabase
else
    echo "Database file does not exist. Creating database and populating tables..."
    # Run Main to create tables
    java -cp "bin:lib/sqlite-jdbc-3.47.1.0.jar" Main

    # Populate database tables
    java -cp "bin:lib/sqlite-jdbc-3.47.1.0.jar" PopulateDatabase
fi

# Run Main program to confirm tables exist
echo "Running Main program..."
java -cp "bin:lib/sqlite-jdbc-3.47.1.0.jar" Main

# Prompt to run specific CLI programs
while true; do
    echo
    echo "Available CLI programs:"
    echo "1. AddChildCLI"
    echo "2. DeleteChildCLI"
    echo "3. AddAttendanceCLI"
    echo "4. UpdateChildCLI"
    echo "5. ViewAttendanceCLI"
    echo
    read -p "Enter the CLI name to run (e.g., AddChildCLI), or type 'exit' to quit: " cliName

    # Exit
    if [[ "$cliName" == "exit" ]]; then
        echo "Goodbye!"
        break
    fi

    # Run the selected CLI
    java -cp "bin:lib/sqlite-jdbc-3.47.1.0.jar" cli.$cliName || echo "Error: Could not run $cliName. Make sure it is compiled and the name is correct."
done
