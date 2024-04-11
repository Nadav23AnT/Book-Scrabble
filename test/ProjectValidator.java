package test;

public class ProjectValidator {

    private static Tile.Bag bag = Tile.Bag.getBag();

    public static void main(String[] args) {
        testTilePlacement();
        testWordCreation();
        testScoreCalculation();
    }

    private static void testTilePlacement() {
        System.out.println("Testing Tile Placement...");
        Board board = Board.getBoard();
        Tile tile = bag.getTile('H');  // Obtaining a tile from the bag
        board.placeTile(7, 7, tile);

        if (board.getTiles(7, 7) != null && board.getTiles(7, 7).getLetter() == 'H') {
            System.out.println("Tile Placement Passed");
        } else {
            System.out.println("Tile Placement Failed");
        }
    }

    private static void testWordCreation() {
        System.out.println("Testing Word Creation...");
        Board board = Board.getBoard();
        Tile[] tiles = {bag.getTile('H'), bag.getTile('E'), bag.getTile('L'), bag.getTile('L'), bag.getTile('O')};
        Word word = new Word(tiles, 7, 7, false);
        board.tryPlaceWord(word);

        String formedWord = board.getTiles(7, 7).getLetter() + "" +
                            board.getTiles(7, 8).getLetter() + "" +
                            board.getTiles(7, 9).getLetter() + "" +
                            board.getTiles(7, 10).getLetter() + "" +
                            board.getTiles(7, 11).getLetter();

        if ("HELLO".equals(formedWord)) {
            System.out.println("Word Creation Passed");
        } else {
            System.out.println("Word Creation Failed");
        }
    }

    private static void testScoreCalculation() {
        System.out.println("Testing Score Calculation...");
        Board board = Board.getBoard();
        Tile[] tiles = {bag.getTile('W'), bag.getTile('O'), bag.getTile('R'), bag.getTile('L'), bag.getTile('D')};
        Word word = new Word(tiles, 7, 12, false);
        int score = board.tryPlaceWord(word);

        // Assuming the score for 'WORLD' is 8 (no multipliers applied in this test)
        if (score == 8) {
            System.out.println("Score Calculation Passed");
        } else {
            System.out.println("Score Calculation Failed");
        }
    }
}
