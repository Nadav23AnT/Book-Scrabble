package test;

public class Tile {
  public final char letter;
  public final int score;

  private Tile(char letter, int score) {
    this.letter = letter;
    this.score = score;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    Tile tile = (Tile) o;
    return letter == tile.letter && score == tile.score;
  }

  @Override
  public int hashCode() {
    int result = Character.hashCode(letter);
    result = 31 * result + Integer.hashCode(score);
    return result;
  }

  // Static inner class Bag
  public static class Bag {
    private static final int[] initialQuantities = {
        9, 2, 2, 4, 12, 2, 3, 2, 9, 1, 1, 4, 2, 6, 8, 2, 1, 6, 4, 6, 4, 2, 2, 1, 2, 1
    };
    private int[] quantities = initialQuantities.clone();
    private static final Tile[] tiles = new Tile[26];
    private static Bag instance = null;

    static {
      for (char c = 'A'; c <= 'Z'; c++) {
        int score = getScoreForLetter(c);
        tiles[c - 'A'] = new Tile(c, score);
      }
    }

    private Bag() {
    }

    public static Bag getBag() {
      if (instance == null) {
        instance = new Bag();
      }
      return instance;
    }

    public Tile getRand() {
      int totalTiles = size();
      if (totalTiles == 0) {
        return null;
      }

      int index = (int) (Math.random() * totalTiles);
      for (int i = 0; i < quantities.length; i++) {
        if (index < quantities[i]) {
          quantities[i]--;
          return tiles[i];
        }
        index -= quantities[i];
      }
      return null; // Should never reach here if logic is correct
    }

    public Tile getTile(char letter) {
      int index = Character.toUpperCase(letter) - 'A';
      if (index < 0 || index >= quantities.length || quantities[index] == 0) {
        return null;
      }
      quantities[index]--;
      return tiles[index];
    }

    public void put(Tile tile) {
      int index = tile.letter - 'A';
      if (quantities[index] < initialQuantities[index]) {
        quantities[index]++;
      }
    }

    public int size() {
      int sum = 0;
      for (int quantity : quantities) {
        sum += quantity;
      }
      return sum;
    }

    public int[] getQuantities() {
      return quantities.clone();
    }

    private static int getScoreForLetter(char letter) {
      switch (letter) {
        case 'A':
        case 'E':
        case 'I':
        case 'O':
        case 'U':
        case 'L':
        case 'N':
        case 'S':
        case 'T':
        case 'R':
          return 1;
        case 'D':
        case 'G':
          return 2;
        case 'B':
        case 'C':
        case 'M':
        case 'P':
          return 3;
        case 'F':
        case 'H':
        case 'V':
        case 'W':
        case 'Y':
          return 4;
        case 'K':
          return 5;
        case 'J':
        case 'X':
          return 8;
        case 'Q':
        case 'Z':
          return 10;
        default:
          return 0; // Handle unexpected characters
      }
    }
  }

}
