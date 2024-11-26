/**
 * The GridManager is responsible for managing the grid of elements.
 * It initializes the grid based on a template, provides access to individual elements, and updates the grid
 */
public class GridManager {
    private final Element[][] elementGrid;

    public GridManager(int[][] gridTemplate) {
        this.elementGrid = new Element[gridTemplate.length][gridTemplate[0].length];
        initializeGrid(gridTemplate);
    }

    public void initializeGrid(int[][] gridTemplate) {
        for (int row = 0; row < gridTemplate.length; row++) {
            for (int col = 0; col < gridTemplate[row].length; col++) {
                elementGrid[row][col] = createElement(gridTemplate[row][col], row, col);
            }
        }
    }

    private Element createElement(int code, int row, int col) {
        return switch (code) {
            case 0 -> new Path(row, col);
            case 1 -> new Dirt(row, col);
            case 2 -> new Player(row, col);
            case 3 -> new NormalWall(row, col);
            case 4 -> new TitaniumWall(row, col);
            case 5 -> new MagicWall(row, col);
            case 6 -> new LockedDoor(row, col, KeyColour.RED);
            case 7 -> new Key(row, col, KeyColour.RED);
            case 8 -> new LockedDoor(row, col, KeyColour.GREEN);
            case 9 -> new Key(row, col, KeyColour.GREEN);
            case 10 -> new LockedDoor(row, col, KeyColour.YELLOW);
            case 11 -> new Key(row, col, KeyColour.YELLOW);
            case 12 -> new LockedDoor(row, col, KeyColour.BLUE);
            case 13 -> new Key(row, col, KeyColour.BLUE);
            default -> throw new IllegalArgumentException("Unknown element code: " + code);
        };
    }

    public Element[][] getElementGrid() {
        return elementGrid;
    }

    public Element getElement(int row, int col) {
        return elementGrid[row][col];
    }

    public void setElement(int row, int col, Element element) {
        elementGrid[row][col] = element;
    }
}
