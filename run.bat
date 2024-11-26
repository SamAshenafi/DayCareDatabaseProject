@echo off
echo Compiling Java program...
javac -cp lib\sqlite-jdbc-3.47.1.jar src\Main.java -d bin
javac -cp lib/sqlite-jdbc-3.47.1.jar src\PopulateDatabase.java -d bin


echo Running Java program...
java -cp ".;lib\sqlite-jdbc-3.47.1.0.jar;bin" Main
java -cp "lib/sqlite-jdbc-3.47.1.0.jar;bin" PopulateDatabase


pause
