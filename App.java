import java.util.Random;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;

public class App {
    
    static boolean turnPointAdded = true;
    public static void main (String[] args) {
        Random rnd = new Random(27);

        final int BOARD_SIZE = 20;
        final int GENERATION_OFFSET = 4;
        String[][] board = new String[BOARD_SIZE][BOARD_SIZE];
        TurningPoint turnPoint = new TurningPoint(0, 0, Snake.NORTH);

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j ++) {
                board[i][j] = " _ ";
            }
        }

        int initialDirection = rnd.nextInt(4);
        int xcoord = rnd.nextInt(BOARD_SIZE - (GENERATION_OFFSET * 2)) + GENERATION_OFFSET;
        int ycoord = rnd.nextInt(BOARD_SIZE - (GENERATION_OFFSET * 2)) + GENERATION_OFFSET;
        Point initialHead = new Point(xcoord, ycoord);
        Snake player = new Snake(board, initialHead, initialDirection, 2);

        Action setNorth = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                if (player.getDirection() != Snake.NORTH && player.getDirection() != Snake.SOUTH && turnPointAdded) {
                    turnPoint.setX(player.getHead().getX());
                    turnPoint.setY(player.getHead().getY());
                    turnPoint.setDirection(player.getDirection());
                    turnPointAdded = false;
                    player.setDirection(Snake.NORTH);
                }
            } 
        };
        Action setEast = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                if (player.getDirection() != Snake.EAST && player.getDirection() != Snake.WEST && turnPointAdded) {
                    turnPoint.setX(player.getHead().getX());
                    turnPoint.setY(player.getHead().getY());
                    turnPoint.setDirection(player.getDirection());
                    turnPointAdded = false;
                    player.setDirection(Snake.EAST);
                }
            } 
        };
        Action setSouth = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                if (player.getDirection() != Snake.NORTH && player.getDirection() != Snake.SOUTH && turnPointAdded) {
                    turnPoint.setX(player.getHead().getX());
                    turnPoint.setY(player.getHead().getY());
                    turnPoint.setDirection(player.getDirection());
                    turnPointAdded = false;
                    player.setDirection(Snake.SOUTH);
                }
            } 
        };
        Action setWest = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                if (player.getDirection() != Snake.EAST && player.getDirection() != Snake.WEST && turnPointAdded) {
                    turnPoint.setX(player.getHead().getX());
                    turnPoint.setY(player.getHead().getY());
                    turnPoint.setDirection(player.getDirection());
                    turnPointAdded = false;
                    player.setDirection(Snake.WEST);
                }
            } 
        };

        JFrame app = new JFrame();
        app.setTitle("Snake Game");
        app.setSize(12 * BOARD_SIZE, 12 * BOARD_SIZE);
        app.setResizable(false);
        app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        app.setLayout(null); 
        app.setVisible(true);
        
        JPanel boardPanel = new JPanel(new GridLayout(BOARD_SIZE, BOARD_SIZE));
        boardPanel.setSize(10 * BOARD_SIZE, 10 * BOARD_SIZE);
        boardPanel.setVisible(true);
        boardPanel.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "setNorth");
        boardPanel.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "setEast");
        boardPanel.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "setSouth");
        boardPanel.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "setWest");
        boardPanel.getActionMap().put("setNorth", setNorth);
        boardPanel.getActionMap().put("setEast", setEast);
        boardPanel.getActionMap().put("setSouth", setSouth);
        boardPanel.getActionMap().put("setWest", setWest);
        app.add(boardPanel);

        boolean exit = false;
        boolean spawnFood = true;
        boolean init = true;
        long start;
        long end = 0L;
        while (!exit) {    
            start = System.nanoTime();
            if ((start - end) / 1000000 > 200) {
                if (!turnPointAdded) {
                    player.addTurnPoint(new Point(turnPoint.getX(), turnPoint.getY()), turnPoint.getDirection());
                    turnPointAdded = true;
                }
                System.out.println("------------------------------------");
                if (spawnFood) {
                    xcoord = rnd.nextInt(BOARD_SIZE);
                    ycoord = rnd.nextInt(BOARD_SIZE);
                    Point food = new Point(xcoord, ycoord);
                    board[food.getY()][food.getX()] = " $ ";
                    player.setFood(food);
                    if (!init) {
                        player.grow();
                    }
                    else {
                        init = false;
                    }
                }

                if (!player.willBiteBoard() && !player.willBiteBody()) {
                    spawnFood = player.shiftHead();
                }
                else {
                    exit = true;
                    break;
                }

                board = player.makeSnake();
                for (int i = 0; i < board.length; i++) {
                    for (int j = 0; j < board[i].length; j ++) {
                        System.out.print(board[i][j]);
                    }
                    System.out.println("");
                }

                end = start;
            }
        }

        System.out.println("GAME OVER!");

    }
}