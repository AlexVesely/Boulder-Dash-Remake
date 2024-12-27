import javafx.scene.image.Image;

/**
 * Represents a Normal Wall tile in the game.
 * This tile cannot be entered/walked over but can explode.
 * It is represented by an image located at "images/normalwall.png".
 * @author Joshua Aka
 * @author Rhys Llewellyn
 */
public class NormalWall extends Element {

    /**
     * Creates a new Normal Wall tile at the specified row(X co-ord) and column(Y co-ord).
     *
     * @param row the row position (X co-ord) of the tile
     * @param column the column (Y co-ord) position of the tile
     */
    public NormalWall(final int row, final int column) {
        super(row, column);
        image = new Image("images/normal-wall.png");
        canBeEntered = false;
        canExplode = true;
        name = "NormalWall";
    }

    /**
     * Returns the name of the tile as a string.
     *
     * @return the name "NormalWall"
     */
    @Override
    public String toString() {
        return "NormalWall";
    }
}
