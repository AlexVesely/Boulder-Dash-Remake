import javafx.scene.image.Image;

public class MagicWall extends Tile {

    public MagicWall(int x, int y) {
        super(x, y);
        image = new Image("images/MagicWall.png");
        canBeEntered = false;
        canExplode = true;
        name = "MagicWall";
    }
    public void transformRock(Element element,GridManager gridManager) {
        if (element instanceof Boulder) {
            Diamond diamond = new Diamond(element.getColumn(), element.getRow()+2);
            gridManager.setElement(element.getRow() + 2, element.getColumn(),diamond );
            gridManager.addToList(diamond);
            gridManager.removeFromList(element);
            gridManager.removeElement(element.getRow(), element.column);
        } else if (element instanceof Diamond) {
            System.out.println("Diamond entered a magic wall");
            Boulder boulder = new Boulder(element.getColumn(), element.getRow() + 2);
            gridManager.setElement(element.getRow() + 2, element.getColumn(), boulder);
            gridManager.addToList(boulder);
            gridManager.removeFromList(element);
            gridManager.removeElement(element.getRow(), element.column);
        }
    }

    @Override
    public String toString() {
        return "MagicWall";
    }
}
