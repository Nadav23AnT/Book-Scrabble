package test;

import java.util.ArrayList;
import java.util.HashMap;

public class Board {
  private static Board instance = null; // Singleton instance
  private HashMap<Position, Tile> boardTiles;
  private final Position centerTile = new Position(7, 7);
  int[][] doubleLetterScores = { { 3, 0 }, { 11, 0 }, { 6, 2 }, { 8, 2 }, { 0, 3 }, { 7, 3 }, { 14, 3 }, { 2, 6 },
      { 6, 6 }, { 8, 6 }, { 12, 6 }, { 2, 8 }, { 6, 8 }, { 8, 8 }, { 12, 8 }, { 3, 7 }, { 11, 7 }, { 0, 11 },
      { 7, 11 }, { 14, 11 }, { 6, 12 }, { 8, 12 }, { 3, 14 }, { 11, 14 } };
  int[][] tripleLetterScores = { { 5, 1 }, { 9, 1 }, { 1, 5 }, { 5, 5 }, { 9, 5 }, { 13, 5 }, { 1, 9 }, { 5, 9 },
      { 9, 9 }, { 13, 9 }, { 5, 13 }, { 9, 13 } };
  int[][] doubleWordScores = { { 1, 1 }, { 2, 2 }, { 3, 3 }, { 4, 4 }, { 10, 4 }, { 11, 3 }, { 12, 2 }, { 13, 1 },
      { 1, 13 }, { 2, 12 }, { 3, 11 }, { 4, 10 }, { 10, 10 }, { 11, 11 }, { 12, 12 }, { 13, 13 } };
  int[][] tripleWordScores = { { 0, 0 }, { 7, 0 }, { 14, 0 }, { 0, 7 }, { 14, 7 }, { 0, 14 }, { 7, 14 }, { 14, 14 } };

  public Board() {
    this.boardTiles = new HashMap<>();
  }

  // Static method to get the singleton instance of the Board
  public static Board getBoard() {
    if (instance == null) {
      instance = new Board();
    }
    return instance;
  }

  public void placeTile(int row, int col, Tile tile) {
    Position position = new Position(row, col);
    boardTiles.put(position, tile);
    System.out.println("Placing tile " + tile.getLetter() + " at (" + row + ", " + col + ")");
  }

  public Tile getTiles(int row, int col) {
    return boardTiles.get(new Position(row, col));
  }

  public boolean isFirstWord() {
    boolean firstWord = boardTiles.isEmpty();
    System.out.println("Is first word: " + firstWord);
    return firstWord;
  }

  public boolean isAdjacentToTile(int row, int col) {
    Position[] adjacentPositions = {
        new Position(row - 1, col),
        new Position(row + 1, col),
        new Position(row, col - 1),
        new Position(row, col + 1)
    };

    for (Position pos : adjacentPositions) {
      if (boardTiles.containsKey(pos)) {
        return true;
      }
    }

    return false;
  }

  public ArrayList<Word> getWords(Word word) {
    ArrayList<Word> formedWords = new ArrayList<>();
    formedWords.add(word); // Add the main word

    // Check for new perpendicular words at each tile of the main word
    for (int i = 0; i < word.getTiles().length; i++) {
      int row = word.isVertical() ? word.getRow() + i : word.getRow();
      int col = word.isVertical() ? word.getCol() : word.getCol() + i;

      // If there's a new tile placed here, check for perpendicular words
      if (boardTiles.containsKey(new Position(row, col))) {
        Word perpendicularWord = findPerpendicularWord(row, col, !word.isVertical());
        if (perpendicularWord != null) {
          formedWords.add(perpendicularWord);
        }
      }
    }
    return formedWords;
  }

