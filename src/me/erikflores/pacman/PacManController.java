package me.erikflores.pacman;
/*
 * Filename: PacMan.java
 * Author: Erik Flores
 * Date: December 4, 2018
 *
 */
import me.erikflores.pacman.Entity.Food;
import me.erikflores.pacman.Entity.Ghosts.Blinky;
import me.erikflores.pacman.Entity.Ghosts.Pinky;
import me.erikflores.pacman.Entity.PacMan;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Controls the Display of the game PacMan as well as the main game loop
 *
 */
public class PacManController extends JPanel implements ActionListener {

    private static final int WIDTH = 560;
    private static final int HEIGHT = 620;
    private static final int DELAY = 45;
    private static final int PIXELS = 20;

    private boolean isPaused = true;

    private Image mapImage;
    private Image[] sprites = new Image[20];

    private Grid grid;
    private PacMan pacMan;
    private Blinky blinky;
    private Pinky pinky;

    private Timer timer = new Timer(DELAY, this);

    /**
     *  Initializes the JFrame and panel
     *
     * @param args Command line arguments, not used
     */
    public static void main(String[] args){

        JFrame frm = new JFrame();

        frm.setTitle("PacMan");
        frm.setContentPane(new PacManController());
        frm.setSize(WIDTH + 60, HEIGHT + 60);
        frm.setResizable(false);
        frm.setVisible(true);
        frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /**
     *  Default constructor, loads map image and begins the main game loop
     */
    public PacManController(){

        // Adding the map image and sprites
        try {
            mapImage = ImageIO.read(new File("pacmanMap.png")).getScaledInstance(WIDTH, HEIGHT, Image.SCALE_DEFAULT);
            BufferedImage spriteSheet = ImageIO.read(new File("pacmanSpriteSheet.png"));
            sprites[0] = spriteSheet.getSubimage(4, 1, 13, 13).getScaledInstance(30, 30, Image.SCALE_DEFAULT);
            sprites[1] = spriteSheet.getSubimage(20, 1, 13, 13).getScaledInstance(30, 30, Image.SCALE_DEFAULT);
            sprites[2] = spriteSheet.getSubimage(4, 17, 13, 13).getScaledInstance(30, 30, Image.SCALE_DEFAULT);
            sprites[3] = spriteSheet.getSubimage(20, 17, 13, 13).getScaledInstance(30, 30, Image.SCALE_DEFAULT);
            sprites[4] = spriteSheet.getSubimage(4, 33, 13, 13).getScaledInstance(30, 30, Image.SCALE_DEFAULT);
            sprites[5] = spriteSheet.getSubimage(20, 33, 13, 13).getScaledInstance(30, 30, Image.SCALE_DEFAULT);
            sprites[6] = spriteSheet.getSubimage(4, 49, 13, 13).getScaledInstance(30, 30, Image.SCALE_DEFAULT);
            sprites[7] = spriteSheet.getSubimage(20, 49, 13, 13).getScaledInstance(30, 30, Image.SCALE_DEFAULT);
            sprites[8] = spriteSheet.getSubimage(4, 65, 14, 14).getScaledInstance(30, 30, Image.SCALE_DEFAULT);
            sprites[9] = spriteSheet.getSubimage(20, 65, 14, 14).getScaledInstance(30, 30, Image.SCALE_DEFAULT);
            sprites[10] = spriteSheet.getSubimage(4, 81, 14, 14).getScaledInstance(30, 30, Image.SCALE_DEFAULT);
            sprites[11] = spriteSheet.getSubimage(20, 81, 14, 14).getScaledInstance(30, 30, Image.SCALE_DEFAULT);

        }catch(IOException e){ // Exit if any images not found
            System.out.println("Image not found!");
            System.exit(1);
        }

        setFocusable(true);
        setFocusTraversalKeysEnabled(false);

        timer.setInitialDelay(DELAY);

        // Creates Grid with Tiles using 2D Map Array, and spawns PacMan, Blinky, and Pinky
        grid = new Grid(getMap(), PIXELS);
        pacMan = new PacMan(sprites, new Location(13, 25), PIXELS, Direction.LEFT, grid);
        blinky = new Blinky(sprites, pacMan, grid, PIXELS);
        pinky = new Pinky(sprites, pacMan, grid, PIXELS);


        addKeyListener(new PacManListener()); // Adds key listener
        timer.start(); // starts loop
    }

    /**
     * Draws the map image, sprites, and food
     *
     * @param g Graphics
     */
    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D graphics = (Graphics2D) g;

        graphics.drawImage(mapImage, 0, 40, null);

        graphics.drawImage(pacMan.getImage(), pacMan.getX(), pacMan.getY(), null);

        for(Food food : grid.getFood()){
            graphics.setColor(Color.PINK);
            graphics.fill(food.getShape());
        }

        graphics.drawImage(blinky.getImage(), blinky.getX(), blinky.getY(), null);
        graphics.drawImage(pinky.getImage(), pinky.getX(), pinky.getY(), null);


    }

    /**
     * Called every DELAY, if game is not paused, moves pacman, blinky, pinky
     *
     * @param e ActionEvent
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if(!isPaused) {
            pacMan.move();
            blinky.move();
            pinky.move();
            repaint();
        }
    }

    /**
     *  Toggles paused state
     */
    private void togglePause(){
        isPaused = !isPaused;
    }


