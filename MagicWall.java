import javafx.scene.image.Image;

/**
 * Represents a Magic Wall tile in the game.
 * This tile cannot be entered/walked over but can explode.
 * It is represented by an image found at "images/MagicWall.png".
 * @author Omar Sanad
 * @author Rhys Llewellyn
 */
public class MagicWall extends Element {

    /**
     * Creates a new Magic Wall tile at the specified row and column.
     * @param row the row (X co-ord)position of the tile
     * @param column the column (Y co-ord) position of the tile
     */
    public MagicWall(final int row, final int column) {
        super(row, column);
        image = new Image("images/magic-wall.png");
        canBeEntered = false;
        canExplode = true;
        name = "MagicWall";
    }

    /**
     * Transforms a rock element (Boulder or Diamond)
     * when it enters a Magic Wall.
     * @param element the element to transform
     * @param gridManager the grid manager to handle element transformations
     */
    public void transformRock(final Element element, final GridManager gridManager) {
        if (element instanceof Boulder) {
            //disable old rock from falling
            gridManager.removeFromList(element);
            // replace rock location above the magicWall with a path
            gridManager.removeElement(element.getRow(), element.getColumn());
            Diamond diamond = new Diamond(element.getRow() + 1,element.getColumn() );
            gridManager.addToList(diamond);
            diamond.gainMomentum();
        } else if (element instanceof Diamond) {
            //disable old rock from falling
            gridManager.removeFromList(element);
            // replace rock location above the magicWall with a path
            gridManager.removeElement(element.getRow(), element.getColumn());
            Boulder boulder = new Boulder(element.getRow() + 1,element.getColumn() );
            gridManager.addToList(boulder);
            boulder.gainMomentum();
        }
    }

    /**
     * Returns the name of the tile as a string.
     * @return the name "MagicWall"
     */
    @Override
    public String toString() {
        return "MagicWall";
    }
}
