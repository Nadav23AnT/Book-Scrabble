package test;

import java.util.ArrayList;
import java.util.HashMap;

public class Board {
  int player1Score = 0;
  int player2Score = 0;
  int turnCount = 0; // This keeps track of the turn number.
  public static final int BOARD_SIZE = 15; // Adjust if your board size is different
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
  }

  public Tile getTiles(int row, int col) {
    return boardTiles.get(new Position(row, col));
  }

  public boolean isFirstWord() {
    boolean firstWord = boardTiles.isEmpty();
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
    formedWords.add(word); // Add the main word itself to the list

    // Iterate through each tile of the placed word
    for (int i = 0; i < word.getTiles().length; i++) {
        int row = word.isVertical() ? word.getRow() + i : word.getRow();
        int col = word.isVertical() ? word.getCol() : word.getCol() + i;

        // If the word is vertical, check horizontally at each tile, and vice versa
        if (word.isVertical()) {
            // Check horizontally
            if (boardTiles.containsKey(new Position(row, col - 1)) || boardTiles.containsKey(new Position(row, col + 1))) {
                Word horizontalWord = getHorizontalWordAt(row, col);
                if (horizontalWord != null && !formedWords.contains(horizontalWord)) {
                    formedWords.add(horizontalWord);
                }
            }
        } else {
            // Check vertically
            if (boardTiles.containsKey(new Position(row - 1, col)) || boardTiles.containsKey(new Position(row + 1, col))) {
                Word verticalWord = getVerticalWordAt(row, col);
                if (verticalWord != null && !formedWords.contains(verticalWord)) {
                    formedWords.add(verticalWord);
                }
            }
        }
    }
    return formedWords;
}

  public int getScore(Word word) {
    int score = 0;
    boolean centerTileCovered = false; // Flag to check if the center tile is covered
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
   boolean isLegal = false;// result of the boardLegal check
    // Check if the entire word fits on the board
    for (int i = 0; i < word.getTiles().length; i++) {
      int row = word.isVertical() ? word.getRow() + i : word.getRow();
      int col = word.isVertical() ? word.getCol() : word.getCol() + i;

      // Check board boundaries
      if (row < 0 || row >= 15 || col < 0 || col >= 15) {
        return isLegal; // Word goes beyond the board
      }

      // Check for overlapping tiles
      Tile existingTile = getTiles(row, col);
      Tile newTile = word.getTiles()[i];
      if (existingTile != null && newTile != null && !existingTile.equals(newTile)) {
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
          return isLegal; // The word is adjacent to an existing tile
        }
      }
    }
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
    int wordScore = 0;

    if (!boardLegal(word) || !dictionaryLegal(word)) {
        return 0; // If the word is not legal, return a score of 0
    }

    // Score the main word
    wordScore += getScore(word);
    applyWordToBoard(word); // Place the word on the board

    // Score any additional connected words formed by placing this word
    // Here we make sure not to double-count the score for tiles that are part of both the main word and connected words
    int connectedWordsScore = findAndScoreConnectedWords(word);
    wordScore += connectedWordsScore;

    // Update the score for the current player
    if (turnCount % 2 == 0) {
        player1Score += wordScore;
    } else {
        player2Score += wordScore;
    }

    turnCount++; // Move to the next player's turn

    
    return wordScore;
}

private int findAndScoreConnectedWords(Word word) {
  int connectedWordsScore = 0;
  // int oldValuecConnectedWordsScore = connectedWordsScore;
  for (int i = 0; i < word.getTiles().length; i++) {
      int row = word.isVertical() ? word.getRow() + i : word.getRow();
      int col = word.isVertical() ? word.getCol() : word.getCol() + i;

      // Only process newly placed tiles
      if (word.getTiles()[i] != null) {
        // If the word is vertical, check horizontally at each tile position for new words
          if (word.isVertical()) {
              // oldValuecConnectedWordsScore = connectedWordsScore;
              if (scoreIfNewWord(row, col - 1, false) > 0) {
                connectedWordsScore += scoreIfNewWord(row, col - 1, false);
              }
              else {
                connectedWordsScore += scoreIfNewWord(row, col + 1, false);
              }
              
              // if (oldValuecConnectedWordsScore != connectedWordsScore) {
              //   connectedWordsScore += word.getTiles()[i].getScore();
              // }
          }
          // If the word is horizontal, check vertically at each tile position for new words
          if (!word.isVertical()) {
              // oldValuecConnectedWordsScore = connectedWordsScore;
              if (scoreIfNewWord(row - 1, col, true) > 0) {
                connectedWordsScore += scoreIfNewWord(row - 1, col, true);
              }
              else {
                connectedWordsScore += scoreIfNewWord(row + 1, col, true);
              }
              
              // if (oldValuecConnectedWordsScore != connectedWordsScore) {
              //   connectedWordsScore += word.getTiles()[i].getScore();
              // }
          }
      }
  }

  return connectedWordsScore;
}

private int scoreIfNewWord(int row, int col, boolean vertical) {
  // Check if there's a tile at the given position; if not, it's not a new word
  if (getTile(row, col) == null) {
      return 0;
  }
  // Find the word at this position
  Word newWord = vertical ? getVerticalWordAt(row, col) : getHorizontalWordAt(row, col);
  // If there's a word and it's longer than one letter, score it
  return newWord != null && newWord.getTiles().length > 1 ? getScore(newWord) : 0;
}


private Word getHorizontalWordAt(int row, int col) {
  // Find the start of the word
  while (col > 0 && boardTiles.containsKey(new Position(row, col - 1))) {
      col--;
  }

  // Build the word from left to right
  StringBuilder word = new StringBuilder();
  ArrayList<Tile> tiles = new ArrayList<>();
  while (col < BOARD_SIZE && boardTiles.containsKey(new Position(row, col))) {
      Tile tile = boardTiles.get(new Position(row, col));
      word.append(tile.getLetter());
      tiles.add(tile);
      col++;
  }

  return new Word(tiles.toArray(new Tile[0]), row, col - word.length(), false);
}

private Word getVerticalWordAt(int row, int col) {
  // Find the start of the word
  while (row > 0 && boardTiles.containsKey(new Position(row - 1, col))) {
      row--;
  }

  // Build the word from top to bottom
  StringBuilder word = new StringBuilder();
  ArrayList<Tile> tiles = new ArrayList<>();
  while (row < BOARD_SIZE && boardTiles.containsKey(new Position(row, col))) {
      Tile tile = boardTiles.get(new Position(row, col));
      word.append(tile.getLetter());
      tiles.add(tile);
      row++;
  }

  return new Word(tiles.toArray(new Tile[0]), row - word.length(), col, true);
}

  public Tile getTile(int row, int col) {
    Position position = new Position(row, col);
    return boardTiles.get(position); // Assuming boardTiles is a map storing tiles by their positions
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
