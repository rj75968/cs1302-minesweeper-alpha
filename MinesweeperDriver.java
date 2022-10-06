package cs1302.game;

import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

/**
 * A driver class that creates a {@link MinesweeperGame} object. This class
 * contains the main method and calls the {@link MinesweeperGame} object's play method.
 */
public class MinesweeperDriver {

    /**
     * Main method in which the scanner object {@code stdIn} is created.
     * Checks whether multiple arguments are entered and displays error message if yes.
     * @param args contains the supplied command-line arguments as an array of String objects
     */
    public static void main(String[] args) {

        //creates the standard input Scanner object
        Scanner stdIn = new Scanner(System.in);

        //if multiple arguments are entered, an error message is displayed
        if (args.length == 1) {
            String seedPath = args[0];
            MinesweeperGame game = new MinesweeperGame(stdIn, seedPath);
            game.play();
        } else {
            System.out.println();
            System.err.println("Usage: MinesweeperDriver SEED_FILE_PATH");
            System.exit(1);
        }
    }
}
