import java.util.ArrayList;
import javafx.scene.image.Image;

/**
 * The Player class represents a player in the game, managing their position
 * on the grid, inventory of keys, diamonds collected, and game status.
 * @author Omar Sanad
 */
public class Player extends Element {

    public static boolean dropDiamond = true;

    private ArrayList<KeyColour> keyInventory;
    private int diamondCount = 0;
    private boolean hasEnoughDiamonds;
    private boolean hasPlayerWon;
    private int diamondsRequired;
    public boolean lookingRight;

    /**
     * Constructs a new Player object with the specified row and column positions.
     * @param row the initial row position of the player.
     * @param column the initial column position of the player.
     */
    public Player(int row, int column) {
        super(row, column);
        canExplode = true;
        image = new Image("images/player.png");
        canBeEntered = true;
        name = "Player";
        this.keyInventory = new ArrayList<>();
        hasEnoughDiamonds = false;
    }

    /**
     * Updates the player's image at every call of the animation.
     * If the player is currently facing right, they will be made to face left and
     * vice versa.
     */
    public void imageAnimation() {
        lookingRight = !lookingRight;
        if (lookingRight) {
            image = new Image("images/player-looking-right.png");
        } else if (!lookingRight) {
            image = new Image("images/player.png");
        }
    }

    /**
     * Adds a key to the player's inventory.
     * @param key the key to collect
     */
    public void collectKey(Key key) {
        System.out.println(key);
        keyInventory.add(key.getColour());
    }

    /**
     * Clears all keys from the player's inventory.
     */
    public void resetKeyInventory() {
        System.out.println("Key inventory reset");
        keyInventory.clear();
    }

    /**
     * Checks if the player has a key of the specified color.
     * @param colour the color of the key to check.
     * @return true if the player has the key, false otherwise.
     */
    public boolean hasKey(KeyColour colour) {
        return keyInventory.contains(colour);
    }

    /**
     * Removes a key of the specified color from the player's inventory.
     * @param colour the color of the key to use.
     */
    public void useKey(KeyColour colour) {
        keyInventory.remove(colour);
    }

    /**
     * Gets the number of diamonds collected by the player.
     * @return the number of diamonds collected.
     */
    public int getDiamondCount() {
        return diamondCount;
    }

    /**
     * Sets the number of diamonds collected by the player.
     * @param diamondCount the new diamond count.
     */
    public void setDiamondCount(int diamondCount) {
        this.diamondCount = diamondCount;
    }

    /**
     * Checks if the player has enough diamonds to finish the level.
     * @return true if the player has enough diamonds, false otherwise.
     */
    public boolean isHasEnoughDiamonds() {
        return hasEnoughDiamonds;
    }


    /**
     * Sets whether the player has enough diamonds to finish the level.
     *
     * @param hasEnoughDiamonds true if the player has enough diamonds, false otherwise
     */
    public void setHasEnoughDiamonds(boolean hasEnoughDiamonds) {
        this.hasEnoughDiamonds = hasEnoughDiamonds;
    }

    /**
     * Checks if the player has collected the required number of diamonds to finish the level.
     * If so, updates the player's status.
     */
    public void checkDiamonds() {
        if (getDiamondCount() >= diamondsRequired) {
            setHasEnoughDiamonds(true);
            System.out.println("You have enough Diamonds to finish the level!");
        }
    }

    /**
     * Moves the player to a new position on the grid if the move is valid.
     * Updates the player's current position and the grid elements accordingly.
     * @param newRow      the new row position for the player
     * @param newColumn   the new column position for the player
     * @param gridManager the grid manager to update the player's position in the grid
     */
    public void movePlayer(int newRow, int newColumn, GridManager gridManager) {
        if (isValidMove(newRow, newColumn, gridManager)) {
            // Replace the player's current position with a Path
            gridManager.setElement(this.getRow(), this.getColumn(), new Path(this.getRow(), this.getColumn()));
            gridManager.removeElement(this.getRow(), this.getColumn());
            // Update the grid and the player's position
            gridManager.setElement(newRow, newColumn, this);
            this.setRow(newRow);
            this.setColumn(newColumn);
        }
    }

