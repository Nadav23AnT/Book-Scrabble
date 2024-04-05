package test;

public class Word {
    private String word;
    private int row;
    private int col;
    private boolean isVertical;
    private Tile[] tiles;

    public Word(Tile[] tiles, int row, int col, boolean isVertical) {
        this.tiles = tiles;
        this.row = row;
        this.col = col;
        this.isVertical = isVertical;
        System.out.println("Creating Word: " + getWord() + " at (" + row + "," + col + ") vertical: " + isVertical);
    }

    public boolean isVertical() {
        return isVertical;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public Tile[] getTiles() {
        return tiles.clone();
    }

    public String getWord() {
        StringBuilder sb = new StringBuilder();
        for (Tile tile : tiles) {
            sb.append(tile != null ? tile.getLetter() : "_");
        }
        System.out.println("Assembling word: " + sb);
        return sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Word)) return false;
        Word other = (Word) obj;
        return row == other.row && col == other.col && isVertical == other.isVertical &&
               word.equals(other.word) && java.util.Arrays.equals(tiles, other.tiles);
    }

    @Override
    public int hashCode() {
        int result = word.hashCode();
        result = 31 * result + row;
        result = 31 * result + col;
        result = 31 * result + (isVertical ? 1 : 0);
        result = 31 * result + java.util.Arrays.hashCode(tiles);
        return result;
    }
}