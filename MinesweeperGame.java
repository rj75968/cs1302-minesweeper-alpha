package cs1302.game;

import java.util.Scanner;
import java.io.File;
import java.util.InputMismatchException;
import java.io.FileNotFoundException;

/**
 * The {@link cs1302.game.MinesweeperGame} class that contains
 * the methods and variables needed to run the game, Minesweeper.
 */
public class MinesweeperGame {

    private int rows;
    private int cols;
    private String[][] minesweeperGameArray;
    private boolean [][] isMineArray;
    private String [][] noFogArray;

    private int totalMines = 0;
    private int roundsCompleted = 0;
    public boolean noFog = false;
    private final Scanner stdIn;

    /**
     * Initializes the {@code stdIn} instance constant.
     * Checks various conditions to determine valid input.
     * Outputs various error messages.
     * @param stdIn of type Scanner which reads in standard input
     * @param seedPath of type String which specifies name of file to be used
     */
    public MinesweeperGame(Scanner stdIn, String seedPath) {
        this.stdIn = stdIn;
        try {
            File seedFile = new File(seedPath);
            Scanner configScanner = new Scanner(seedFile);
            if (configScanner.hasNextInt()) {
                this.rows = configScanner.nextInt();
                validateRows(rows);
                if (configScanner.hasNextInt()) {
                    this.cols = configScanner.nextInt();
                    validateCols(cols);
                }
            } else {
                System.err.println();
                System.err.println("Seed File Malformed Error: Cannot"
                    + " create a mine field with that many rows and/or columns!");
                System.exit(3);
            }
            if (configScanner.hasNextInt()) {
                this.totalMines = configScanner.nextInt();
                if ((this.totalMines > rows * cols)) {
                    System.err.println();
                    System.err.println("Seed File Malformed Error: Cannot"
                             + " create a mine field with that many rows and/or columns!");
                    System.exit(3);
                }
            }
            isMineArray = new boolean[rows][cols];
            minesweeperGameArray = new String[rows][cols];
            noFogArray = new String[rows][cols];
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    isMineArray[i][j] = false;
                    minesweeperGameArray[i][j] = "   ";
                    noFogArray[i][j] = "   ";
                }
            }
            for (int i = 0; i < totalMines; i++) {
                int mineRow;
                int mineCol;
                if (configScanner.hasNextInt()) {
                    mineRow = configScanner.nextInt();
                    mineCol = configScanner.nextInt();
                    if ((mineRow > rows - 1) || (mineCol > cols - 1)) {
                        System.err.println();
                        System.err.println("Seed File Malformed Error: Cannot"
                            + " create a mine field with that many rows and/or columns!");
                        System.exit(3);
                    } else {
                        isMineArray[mineRow][mineCol] = true;
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println();
            System.err.println("Seed File Not Found Error: " + e.getMessage());
            e.printStackTrace();
            System.exit(2);
        }
    }

    /**
     * Validates the number of rows in file.
     * @param rows of type int
     */
    public void validateRows(int rows) {
        //rows must be nonnegative and greater than 5 but less than 10
        if ((rows <= 0 || rows < 5 || rows > 10)) {
            System.err.println();
            System.err.println("Seed File Malformed Error: Cannot"
                             + " create a mine field with that many rows and/or columns!");
            System.exit(3);
        }
    }

    /**
     * Validates the number of columns in file.
     * @param cols of type int
     */
    public void validateCols(int cols) {
        //columns must be nonnegative and greater than 5 but less than 10
        if ((cols <= 0 || cols < 5 || cols > 10)) {
            System.err.println();
            System.err.println("Seed File Malformed Error: Cannot"
                              + " create a mine field with that many rows and/or columns!");
            System.exit(3);
        }
    }

    /**
     * Prints the welcome banner to standard output.
     */
    public void printWelcome() {

        System.out.println("        _");
        System.out.println("  /\\/\\(F)_ __   ___  _____      _____  ___ _ __   ___ _ __");
        System.out.println(" /    \\| | '_ \\ / _ \\/ __\\ \\ /\\ / / _ \\/ _ \\ '_ \\"
            + "/ _ \\ '__|");
        System.out.println("/ /\\/\\ \\ | | | |  __/\\__ \\\\ V  V /  __/  __/ |_) |  __/ |");
        System.out.println("\\/    \\/_|_| |_|\\___||___/ \\_/\\_/ \\___|\\___| .__/ \\___|_|");
        System.out.println("                             ALPHA EDITION |_| v2021.sp");
    }

    /**
     * Prints the current contents of the mine field to standard output.
     */
    public void printMineField() {
        //prints mine field array
        for (int i = 0; i < minesweeperGameArray.length; i++) {
            //prints out index numbers on grid
            System.out.print(i + " |");
            for (int j = 0; j < minesweeperGameArray[i].length; j++) {
                System.out.print(minesweeperGameArray[i][j]);
                if (j < minesweeperGameArray[i].length - 1) {
                    System.out.print("|");
                }
            }
            System.out.println("|");
        }
        System.out.print("    ");
        for (int i = 0; i < minesweeperGameArray[0].length; i++) {
            System.out.print(i + "   ");
        }
        System.out.println();
    }

    /**
     * Outputs rounds completed line.
     */
    public void printRoundsCompleted() {
        System.out.println();
        System.out.println("Rounds Completed: " + roundsCompleted);
        System.out.println();
    }

    /**
     * Prints line prompt for user command input.
     */
    public void printMineAlpha() {
        System.out.println();
        System.out.print("minesweeper-alpha: ");
    }

    /**
     * Prints the game prompt to standard output.
     * Interprets user input from {@code stdIn}.
     * Calls other methods based on standard command input.
     * Commands within {@link promptUser} can print the help menu, reveal a square,
     * quit the game, guess a mine's location, or mark a mine's location.
     * If the {@code noFog} command is used, the {@link noFog} method is called.
     */
    public void promptUser() {
        String fullCommand = this.stdIn.nextLine().trim();
        Scanner commandScan = new Scanner(fullCommand);
        String command = "";

        try {
            command = commandScan.next().trim();
        } catch (Exception e) {
            e.getMessage();
        }
        switch (command) {
        case "h":
        case "help":
            help();
            break;
        case "g":
        case "guess":
            guess(commandScan);
            break;
        case "q":
        case "quit":
            quit();
            break;
        case "m":
        case "mark":
            mark(commandScan);
            break;
        case "r":
        case "reveal":
            reveal(commandScan);
            break;
        case "nofog":
            noFog();
            break;
        default:
            printInvalid();
            break;
        }
    }

    /**
     * Prints and displays the help menu.
     */
    public void help() {
        this.roundsCompleted++; //displaying the command list uses up a round
        System.out.println();
        System.out.println("Commands Available...");
        System.out.println(" - Reveal: r/reveal row col");
        System.out.println(" -   Mark: m/mark   row col");
        System.out.println(" -  Guess: g/guess  row col");
        System.out.println(" -   Help: h/help");
        System.out.println(" -   Quit: q/quit");
    }

    /**
     * Method for revealing a square on the grid.
     * @param stdIn interprets square row and column indices
     * @return true if reveal is in bounds and follows correct syntax, otherwise false
     */
    public boolean reveal(Scanner stdIn) {
        boolean userReveal = false;
        int rowReveal = 0;
        int columnReveal = 0;
        while (stdIn.hasNext()) {
            if (stdIn.hasNextInt()) {
                rowReveal = stdIn.nextInt();
                if (stdIn.hasNextInt()) {
                    columnReveal = stdIn.nextInt();
                    //checks if there is a subsequent token, prints invalid if true
                    if (stdIn.hasNextInt() || stdIn.hasNextInt()) {
                        printInvalid();
                        return true;
                    }
                }
                if (isInBounds(rowReveal, columnReveal)) {
                    userReveal = true;
                    roundsCompleted++;
                    if (!isMineArray[rowReveal][columnReveal]) {
                        minesweeperGameArray[rowReveal][ columnReveal] = " "
                            + getNumAdjMines(rowReveal, columnReveal) + " ";
                        System.out.println();
                    } else {
                        printLoss();
                    }
                } else {
                    printInvalid();
                }
            }
        }
        return userReveal;
    } //reveal method

    /**
     * Method for marking a square definitely containing a mine.
     * @param stdIn interprets row and column indices
     * @return true if mark is in bounds and follows correct syntax, otherwise false
     */
    public boolean mark(Scanner stdIn) {
        boolean userMark = false;
        int rowMark = 0;
        int columnMark = 0;
        while (stdIn.hasNext()) {
            if (stdIn.hasNextInt()) {
                rowMark = stdIn.nextInt();
                if (stdIn.hasNextInt()) {
                    columnMark = stdIn.nextInt();
                    //checks if there is a subsequent token, prints invalid if true
                    if (stdIn.hasNextInt() || stdIn.hasNext()) {
                        printInvalid();
                        return true;
                    }
                    if (isInBounds(rowMark, columnMark)) {
                        userMark = true;
                        roundsCompleted++;
                        //"F" denotes the flag placed on the square
                        minesweeperGameArray[rowMark][columnMark] = " F ";
                    }
                } else {
                    printInvalid();
                    return true;
                }
            }
            return true;
        }
        return userMark;
    } //mark method

    /**
     * Method for marking a square as potentially containg a mine.
     * @param stdIn interprets row and column indices
     * @return true if guess is in bounds and follows correct syntax, otherwise false
     */
    public boolean guess(Scanner stdIn) {
        boolean userGuess = false;
        int rowGuess = 0;
        int columnGuess = 0;
        while (stdIn.hasNext()) {
            if (stdIn.hasNextInt()) {
                rowGuess = stdIn.nextInt();
                if (stdIn.hasNextInt()) {
                    columnGuess = stdIn.nextInt();
                    //checks if there is a subsequent token, prints invalid if true
                    if (stdIn.hasNextInt() || stdIn.hasNext()) {
                        printInvalid();
                        return true;
                    }
                    if (isInBounds(rowGuess, columnGuess)) {
                        userGuess = true;
                        roundsCompleted++;
                        // a question mark is placed on the guess square
                        minesweeperGameArray[rowGuess][columnGuess] = " ? ";
                        System.out.println();
                    }
                } else {
                    printInvalid();
                    return true;
                }
            }
        }
        return userGuess;
    } //guess method

    /**
     * Method displays goodbye message and gracefully exits the game.
     */
    public void quit() {
        System.out.println();
        System.out.println("Quitting the game...");
        System.out.println("Bye!");
        System.out.println();
        System.exit(0);
    }

    /**
     * Method used within the {@link newMove} method.
     * Increments {@code roundsCompleted}.
     * Sets {@code noFog} variable to true
     */
    public void noFog() {
        roundsCompleted++;
        noFog = true;
    }

    /**
     * When the "nofog" command is used, this method prints out the {@code noFog} grid.
     * Implements command features of removing the "fog of war" for the next round only.
     * Less-than and greater-than symbols are placed on either side of the square's center.
     */
    public void printNoFogArray() {
        for (int i = 0; i < noFogArray.length; i++) {
            System.out.print(i + " |");
            for (int j = 0; j < noFogArray[i].length; j++) {
                //compares mine array and game array to validate mine locations
                if (isMineArray[i][j] && minesweeperGameArray[i][j].trim().equals("F")) {
                    //places noFog notation around mines which are correctly marked
                    System.out.print("< " + minesweeperGameArray[i][j].trim() + ">");
                } else if (isMineArray[i][j]) {
                    //places noFog notation around mines which are not marked
                    System.out.print("< " + minesweeperGameArray[i][j].trim() + ">");
                } else {
                    System.out.print(minesweeperGameArray[i][j]);
                }
                if (j < noFogArray[i].length - 1) {
                    System.out.print("|");
                }
            }
            System.out.println("|");
        }
        System.out.print("    ");
        for (int i = 0; i < minesweeperGameArray[0].length; i++) {
            System.out.print(i + "   ");
        }
        System.out.println();
    }

    /**
     * Prints out error message when {@code stdIn} command input does not follow proper syntax.
     */
    public void printInvalid() {
        System.err.println();
        System.err.println("Invalid Command: Command not recognized!");
        System.out.println();
    }

/**
 * Returns the number of mines adjacent to the specified
 * square in the grid.
 *
 * @param row the row index of the square
 * @param col the column index of the square
 * @return the number of adjacent mines
 */
    private int getNumAdjMines(int row, int col) {
        int numAdjMines = 0;
        //checks the rows and columns surrounding a square to find nearby mines
        for (int i = row - 1; i <= row + 1; i++) {
            if (!(i >= 0 && i < minesweeperGameArray.length)) {
                continue;
            }
            for ( int j = col - 1; j <= col + 1; j++) {
                if ((i == row && j == col) || (!(j >= 0 && j < minesweeperGameArray[0].length))) {
                    continue;
                } else {
                    if (isMineArray[i][j]) {
                        numAdjMines++;
                    }
                }
            }
        }
        return numAdjMines;
    }

/**
 * Indicates whether or not the square is in the game grid.
 *
 * @param row the row index of the square
 * @param col the column index of the square
 * @return true if the square is in the game grid; false otherwise
 */
    private boolean isInBounds(int row, int col) {
        //prints an error message for out of bound row input
        if (row < 0 || row >= minesweeperGameArray.length) {
            System.err.println("Invalid Command: Index " + row + " out of bounds for length "
                + minesweeperGameArray.length);
        }
        //prints an error message for out of bound column input
        if (col < 0 || col >= minesweeperGameArray[0].length) {
            System.err.println("Invalid Command: Index " + col + " out of bounds for length "
                + minesweeperGameArray.length);
        }
        return (row >= 0 && row < minesweeperGameArray.length && col >= 0
            && col < minesweeperGameArray[0].length);
    }

    /**
     * Method which defines the winning conditions.
     * @return true when all mines have been marked and all squares are revealed, otherwise false
     */
    public boolean isWon() {
        for (int i = 0; i < minesweeperGameArray.length; i++) {
            for (int j = 0; j < minesweeperGameArray.length; j++) {
                //checks if the game array still contains any blank or guess squares
                if (minesweeperGameArray[i][j].equals("   ")
                    || minesweeperGameArray[i][j].equals(" ? " )) {
                    return false;
                }
                //if the mines squares are not marked, then returns false
                if (isMineArray[i][j] && !minesweeperGameArray[i][j].equals(" F ")) {
                    return false;
                    //if a square is marked, but does not contain a mine, returns false
                } else if (!isMineArray[i][j] && minesweeperGameArray[i][j].equals(" F ")) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Method used within the {@link play} method.
     * Defines the sequence of methods and variables that are implemented during a round.
     */
    public void newMove() {
        printRoundsCompleted();
        //checks if the noFog command was used
        if (noFog) {
            printNoFogArray();
            noFog = false;
        } else {
            printMineField();
        }
        //reprints the prompt line
        printMineAlpha();
        promptUser();
    }

    /**
     * Provides the main game loop by invoking other instance methods.
     */
    public void play() {
        printWelcome();
        while (true) {
            newMove();
            if (isWon()) {
                break;
            }
        }
        printWin();
    } // play method

    /**
     * Prints the win message to standard output.
     */
    private void printWin() {
        System.out.println("" +
                           " ░░░░░░░░░▄░░░░░░░░░░░░░░▄░░░░ \"So Doge\"\n" +
                           " ░░░░░░░░▌▒█░░░░░░░░░░░▄▀▒▌░░░\n" +
                           " ░░░░░░░░▌▒▒█░░░░░░░░▄▀▒▒▒▐░░░ \"Such Score\"\n" +
                           " ░░░░░░░▐▄▀▒▒▀▀▀▀▄▄▄▀▒▒▒▒▒▐░░░\n" +
                           " ░░░░░▄▄▀▒░▒▒▒▒▒▒▒▒▒█▒▒▄█▒▐░░░ \"Much Minesweeping\"\n" +
                           " ░░░▄▀▒▒▒░░░▒▒▒░░░▒▒▒▀██▀▒▌░░░\n" +
                           " ░░▐▒▒▒▄▄▒▒▒▒░░░▒▒▒▒▒▒▒▀▄▒▒▌░░ \"Wow\"\n" +
                           " ░░▌░░▌█▀▒▒▒▒▒▄▀█▄▒▒▒▒▒▒▒█▒▐░░\n" +
                           " ░▐░░░▒▒▒▒▒▒▒▒▌██▀▒▒░░░▒▒▒▀▄▌░\n" +
                           " ░▌░▒▄██▄▒▒▒▒▒▒▒▒▒░░░░░░▒▒▒▒▌░\n" +
                           " ▀▒▀▐▄█▄█▌▄░▀▒▒░░░░░░░░░░▒▒▒▐░\n" +
                           " ▐▒▒▐▀▐▀▒░▄▄▒▄▒▒▒▒▒▒░▒░▒░▒▒▒▒▌\n" +
                           " ▐▒▒▒▀▀▄▄▒▒▒▄▒▒▒▒▒▒▒▒░▒░▒░▒▒▐░\n" +
                           " ░▌▒▒▒▒▒▒▀▀▀▒▒▒▒▒▒░▒░▒░▒░▒▒▒▌░\n" +
                           " ░▐▒▒▒▒▒▒▒▒▒▒▒▒▒▒░▒░▒░▒▒▄▒▒▐░░\n" +
                           " ░░▀▄▒▒▒▒▒▒▒▒▒▒▒░▒░▒░▒▄▒▒▒▒▌░░\n" +
                           " ░░░░▀▄▒▒▒▒▒▒▒▒▒▒▄▄▄▀▒▒▒▒▄▀░░░ CONGRATULATIONS!\n" +
                           " ░░░░░░▀▄▄▄▄▄▄▀▀▀▒▒▒▒▒▄▄▀░░░░░ YOU HAVE WON!");
        System.out.printf(" ░░░░░░░░░▒▒▒▒▒▒▒▒▒▒▀▀░░░░░░░░ SCORE: %.2f\n",  score());

    }

    /**
     * Calculates the score based on grid size and rounds played.
     * A perfect score of 100 is only attained by playing as many rounds as (rows * columns).
     * @return score calculation if rounds not equal to zero, otherwise no score is returned
     */
    private double score() {
        if (roundsCompleted != 0) {
            return ((minesweeperGameArray.length * minesweeperGameArray[0].length * 100.0)
                / roundsCompleted);
        } else {
            return 0;
        }
    }

    /**
     * Prints the gameover message to standard output.
     * Exits the game gracefully.
     */
    private void printLoss() {
        System.out.println();
        System.out.println(" Oh no... You revealed a mine!");
        System.out.println("  __ _  __ _ _ __ ___   ___    _____   _____ _ __");
        System.out.println(" / _` |/ _` | '_ ` _ \\ / _ \\  / _ \\ \\ / / _ \\ '__|");
        System.out.println("| (_| | (_| | | | | | |  __/ | (_) \\ V /  __/ |");
        System.out.println(" \\__, |\\__,_|_| |_| |_|\\___|  \\___/ \\_/ \\___|_|");
        System.out.println(" |___/");
        System.out.println();
        System.exit(0);
    }
}
