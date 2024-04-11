package test;

import java.util.ArrayList;
import java.util.Arrays;

public class Board-arr {
  private static Board instance = null;
  private Tile[][] boardTiles;

  private Board() {
    this.boardTiles = new Tile[15][15]; // Assuming a 15x15 board for Scrabble-like game
  }

  public static Board getBoard() {
    if (instance == null) {
      instance = new Board();
    }
    return instance;
  }

  public Tile[][] getTiles() {
    // Deep copy to prevent external modification
    Tile[][] tilesCopy = new Tile[boardTiles.length][];
    for (int i = 0; i < boardTiles.length; i++) {
      tilesCopy[i] = boardTiles[i].clone();
    }
    return tilesCopy;
  }

  public boolean boardLegal(Word word) {
    printBoardState();
    System.out.println("Checking boardLegal for word: " + Arrays.toString(word.getTiles()));
    
    boolean isConnected = false;
    boolean isFirstWord = isFirstWord();
    boolean coversCenterTile = isFirstWord && doesCoverCenterTile(word);

    System.out.println("Is this the first word? " + isFirstWord);
    System.out.println("Does the word cover the center tile? " + coversCenterTile);

    for (int i = 0; i < word.getTiles().length; i++) {
      int row = word.isVertical() ? word.getRow() + i : word.getRow();
      int col = word.isVertical() ? word.getCol() : word.getCol() + i;

      System.out.println("Checking tile at position: " + row + ", " + col);

      if (row < 0 || row >= boardTiles.length || col < 0 || col >= boardTiles[row].length) {
        System.out.println("Word goes out of board bounds.");
        return false;
      }

      if (boardTiles[row][col] != null) {
        if (!boardTiles[row][col].equals(word.getTiles()[i])) {
          System.out.println("Overlap issue at: " + row + ", " + col);
          return false;
        }
        isConnected = true;
      } else if (!isFirstWord) {
        // Call isAdjacentToTile only if it's not the first word
        isConnected = isAdjacentToTile(row, col);
        if (isConnected) {
          System.out.println("Tile at " + row + ", " + col + " is adjacent to an existing tile.");
        } else {
          System.out.println("Tile at " + row + ", " + col + " is not adjacent to any existing tile.");
        }
      }
    }

    if (isFirstWord && !coversCenterTile) {
      System.out.println("The first word does not cover the center tile.");
      return false;
    }

    if (!isFirstWord && !isConnected) {
      System.out.println("The word is not connected to any existing tile.");
      return false;
    }

    return true;
}


  private boolean doesCoverCenterTile(Word word) {
    System.out.println("Checking if the word covers the center tile.");
    int center = 7; // Center position for a 15x15 board
    for (int i = 0; i < word.getTiles().length; i++) {
      int row = word.isVertical() ? word.getRow() + i : word.getRow();
      int col = word.isVertical() ? word.getCol() : word.getCol() + i;
      if (row == center && col == center) {
        System.out.println("The word covers the center tile.");
        return true;
      }
    }
    System.out.println("The word does not cover the center tile.");
    return false;
  }

  private boolean isAdjacentToTile(int row, int col) {
    System.out.println("Checking adjacency for tile at: " + row + ", " + col);
    // Check above
    if (row > 0 && boardTiles[row - 1][col] != null) {
        System.out.println("Tile connected above at " + (row - 1) + ", " + col);
        return true;
    }
    // Check below
    if (row < boardTiles.length - 1 && boardTiles[row + 1][col] != null) {
        System.out.println("Tile connected below at " + (row + 1) + ", " + col);
        return true;
    }
    // Check left
    if (col > 0 && boardTiles[row][col - 1] != null) {
        System.out.println("Tile connected to the left at " + row + ", " + (col - 1));
        return true;
    }
    // Check right
    if (col < boardTiles[row].length - 1 && boardTiles[row][col + 1] != null) {
        System.out.println("Tile connected to the right at " + row + ", " + (col + 1));
        return true;
    }
    System.out.println("No adjacent tiles found for tile at " + row + ", " + col);
    return false;
}


public boolean isFirstWord() {
  for (int i = 0; i < boardTiles.length; i++) {
      for (int j = 0; j < boardTiles[i].length; j++) {
          if (boardTiles[i][j] != null) {
              System.out.println("Tile found at " + i + ", " + j + ": " + boardTiles[i][j].getLetter());
              return false; // Found a tile, so it's not the first word.
          }
      }
  }
  System.out.println("No tiles found on the board, it's the first word.");
  return true; // No tiles found on the board, it's the first word.
}


