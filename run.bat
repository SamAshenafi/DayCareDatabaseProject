@echo off

:: Step 1: Compile Main and PopulateDatabase
echo Compiling Main.java and PopulateDatabase.java...
javac -cp "lib\sqlite-jdbc-3.47.1.0.jar" src\Main.java -d bin
javac -cp "lib\sqlite-jdbc-3.47.1.0.jar" src\PopulateDatabase.java -d bin

:: Step 2: Compile CLI programs
echo Compiling CLI programs...
javac -cp "lib\sqlite-jdbc-3.47.1.0.jar" src\cli\*.java -d bin

:: Step 3: Check if database exists
if exist daycare.db (
    echo Database file exists. Checking if it is populated...
    java -cp "bin;lib\sqlite-jdbc-3.47.1.0.jar" PopulateDatabase
) else (
    echo Database file does not exist. Creating database and populating tables...
    
    :: Run Main to create tables
    java -cp "bin;lib\sqlite-jdbc-3.47.1.0.jar" Main

    ::Populate database tables
    java -cp "bin;lib\sqlite-jdbc-3.47.1.0.jar" PopulateDatabase
)

:: Run Main program
echo Running Main program...
java -cp "bin;lib\sqlite-jdbc-3.47.1.0.jar" Main

::ClI menu
:run_cli
echo.
echo Available CLI programs:
echo 1. AddChildCLI
echo 2. DeleteChildCLI
echo 3. AddAttendanceCLI
echo 4. UpdateChildCLI
echo 5. ViewAttendanceCLI
echo.
set /p cliName="Enter the CLI name to run (e.g., AddChildCLI), or type 'exit' to quit: "

:: Exit
if /i "%cliName%"=="exit" (
    goto end
)

:: Run the selected CLI
java -cp "bin;lib\sqlite-jdbc-3.47.1.0.jar" cli.%cliName%
if errorlevel 1 echo Error: Could not run %cliName%. Make sure it is compiled and the name is correct.
goto run_cli

:end
echo Goodbye!
pause