  private Word findPerpendicularWord(int row, int col, boolean isVertical) {
    System.out.println("Finding perpendicular word at (" + row + "," + col + ") vertical: " + isVertical);

    ArrayList<Tile> tileList = new ArrayList<>();
    int startRow = row;
    int startCol = col;

    // Extend backwards to the beginning of the word
    while (true) {
      int checkRow = isVertical ? startRow - 1 : startRow;
      int checkCol = isVertical ? startCol : startCol - 1;

      if (checkRow < 0 || checkCol < 0) {
        break;
      }

      Position checkPosition = new Position(checkRow, checkCol);
      Tile checkTile = boardTiles.get(checkPosition);
      if (checkTile == null) {
        break;
      }

      startRow = checkRow;
      startCol = checkCol;
      tileList.add(0, checkTile); // Add at the beginning
    }

    // Extend forward to the end of the word
    int currentRow = startRow;
    int currentCol = startCol;
    while (true) {
      Position currentPosition = new Position(currentRow, currentCol);
      Tile currentTile = boardTiles.get(currentPosition);
      if (currentTile == null) {
        break;
      }

      tileList.add(currentTile); // Add at the end

      if (isVertical) {
        currentRow++;
      } else {
        currentCol++;
      }

      // Break if out of bounds
      if (currentRow >= 15 || currentCol >= 15) {
        break;
      }
    }

    // Building the foundWord string from tileList
    StringBuilder wordBuilder = new StringBuilder();
    for (Tile tile : tileList) {
      wordBuilder.append(tile.getLetter());
    }
    String foundWord = wordBuilder.toString();

    // Only create a Word object if the length is greater than 1
    if (tileList.size() > 1) {
      System.out.println("Found perpendicular word: " + foundWord);
      Tile[] tiles = tileList.toArray(new Tile[0]);
      return new Word(tiles, startRow, startCol, isVertical);
    }

    return null;
  }
  // System.out.println("Calculating score for word: " + word.getWord());
  // System.out.println("Score for word " + word.getWord() + ": " + score);

  public int getScore(Word word) {
    int score = 0;
    boolean centerTileCovered = false; // Flag to check if the center tile is covered
    System.out.println("Calculating score for word: " + word.getWord());
    int wordMultiplier = 1;

    for (int i = 0; i < word.getTiles().length; i++) {
      int row = word.isVertical() ? word.getRow() + i : word.getRow();
      int col = word.isVertical() ? word.getCol() : word.getCol() + i;
      Tile tile = word.getTiles()[i];

      // If the tile is null, it indicates we're using an existing tile on the board.
      if (tile == null) {
        tile = getTiles(row, col); // Get the existing tile from the board.
      }

      if (tile != null) {
        int tileScore = tile.getScore();
        Position position = new Position(row, col);

        // Check if the word covers the center tile.
        if (isCenterTile(position) && isFirstWord()) {
          centerTileCovered = true;
        }

        // Apply letter score multipliers
        if (isDoubleLetterScore(position)) {
          tileScore *= 2;
        } else if (isTripleLetterScore(position)) {
          tileScore *= 3;
        }

        // Apply word score multipliers
        if (isDoubleWordScore(position)) {
          wordMultiplier *= 2;
        } else if (isTripleWordScore(position)) {
          wordMultiplier *= 3;
        }

        score += tileScore;
      }
    }

    score *= wordMultiplier;

    // Double the word's score if the center tile is covered in the first word.
    if (centerTileCovered) {
      score *= 2;
    }

    System.out.println("Score for word " + word.getWord() + ": " + score);
    return score;
  }

  private boolean isDoubleLetterScore(Position position) {
    for (int[] doubleLetterScore : doubleLetterScores) {
      if (doubleLetterScore[0] == position.getRow() && doubleLetterScore[1] == position.getCol()) {
        return true;
      }
    }
    return false;
  }

  private boolean isTripleLetterScore(Position position) {
    for (int[] tripleLetterScore : tripleLetterScores) {
      if (tripleLetterScore[0] == position.getRow() && tripleLetterScore[1] == position.getCol()) {
        return true;
      }
    }
    return false;
  }

  private boolean isDoubleWordScore(Position position) {
    for (int[] doubleWordScore : doubleWordScores) {
      if (doubleWordScore[0] == position.getRow() && doubleWordScore[1] == position.getCol()) {
        return true;
      }
    }
    return false;
  }

  private boolean isTripleWordScore(Position position) {
    for (int[] tripleWordScore : tripleWordScores) {
      if (tripleWordScore[0] == position.getRow() && tripleWordScore[1] == position.getCol()) {
        return true;
      }
    }
    return false;
  }

  // isCenterTile method
  private boolean isCenterTile(Position position) {
    return position.equals(centerTile);
  }