    /**
     * Determines if the player's desired move to a specified location on the grid is valid.
     * @param targetRow  the row player intends to move to
     * @param targetColumn the column the player intends to move to
     * @param gridManager the grid manager responsible for managing the grid's state and elements
     * @return true if the move is valid according to the game's rules, false otherwise
     */
    private boolean isValidMove(int targetRow, int targetColumn, GridManager gridManager) {
        Element[][] grid = gridManager.getElementGrid();
        // Ensure the move is within bounds
        if (targetRow < 0 || targetRow >= grid.length || targetColumn < 0 || targetColumn >= grid[0].length) {
            return false;
        }

        // Check if the target cell is enterable (e.g., a Path)
        if (grid[targetRow][targetColumn] instanceof Dirt || grid[targetRow][targetColumn] instanceof Path) {
            return true;
        }

        // Check if the target cell contains a Boulder
        if (targetRow == this.getRow() && grid[targetRow][targetColumn] instanceof Boulder) {
            // Determine the direction of the push
            int pushBoulderToRow = targetRow;
            int pushBoulderToColumn;
            //right or left depending on
            if (targetColumn > this.getColumn()) {
                pushBoulderToColumn = targetColumn + 1;
            } else {
                pushBoulderToColumn = targetColumn - 1;
            }

            // Ensure the adjacent cell (where the boulder would move) is within bounds and is a Path
            if (pushBoulderToColumn >= 0 && pushBoulderToColumn < grid[0].length
                    && gridManager.getElement(pushBoulderToRow, pushBoulderToColumn) instanceof Path) {
                // Move the boulder to the new position
                Boulder boulder = (Boulder)gridManager.getElement(targetRow, targetColumn);

                gridManager.setElement(pushBoulderToRow, pushBoulderToColumn, boulder);
                gridManager.setElement(targetRow, targetColumn, new Path(targetRow, targetColumn));
                boulder.setColumn(pushBoulderToColumn);
                return true;
            }
        }
        if (grid[targetRow][targetColumn] instanceof Diamond) {
            diamondCount++;
            gridManager.removeFromList(gridManager.getElement(targetRow, targetColumn)); //remove from diamonds list to stop falling
            gridManager.removeElement(targetRow, targetColumn);
            grid[targetRow][targetColumn] = null;
            System.out.println("Diamond removed");
            checkDiamonds();
            return true;
        }
        if (grid[targetRow][targetColumn] instanceof Key key) {
            collectKey(key);
            return true;
        }
        if (grid[targetRow][targetColumn] instanceof LockedDoor lockedDoor) {
            if (hasKey(lockedDoor.getColour())) {
                useKey(lockedDoor.getColour());
                lockedDoor.unlock();
                return true;
            } else if (hasKey(KeyColour.RAINBOW)) {
                useKey(KeyColour.RAINBOW);
                lockedDoor.unlock();
                return true;
            } else {
                System.out.println("Player needs a " + lockedDoor.getColour() + " key to open this door.");
                return false;
            }
        }
        if (grid[targetRow][targetColumn] instanceof Exit exit) {
            checkDiamonds();
            if (isHasEnoughDiamonds()) {
                exit.unlock();
                hasPlayerWon = true;
                return true;
            } else {
                System.out.println("Player needs more diamonds to enter the exit!");
                return false;
            }

            // Otherwise, the move is invalid
        }
        return false;
    }


    /**
     * Reset collected diamond count when user restarts the game.
     */
    public void resetDiamondCountStatus() {
        this.diamondCount = 0;
        this.hasEnoughDiamonds = false;
        System.out.println("Diamond count reset to: " + this.diamondCount);
    }

    /**
     * Checks if the player has won the level.
     * @return true if player has won, false otherwise.
     */
    public boolean hasPlayerWon() {
        return hasPlayerWon;
    }

    /**
     * Gets the player's inventory of keys.
     * @return KeyInventory.
     */
    public ArrayList<KeyColour> getKeyInventory() {
        return keyInventory;
    }

    /**
     * Sets the player's inventory of keys.
     * @param keyInventory the player's new key inventory.
     */
    public void setKeyInventory(ArrayList<KeyColour> keyInventory) {
        this.keyInventory = keyInventory;
    }

    /**
     * Sets the number of diamonds required to finish the level.
     * @param diamondsRequired the number of diamonds required to finish the level
     */
    public void setDiamondsRequired(int diamondsRequired) {
        this.diamondsRequired = diamondsRequired;
    }

    /**
     * Returns a string representation of the player, including the number of diamonds collected.
     * @return a string showing the number of collected diamonds
     */
    @Override
    public String toString() {
        return "Player CollectedDiamonds: " + diamondCount;
    }

}
