package org.cis120;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import javax.swing.*;

public class Grid extends JPanel {
    private Minesweeper minesweeper;
    private final int width = 10;
    private final int height = 10;
    public static final int BOARD_W = 500;
    public static final int BOARD_H = 500;
    
    public Grid() {
        minesweeper = new Minesweeper();
        Game.start();
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        setFocusable(true);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event) {
                Point point = event.getPoint();
                
                if (SwingUtilities.isLeftMouseButton(event)) {
                    minesweeper.leftClick(point.x / 50, point.y / 50);
                    Game.gameOver(minesweeper.getGameOver());
                    Game.win(minesweeper.getWon());
                } else if (SwingUtilities.isRightMouseButton(event)) {
                    minesweeper.rightClick(point.x / 50, point.y / 50);
                    Game.updateFlag(minesweeper.getFlags());
                    Game.win(minesweeper.getWon());
                }
                repaint();
            }
        });
    }
    
    public void reset() {
        minesweeper.reset();
        Game.start();
        repaint();
        requestFocusInWindow();
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                Block b = minesweeper.getCell(i, j);
                int w = b.getSize();
                int h = b.getSizeVertical();
                Color color = new Color(0, 100, 0);
                g.setColor(color);
                g.drawRect(i * 50, j * 50, w + (i * 50), h + (j * 50));
                if (b.getCovered()) {
                    g.setColor(Color.green);
                    g.fillRect(2 + (i * 50), 2 + (j * 50), (i * 50) + (w - 3), 
                               (j * 50) + h - 3);
                }
                if (b.getFlagged()) {
                    g.setColor(Color.blue);
                    g.fillPolygon(new int[] {(i * 50) + (w / 2) - 5, (i * 50) + (w / 2) + 5,
                                                (i * 50) + (w / 2) - 5},
                                 new int[] {(j * 50) + (h / 2) - 2, (j * 50) + (h / 2),
                                               (j * 50) + (h / 2) + 3}, 3);
                }
                if (b.gameOver() && b.getIsBomb()) {
                    g.setColor(Color.gray);
                    g.fillRect(2 + (i * 50), (j * 50) + 2, (i * 50) + (w - 3), 
                               (j * 50) + (h - 3));
                    g.setColor(Color.red);
                    g.fillOval((i * 50) + (w / 4), (j * 50) + (h / 4), 
                               (w / 2), (h / 2));
                }                     
                if (!b.getCovered()) {
                    g.setColor(Color.lightGray);
                    g.fillRect((i * 50) + 2, (j * 50) + 2, (i * 50) + (w - 3),
                               (j * 50) + (h - 3));
                    g.setColor(Color.black);
                    g.drawRect((i * 50) + 2, (j * 50) + 2, (i * 50) + (w - 3),
                               (j * 50) + (h - 3));
                    if (b.getNumberAdjacent() != 0) {
                        g.setColor(Color.black);
                        g.setFont(new Font(g.getFont().getFontName(), g.getFont().getStyle(), 10));
                        g.drawString(Integer.toString(b.getNumberAdjacent()), 
                                     (i * 50) + (w / 3), (j * 50) + (h / 3));
                    }
                }             
            }
        }
    }
    
    public boolean getWon() {
        return minesweeper.getWon();
    }
    
    public int getFlag() {
        return minesweeper.getFlags();
    }
    
    public Dimension getPrefSize() {
        return new Dimension(BOARD_W, BOARD_H);
    }
}