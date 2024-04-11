package test;

public class Tile {
  // Immutable fields
  public final char letter; // The letter on the tile
  public final int score; // The score value of the tile

  // Private constructor for immutability
  private Tile(char letter, int score) {
    this.letter = letter;
    this.score = score;
  }

  // Getters
  public char getLetter() {
    return letter;
  }

  public int getScore() {
    return score;
  }

  // Override equals method
  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null || getClass() != obj.getClass())
      return false;
    Tile tile = (Tile) obj;
    return letter == tile.letter && score == tile.score;
  }

  // Override hashCode method
  @Override
  public int hashCode() {
    return 31 * letter + score;
  }

  // Static inner class Bag
  public static class Bag {
    // Singleton instance
    private static Bag instance = null;

    // Array to hold the count of each letter tile
    private int[] quantities = new int[26];

    // Array to hold the initial count of each letter tile
    private int[] initialQuantities = new int[26];

    // Array to hold Tile objects for each letter
    private Tile[] tiles = new Tile[26];

    // Private constructor for singleton
    private Bag() {
      quantities = new int[]{9, 2, 2, 4, 12, 2, 3, 2, 9, 1, 1, 4, 2, 6, 8, 2, 1, 6, 4, 6, 4, 2, 2, 1, 2, 1}; // Quantities for A-Z
        System.arraycopy(quantities, 0, initialQuantities, 0, quantities.length);

      // Initialize quantities and tiles based on predefined game rules
      // Example initialization (actual game values needed)
      
      // Initialize Tile objects with letter and score for each letter
      tiles[0] = new Tile('A', 1); // Score is hypothetical
      tiles[1] = new Tile('B', 3);
      tiles[2] = new Tile('C', 3);
      tiles[3] = new Tile('D', 2);
      tiles[4] = new Tile('E', 1);
      tiles[5] = new Tile('F', 4);
      tiles[6] = new Tile('G', 2);
      tiles[7] = new Tile('H', 4);
      tiles[8] = new Tile('I', 1);
      tiles[9] = new Tile('J', 8);
      tiles[10] = new Tile('K', 5);
      tiles[11] = new Tile('L', 1);
      tiles[12] = new Tile('M', 3);
      tiles[13] = new Tile('N', 1);
      tiles[14] = new Tile('O', 1);
      tiles[15] = new Tile('P', 3);
      tiles[16] = new Tile('Q', 10);
      tiles[17] = new Tile('R', 1);
      tiles[18] = new Tile('S', 1);
      tiles[19] = new Tile('T', 1);
      tiles[20] = new Tile('U', 1);
      tiles[21] = new Tile('V', 4);
      tiles[22] = new Tile('W', 4);
      tiles[23] = new Tile('X', 8);
      tiles[24] = new Tile('Y', 4);
      tiles[25] = new Tile('Z', 10);

    }

    // Public method to get the singleton instance
    public static Bag getBag() {
      if (instance == null) {
        instance = new Bag();
      }
      return instance;
    }

    // Method to get a random Tile from the bag
    public Tile getRand() {
      int totalTiles = size();
      if (totalTiles == 0)
        return null;

      int randomIndex = (int) (Math.random() * totalTiles);
      for (int i = 0; i < quantities.length; i++) {
        if (quantities[i] == 0)
          continue;

        randomIndex -= quantities[i];
        if (randomIndex < 0) {
          quantities[i]--;
          return tiles[i];
        }
      }
      return null;
    }

    public Tile getTile(char letter) {
      int index = letter - 'A';
      if (index >= 0 && index < quantities.length && quantities[index] > 0) {
        quantities[index]--;
        return tiles[index];
      }
      return null;
    }

    public void put(Tile tile) {
      int index = tile.letter - 'A';
      if (index >= 0 && index < quantities.length) {
        // Ensure the quantity does not exceed the initial value.
        // The initial quantities are defined when the Bag is instantiated.
        if (quantities[index] < initialQuantities[index]) {
          quantities[index]++;
        }
      }
    }

    // Method to get the total number of Tiles in the bag
    public int size() {
      int total = 0;
      for (int quantity : quantities) {
        total += quantity;
      }
      return total;
    }

    // Method to get a copy of the quantities array (for testing)
    public int[] getQuantities() {
      return quantities.clone();
    }
  }
}
