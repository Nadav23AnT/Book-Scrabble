package archive2.test;

import java.util.Arrays;
import java.util.Objects;

public class Word {
  private final Tile[] tiles;
  private final int row;
  private final int col;
  private final boolean vertical;

  public Word(Tile[] tiles, int row, int col, boolean vertical) {
    this.tiles = Arrays.copyOf(tiles, tiles.length); // Ensure the array is immutable by copying it
    this.row = row;
    this.col = col;
    this.vertical = vertical;
  }

  public Tile[] getTiles() {
    return Arrays.copyOf(tiles, tiles.length); // Return a copy to maintain immutability
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

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    Word word = (Word) o;
    return row == word.row &&
        col == word.col &&
        vertical == word.vertical &&
        Arrays.equals(tiles, word.tiles);
  }

  @Override
  public int hashCode() {
    int result = Objects.hash(row, col, vertical);
    result = 31 * result + Arrays.hashCode(tiles);
    return result;
  }

  // Optional: Override toString for debugging purposes
  @Override
  public String toString() {
    return "Word{" +
        "tiles=" + Arrays.toString(tiles) +
        ", row=" + row +
        ", col=" + col +
        ", vertical=" + vertical +
        '}';
  }
}
