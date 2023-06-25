package org.cis120;

import java.awt.event.*;
import java.util.ArrayList;
import java.text.DecimalFormat;
import java.io.FileNotFoundException;
import java.io.File;
import java.nio.file.Paths;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.awt.event.ActionListener;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.*;


public class Game extends JPanel implements Runnable {
    
    private static long start;
    private static Timer timer;
    private static JLabel flags;
    private static long second;
    private static final int FLAGS = 10;
    private static final JFrame FRAME = new JFrame("Minesweeper");
    private static final JPanel ENDING = new JPanel();
    
    public void run() {
        FRAME.setLocation(100, 100);
        ENDING.setSize(500, 100);
        final JPanel controlPanel = new JPanel();
        final JLabel time = new JLabel("000");
        flags = new JLabel("0");
        DecimalFormat form = new DecimalFormat("000");
        ActionListener timerAction = new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                long current = System.currentTimeMillis();
                second = (current - start) / 1000;
                time.setText(form.format(second));
            }
        };
        
        timer = new Timer(1000, timerAction);
        final JButton highScore = new JButton("High Score");
        ActionListener score = new ActionListener() {
            
            public void actionPerformed(ActionEvent event) {
                JFrame scoreBoard = new JFrame("Score Board");
                scoreBoard.setLayout(new GridLayout(6,1));
                scoreBoard.setLocation(500,500);
                scoreBoard.setSize(300,500);
                JLabel head = new JLabel("High Scores");
                scoreBoard.add(head);

                try {
                    FileReader r = new FileReader("files/scores.txt");
                    BufferedReader b = new BufferedReader(r);
                    for (int i = 0; i < 5; i++) {
                        String string = b.readLine();
                        if (string != null) {
                            JLabel label = new JLabel((i + 1) + "." + string, 
                                                      SwingConstants.CENTER);
                            scoreBoard.add(label);
                        }
                    }
                    b.close();
                } catch (FileNotFoundException e) {
                    System.out.println("NO FILE FOUND");
                } catch (IOException y) {
                    System.out.println(y.getMessage());
                }
                scoreBoard.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                scoreBoard.setVisible(true);
            }
        };
        highScore.addActionListener(score);
        final Grid g = new Grid();
        flags.setText(form.format(g.getFlag()));
        final JButton reset = new JButton("New Game");
        reset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                g.reset();
            }
        });
        JButton inst = new JButton("Instructions");
        ActionListener instruct = new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                JFrame x = new JFrame("Instructions");
                x.setSize(750,750);
                x.setLocation(300,500);
                x.setLayout(new GridLayout(9,1));
                x.add(new JLabel("Minesweeper \n"));
                x.add(new JLabel("Try to uncover all tiles without uncovering a bomb! \n"));
                x.add(new JLabel("Left click to uncover a tile. \n"));
                x.add(new JLabel("Right click to flag a place where you think a bomb may be \n"));
                x.add(new JLabel("Uncover all tiles without touching a bomb and you win!"));
                x.add(new JLabel("High scores are available. Win quickly and join the ranks \n"));
                x.pack();
                x.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                x.setVisible(true);
            }
        };
        
        inst.addActionListener(instruct);
        controlPanel.add(inst);
        controlPanel.add(reset);
        controlPanel.add(flags);
        controlPanel.add(time);
        controlPanel.add(highScore);
        FRAME.add(controlPanel, BorderLayout.NORTH);
        FRAME.add(g, BorderLayout.CENTER);
        FRAME.add(ENDING, BorderLayout.SOUTH);
        FRAME.pack();
        FRAME.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        FRAME.setVisible(true);
    }
    
    public static String[] readString(String line) {
        String[] s = line.split(":");
        return s;
    }
    
    public static void start() {
        start = System.currentTimeMillis();
        DecimalFormat f = new DecimalFormat("000");
        flags.setText(f.format(FLAGS));
        timer.start();
    }
    
    public static void win(boolean bool) {
        if (bool) {
            timer.stop();
            JFrame results = new JFrame("You win!");
            JLabel w = new JLabel("You took " + second + "seconds");
            results.setLayout(new BorderLayout());
            results.setLocation(400, 400);
            
            try {
                FileReader f = new FileReader("files/scores.txt");
                BufferedReader b = new BufferedReader(f);        
                ArrayList<Integer> scores = new ArrayList<Integer>();
                ArrayList<String> names = new ArrayList<String>();
                for (int i = 0; i < 5; i++) {
                    String line = b.readLine();
                    if (line != null) {
                        String[] s = readString(line);
                        names.add(i, s[0].trim());
                        scores.add(i, Integer.parseInt(s[1].trim()));
                    }
                }
                int s = scores.size();
                int beat = 0;
                if (s >= 5) {
                    beat = scores.get(4);
                } else {
                    beat = Integer.MAX_VALUE;
                }
                if ((int) second <= beat) {
                    JLabel high = new JLabel("New high score");
                    JButton enter = new JButton("enter");
                    JTextField username = new JTextField("Username", 1);
                    JPanel form = new JPanel();
                    form.setLayout(new GridLayout(1, 3));
                    form.add(username);
                    results.add(high, BorderLayout.NORTH);
                    results.add(form, BorderLayout.SOUTH);
                    ActionListener enterAction = new ActionListener() {
                        public void actionPerformed(ActionEvent event) {
                            String name = username.getText();
                            File file = Paths.get("files/scores.txt").toFile();
                            FileWriter writer = null;
                            try {
                                writer = new FileWriter(file, false);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            boolean written = false;
                            for (int i = 0; i <= Math.min(4, scores.size() - 1); i++) {
                                if (second < scores.get(i) && !written) {
                                    scores.add(i, (int) second);
                                    names.add(i, name);
                                    written = true;
                                }
                            }
                            if (!written) {
                                scores.add(0, (int) second);
                                names.add(0, name);
                            }
                            for (int i = 0; i <= Math.min(4, scores.size() - 1); i++) {
                                try {
                                    writer.write(names.get(i) + ":" + scores.get(i) + "\n");
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            if (scores.size() == 0) {
                                try {
                                    writer.write(names.get(0) + ":" + scores.get(0));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            
                            try {
                                writer.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    enter.addActionListener(enterAction);
                }
            } catch (IOException e) {
                System.out.println("error");
            }
            
            results.add(w, BorderLayout.CENTER);
            results.setSize(500,300);
            results.pack();
            results.setVisible(true);
        }
    }
    
    public static void gameOver(boolean bool) {
        if (bool) {
            timer.stop();
            JLabel e = new JLabel("You lost! Better luck next time");
            JFrame l = new JFrame("You lost! Better luck next time");
            l.setSize(200, 200);
            l.setLocation(400, 400);
            l.setLayout(new BorderLayout());
            l.add(e);
            l.pack();
            l.setVisible(true);
        }
    }
    
    public static void updateFlag(int flag) {
        if (flags != null) {
            DecimalFormat f = new DecimalFormat("00");
            flags.setText(f.format(flag));
        }
    }
    
    
    /**
     * Main method run to start and run the game. Initializes the runnable game
     * class of your choosing and runs it. IMPORTANT: Do NOT delete! You MUST
     * include a main method in your final submission.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Game());
    }
}