    /**
     * gets 2D array of map tiles
     *
     * @return 2D array of map tiles, 0 being empty, 1 being wall, 2 being with food, 3 intersection w/ food, 4 w/o, etc.
     */
    private int[][] getMap(){
        int[][] map = {
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 3, 2, 2, 2, 2, 3, 2, 2, 2, 2, 2, 3, 1, 1, 3, 2, 2, 2, 2, 2, 3, 2, 2, 2, 2, 3, 1},
                {1, 2, 1, 1, 1, 1, 2, 1, 1, 1, 1, 1, 2, 1, 1, 2, 1, 1, 1, 1, 1, 2, 1, 1, 1, 1, 2, 1},
                {1, 2, 1, 1, 1, 1, 2, 1, 1, 1, 1, 1, 2, 1, 1, 2, 1, 1, 1, 1, 1, 2, 1, 1, 1, 1, 2, 1},
                {1, 2, 1, 1, 1, 1, 2, 1, 1, 1, 1, 1, 2, 1, 1, 2, 1, 1, 1, 1, 1, 2, 1, 1, 1, 1, 2, 1},
                {1, 3, 2, 2, 2, 2, 3, 2, 2, 3, 2, 2, 3, 2, 2, 3, 2, 2, 3, 2, 2, 3, 2, 2, 2, 2, 3, 1},
                {1, 2, 1, 1, 1, 1, 2, 1, 1, 2, 1, 1, 1, 1, 1, 1, 1, 1, 2, 1, 1, 2, 1, 1, 1, 1, 2, 1},
                {1, 2, 1, 1, 1, 1, 2, 1, 1, 2, 1, 1, 1, 1, 1, 1, 1, 1, 2, 1, 1, 2, 1, 1, 1, 1, 2, 1},
                {1, 3, 2, 2, 2, 2, 3, 1, 1, 3, 2, 2, 3, 1, 1, 3, 2, 2, 3, 1, 1, 3, 2, 2, 2, 2, 3, 1},
                {1, 1, 1, 1, 1, 1, 2, 1, 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 2, 1, 1, 1, 1, 1, 1},
                {0, 0, 0, 0, 0, 1, 2, 1, 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 2, 1, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 1, 2, 1, 1, 4, 0, 0, 4, 0, 0, 4, 0, 0, 4, 1, 1, 2, 1, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 1, 2, 1, 1, 0, 1, 1, 1, 0, 0, 1, 1, 1, 0, 1, 1, 2, 1, 0, 0, 0, 0, 0},
                {1, 1, 1, 1, 1, 1, 2, 1, 1, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 1, 1, 2, 1, 1, 1, 1, 1, 1},
                {0, 0, 0, 0, 0, 0, 3, 0, 0, 4, 1, 0, 0, 0, 0, 0, 0, 1, 4, 0, 0, 3, 0, 0, 0, 0, 0, 0},
                {1, 1, 1, 1, 1, 1, 2, 1, 1, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 1, 1, 2, 1, 1, 1, 1, 1, 1},
                {0, 0, 0, 0, 0, 1, 2, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 2, 1, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 1, 2, 1, 1, 4, 0, 0, 0, 0, 0, 0, 0, 0, 4, 1, 1, 2, 1, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 1, 2, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 2, 1, 0, 0, 0, 0, 0},
                {1, 1, 1, 1, 1, 1, 2, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 2, 1, 1, 1, 1, 1, 1},
                {1, 3, 2, 2, 2, 2, 3, 2, 2, 3, 2, 2, 3, 1, 1, 3, 2, 2, 3, 2, 2, 3, 2, 2, 2, 2, 3, 1},
                {1, 2, 1, 1, 1, 1, 2, 1, 1, 1, 1, 1, 2, 1, 1, 2, 1, 1, 1, 1, 1, 2, 1, 1, 1, 1, 2, 1},
                {1, 2, 1, 1, 1, 1, 2, 1, 1, 1, 1, 1, 2, 1, 1, 2, 1, 1, 1, 1, 1, 2, 1, 1, 1, 1, 2, 1},
                {1, 3, 2, 3, 1, 1, 3, 2, 2, 3, 2, 2, 2, 2, 2, 2, 2, 2, 3, 2, 2, 3, 1, 1, 3, 2, 3, 1},
                {1, 1, 1, 2, 1, 1, 2, 1, 1, 2, 1, 1, 1, 1, 1, 1, 1, 1, 2, 1, 1, 2, 1, 1, 2, 1, 1, 1},
                {1, 1, 1, 2, 1, 1, 2, 1, 1, 2, 1, 1, 1, 1, 1, 1, 1, 1, 2, 1, 1, 2, 1, 1, 2, 1, 1, 1},
                {1, 3, 2, 3, 2, 2, 3, 1, 1, 3, 2, 2, 3, 1, 1, 3, 2, 2, 3, 1, 1, 3, 2, 2, 3, 2, 3, 1},
                {1, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 1, 1, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 1},
                {1, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 1, 1, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 1},
                {1, 3, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 3, 2, 2, 3, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 3, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}};
        return map;
    }

    /**
     *  Handles user input
     */
    private class PacManListener implements KeyListener {

        /**
         *  Not used
         * @param e Key Typed event
         */
        @Override
        public void keyTyped(KeyEvent e) {}

        /**
         * User key press handling
         *
         * @param e Key Pressed event
         */
        @Override
        public void keyPressed(KeyEvent e) {
            switch(e.getKeyCode()){
                case KeyEvent.VK_W: // W, try to go upward
                    pacMan.setDirection(Direction.UP);
                    break;
                case KeyEvent.VK_S: // S, try to go downward
                    pacMan.setDirection(Direction.DOWN);
                    break;
                case KeyEvent.VK_A: // A, try to go left
                    pacMan.setDirection(Direction.LEFT);
                    break;
                case KeyEvent.VK_D: // D, try to go right
                    pacMan.setDirection(Direction.RIGHT);
                    break;
                case KeyEvent.VK_SPACE: togglePause(); break; // Space, toggle pause
            }
        }

        /**
         *  Not used
         * @param e
         */
        @Override
        public void keyReleased(KeyEvent e) {}
    }
}

