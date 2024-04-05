package test;

import java.util.ArrayList;
import java.util.Arrays;

public class Board {
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
        Tile[][] tilesCopy = new Tile[boardTiles.length][];
        for (int i = 0; i < boardTiles.length; i++) {
            tilesCopy[i] = boardTiles[i].clone();
        }
        return tilesCopy;
    }

    public boolean boardLegal(Word word) {
        boolean isConnected = false;
        boolean isFirstWord = isFirstWord();
        boolean coversCenterTile = isFirstWord && doesCoverCenterTile(word);

        for (int i = 0; i < word.getTiles().length; i++) {
            int row = word.isVertical() ? word.getRow() + i : word.getRow();
            int col = word.isVertical() ? word.getCol() : word.getCol() + i;

            if (row < 0 || row >= boardTiles.length || col < 0 || col >= boardTiles[row].length) {
                return false;
            }

            if (boardTiles[row][col] != null) {
                if (!boardTiles[row][col].equals(word.getTiles()[i])) {
                    return false;
                }
                isConnected = true;
            } else if (!isFirstWord && isAdjacentToTile(row, col)) {
                isConnected = true;
            }
        }

        return (isFirstWord && coversCenterTile) || (!isFirstWord && isConnected);
    }

    private boolean doesCoverCenterTile(Word word) {
        int center = 7; // Center position for a 15x15 board
        for (int i = 0; i < word.getTiles().length; i++) {
            int row = word.isVertical() ? word.getRow() + i : word.getRow();
            int col = word.isVertical() ? word.getCol() : word.getCol() + i;
            if (row == center && col == center) {
                return true;
            }
        }
        return false;
    }

    private boolean isAdjacentToTile(int row, int col) {
        if (row > 0 && boardTiles[row - 1][col] != null) return true;
        if (row < boardTiles.length - 1 && boardTiles[row + 1][col] != null) return true;
        if (col > 0 && boardTiles[row][col - 1] != null) return true;
        if (col < boardTiles[row].length - 1 && boardTiles[row][col + 1] != null) return true;
        return false;
    }

    public boolean isFirstWord() {
        for (Tile[] row : boardTiles) {
            for (Tile tile : row) {
                if (tile != null) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean dictionaryLegal(Word word) {
        // Placeholder: integrate with a dictionary to validate word
        return true;
    }

    public ArrayList<Word> getWords(Word word) {
        ArrayList<Word> formedWords = new ArrayList<>();
        formedWords.add(word);

        // Check for new perpendicular words at each tile of the word
        for (int i = 0; i < word.getTiles().length; i++) {
            int startRow = word.isVertical() ? word.getRow() + i : word.getRow();
            int startCol = word.isVertical() ? word.getCol() : word.getCol() + i;
            if (boardTiles[startRow][startCol] == null) {
                Word perpendicularWord = findPerpendicularWord(startRow, startCol, !word.isVertical());
                if (perpendicularWord != null) {
                    formedWords.add(perpendicularWord);
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
        int score = 0;
        int wordMultiplier = 1;

        // Define bonus positions with numerical coordinates
        // Bonus scoring implementation

        for (int i = 0; i < word.getTiles().length; i++) {
            Tile tile = word.getTiles()[i];
            if (tile == null) {
                continue;
            }

            int row = word.getRow() + (word.isVertical() ? i : 0);
            int col = word.getCol() + (word.isVertical() ? 0 : i);

            int tileScore = tile.getScore();
            // Apply any scoring multipliers for the tile and word
            score += tileScore;
        }

        return score * wordMultiplier;
    }

    public int tryPlaceWord(Word word) {
        if (!boardLegal(word)) {
            return 0;
        }

        ArrayList<Word> formedWords = getWords(word);
        int cumulativeScore = 0;
        for (Word formedWord : formedWords) {
            if (!dictionaryLegal(formedWord)) {
                return 0;
            }
            cumulativeScore += getScore(formedWord);
        }

        // If word placement is successful, update the board state
        return cumulativeScore;
    }
}