  public boolean boardLegal(Word word) {
    System.out.println(
        "Checking boardLegal for word: " + word.getWord() + " at (" + word.getRow() + "," + word.getCol() + ")");
    boolean isLegal = false;// result of the boardLegal check
    // Check if the entire word fits on the board
    for (int i = 0; i < word.getTiles().length; i++) {
      int row = word.isVertical() ? word.getRow() + i : word.getRow();
      int col = word.isVertical() ? word.getCol() : word.getCol() + i;

      // Check board boundaries
      if (row < 0 || row >= 15 || col < 0 || col >= 15) {
        System.out.println("Is placement legal for " + word.getWord() + ": " + isLegal);
        return isLegal; // Word goes beyond the board
      }

      // Check for overlapping tiles
      Tile existingTile = getTiles(row, col);
      Tile newTile = word.getTiles()[i];
      if (existingTile != null && newTile != null && !existingTile.equals(newTile)) {
        System.out.println("Is placement legal for " + word.getWord() + ": " + isLegal);
        return isLegal; // Overlapping tiles do not match
      }
    }

    // For the first word, check if it covers the center tile
    if (isFirstWord()) {
      return doesCoverCenterTile(word);
    } else {
      // For subsequent words, check if at least one tile is adjacent to existing
      // tiles
      for (int i = 0; i < word.getTiles().length; i++) {
        int row = word.isVertical() ? word.getRow() + i : word.getRow();
        int col = word.isVertical() ? word.getCol() : word.getCol() + i;

        // If there's a new tile, check if it's adjacent to existing tiles
        if (word.getTiles()[i] != null && isAdjacentToTile(row, col)) {
          isLegal = true;
          System.out.println("Is placement legal for " + word.getWord() + ": " + isLegal);
          return isLegal; // The word is adjacent to an existing tile
        }
      }
    }
    System.out.println("Is placement legal for " + word.getWord() + ": " + isLegal);
    return isLegal; // No adjacency found for any tile in the word
  }

  public boolean doesCoverCenterTile(Word word) {
    // Iterate through each tile in the word to check if it covers the center tile
    for (int i = 0; i < word.getTiles().length; i++) {
      int row = word.isVertical() ? word.getRow() + i : word.getRow();
      int col = word.isVertical() ? word.getCol() : word.getCol() + i;
      Position currentPosition = new Position(row, col);

      // Check if the current tile's position is the center tile
      if (currentPosition.equals(centerTile)) {
        return true; // The word covers the center tile
      }
    }
    return false; // None of the word's tiles cover the center tile
  }

  public boolean dictionaryLegal(Word word) {
    // This should check the word against a dictionary.
    // Since this is highly dependent on your game's dictionary, it's a placeholder.
    // In a real implementation, this might involve a call to a Dictionary service
    // or a lookup in a set of valid words.
    return true;
  }

  public int tryPlaceWord(Word word) {
    System.out.println("Trying to place word: " + word.getWord());

    int totalScore = 0;
    if (!boardLegal(word)) {
      return 0; // If the word is not legal, return a score of 0
    }
    // If the word placement is legal, get all new words formed
    ArrayList<Word> formedWords = getWords(word);
    // Iterate through all formed words to calculate the total score
    for (Word formedWord : formedWords) {
      // Here, you can also add a dictionary check for each word
      // if (dictionaryLegal(formedWord)) {
      totalScore += getScore(formedWord);
      // } else {
      // return 0; // If any formed word is not in the dictionary, return 0
      // }
    }
    // Apply the score for the word placement on the board
    // Assuming you have a method to apply the tiles to the board
    applyWordToBoard(word);
    System.out.println("Final score for placing " + word.getWord() + ": " + totalScore);
    printBoardState();
    return totalScore;
  }

  // Placeholder for the method that applies the word to the board
  private void applyWordToBoard(Word word) {
    for (int i = 0; i < word.getTiles().length; i++) {
      int row = word.isVertical() ? word.getRow() + i : word.getRow();
      int col = word.isVertical() ? word.getCol() : word.getCol() + i;

      Tile tile = word.getTiles()[i];
      if (tile != null) {
        placeTile(row, col, tile);
      }
    }
  }

  public void printBoardState() {
    for (int i = 0; i < 15; i++) {
      for (int j = 0; j < 15; j++) {
        Tile tile = getTiles(i, j);
        System.out.print(tile != null ? tile.getLetter() : ".");
      }
      System.out.println();
    }
    System.out.println("---------------");
  }

  class Position {
    private int row;
    private int col;

    public Position(int row, int col) {
      this.row = row;
      this.col = col;
    }

    public int getRow() {
      return row;
    }

    public int getCol() {
      return col;
    }

    // Getters, equals, and hashCode

    @Override
    public boolean equals(Object o) {
      if (this == o)
        return true;
      if (o == null || getClass() != o.getClass())
        return false;

      Position position = (Position) o;

      if (row != position.row)
        return false;
      return col == position.getCol();
    }

    @Override
    public int hashCode() {
      int result = row;
      result = 31 * result + col;
      return result;
    }
  }
}
