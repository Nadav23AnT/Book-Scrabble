package test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Board {
  private static Board instance; // Singleton instance
  private Tile[][] boardTiles;
  private Tile.Bag tileBag = Tile.Bag.getBag(); // Accessing the singleton instance of Bag

  // Private constructor with predefined dimensions or adjustable through a
  // separate method
  private Board() {
    // Assuming predefined dimensions for the board
    int rows = 15; // Typical for Scrabble, adjust as needed
    int cols = 15; // Typical for Scrabble, adjust as needed
    this.boardTiles = new Tile[rows][cols];
  }

  // Singleton accessor without parameters
  public static Board getBoard() {
    if (instance == null) {
      instance = new Board();
    }
    return instance;
  }

  public void placeTile(int row, int col) {
    if (boardTiles[row][col] == null) {
      Tile tile = tileBag.getRand(); // Getting a random tile from the bag
      if (tile != null) {
        boardTiles[row][col] = tile; // Placing the tile on the board
      }
    }
  }

  public Tile getTile(int row, int col) {
    if (row >= 0 && row < boardTiles.length && col >= 0 && col < boardTiles[row].length) {
      return boardTiles[row][col];
    }
    return null; // Out of bounds
  }

  public boolean isTileEmpty(int row, int col) {
    return getTile(row, col) == null;
  }

  // This method is a placeholder to demonstrate how you might start implementing
  // word placement logic.
  // The actual implementation will depend on your game's rules and mechanics.
  public void placeWord(String word, int startRow, int startCol, boolean vertical) {
    // Placeholder implementation
    for (int i = 0; i < word.length(); i++) {
      int currentRow = startRow + (vertical ? i : 0);
      int currentCol = startCol + (vertical ? 0 : i);
      if (isTileEmpty(currentRow, currentCol)) {
        char letter = word.charAt(i);
        Tile tile = tileBag.getTile(letter);
        if (tile != null) {
          boardTiles[currentRow][currentCol] = tile;
        }
      }
    }
  }

  // This method should return the board state as a string for display or
  // debugging purposes.
  public String toString() {
    StringBuilder sb = new StringBuilder();
    for (Tile[] row : boardTiles) {
      for (Tile tile : row) {
        sb.append(tile == null ? "." : tile.letter);
        sb.append(" ");
      }
      sb.append("\n");
    }
    return sb.toString();
  }

  public boolean boardLegal(Word word) {
    if (word == null || word.getTiles() == null || word.getTiles().length == 0) {
      return false;
    }

    int row = word.getRow();
    int col = word.getCol();
    boolean connects = false; // True if the word connects to any existing tile
    boolean isFirstWord = isEmptyBoard();

    for (int i = 0; i < word.getTiles().length; i++) {
      int currentRow = row + (word.isVertical() ? i : 0);
      int currentCol = col + (word.isVertical() ? 0 : i);

      // Check if the word fits within the board
      if (currentRow < 0 || currentRow >= boardTiles.length || currentCol < 0 || currentCol >= boardTiles[0].length) {
        return false;
      }

      Tile existingTile = boardTiles[currentRow][currentCol];
      Tile wordTile = word.getTiles()[i];

      // Check if the word connects to existing tiles or overlaps them correctly
      if (existingTile != null && wordTile != null) {
        if (existingTile.letter != wordTile.letter) {
          return false; // The word tries to replace an existing tile
        }
        connects = true; // The word overlaps with an existing tile
      } else if (existingTile == null && wordTile != null && !isFirstWord) {
        // Check if the word is adjacent to existing tiles (for subsequent words)
        if (!isAdjacentToTile(currentRow, currentCol)) {
          return false;
        }
      }
    }

    // For the first word, ensure it crosses the center
    if (isFirstWord && !crossesCenter(word)) {
      return false;
    }

    return connects || isFirstWord; // The word is legal if it connects or if it's the first word
  }

  private boolean isAdjacentToTile(int row, int col) {
    // Check the adjacent tiles (up, down, left, right) for an existing tile
    int[][] directions = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } };
    for (int[] dir : directions) {
      int adjRow = row + dir[0];
      int adjCol = col + dir[1];
      if (adjRow >= 0 && adjRow < boardTiles.length && adjCol >= 0 && adjCol < boardTiles[0].length) {
        if (boardTiles[adjRow][adjCol] != null) {
          return true; // Found an adjacent tile
        }
      }
    }
    return false; // No adjacent tiles found
  }

  private boolean crossesCenter(Word word) {
    int centerRow = boardTiles.length / 2;
    int centerCol = boardTiles[0].length / 2;

    // Check if the word crosses the center tile
    if (word.isVertical()) {
      return word.getCol() == centerCol && word.getRow() <= centerRow
          && (word.getRow() + word.getTiles().length) > centerRow;
    } else {
      return word.getRow() == centerRow && word.getCol() <= centerCol
          && (word.getCol() + word.getTiles().length) > centerCol;
    }
  }

  private boolean isEmptyBoard() {
    // Assuming that the board is initialized with null for empty tiles
    for (Tile[] row : boardTiles) {
      for (Tile tile : row) {
        if (tile != null) {
          return false;
        }
      }
    }
    return true;
  }

  public boolean dictionaryLegal(Word word) {
    // Replace this with actual dictionary checking logic.
    return true;
  }

  public List<Word> getWords(Word word) {
    // Implement logic to find all new words formed by the placement of 'word'
    // For now, let's assume it only creates one word.
    List<Word> words = new ArrayList<>();
    words.add(word);
    return words;
  }

  public int getScore(Word word) {
    int score = 0;
    for (Tile tile : word.getTiles()) {
      score += tile.score; // Add the score of each tile to the total score
    }
    // Add logic for bonus squares, word multipliers, etc.
    return score;
  }

  public int tryPlaceWord(Word word) {
    System.out.println("Trying to place word: " + word); // Debug print
    boolean isFirstWord = isEmptyBoard(); // Check if the board is empty before placing any tiles

    if (!boardLegal(word)) {
      System.out.println("Word placement not legal: " + word); // Debug print
      return 0;
    }
    if (!dictionaryLegal(word)) {
      System.out.println("Word not in dictionary: " + word); // Debug print
      return 0;
    }

    int score = 0;
    int wordMultiplier = 1;
    boolean starTileUsed = false; // To ensure the star tile bonus is applied only once

    System.out.println("Calculating score for each tile:"); // Debug print

    for (int i = 0; i < word.getTiles().length; i++) {
      int currentRow = word.getRow() + (word.isVertical() ? i : 0);
      int currentCol = word.getCol() + (word.isVertical() ? 0 : i);

      if (word.getTiles()[i] != null) {
        Tile tile = word.getTiles()[i];
        int letterScore = getScoreForLetter(tile.letter);
        int letterMultiplier = 1;

        System.out.println("Tile " + tile.letter + " at position " + (char) ('A' + currentCol - 1) + (currentRow + 1)); // Debug
                                                                                                                        // print

        if (isDoubleLetterScore(currentRow, currentCol)) {
          letterMultiplier *= 2;
          System.out.println("Tile " + tile.letter + " on Double Letter Score");
        } else if (isTripleLetterScore(currentRow, currentCol)) {
          letterMultiplier *= 3;
          System.out.println("Tile " + tile.letter + " on Triple Letter Score");
        }

        letterScore *= letterMultiplier;
        score += letterScore;

        System.out.println("Score for " + tile.letter + " with multiplier: " + letterScore); // Debug print

        if (!starTileUsed
            && (isDoubleWordScore(currentRow, currentCol) || (isFirstWord && isStarTile(currentRow, currentCol)))) {
          wordMultiplier *= 2;
          starTileUsed = true;
          System.out.println("Word on Double Word Score tile or Star tile.");
        } else if (!starTileUsed && isTripleWordScore(currentRow, currentCol)) {
          wordMultiplier *= 3;
          starTileUsed = true;
          System.out.println("Word on Triple Word Score tile.");
        }
      } else {
        System.out.println("Null tile at position " + (char) ('A' + currentCol - 1) + (currentRow + 1)); // Debug print
      }
    }

    score *= wordMultiplier; // Apply word multipliers
    System.out.println("Total calculated score after word multipliers: " + score); // Debug print
    return score;
  }

  // Helper methods for each bonus type
  private boolean isDoubleLetterScore(int row, int col) {
    return Set.of(
        // Convert given positions like 'D1' to (row, col) starting from (1,1) to
        // (15,15)
        "D1", "L1", "G3", "I3", "A4", "H4", "O4", "C7", "G7", "I7", "M7",
        "C9", "G9", "I9", "M9", "D8", "L8", "A12", "H12", "O12", "G13",
        "I13", "D15", "L15").contains(convertToPosition(row, col));
  }

  private boolean isTripleLetterScore(int row, int col) {
    return Set.of(
        "F2", "J2", "B6", "F6", "J6", "N6", "B10", "F10", "J10", "N10",
        "F14", "J14").contains(convertToPosition(row, col));
  }

  private boolean isDoubleWordScore(int row, int col) {
    return Set.of(
        "B2", "C3", "D4", "E5", "K5", "L4", "M3", "N2", "B14", "C13",
        "D12", "E11", "K11", "L12", "M13", "N14").contains(convertToPosition(row, col));
  }

  private boolean isTripleWordScore(int row, int col) {
    return Set.of(
        "A1", "H1", "O1", "A8", "O8", "A15", "H15", "O15").contains(convertToPosition(row, col));
  }

  private boolean isStarTile(int row, int col) {
    boolean isStar = (row == 7 && col == 7); // 0-indexed
    System.out.println("Checking if star tile used at position " + (row + 1) + "," + (col + 1) + ": " + isStar);
    return isStar;
  }

  // Helper method to convert row and column indices to the format used in the
  // sets
  private String convertToPosition(int row, int col) {
    // Converts numeric indices to letter-number format, e.g., (1,1) -> "A1"
    return "" + (char) ('A' + col - 1) + row;
  }

  private int getScoreForLetter(char letter) {
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
        return 0; // Handle unexpected characters
    }
  }
}
