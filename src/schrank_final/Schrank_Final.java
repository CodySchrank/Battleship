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
import java.util.List;
import java.lang.Math;

/**
 *1
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
    water ("water", 0),
    watercloud ("watercloud", 0),
    watercloudmiss ("watercloudmiss", 0),
    waterfire ("waterfire", 0),
    fire ("fire", 0),
    tugboat ("tugboat", 2),
    submarine ("submarine", 3),
    destroyer ("destroyer", 3),
    carrier ("carrier", 5),
    battleship ("battleship", 4);

    private final String value;
    private final int length;

    private Resources(String value, int length) {
        this.value = value;
        this.length = length;
    }

    public String getValue() {
        return value;
    }

    public int getLength() {
        return length;
    }
}

class MainScreen extends JFrame {
    private HashMap<String, Image> images = new HashMap<String, Image>();

    protected GlassPane glassPane;

    public MainScreen() {
        super("Schrank_BattleShip");

        try {
            Image water = ImageIO.read(getClass().getResource("assets/Water.png"));
            images.put(Resources.water.getValue(), water);

            Image watercloud = ImageIO.read(getClass().getResource("assets/WaterCloud.png"));
            images.put(Resources.watercloud.getValue(), watercloud);

            Image watercloudmiss = ImageIO.read(getClass().getResource("assets/WaterCloudMiss.png"));
            images.put(Resources.watercloudmiss.getValue(), watercloudmiss);

            Image waterfire = ImageIO.read(getClass().getResource("assets/WaterFire.png"));
            images.put(Resources.waterfire.getValue(), waterfire);

            Image fire = ImageIO.read(getClass().getResource("assets/Fire.png"));
            images.put(Resources.fire.getValue(), fire);

            Image tugboat = ImageIO.read(getClass().getResource("assets/TugBoat.png"));
            images.put(Resources.tugboat.getValue(), tugboat);

            Image destroyer = ImageIO.read(getClass().getResource("assets/Destroyer.png"));
            images.put(Resources.destroyer.getValue(), destroyer);

            Image battleship = ImageIO.read(getClass().getResource("assets/Battleship.png"));
            images.put(Resources.battleship.getValue(), battleship);

            Image submarine = ImageIO.read(getClass().getResource("assets/Submarine.png"));
            images.put(Resources.submarine.getValue(), submarine);

            Image carrier = ImageIO.read(getClass().getResource("assets/Carrier.png"));
            images.put(Resources.carrier.getValue(), carrier);
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

        glassPane = new GlassPane();
        glassPane.setSize(1000, 500);
        glassPane.setVisible(true);

        this.setGlassPane(glassPane);

        BattleShip battleShip = new BattleShip(this);
        battleShip.startGame();
    }
}

class BattleShip {
    public ShipButton shipToSet;
    public MainScreen screen;
    public BoardPanel boardPanel;
    public BoardPanel enemyBoardPanel;
    public JPanel shipPanel;
    public JLabel turnLabel;

    public boolean enemyTurn = false;
    public GamePhase phase = GamePhase.start;

    public JLabel invalidShipPlacementLabel = new JLabel("Current Ship Placement Invalid!");
    
    private int[][] playerBoard = new int[10][10];
    private int[][] enemyBoard = new int[10][10];

    private String yourTurnString = String.format("<html><div style=\"width:%dpx;\">%s</div></html>", 76, "Your Turn!  Click on a cloud to shoot a missile");
    private String enemyTurnString = String.format("<html><div style=\"width:%dpx;\">%s</div></html>", 76, "Enemy's Turn!  Better hope there missile misses your boat!");

    private final Random RND = new Random();

    BattleShip(MainScreen screen) {
        this.screen = screen;
    }

    public void startGame() {
        this.phase = GamePhase.setting;

        this.boardPanel = new BoardPanel(this, false);

        screen.add(boardPanel);

        //ship panel

        this.shipPanel = new JPanel();

        shipPanel.setSize(200, 500);
        
        shipPanel.setBorder(new EmptyBorder(0, 100, 0, 0));

        shipPanel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0;

        shipPanel.setOpaque(false);

        JLabel shipLabel = new JLabel("Click each ship, then click board, to set their position");

        shipPanel.add(shipLabel, gbc);

        ShipButton tugboat = new ShipButton(Resources.tugboat, this);
        
        gbc.gridy = 1;
        shipPanel.add(tugboat, gbc);

        ShipButton submarine = new ShipButton(Resources.submarine, this);
        
        gbc.gridy = 2;
        shipPanel.add(submarine, gbc);

        ShipButton destroyer = new ShipButton(Resources.destroyer, this);
        
        gbc.gridy = 3;
        shipPanel.add(destroyer, gbc);

        ShipButton battleship = new ShipButton(Resources.battleship, this);
        
        gbc.gridy = 4;
        shipPanel.add(battleship, gbc);

        ShipButton carrier = new ShipButton(Resources.carrier, this);
        
        gbc.gridy = 5;
        shipPanel.add(carrier, gbc);

        gbc.gridy = 6;
        invalidShipPlacementLabel.setForeground(Color.red);
        invalidShipPlacementLabel.setVisible(false);
        shipPanel.add(invalidShipPlacementLabel, gbc);

        screen.add(shipPanel);
    }

    //returns false if ship placement is invalid
    boolean setShip(int x, int y, int i, int j) {
        System.out.println(shipToSet.shipName);
        System.out.println("x: " + x);
        System.out.println("y: " + y);
        System.out.println("i: " + i);
        System.out.println("j: " + j);
        System.out.println();


        // only going to support hozizontal placement because it is easier
        int shipLength = shipToSet.shipLength;

        // i is row
        // j is col
        int realI = i - 1;
        int realJ = j - 1;

        //invalid
        if(realJ + shipLength > 10) {
            return false;
        }
        
        //test for ships already there
        for(int testCol = realJ; testCol < realJ + shipLength; testCol++) {
            if(playerBoard[realI][testCol] == 1) {
                return false;
            }
        }

        for(int col = realJ; col < 10; col++) {
            //because 0-10, a-j adds an extra spot but player cant be there

            //valid placement
            if(shipLength > 0) {
                playerBoard[realI][col] = 1;
                shipLength--;
            }
        }

        printBoard(this.playerBoard);

        screen.glassPane.paintShip(x, y, screen.getImage(shipToSet.resource));
        screen.glassPane.repaint();
        screen.glassPane.revalidate();
        screen.glassPane.setVisible(true);

        shipToSet = null;

        return true;
    }

    void play() {
        this.phase = GamePhase.playing;

        screen.remove(this.shipPanel);

        JPanel emptyPanel = new JPanel();

        turnLabel = new JLabel(this.yourTurnString);

        emptyPanel.add(turnLabel);
        
        emptyPanel.setOpaque(false);

        screen.add(emptyPanel);

        this.enemyBoardPanel = new BoardPanel(this, true);

        screen.add(enemyBoardPanel);

        generateEnemyBoard();

        System.out.println("Enemy Board:");
        printBoard(this.enemyBoard);
    }

    void printBoard(int[][] board) {
        for(int i = 0; i < 10; i++) {
            for(int j = 0; j < 10; j++) {
                System.out.printf("[%d]", board[i][j]);
            }

            System.out.println();
        }
    }

    public boolean checkIfAllShipsSet() {
        // int total = 2 + 3 + 3 + 4 + 5;
        int total = 2;
        int runningTotal = 0;

        for(int i = 0; i < 10; i++) {
            for(int j = 0; j < 10; j++) {
                if(this.playerBoard[i][j] == 1){
                    runningTotal++;
                }
            }
        }

        return runningTotal == total;
    }

    public void generateEnemyBoard() {
        int[] ships = { 2, 3, 3, 4, 5 };

        int[] rows = generateRandomSetOfNumbers(0, 10, ships.length);

        for(int i = 0; i < ships.length; i++) {
            int col = generateRandomSetOfNumbers(0, 10 - ships[i], 1)[0];

            System.out.println("col: " + col + " row: " + rows[i] + " shiplength: " + ships[i]);
            final int lastCol = col + ships[i];

            for(int c = col; c < lastCol; c++) {
                System.out.println(c);
                this.enemyBoard[rows[i]][c] = 1;
            }
        }
    }

    public int[] generateRandomSetOfNumbers(int lowerBound, int upperBound, int count) {
        ArrayList<Integer> list = new ArrayList<Integer>();

        for (int i = lowerBound; i < upperBound; i++) {
            list.add(new Integer(i));
        }

        Collections.shuffle(list);

        int[] nums = new int[count];

        for (int i = 0; i < count; i++) {
            nums[i] = list.get(i);
        }

        return nums;
    }

    public boolean checkIfPlayerShotIsHit(int i, int j) {
        return this.enemyBoard[i][j] == 1;
    }
}


class GlassPane extends JComponent {
    private List<Ship> ships;
    private int[] xPossible = new int[10];
    private int[] yPossible = new int[10];

    GlassPane() {
        ships = new ArrayList<Ship>();

        int xStart = 160;
        for(int i = 0; i < xPossible.length; i++) {
            xPossible[i] = xStart + (i * 30);
        }

        int yStart = 165;
        for(int i = 0; i < yPossible.length; i++) {
            yPossible[i] = yStart + (i * 30);
        }
    }

    public void paintShip(int x, int y, Image shipImage) {
        x = closestTo(x, xPossible);
        y = closestTo(y, yPossible);

        int X_CORRECTION = 12;
        int Y_CORRECTION = 60;

        ships.add(new Ship(x - X_CORRECTION, y - Y_CORRECTION, shipImage));
    }

    @Override
    public void paintComponent(Graphics g) {
        for (Ship ship : this.ships) {
            g.drawImage(ship.image, ship.x, ship.y, this);
        }
    }

    public int closestTo(int number, int[] set) {
        int closest = set[0];
        int prev = Math.abs(set[0] - number);

        for(int i = 1; i < set.length; i++) {
            int diff = Math.abs(set[i] - number);

            if(diff < prev) {
                prev = diff;
                closest = set[i];
            }
        }

        return closest;
    }
}

class BoardPanel extends JPanel {
    public boolean enemyBoard = false;

    BoardPanel(BattleShip game, boolean enemyboard) {
        super();

        this.enemyBoard = enemyboard;

        setSize(500, 500);

        GridLayout layout = new GridLayout(11, 11);
        setLayout(layout);

        for (int i = 0; i < 11; i++) {
            for (int j = 0; j < 11; j++) {
                JButton button = new JButton();
                
                if(j == 0) {
                    button.setText(Character.toString((char) (i + 96))); // a - j
                }
                
                if(i == 0) {
                    button.setText(String.valueOf(j));
                }

                if (i != 0 && j != 0) {
                    if (game.phase == GamePhase.setting) {
                        Image img = game.screen.getImage(Resources.water);
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

                            if (game.shipToSet != null) {
                                game.invalidShipPlacementLabel.setVisible(false);

                                boolean validPlacement = game.setShip(x, y, thisI, thisJ);

                                if (!validPlacement) {
                                    game.invalidShipPlacementLabel.setVisible(true);
                                } else {
                                    if (game.checkIfAllShipsSet()) {
                                        System.out.println("Time to play!");
                                        game.play();
                                    } else {
                                        System.out.println("More ships to set");
                                    }
                                }
                            }
                        });
                    } else if(game.phase == GamePhase.playing) {
                        if(this.enemyBoard) {
                            Image img = game.screen.getImage(Resources.watercloud);
                            button.setIcon(new ImageIcon(img));
                            button.setBorderPainted(false);
                            button.setContentAreaFilled(false);
                            button.setFocusPainted(false);
                            button.setOpaque(false);

                            final int realI = i - 1;
                            final int realJ = j - 1;

                            button.addActionListener((ActionEvent e) -> {
                                if(game.checkIfPlayerShotIsHit(realI, realJ)) {
                                    System.out.println("HIT! i " + realI +  " j " + realJ);
                                    Image newImg = game.screen.getImage(Resources.waterfire);
                                    button.setDisabledIcon(new ImageIcon(newImg));
                                    button.setEnabled(false);
                                } else {
                                    System.out.println("MISS! i " + realI +  " j " + realJ);
                                    Image newImg = game.screen.getImage(Resources.watercloudmiss);
                                    button.setDisabledIcon(new ImageIcon(newImg));
                                    button.setEnabled(false);
                                }
                            });
                        } else {
                            Image img = game.screen.getImage(Resources.water);
                            button.setIcon(new ImageIcon(img));
                            button.setBorderPainted(false);
                            button.setContentAreaFilled(false);
                            button.setFocusPainted(false);
                            button.setOpaque(false);

                            final int thisI = i;
                            final int thisJ = j;

                            button.addActionListener((ActionEvent e) -> {
                            
                            });
                        }
                    }
                }
                
                button.setPreferredSize(new Dimension(30, 30));

                add(button);
            }
        }
    }
}

class Ship {
    public int x;
    public int y;
    public Image image;
    
    Ship(int x, int y, Image image) {
        this.x = x;
        this.y = y;
        this.image = image;
    }

}

class ShipButton extends JButton {
    public Resources resource;
    public final String shipName;
    public final int shipLength;

    ShipButton(Resources resource, BattleShip game) {
        super();

        this.resource = resource;
        this.shipName = resource.getValue();
        this.shipLength = resource.getLength();
            
        setIcon(new ImageIcon(game.screen.getImage(resource)));
        setBorderPainted(false); 
        setContentAreaFilled(false); 
        setFocusPainted(false); 
        setOpaque(false);

        addActionListener((ActionEvent e) -> {
            if(game.shipToSet == null) {
                game.shipToSet = this;
                setEnabled(false);
            }
        });
    }
}