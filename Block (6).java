package org.cis120;
import java.awt.*;
import javax.swing.*;

public class Block {
    // fields for block positions and state of the current block
    public static final int BLOCKSIZE = 50;
    private boolean isBomb;
    private boolean isGameOver;
    private boolean isCovered;
    private boolean isFlagged;
    private int numberAdjacent;
    
    // block constructor
    public Block(boolean isBomb) {
        this.isBomb = isBomb;
        isFlagged = false;
        isCovered = true;
        numberAdjacent = 0;
        isGameOver = false;
    }

    // getters and setters
    public boolean gameOver() {
        return isGameOver;
    }
    
    public void setGameOver(boolean x) {
        isGameOver = x;
    }
    
    public boolean getIsBomb() {
        return isBomb;
    }
    
    public void setIsBomb(boolean x) {
        isBomb = x;
    }
    
    public boolean getCovered() {
        return isCovered;
    }
    
    public void setCovered(boolean x) {
        isCovered = x;
    }
    
    public boolean getFlagged() {
        return isFlagged;
    }
    
    public void setFlagged() {
        isFlagged = !isFlagged;
    }
    
    public int getNumberAdjacent() {
        return numberAdjacent;
    }
    public void setNumberAdjacent(int x) {
        numberAdjacent = x;
    }
    
    public int getSize() {
        return BLOCKSIZE;
    }
    
    public int getSizeVertical() {
        return BLOCKSIZE;
    }
} 