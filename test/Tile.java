package test;

import java.util.Random;

public class Tile {
  public final char letter;
  public final int score;

  Tile(char letter, int score) {
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

    if (letter != tile.letter)
      return false;
    return score == tile.score;
  }

  @Override
  public int hashCode() {
    int result = (int) letter;
    result = 31 * result + score;
    return result;
  }

  public static class Bag {
    private static final int[] initialQuantities = {
        9, 2, 2, 4, 12, 2, 3, 2, 9, 1, 1, 4, 2, 6, 8, 2, 1, 6, 4, 6, 4, 2, 2, 1, 2, 1
    };
    private static final int[] tileQuantities = initialQuantities.clone();
    private static final Tile[] tiles;
    private static Bag instance = null;

    static {
      tiles = new Tile[26];
      for (char c = 'A'; c <= 'Z'; c++) {
        int score = getScoreForLetter(c);
        tiles[c - 'A'] = new Tile(c, score);
      }
    }

    private Bag() {
      // Private constructor for Singleton
    }

    public static Tile createTile(char letter, int score) {
      // Validation for letter and score can be added if needed.
      return new Tile(letter, score);
    }

    public static Bag getBag() {
      if (instance == null) {
        instance = new Bag();
      }
      return instance;
    }

    public Tile getRand() {
      Random rand = new Random();
      while (true) {
        int index = rand.nextInt(tileQuantities.length);
        if (tileQuantities[index] > 0) {
          tileQuantities[index]--;
          return tiles[index];
        } else if (size() == 0) {
          return null;
        }
      }
    }

    public Tile getTile(char letter) {
      if (!Character.isUpperCase(letter)) {
          System.out.println("Invalid character or lowercase: " + letter);
          return null;
      }
  
      int index = letter - 'A';
      if (tileQuantities[index] > 0) {
          tileQuantities[index]--;
          System.out.println("Returning tile for: " + letter);
          return tiles[index];
      } else {
          System.out.println("Out of tiles for: " + letter);
          return null;
      }
  }
  

    public void put(Tile tile) {
      int index = tile.letter - 'A';
      if (tileQuantities[index] < initialQuantities[index]) {
        tileQuantities[index]++;
      }
    }

    public int size() {
      int size = 0;
      for (int quantity : tileQuantities) {
        size += quantity;
      }
      return size;
    }

    public int[] getQuantities() {
      return tileQuantities.clone();
    }

    private static int getScoreForLetter(char letter) {
      switch (Character.toUpperCase(letter)) {
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
          return 0; // Might be used for blank tiles or errors
      }
    }

  }

}
