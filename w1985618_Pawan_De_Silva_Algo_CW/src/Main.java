/**
 Name: Pawan De Silva
 IIT ID: 20221846
 UoW ID: w1985618
 */

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    static ArrayList<String> puzzleRows = new ArrayList<>(); // Stores the rows of the puzzle
    static int noOfRows = 0; // Number of rows in the puzzle
    static char[][] puzzle; // 2D array to store the puzzle
    static Scanner input = new Scanner(System.in); // Scanner object to get user input

    public static void main(String[] args) throws IOException {
        String fileName; // File name entered by the user
        boolean exitProgram = false; // Flag to exit the program

        while (!exitProgram) {
            System.out.println("Available Text Files:");
            displayTextFiles(); // Display available text files
            System.out.println("Enter File Name or type 'exit' to exit (*_*)");  // Prompt the user to enter the file name
            fileName = input.next();

            if (fileName.equalsIgnoreCase("exit")) {  // Check if the user wants to exit the program
                exitProgram = true;
            } else if (isValidTextFile(fileName)) { // Check if the entered file name is valid
                processFile(fileName); // Process the file
            } else {
                System.out.println("Invalid file name (-_-). Please enter a valid file name.");
            }
        }
        System.out.println("Exiting the program.(*_*)"); // Exit message
    }

    public static void processFile(String fileName) throws IOException { // Method to process the file
        File myObj = new File("benchmarks/" + fileName + ".txt"); // Read the file
        Scanner myReader = new Scanner(myObj);
        puzzleRows.clear(); // Clear previous puzzle data if any
        noOfRows = 0;

        while (myReader.hasNextLine()) { // Read the file line by line
            noOfRows++;
            puzzleRows.add(myReader.nextLine()); // Add the line to the puzzleRows list
        }
        System.out.println("Provided Puzzle:");
        createPuzzle();  // Create the puzzle
        displayPuzzle();  // Display the puzzle

        if (checkValidDocument()) {
            System.out.println("---------------------------------------------");
            System.out.println("Sliding Path " + fileName);
            SlidingPath slidePath = new SlidingPath(puzzle);
            slidePath.findPath();
            System.out.println("---------------------------------------------");
            System.out.println();
            System.out.println();
        } else {
            System.out.println("Invalid document. Please provide a valid document with a start and end position. (*_*)");
        }
    }

    public static void createPuzzle() { // Method to create the puzzle
        puzzle = new char[noOfRows][noOfRows]; // Initialize the puzzle array
        for (int i = 0; i < puzzleRows.size(); i++) { // Loop through the puzzleRows list
            puzzle[i] = puzzleRows.get(i).toCharArray(); // Convert the string to a character array and add it to the puzzle array
        }
    }

    public static void displayPuzzle() { // Method to print the puzzle
        for (char[] chars : puzzle) {
            for (char aChar : chars) { // Loop through the puzzle and print each character
                System.out.print(aChar + " ");
            }
            System.out.println();
        }
    }

    private static boolean checkValidDocument() { // Method to check if the document is valid
        boolean hasStartPos = false; // Flag to check if the document has a start position
        boolean hasEndPos = false;
        for (char[] chars : puzzle) { // Loop through the puzzle to check for start and end positions
            for (char aChar : chars) {
                if (aChar == 'S') { // Check if the character is 'S'
                    hasStartPos = true;
                }
                if (aChar == 'F') { // Check if the character is 'F'
                    hasEndPos = true;
                }
            }
        }
        return hasStartPos && hasEndPos; // Return true if both start and end positions are present
    }

    private static void displayTextFiles() { // Method to display available text files
        File folder = new File("benchmarks");
        File[] listOfFiles = folder.listFiles(); // Get the list of files in the folder

        if (listOfFiles != null) {
            int count = 0;
            for (File file : listOfFiles) {
                if (file.isFile() && file.getName().endsWith(".txt")) { // Check if the file is a text file
                    System.out.printf("%-20s", file.getName().replaceFirst("[.][^.]+$", ""));
                    count++;
                    if (count % 5 == 0) { // Display 5 files per line
                        System.out.println();
                    }
                }
            }
            System.out.println(); // Add a new line after displaying the files
        }
    }

    private static boolean isValidTextFile(String fileName) { // Method to check if the file is valid
        File file = new File("benchmarks/" + fileName + ".txt");
        return file.exists(); // Return true if the file exists
    }
}