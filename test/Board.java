package test;

import java.util.ArrayList;
import java.util.List;

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
    int row = word.getRow();
    int col = word.getCol();
    boolean connects = false; // This will be true if the word connects to any existing tile.
    Tile[] wordTiles = word.getTiles();

    for (int i = 0; i < wordTiles.length; i++) {
      int currentRow = row + (word.isVertical() ? i : 0);
      int currentCol = col + (word.isVertical() ? 0 : i);

      // Check boundaries
      if (currentRow < 0 || currentRow >= boardTiles.length || currentCol < 0 || currentCol >= boardTiles[0].length) {
        return false;
      }

      // Check for overlapping tiles and connections
      Tile existingTile = boardTiles[currentRow][currentCol];
      if (existingTile != null) {
        if (wordTiles[i] != null && existingTile.letter == wordTiles[i].letter) {
          connects = true; // The word connects to an existing tile.
        } else {
          return false; // Invalid overlap
        }
      }
    }

    // For the first word, ensure it crosses the center
    if (isEmptyBoard() && !crossesCenter(word)) {
      return false;
    }

    // The word is legal if it connects to existing tiles or if it's the first word
    // and crosses the center
    return connects || isEmptyBoard();
  }

  private boolean crossesCenter(Word word) {
    int centerRow = boardTiles.length / 2;
    int centerCol = boardTiles[0].length / 2;

    if (word.isVertical()) {
      return word.getCol() == centerCol && word.getRow() <= centerRow
          && (word.getRow() + word.getTiles().length) > centerRow;
    } else {
      return word.getRow() == centerRow && word.getCol() <= centerCol
          && (word.getCol() + word.getTiles().length) > centerCol;
    }
  }

  private boolean isEmptyBoard() {
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
    if (!boardLegal(word)) {
      System.out.println("Word placement not legal: " + word); // Debug print
      return 0;
    }
    if (!dictionaryLegal(word)) {
      System.out.println("Word not in dictionary: " + word); // Debug print
      return 0;
    }

    int score = 0;
    for (int i = 0; i < word.getTiles().length; i++) {
      int currentRow = word.getRow() + (word.isVertical() ? i : 0);
      int currentCol = word.getCol() + (word.isVertical() ? 0 : i);

      Tile tile = word.getTiles()[i];
      if (tile != null) { // Skip null tiles (e.g., when part of the word is already on the board).
        boardTiles[currentRow][currentCol] = tile; // Place the tile on the board.
        score += getScoreForLetter(tile.letter); // Add the letter's score to the total score.
      }
    }

    return score;
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
