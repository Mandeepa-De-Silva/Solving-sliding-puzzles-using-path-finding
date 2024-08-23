/**
 Name: Pawan De Silva
 IIT ID: 20221846
 UoW ID: w1985618
 */

import java.util.ArrayList;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;

public class SlidingPath {
    private static final String ANSI_BLUE = "\u001B[32m"; // create a string to store the blue color
    private static final String ANSI_RESET = "\u001B[0m"; // create a string to store the reset color
    private final char[][] puzzle; // puzzle array
    private final boolean [][] visited; // create a 2d array to store the visited positions
    private Positions start; // create a position object to store the start position
    private Positions end; // create a position object to store the end position
    private final ArrayList<Positions> queueNode = new ArrayList<>(); // create an array list to store the queue
    private static long startTime;
    private static long endTime;

    public SlidingPath(char[][] puzzle){ // constructor to initialize the puzzle and visited array
        this.visited = new boolean[puzzle.length][puzzle.length];
        this.puzzle = puzzle;

    }

    private void findStart(){ // method to find the start position
        for( int i = 0 ; i<puzzle.length; i++){ // loop through the puzzle to find the start position
            for ( int j = 0; j < puzzle[i].length; j++){ // loop through the puzzle to find the start position
                String startPoint = "F";
                if (puzzle[i][j] == startPoint.charAt(0)){ // if the current position is the start position
                    end = new Positions(i,j); // set the start position
                }
            }
        }
    }
    private void findEnd(){ // method to find the end position
        for(int i = 0; i<puzzle.length; i++) {
            for (int j = 0; j < puzzle[i].length; j++) {
                String startPoint = "S";
                if (puzzle[i][j] == startPoint.charAt(0)) {
                    start = new Positions(i, j); // set the end position
                }
            }
        }
    }

    public boolean checkValidPosition(int row, int column) { // method to check if the position is valid
        return row >= 0 && column >= 0  // check if the row and column are greater than 0
                && row < puzzle.length  // check if the row is less than the length of the puzzle
                && column < puzzle[0].length
                && puzzle[row][column] != '0' // check if the current position is not a wall
                && !visited[row][column]; // check if the current position is not visited
    }

    public void findPath() throws IOException{ // method to find the path
        startTime = System.nanoTime();
        findStart();
        findEnd();

        queueNode.add(start); // add the start position to the queue
        visited[start.getRowNum()][start.getColNum()] = true; // set the start position as visited
        Positions current; // create a new position object
        boolean pathFound = false;

        while(!queueNode.isEmpty()){ // loop through the queue
            current =queueNode.remove(0); // remove the first element from the queue
            int visitedRowNum = current.getRowNum(); // get the row number of the current position
            int visitedColNum = current.getColNum();

            if(puzzle[visitedRowNum][visitedColNum] == 'F'){ // if the current position is the end position
                System.out.println("Path Found");
                endTime = System.nanoTime();
                printPath(current);
                pathFound = true;
                break;
            }
            slidingNode(current, -1, 0); // move the node to the left
            slidingNode(current, 1, 0);  // move the node to the right
            slidingNode(current, 0, 1);  // move the node down
            slidingNode(current, 0, -1); // move the node up

        }
        if (!pathFound){ // if the path is not found
            System.out.println("COULD NOT FIND A PATH");
        }
    }

    public void slidingNode(Positions position, int x, int y){
        int row = position.getRowNum(); // get the row number of the current position
        int column = position.getColNum(); // get the column number of the current position

        while(true){ // loop through the puzzle
            row += y;
            column += x;

            if(!checkValidPosition(row, column)){ // if the position is not valid
                break;
            }
            if(puzzle[row][column] =='F'){ // if the current position is the end position
                Positions newPosition = new Positions(row, column); // create a new position object with the row and column
                newPosition.setParent(position); // set the parent of the new position as the current position
                queueNode.add(0, newPosition); // add the new position to the queue
                newPosition.directions = movingDirection(position, newPosition);
                visited[row][column] = true;
                break;
            }

            int nextRow = row + y;
            int nextColumn = column + x;
            // if the next position is not valid
            if((nextRow < 0 || nextColumn < 0) || (nextRow >= puzzle.length || nextColumn >= puzzle.length) || (puzzle[nextRow][nextColumn] == '0')){
                Positions newPosition = new Positions(row, column);
                newPosition.setParent(position);
                queueNode.add(newPosition);
                newPosition.directions = movingDirection(position, newPosition); // find the direction of the movement
                visited[row][column] = true;
                break;
            }
        }
    }