  private boolean dictionaryLegal(Word word) {
    // Placeholder: integrate with a dictionary to validate word
    return true;
  }

  public ArrayList<Word> getWords(Word word) {
    ArrayList<Word> formedWords = new ArrayList<>();
    formedWords.add(word); // Add the primary word
    System.out.println("Primary word added: " + Arrays.toString(word.getTiles()));

    // Check for new perpendicular words at each tile of the word
    for (int i = 0; i < word.getTiles().length; i++) {
      int startRow = word.isVertical() ? word.getRow() + i : word.getRow();
      int startCol = word.isVertical() ? word.getCol() : word.getCol() + i;

      if (boardTiles[startRow][startCol] == null) {
        Word perpendicularWord = findPerpendicularWord(startRow, startCol, !word.isVertical());
        if (perpendicularWord != null) {
          formedWords.add(perpendicularWord);
          System.out.println("Perpendicular word added: " + Arrays.toString(perpendicularWord.getTiles()));
        }
      }
    }
    return formedWords;
  }

  private Word findPerpendicularWord(int row, int col, boolean isVertical) {
    ArrayList<Tile> tiles = new ArrayList<>();
    int startRow = isVertical ? row - 1 : row;
    int startCol = isVertical ? col : col - 1;
    int endRow = isVertical ? row + 1 : row;
    int endCol = isVertical ? col : col + 1;

    while (startRow >= 0 && startCol >= 0 && boardTiles[startRow][startCol] != null) {
      if (isVertical) {
        startRow--;
      } else {
        startCol--;
      }
    }

    if (isVertical && startRow < row - 1) {
      startRow++;
    } else if (!isVertical && startCol < col - 1) {
      startCol++;
    }

    while ((isVertical && endRow < boardTiles.length) || (!isVertical && endCol < boardTiles[0].length)) {
      Tile currentTile = isVertical ? boardTiles[endRow][col] : boardTiles[row][endCol];
      if (currentTile == null) {
        break;
      }
      tiles.add(currentTile);

      if (isVertical) {
        endRow++;
      } else {
        endCol++;
      }
    }

    if (!tiles.isEmpty()) {
      Tile[] tilesArray = tiles.toArray(new Tile[0]);
      return new Word(tilesArray, startRow, startCol, isVertical);
    }
    return null;
  }

