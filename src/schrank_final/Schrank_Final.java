/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schrank_final;

import javax.imageio.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

/**
 *
 * @author Cody Schrank
 */
public class Schrank_Final {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        MainScreen screen = new MainScreen();
        screen.initScreen();
    }

}

enum GamePhase {
    start,
    setting,
    playing,
    end
}

enum Resources {
    water ("water"),
    tugboat ("tugboat"),
    submarine ("submarine"),
    destroyer ("destroyer"),
    carrier ("carrier"),
    battleship ("battleship");

    private final String value;

    private Resources(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

class MainScreen extends JFrame {
    private HashMap<String, Image> images = new HashMap<String, Image>();

    public MainScreen() {
        super("BattleShip");

        try {
            Image water = ImageIO.read(getClass().getResource("water.png"));
            images.put(Resources.water.getValue(), water);

            Image tugboat = ImageIO.read(getClass().getResource("tugboat.png"));
            images.put(Resources.tugboat.getValue(), tugboat);
        } catch (Exception ex) {
            System.out.println(ex);

            System.exit(0);
        }
    }

    public void clearScreen() {
        this.getContentPane().removeAll();
        this.getContentPane().revalidate();
        this.getContentPane().repaint();
    }

    public void refreshScreen() {
        this.getContentPane().revalidate();
        this.getContentPane().repaint();
    }

    public void initScreen() {
        this.openMenu();

        this.getContentPane().setBackground(Color.gray);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(500, 500);
        this.setResizable(true);
        this.setVisible(true);
    }

    public Image getImage(Resources resource) {
        return this.images.get(resource.getValue());
    }

    private void openMenu() {
        GridBagLayout layout = new GridBagLayout();
        setLayout(layout);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0;

        JLabel title = new JLabel("BattleShip");
        title.setForeground(Color.WHITE);

        add(title, gbc);

        JButton startGameButton = new JButton("Start Game");
        startGameButton.addActionListener((ActionEvent e) -> {
            this.initGame();
        });

        gbc.gridy = 1;

        add(startGameButton, gbc);
    }

    private void initGame() {
        this.setSize(1000, 500);

        this.clearScreen();

        BattleShip battleShip = new BattleShip(this);
        battleShip.startGame();
    }
}

class BattleShip {
    public JButton shipToSet;
    public MainScreen screen;
    
    private GamePhase phase = GamePhase.start;
    
    private int[][] playerBoard = new int[10][10];
    private int[][] enemyBoard = new int[10][10];

    BattleShip(MainScreen screen) {
        this.screen = screen;
    }

    public void startGame() {
        this.phase = GamePhase.setting;

        JPanel boardPanel = new JPanel();

        boardPanel.setSize(500, 500);

        GridLayout layout = new GridLayout(11, 11);
        boardPanel.setLayout(layout);

        for (int i = 0; i < 11; i++) {
            for (int j = 0; j < 11; j++) {
                JButton button = new JButton();
                
                if(j == 0) {
                    button.setText(Character.toString((char) (i + 96))); // a - j
                }
                
                if(i == 0) {
                    button.setText(String.valueOf(j));
                }

                if(i != 0 && j != 0) {
                    Image img = screen.getImage(Resources.water);
                    button.setIcon(new ImageIcon(img));
                    button.setBorderPainted(false); 
                    button.setContentAreaFilled(false); 
                    button.setFocusPainted(false); 
                    button.setOpaque(false);
                    
                    final int thisI = i;
                    final int thisJ = j;

                    button.addActionListener((ActionEvent e) -> {
                        PointerInfo a = MouseInfo.getPointerInfo();
                        Point b = a.getLocation();
                        int x = (int) b.getX();
                        int y = (int) b.getY();

                        if(shipToSet != null) {
                            this.setShip(x, y, thisI, thisJ);
                        }
                    });
                }
                
                button.setPreferredSize(new Dimension(30, 30));

                boardPanel.add(button);
            }
        }

        screen.add(boardPanel);

        //ship panel

        JPanel shipPanel = new JPanel();

        shipPanel.setSize(200, 500);
        
        shipPanel.setBorder(new EmptyBorder(0, 50, 0, 0));

        shipPanel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0;

        shipPanel.setOpaque(false);

        JLabel shipLabel = new JLabel("Click each ship, then click board, to set their position");

        shipPanel.add(shipLabel, gbc);

        ShipButton tugboat = new ShipButton(Resources.tugboat, this);
        
        gbc.gridy = 1;
        shipPanel.add(tugboat, gbc);

        screen.add(shipPanel);
    }

    void setShip(int x, int y, int i, int j) {
        System.out.println("x: " + x);
        System.out.println("y: " + y);
        System.out.println("i: " + i);
        System.out.println("j: " + j);

        shipToSet = null;

        // if() some condition to move on
    }

    void play() {

    }
}

class ShipButton extends JButton {
    ShipButton(Resources resource, BattleShip game) {
        setIcon(new ImageIcon(game.screen.getImage(Resources.tugboat)));
        setBorderPainted(false); 
        setContentAreaFilled(false); 
        setFocusPainted(false); 
        setOpaque(false);

        addActionListener((ActionEvent e) -> {
            game.shipToSet = this;
            setEnabled(false);
        });
    }
}