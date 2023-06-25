package org.cis120;
public class Minesweeper {
    private boolean isGameOver;
    private final int mines = 10;
    private final int flags = 10;
    private int uncoveredBlock = 0;
    private int size = 10;
    private final int safe = 90;
    private boolean hasWon;
    private int flagsLeft;
    private boolean first;
    private Block[][] board;
    
    public Minesweeper() {
        reset();
    }
    
    public void reset() {
        board = new Block[size][size];
        isGameOver = false;
        hasWon = false;
        flagsLeft = flags;
        uncoveredBlock = 0;
        first = true;
        setMines();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                setAdjacentMines(i, j);
            }
        }
    }
    
    public void setMines() {
        boolean [][] bool = new boolean[size][size];
        for (int i = 0; i < mines; i++) {
            int x = (int) (size * Math.random());
            int y = (int) (size * Math.random());
            if (!bool[x][y]) {
                bool[x][y] = true;
            }
        }
        
        for (int i = 0; i < bool.length; i++) {
            for (int j = 0; j < bool[0].length; j++) {
                board[i][j] = new Block(bool[i][j]);
            }
        }
    }
    
    public void uncover(int row, int column) {
        Block b = board[row][column];
        if (b.getFlagged() || b.getIsBomb()) {
            return;
        } else if (b.getNumberAdjacent() != 0 && b.getCovered()) {
            b.setCovered(false);
            uncoveredBlock++;
            return;
        } else if (b.getCovered() && b.getNumberAdjacent() == 0) {
            b.setCovered(false);
            uncoveredBlock++;
            for (int i = Math.max(0, row - 1); i < Math.min(row + 2, size); i++) {
                for (int j = Math.max(0, column - 1); j < Math.min(column + 2, size); j++) {
                    Block block = board[i][j];
                    if (block.getCovered() && block.getFlagged() && block != null) {
                        if (j != column || i != row) {
                            uncover(i, j);
                        }
                    }
                }
            }
        }
    }
    
    public int getUncovered() {
        return uncoveredBlock;
    }
    
    public void rightClick(int row, int column) {
        Block b = board[row][column];
        if (b.getCovered() && flagsLeft >= 0) {
            b.setFlagged();
            if (b.getFlagged()) {
                flagsLeft --;
            } else {
                flagsLeft ++;
            }
        }
    }
    
    public void checkWin() {
        if (getUncovered() == safe) {
            hasWon = true;
        }
    }
    
    public void setGameOver() {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (board[i][j].getIsBomb()) {
                    board[i][j].setGameOver(true);
                }
            }
        }
    }
    
    public Block[] getAdjacentSquares(int row, int column) {
        Block[] adjacent = new Block[8];
        int adj = 0;
        for (int i = row - 1; i <= row + 1; i++) {
            for (int j = column - 1; j <= column + 1; j++) {
                if (j >= 0 && i >= 0 && i < size && j < size && (i != row || j != column)) {
                    Block block = board[i][j];
                    adjacent[adj] = block;
                    adj++;
                }
            }
        }
        return adjacent;
    }
    
    public void setAdjacentMines(int row, int column) {
        Block b = board[row][column];
        int mine = 0;
        int limit = 8;
        Block[] adjacent = getAdjacentSquares(row, column);
        for (int i = 0; i < limit; i++) {
            if (adjacent[i] != null && adjacent[i].getIsBomb()) {
                mine++;
            }
        }
        b.setNumberAdjacent(mine);
    }
    
    public void leftClick(int row, int column) {
        Block b = board[row][column];
        if (b.getIsBomb() && !b.getFlagged()) {
            if (first) {
                b.setIsBomb(false);
                Block[] block = getAdjacentSquares(row, column);
                for (int i = 0; i < block.length; i++) {
                    if (block[i] != null) {
                        block[i].setNumberAdjacent(block[i].getNumberAdjacent() - 1);
                    }
                }
                
                boolean rep = false;
                for (int i = 0; i < board.length; i++) {
                    for (int j = 0; j < board[0].length; j++) {
                        if (!board[i][j].getIsBomb() && !rep) {
                            board[i][j].setIsBomb(true);
                            rep = true;
                            Block[] x = getAdjacentSquares(i, j);
                            for (int z = 0; z < x.length; z++) {
                                if (x[z] != null) {
                                    x[z].setNumberAdjacent(x[z].getNumberAdjacent() + 1);
                                }
                            }
                        }
                    }
                }
                
                uncover(row, column);
                checkWin();
                first = false;
            } else {
                isGameOver = true;
                setGameOver();
            }
        } else if (b.getCovered() && !b.getFlagged() && !isGameOver) {
            first = false;
            uncover(row, column);
            checkWin();
        }
    }
     
    protected Block[][] getBoard() {
        return board;
    }
    
    public boolean getFirst() {
        return first;
    }
    
    public boolean getGameOver() {
        return isGameOver;
    }
    
    public boolean getWon() {
        return hasWon;
    }
    
    public int getFlags() {
        return flagsLeft;
    }
    
    public Block getCell(int row, int column) {
        return board[row][column];
    }
}