/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schrank_final;

import java.awt.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.event.ActionEvent;

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

class MainScreen extends JFrame {
    public MainScreen() {
        super("BattleShip");
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
    private MainScreen screen;
    
    private int[][] playerBoard = new int[10][10];
    private int[][] enemyBoard = new int[10][10];

    BattleShip(MainScreen screen) {
        this.screen = screen;
    }

    public void startGame() {
        this.getPlayerShips();
    }

    public void getPlayerShips() {
        JPanel boardPanel = new JPanel();

        boardPanel.setSize(500, 500);

        GridLayout layout = new GridLayout(11, 11);
        boardPanel.setLayout(layout);

        for (int i = 0; i < 11; i++) {
            for (int j = 0; j < 11; j++) {
                JButton button = new JButton();
                
                if(j == 0) {
                    button.setText(String.valueOf(i));
                }
                
                 if(i == 0) {
                    button.setText(String.valueOf(j));
                }

//                try {
//                    Image img = ImageIO.read(getClass().getResource("target.jpg"));
//                    button.setIcon(new ImageIcon(img));
//                } catch (Exception ex) {
//                    System.out.println(ex);
//                }

                button.setPreferredSize(new Dimension(30, 30));

                boardPanel.add(button);
            }
        }

        screen.add(boardPanel);

        JPanel shipPanel = new JPanel();

        shipPanel.setSize(200, 500);
        
        shipPanel.setBorder(new EmptyBorder(0, 50, 0, 0));

        shipPanel.setLayout(new GridBagLayout());

        shipPanel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();

        JButton ship = new JButton("Ship");

        shipPanel.add(ship, gbc);

        screen.add(shipPanel);
    }

}
