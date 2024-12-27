import javafx.scene.image.Image;

/**
 * Represents an Exit tile in the game.
 * Only one exit can be created per level.
 * Initially, cannot be entered/walked over or exploded.
 * @author Rhys Llewellyn
 * @author Joshua Aka
 */
public class Exit extends Element {
    /**
     * Allows us to keep track of whether an exit has already been created.
     */
    private static boolean exitExists = false;

    /**
     * Creates a new Exit tile at the specified row and column (x and y).
     * @param row the row position of the tile
     * @param column the column position of the tile
     * @throws IllegalStateException if an exit already exists
     */
    public Exit(final int row, final int column) {
        super(row, column);
        // Ensures only one exit can be created per level
        if (exitExists) {
            throw new IllegalStateException("An exit already exists!");
        }
        image = new Image("images/exit.png");
        canBeEntered = false;  // Initially can't be entered
        canExplode = false;
        name = "Exit";
        exitExists = true;
    }

    /**
     * Resets the exit existence flag to allow the creation of a new exit.
     */
    public static void toggleFalseExitExists() {
        exitExists = false;
    }

    /**
     * Method to unlock the Exit so it can be entered
     */
    public void unlock() {
        this.canBeEntered = true;
        System.out.println("Exit Unlocked!");
    }

    /**
     * Locks the exit, preventing it from being entered.
     */
    public void lock() {
        this.canBeEntered = false;
        System.out.println("Exit Locked!");
    }

    /**
     * Checks if the exit is unlocked.
     * @return true if the exit can be entered, false otherwise
     */
    public boolean isLocked() {
        return canBeEntered;
    }

    /**
     * This returns a string representation of the exit and its locked status.
     * @return the string representation of the exit
     */
    @Override
    public String toString() {
        return super.toString() + " Unlocked: " + canBeEntered;
    }
}
