public class Path extends Tile {

    public Path(int row, int column) {
        super(row, column);
        canExplode = true;
        canBeEntered = true;
        name = "Path";
    }

}
