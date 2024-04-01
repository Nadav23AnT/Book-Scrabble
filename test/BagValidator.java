package test;

import test.Tile.Bag;

public class BagValidator {

  public static void testSingleton() {
      Bag bag1 = Tile.Bag.getBag();
      Bag bag2 = Tile.Bag.getBag();

      if (bag1 != bag2) {
          System.out.println("Singleton test failed: Two instances of Bag detected.");
      } else {
          System.out.println("Singleton test passed.");
      }
  }

  public static void testDrawAndPut() {
      Bag bag = Tile.Bag.getBag();
      Tile tile = bag.getRand();

      if (tile == null) {
          System.out.println("Draw test failed: No tile drawn.");
          return;
      }

      System.out.println("Draw test passed: Drew tile " + tile.letter);

      int initialQuantity = bag.getQuantities()[tile.letter - 'A'];
      bag.put(tile);
      int updatedQuantity = bag.getQuantities()[tile.letter - 'A'];

      if (updatedQuantity != initialQuantity) {
          System.out.println("Put test passed: Tile quantity updated correctly.");
      } else {
          System.out.println("Put test failed: Tile quantity not updated correctly.");
      }
  }

  public static void testQuantities() {
      Bag bag = Tile.Bag.getBag();
      int[] quantities = bag.getQuantities();

      int sum = 0;
      for (int quantity : quantities) {
          sum += quantity;
      }

      if (sum == 98) {
          System.out.println("Quantities test passed: Correct total number of tiles.");
      } else {
          System.out.println("Quantities test failed: Incorrect total number of tiles.");
      }
  }

  public static void main(String[] args) {
      testSingleton();
      testDrawAndPut();
      testQuantities();
  }
}