  public int getScore(Word word) {
    System.out.println("Calculating score for word: " + Arrays.toString(word.getTiles()));
    boolean isFirstWord = isFirstWord();
    int score = 0;
    int wordMultiplier = 1;
    boolean coversCenter = doesCoverCenterTile(word);

    // Define bonus positions with numerical coordinates
    int[][] doubleLetterScores = { { 3, 0 }, { 11, 0 }, { 6, 2 }, { 8, 2 }, { 0, 3 }, { 7, 3 }, { 14, 3 }, { 2, 6 },
        { 6, 6 }, { 8, 6 }, { 12, 6 }, { 2, 8 }, { 6, 8 }, { 8, 8 }, { 12, 8 }, { 3, 7 }, { 11, 7 }, { 0, 11 },
        { 7, 11 }, { 14, 11 }, { 6, 12 }, { 8, 12 }, { 3, 14 }, { 11, 14 } };
    int[][] tripleLetterScores = { { 5, 1 }, { 9, 1 }, { 1, 5 }, { 5, 5 }, { 9, 5 }, { 13, 5 }, { 1, 9 }, { 5, 9 },
        { 9, 9 }, { 13, 9 }, { 5, 13 }, { 9, 13 } };
    int[][] doubleWordScores = { { 1, 1 }, { 2, 2 }, { 3, 3 }, { 4, 4 }, { 10, 4 }, { 11, 3 }, { 12, 2 }, { 13, 1 },
        { 1, 13 }, { 2, 12 }, { 3, 11 }, { 4, 10 }, { 10, 10 }, { 11, 11 }, { 12, 12 }, { 13, 13 } };
    int[][] tripleWordScores = { { 0, 0 }, { 7, 0 }, { 14, 0 }, { 0, 7 }, { 14, 7 }, { 0, 14 }, { 7, 14 }, { 14, 14 } };

    for (int i = 0; i < word.getTiles().length; i++) {
      Tile tile = word.getTiles()[i];

      // Check if the tile is null (it may happen with placeholders like "_")
      if (tile == null) {
        continue;
      }

      int row = word.getRow() + (word.isVertical() ? i : 0);
      int col = word.getCol() + (word.isVertical() ? 0 : i);

      System.out.println("Processing tile " + tile.getLetter() + " at " + row + ", " + col);

      int tileScore = tile.getScore();
      System.out.println("Base score for tile " + tile.getLetter() + ": " + tileScore);

      // Check and apply letter and word multipliers
      for (int[] dl : doubleLetterScores) {
        if (dl[0] == row && dl[1] == col) {
          System.out.println("Applying double letter score at " + row + ", " + col);
          tileScore *= 2;
        }
      }
      for (int[] tl : tripleLetterScores) {
        if (tl[0] == row && tl[1] == col) {
          System.out.println("Applying triple letter score at " + row + ", " + col);
          tileScore *= 3;
        }
      }
      for (int[] dw : doubleWordScores) {
        if (dw[0] == row && dw[1] == col) {
          System.out.println("Applying double word score at " + row + ", " + col);
          wordMultiplier *= 2;
        }
      }
      for (int[] tw : tripleWordScores) {
        if (tw[0] == row && tw[1] == col) {
          System.out.println("Applying triple word score at " + row + ", " + col);
          wordMultiplier *= 3;
        }
      }

      System.out.println("Score for tile " + tile.getLetter() + " after multipliers: " + tileScore);
      score += tileScore;
    }

    if (isFirstWord && coversCenter) {
      System.out.println("Doubling score for covering center tile.");
      wordMultiplier *= 2;
    }

    int totalScore = score * wordMultiplier;
    System.out.println("Total score for word after word multipliers: " + totalScore);
    return totalScore;
  }

  public int tryPlaceWord(Word word) {
    System.out.println("Trying to place word: " + Arrays.toString(word.getTiles()));

    if (!boardLegal(word)) {
        System.out.println("Word placement is illegal.");
        return 0;
    }

    // Directly update the board state here once the placement is confirmed to be legal
    int row = word.getRow();
    int col = word.getCol();
    for (Tile tile : word.getTiles()) {
        if (tile != null) { // Only place non-null tiles
            boardTiles[word.isVertical() ? row++ : row][word.isVertical() ? col : col++] = tile;
            System.out.println("Placing tile " + tile.getLetter() + " at " + (word.isVertical() ? row - 1 : row) + ", " + (word.isVertical() ? col : col - 1));
        } else {
            if (word.isVertical()) {
                row++;
            } else {
                col++;
            }
        }
    }

    printBoardState(); // Print the board state after placing the word

    ArrayList<Word> formedWords = getWords(word);
    int cumulativeScore = 0;
    for (Word formedWord : formedWords) {
        if (!dictionaryLegal(formedWord)) {
            System.out.println("Word is not in dictionary: " + formedWord.getTiles());
            return 0; // If any formed word is illegal, do not update the score or board state.
        }
        cumulativeScore += getScore(formedWord);
    }

    System.out.println("Cumulative score for placement: " + cumulativeScore);
    return cumulativeScore;
}


private void printBoardState() {
  System.out.println("Current board state:");
  for (Tile[] row : boardTiles) {
      for (Tile tile : row) {
          System.out.print(tile == null ? "." : tile.getLetter());
      }
      System.out.println();
  }
}

}