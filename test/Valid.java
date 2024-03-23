package test;

import java.util.ArrayList;

public class Valid {

    public static void main(String[] args) {
        testSingletonPattern();
        testTileDrawing();
        testTileReplacing();
        testWordCreationAndEquality();
        testBoardFunctionality();
    }

    private static void testWordCreationAndEquality() {
        System.out.println("\nTesting Word Creation and Equality:");

        Tile.Bag bag = Tile.Bag.getBag();
        Tile tile1 = bag.getTile('A');
        Tile tile2 = bag.getTile('B');
        Tile tile3 = bag.getTile('C');

        // Assuming we have these tiles in the bag for the test
        if (tile1 != null && tile2 != null && tile3 != null) {
            Tile[] tiles = { tile1, tile2, tile3 };
            Word word1 = new Word(tiles, 7, 7, true);
            Word word2 = new Word(tiles, 7, 7, true);

            System.out.println("Word1: " + word1);
            System.out.println("Word2: " + word2);

            System.out.println("Words are equal: " + word1.equals(word2));
            System.out.println("Word1 hashcode: " + word1.hashCode());
            System.out.println("Word2 hashcode: " + word2.hashCode());

            // Check the getters
            System.out.println("Word1 tiles match original tiles: " + java.util.Arrays.equals(word1.getTiles(), tiles));
            System.out.println("Word1 row is 7: " + (word1.getRow() == 7));
            System.out.println("Word1 col is 7: " + (word1.getCol() == 7));
            System.out.println("Word1 is vertical: " + (word1.isVertical()));
        } else {
            System.out.println("Could not create words for comparison. Ensure there are enough tiles in the bag.");
        }
    }

    private static void testSingletonPattern() {
        Tile.Bag bag1 = Tile.Bag.getBag();
        Tile.Bag bag2 = Tile.Bag.getBag();

        System.out.println("Testing Singleton Pattern:");
        System.out.println("Bag1 and Bag2 are the same instance: " + (bag1 == bag2));
    }

    private static void testTileDrawing() {
        Tile.Bag bag = Tile.Bag.getBag();
        System.out.println("\nTesting Tile Drawing:");
        Tile drawnTile = bag.getRand();
        if (drawnTile != null) {
            System.out.println("Drawn Tile: " + drawnTile.letter + " with score: " + drawnTile.score);
        } else {
            System.out.println("No Tile could be drawn (Bag might be empty).");
        }
    }

    private static void testTileReplacing() {
        Tile.Bag bag = Tile.Bag.getBag();
        System.out.println("\nTesting Tile Replacing:");
        int sizeBefore = bag.size();
        Tile tile = new Tile('A', 1); // Assuming 'A' with a score of 1 for testing purposes.
        bag.put(tile);
        int sizeAfter = bag.size();

        System.out.println("Size before replacing a tile: " + sizeBefore);
        System.out.println("Size after replacing a tile: " + sizeAfter);
        System.out.println("Size should be the same or one greater if 'A' was drawn before: " +
                (sizeAfter == sizeBefore || sizeAfter == sizeBefore + 1));
    }

    private static void testBoardFunctionality() {
        System.out.println("\nTesting Board Functionality:");

        Board board = Board.getBoard();
        Tile.Bag bag = Tile.Bag.getBag();

        // Create a word to place on the board
        Tile tile1 = bag.getTile('H');
        Tile tile2 = bag.getTile('E');
        Tile tile3 = bag.getTile('L');
        Tile tile4 = bag.getTile('L');
        Tile tile5 = bag.getTile('O');

        if (tile1 != null && tile2 != null && tile3 != null && tile4 != null && tile5 != null) {
            Tile[] helloTiles = { tile1, tile2, tile3, tile4, tile5 };
            Word word = new Word(helloTiles, 7, 7, false); // Place "HELLO" starting at (7,7) horizontally

            System.out.println("Board Legal for 'HELLO': " + board.boardLegal(word));
            System.out.println("Dictionary Legal for 'HELLO': " + board.dictionaryLegal(word));

            int score = board.tryPlaceWord(word);
            System.out.println("Score for placing 'HELLO': " + score);

            ArrayList<Word> newWords = board.getWords(word);
            System.out.println("New words created by placing 'HELLO': " + newWords.size());
            for (Word newWord : newWords) {
                System.out.println(newWord);
            }
        } else {
            System.out.println("Could not create the word 'HELLO' for testing.");
        }
    }
}