    private String movingDirection(Positions node, Positions neighbour){ // method to find the direction of the movement
        String direction = "";
        if (neighbour.getColNum() < node.getColNum()){ // if the column number of the neighbour is less than the column number of the current position
            direction = "Left";
        }
        if (neighbour.getColNum() > node.getColNum()){
            direction = "Right";
        }
        if (neighbour.getRowNum() > node.getRowNum()){ // if the row number of the neighbour is greater than the row number of the current position
            direction = "Down";
        }
        if (neighbour.getRowNum() < node.getRowNum()){
            direction = "Up";
        }
        return direction;
    }

    private void printPath(Positions current) throws IOException{ // method to print the path
        FileWriter fileWriter = null; // create a file writer object
        try{
            fileWriter = new FileWriter("src/SlidingPath.txt");
        }catch (IOException e){
            e.printStackTrace();
        }

        ArrayList<Positions> path = new ArrayList<>(); // create an array list to store the path
        Positions temp = current;
        while(temp.getParent() != null){
            path.add(temp);
            temp = temp.getParent(); // set the temp as the parent of the current position
        }

        Collections.reverse(path); // reverse the path to get the correct path order
        System.out.println("01. Start at (" + (start.getColNum()+1) + "," + (start.getRowNum()+1) + ")");
        fileWriter.write("01. Start at (" + (start.getColNum()+1) + "," + (start.getRowNum()+1) + ")\n");
        int count = 2;
        for(Positions positions : path){
            if (count < 10){
                System.out.println("0" + count  + ". " + "Move " + positions.directions + " To (" + (positions.getColNum()+1) + "," + (positions.getRowNum()+1) + ")");
                fileWriter.write("0" + count  + ". " + "Move " + positions.directions + " To (" + (positions.getColNum()+1) + "," + (positions.getRowNum()+1) + ")\n");
            }
            else {
                System.out.println(count  + ". " + "Move " + positions.directions + " To (" + (positions.getColNum()+1) + "," + (positions.getRowNum()+1) + ")");
                fileWriter.write(count  + ". " + "Move " + positions.directions + " To (" + (positions.getColNum()+1) + "," + (positions.getRowNum()+1) + ")\n");
            }
            count++;
        }
        System.out.println(count + ". "  + "Done!");
        fileWriter.write(count + ". "  + "Done!\n");
        double time = (endTime - startTime)/1000000000.0;
        System.out.println("Time taken: " + time + " s");
        fileWriter.write("Time taken: " + time + " s");
        fileWriter.close();
        System.out.println();
        printSolvedPuzzle(path);
    }
    public void printSolvedPuzzle(ArrayList<Positions> path) { // method to print the solved puzzle
        System.out.println("* DISPLAYING FOUND PATH *");

        String[][] solvedPuzzle2 = new String[puzzle.length][puzzle[0].length]; // create a 2d array to store the solved puzzle
        for (int i = 0; i < solvedPuzzle2.length; i++) { // loop through the puzzle
            for (int j = 0; j < solvedPuzzle2[i].length; j++) {
                solvedPuzzle2[i][j] = String.valueOf(puzzle[i][j]); // set the value of the solved puzzle as the value of the puzzle
            }
        }

        String pathSign2 = "v<^>"; // create a string to store the path signs

        path.add(0, start); // add the start position to the path
        for (int i = 0; i < path.size() - 1; i++) { // loop through the path
            Positions currentPos = path.get(i);
            Positions nextPos = path.get(i + 1);
            int currentRow = currentPos.getRowNum();
            int currentCol = currentPos.getColNum();
            int nextRow = nextPos.getRowNum();
            int nextCol = nextPos.getColNum();

            // if the next position is valid
            if (nextRow >= 0 && nextRow < solvedPuzzle2.length && nextCol >= 0 && nextCol < solvedPuzzle2[0].length) {
                if (currentRow < nextRow) { // if the row number of the current position is less than the row number of the next position
                    solvedPuzzle2[nextRow][nextCol] = ANSI_BLUE + pathSign2.charAt(0) + ANSI_RESET;
                } else if (currentCol > nextCol) {
                    solvedPuzzle2[nextRow][nextCol] = ANSI_BLUE + pathSign2.charAt(1) + ANSI_RESET;
                } else if (currentRow > nextRow) {
                    solvedPuzzle2[nextRow][nextCol] = ANSI_BLUE + pathSign2.charAt(2) + ANSI_RESET;
                } else if (currentCol < nextCol) {
                    solvedPuzzle2[nextRow][nextCol] = ANSI_BLUE + pathSign2.charAt(3) + ANSI_RESET;
                }
            }
        }

        String endSign = "F"; // create a string to store the end sign
        solvedPuzzle2[end.getRowNum()][end.getColNum()] = String.valueOf(endSign.charAt(0)); // set the end position as the end sign
        for (String[] chars : solvedPuzzle2) { // loop through the solved puzzle
            System.out.print("  ");
            for (String aChar : chars) {
                System.out.print(aChar + " "); // print the solved puzzle
            }
            System.out.println();
        }
    }
}
