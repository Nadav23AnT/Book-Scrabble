package archive2.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Arrays;
import java.util.HashSet;

public class Board {
  private static Board instance;
  private final Tile[][] boardTiles;
  private Dict_Board current_dict;

  private static final Set<String> doubleLetterScores = new HashSet<>();
  private static final Set<String> tripleLetterScores = new HashSet<>();
  private static final Set<String> doubleWordScores = new HashSet<>();
  private static final Set<String> tripleWordScores = new HashSet<>();

  static {
    // Double Letter Scores
    doubleLetterScores.addAll(Arrays.asList("D1", "L1", "G3", "I3", "A4", "H4", "O4",
        "C7", "G7", "I7", "M7", "C9", "G9", "I9", "M9",
        "D8", "L8", "A12", "H12", "O12", "G13", "I13",
        "D15", "L15"));
    // Triple Letter Scores
    tripleLetterScores.addAll(Arrays.asList("F2", "J2", "B6", "F6", "J6", "N6",
        "B10", "F10", "J10", "N10", "F14", "J14"));
    // Double Word Scores
    doubleWordScores.addAll(Arrays.asList("B2", "C3", "D4", "E5", "K5", "L4", "M3", "N2",
        "B14", "C13", "D12", "E11", "K11", "L12", "M13", "N14"));
    // Triple Word Scores
    tripleWordScores.addAll(Arrays.asList("A1", "H1", "O1", "A8", "O8", "A15", "H15", "O15"));
  }

  private Board() {
    boardTiles = new Tile[15][15];
    current_dict = new Dict_Board(); // You might want to pass this as a parameter or set it through a setter method.
  }

  public static Board getBoard() {
    if (instance == null) {
      instance = new Board();
    }
    return instance;
  }

  public boolean current_dictLegal(Word word) {
    // Use the current_dict to check if the word is valid.
    return current_dict.contains(word.toString());
  }

  public int tryPlaceWord(Word word) {
    if (!boardLegal(word) || !allWordsValid(word)) {
      return 0; // The word or the resulting new words are not valid.
    }

    // Place the word on the board and calculate the score.
    int score = 0;
    for (Word newWord : getWords(word)) {
      score += getScore(newWord); // Sum up scores for all new words.
      // Here, you can also update the board with the new tiles.
    }
    return score;
  }

  private boolean allWordsValid(Word word) {
    List<Word> formedWords = getWords(word);
    for (Word formedWord : formedWords) {
      // Check if each formed word is in the dictionary.
      if (!current_dict.contains(formedWord.toString())) {
        return false;
      }
    }
    return true;
  }

  // Private constructor to enforce Singleton pattern
  private Board(Dict_Board current_dict) {
    this.boardTiles = new Tile[15][15];
    this.current_dict = current_dict;
  }

  // Static method to get the instance of the Board
  public static Board getBoard(Dict_Board current_dict) {
    if (instance == null) {
      instance = new Board(current_dict);
    }
    return instance;
  }

  // Method to place a tile on the board
  public void placeTile(Tile tile, int row, int col) {
    if (row >= 0 && row < 15 && col >= 0 && col < 15) {
      boardTiles[row][col] = tile;
    }
  }

  // Method to get a copy of the board's tiles
  public Tile[][] getTiles() {
    Tile[][] tilesCopy = new Tile[15][15];
    for (int i = 0; i < boardTiles.length; i++) {
      tilesCopy[i] = Arrays.copyOf(boardTiles[i], boardTiles[i].length);
    }
    return tilesCopy;
  }

  // Method to check if a word is valid according to the current_dict
  public boolean isWordValid(String word) {
    return current_dict.contains(word);
  }

  public boolean boardLegal(Word word) {
    if (word == null) {
      return false;
    }

    boolean touchesExistingTile = false;
    boolean isFirstWord = isEmptyBoard();
    int row = word.getRow();
    int col = word.getCol();

    for (int i = 0; i < word.getTiles().length; i++) {
      int currentRow = word.isVertical() ? row + i : row;
      int currentCol = word.isVertical() ? col : col + i;

      // Check boundaries
      if (currentRow < 0 || currentRow >= boardTiles.length || currentCol < 0 || currentCol >= boardTiles[0].length) {
        return false;
      }

      Tile currentTile = boardTiles[currentRow][currentCol];
      Tile wordTile = word.getTiles()[i];

      if (currentTile != null && wordTile != null) {
        if (currentTile.letter != wordTile.letter) {
          return false; // Overlapping tiles must match
        }
        touchesExistingTile = true; // The word touches an existing tile
      }
    }

    return touchesExistingTile || isFirstWord;
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

  public List<Word> getWords(Word word) {
    List<Word> words = new ArrayList<>();
    words.add(word); // Add the main word.
    // Logic to find and add additional intersecting or adjacent words.
    return words;
  }

  public int getScore(Word word) {
    int score = 0;
    int wordMultiplier = 1;

    for (int i = 0; i < word.getTiles().length; i++) {
      int row = word.getRow() + (word.isVertical() ? i : 0);
      int col = word.getCol() + (word.isVertical() ? 0 : i);

      Tile tile = word.getTiles()[i];
      if (tile == null)
        continue;

      int tileScore = tile.score;
      String position = convertToPosition(row, col);

      // Apply letter multipliers
      if (doubleLetterScores.contains(position)) {
        tileScore *= 2;
      } else if (tripleLetterScores.contains(position)) {
        tileScore *= 3;
      }

      score += tileScore;

      // Apply word multipliers
      if (doubleWordScores.contains(position)) {
        wordMultiplier *= 2;
      } else if (tripleWordScores.contains(position) || isStarTile(row, col)) {
        wordMultiplier *= 3;
      }
    }

    return score * wordMultiplier;
  }

  private boolean isStarTile(int row, int col) {
    // Center tile for a 15x15 board is at (7, 7)
    return row == 7 && col == 7;
  }

  private String convertToPosition(int row, int col) {
    // Convert board indices to positions like A1, B1, etc.
    return "" + (char) ('A' + col) + (row + 1);
  }

}
