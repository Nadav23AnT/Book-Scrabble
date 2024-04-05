package test;

public class WordArr {
    private final Tile[] tiles; // Tiles making up the word
    private final int row; // Starting row on the board
    private final int col; // Starting column on the board
    private final boolean vertical; // Orientation of the word

    // Constructor to initialize the fields
    public Word(Tile[] tiles, int row, int col, boolean vertical) {
        this.tiles = tiles.clone(); // Clone to ensure immutability
        this.row = row;
        this.col = col;
        this.vertical = vertical;
    }

    // Getters
    public Tile[] getTiles() {
        return tiles.clone(); // Return a clone to preserve immutability
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public boolean isVertical() {
        return vertical;
    }

    // Override equals method
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        Word other = (Word) obj;
        return row == other.row && col == other.col && vertical == other.vertical && java.util.Arrays.equals(tiles, other.tiles);
    }

    // Override hashCode method
    @Override
    public int hashCode() {
        int result = java.util.Objects.hash(row, col, vertical);
        result = 31 * result + java.util.Arrays.hashCode(tiles);
        return result;
    }
}
