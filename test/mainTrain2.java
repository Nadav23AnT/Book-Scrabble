package test;
import java.util.Arrays;

import test.Tile.Bag;

public class mainTrain2 {

  public static void testBag() {
    Bag b = Tile.Bag.getBag();
    Bag b1 = Tile.Bag.getBag();

    // Singleton check
    if (b1 != b)
      System.out.println("Bag is not a Singleton (-5)");

    // Clone check
    int[] q0 = b.getQuantities();
    q0[0] += 1;
    int[] q1 = b.getQuantities();
    if (q0[0] == q1[0])
      System.out.println("getQuantities should return a clone (-5)");

    // Exhaustive extraction check


// After extracting all tiles, the bag should be empty
if (b.size() != 0) {
    System.out.println("Bag is not empty after extracting all tiles (-2)");
}

// Attempt to extract one more tile to ensure getRand() behaves correctly when the bag is empty
if (b.getRand() != null) {
    System.out.println("getRand did not return null when the bag was confirmed to be empty (-2)");
}

int totalExtracted = 0;
while (b.size() > 0) {
    Tile t = b.getRand();
    if (t != null) {
        totalExtracted++;  // Increment only when a tile is successfully extracted
    }
}

int expectedTotalTiles = Arrays.stream(b.initialQuantities).sum();  // Summing up the initial quantities for comparison
if (totalExtracted != expectedTotalTiles) {
    System.out.println("The total number of extracted tiles (" + totalExtracted + ") does not match the expected total (" + expectedTotalTiles + ") (-2)");
}

}

  public static void main(String[] args) {
    testBag();
    // testBoard();
    System.out.println("done");
  }
}
