package me.erikflores.pacman;
/*
 * Filename: PacMan.java
 * Author: Erik Flores
 * Date: December 4, 2018
 *
 */
import me.erikflores.pacman.Entity.Food;
import me.erikflores.pacman.Entity.Ghosts.Blinky;
import me.erikflores.pacman.Entity.Ghosts.Clyde;
import me.erikflores.pacman.Entity.Ghosts.Inky;
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
import java.io.PrintWriter;

/**
 * Controls the Display of the game PacMan as well as the main game loop
 *
 */
public class PacManController extends JPanel implements ActionListener {

    private static final int WIDTH = 560;
    private static final int HEIGHT = 620;
    private static final int DELAY = 35;
    private static final int PIXELS = 20;

    private boolean isPaused = true;
    private int waiting = 100;
    public static int frightTimer = 0;
    private int lives = 4;

    private Image mapImage;
    private Image[] sprites = new Image[64];

    private Grid grid;
    private PacMan pacMan;
    private Blinky blinky;
    private Pinky pinky;
    private Inky inky;
    private Clyde clyde;

    private int points = 0;

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
            BufferedImage spriteSheet = ImageIO.read(new File("pacman.png"));
            sprites[0] = spriteSheet.getSubimage(4, 1, 13, 13).getScaledInstance(30, 30, Image.SCALE_DEFAULT);
            sprites[1] = spriteSheet.getSubimage(20, 1, 13, 13).getScaledInstance(30, 30, Image.SCALE_DEFAULT);
            sprites[2] = spriteSheet.getSubimage(4, 17, 13, 13).getScaledInstance(30, 30, Image.SCALE_DEFAULT);
            sprites[3] = spriteSheet.getSubimage(20, 17, 13, 13).getScaledInstance(30, 30, Image.SCALE_DEFAULT);
            sprites[4] = spriteSheet.getSubimage(4, 33, 13, 13).getScaledInstance(30, 30, Image.SCALE_DEFAULT);
            sprites[5] = spriteSheet.getSubimage(20, 33, 13, 13).getScaledInstance(30, 30, Image.SCALE_DEFAULT);
            sprites[6] = spriteSheet.getSubimage(4, 49, 13, 13).getScaledInstance(30, 30, Image.SCALE_DEFAULT);
            sprites[7] = spriteSheet.getSubimage(20, 49, 13, 13).getScaledInstance(30, 30, Image.SCALE_DEFAULT);

            // BLINKY sprites RIGHT, LEFT, UP, DOWN TODO make a for loop to clean this up
            sprites[8] = spriteSheet.getSubimage(4, 65, 14, 14).getScaledInstance(30, 30, Image.SCALE_DEFAULT);
            sprites[9] = spriteSheet.getSubimage(20, 65, 14, 14).getScaledInstance(30, 30, Image.SCALE_DEFAULT);
            sprites[10] = spriteSheet.getSubimage(36, 65, 14, 14).getScaledInstance(30, 30, Image.SCALE_DEFAULT);
            sprites[11] = spriteSheet.getSubimage(52, 65, 14, 14).getScaledInstance(30, 30, Image.SCALE_DEFAULT);
            sprites[12] = spriteSheet.getSubimage(68, 65, 14, 14).getScaledInstance(30, 30, Image.SCALE_DEFAULT);
            sprites[13] = spriteSheet.getSubimage(84, 65, 14, 14).getScaledInstance(30, 30, Image.SCALE_DEFAULT);
            sprites[14] = spriteSheet.getSubimage(100, 65, 14, 14).getScaledInstance(30, 30, Image.SCALE_DEFAULT);
            sprites[15] = spriteSheet.getSubimage(116, 65, 14, 14).getScaledInstance(30, 30, Image.SCALE_DEFAULT);

            // Pinky sprites
            sprites[16] = spriteSheet.getSubimage(4, 81, 14, 14).getScaledInstance(30, 30, Image.SCALE_DEFAULT);
            sprites[17] = spriteSheet.getSubimage(20, 81, 14, 14).getScaledInstance(30, 30, Image.SCALE_DEFAULT);
            sprites[18] = spriteSheet.getSubimage(36, 81, 14, 14).getScaledInstance(30, 30, Image.SCALE_DEFAULT);
            sprites[19] = spriteSheet.getSubimage(52, 81, 14, 14).getScaledInstance(30, 30, Image.SCALE_DEFAULT);
            sprites[20] = spriteSheet.getSubimage(68, 81, 14, 14).getScaledInstance(30, 30, Image.SCALE_DEFAULT);
            sprites[21] = spriteSheet.getSubimage(84, 81, 14, 14).getScaledInstance(30, 30, Image.SCALE_DEFAULT);
            sprites[22] = spriteSheet.getSubimage(100, 81, 14, 14).getScaledInstance(30, 30, Image.SCALE_DEFAULT);
            sprites[23] = spriteSheet.getSubimage(116, 81, 14, 14).getScaledInstance(30, 30, Image.SCALE_DEFAULT);

            // Inky Sprites
            sprites[24] = spriteSheet.getSubimage(4, 97, 14, 14).getScaledInstance(30, 30, Image.SCALE_DEFAULT);
            sprites[25] = spriteSheet.getSubimage(20, 97, 14, 14).getScaledInstance(30, 30, Image.SCALE_DEFAULT);
            sprites[26] = spriteSheet.getSubimage(36, 97, 14, 14).getScaledInstance(30, 30, Image.SCALE_DEFAULT);
            sprites[27] = spriteSheet.getSubimage(52, 97, 14, 14).getScaledInstance(30, 30, Image.SCALE_DEFAULT);
            sprites[28] = spriteSheet.getSubimage(68, 97, 14, 14).getScaledInstance(30, 30, Image.SCALE_DEFAULT);
            sprites[29] = spriteSheet.getSubimage(84, 97, 14, 14).getScaledInstance(30, 30, Image.SCALE_DEFAULT);
            sprites[30] = spriteSheet.getSubimage(100, 97, 14, 14).getScaledInstance(30, 30, Image.SCALE_DEFAULT);
            sprites[31] = spriteSheet.getSubimage(116, 97, 14, 14).getScaledInstance(30, 30, Image.SCALE_DEFAULT);

            // Clyde sprites
            sprites[32] = spriteSheet.getSubimage(4, 113, 14, 14).getScaledInstance(30, 30, Image.SCALE_DEFAULT);
            sprites[33] = spriteSheet.getSubimage(20, 113, 14, 14).getScaledInstance(30, 30, Image.SCALE_DEFAULT);
            sprites[34] = spriteSheet.getSubimage(36, 113, 14, 14).getScaledInstance(30, 30, Image.SCALE_DEFAULT);
            sprites[35] = spriteSheet.getSubimage(52, 113, 14, 14).getScaledInstance(30, 30, Image.SCALE_DEFAULT);
            sprites[36] = spriteSheet.getSubimage(68, 113, 14, 14).getScaledInstance(30, 30, Image.SCALE_DEFAULT);
            sprites[37] = spriteSheet.getSubimage(84, 113, 14, 14).getScaledInstance(30, 30, Image.SCALE_DEFAULT);
            sprites[38] = spriteSheet.getSubimage(100, 113, 14, 14).getScaledInstance(30, 30, Image.SCALE_DEFAULT);
            sprites[39] = spriteSheet.getSubimage(116, 113, 14, 14).getScaledInstance(30, 30, Image.SCALE_DEFAULT);

            // Eyes
            sprites[40] = spriteSheet.getSubimage(132, 81, 14, 14).getScaledInstance(30, 30, Image.SCALE_DEFAULT);
            sprites[41] = spriteSheet.getSubimage(148, 81, 14, 14).getScaledInstance(30, 30, Image.SCALE_DEFAULT);

            // Frightened
            sprites[44] = spriteSheet.getSubimage(132, 65, 14, 14).getScaledInstance(30, 30, Image.SCALE_DEFAULT);
            sprites[45] = spriteSheet.getSubimage(148, 65, 14, 14).getScaledInstance(30, 30, Image.SCALE_DEFAULT);
            sprites[46] = spriteSheet.getSubimage(164, 65, 14, 14).getScaledInstance(30, 30, Image.SCALE_DEFAULT);
            sprites[47] = spriteSheet.getSubimage(180, 65, 14, 14).getScaledInstance(30, 30, Image.SCALE_DEFAULT);





        }catch(IOException e){ // Exit if any images not found
            System.out.println("Image not found!");
            System.exit(1);
        }

        setFocusable(true);
        setFocusTraversalKeysEnabled(false);

        timer.setInitialDelay(DELAY);

        // Creates Grid with Tiles using 2D Map Array, and spawns PacMan, Blinky, Pinky, Inky, and Clyde
        grid = new Grid(getMap(), PIXELS, this);
        pacMan = new PacMan(sprites, new Location(13, 25), PIXELS, grid);
        blinky = new Blinky(sprites, pacMan, grid, PIXELS);
        pinky = new Pinky(sprites, pacMan, grid, PIXELS);
        inky = new Inky(sprites, pacMan, blinky, grid, PIXELS);
        clyde = new Clyde(sprites, pacMan, grid, PIXELS);


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
        graphics.drawImage(inky.getImage(), inky.getX(), inky.getY(), null);
        graphics.drawImage(clyde.getImage(), clyde.getX(), clyde.getY(), null);

        if(isPaused){
            g.drawString("Paused!", 13 * PIXELS, 20 * PIXELS);
        }
        g.drawString("Score: " + points, 13 * PIXELS, PIXELS);
    }

    /**
     * Called every DELAY, if game is not paused, moves pacman, blinky, pinky, etc.
     *
     * @param e ActionEvent
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if(!isPaused && waiting <= 0) { // Waiting is initial delay before starting level

            if(frightTimer > 0){
                frightTimer--;
            }else if(frightTimer == 0){
                if(blinky.getMode() != Mode.DEAD){blinky.setMode(Mode.CHASE);}
                if(pinky.getMode() != Mode.DEAD){pinky.setMode(Mode.CHASE);}
                if(inky.getMode() != Mode.DEAD){inky.setMode(Mode.CHASE);}
                if(clyde.getMode() != Mode.DEAD){clyde.setMode(Mode.CHASE);}
            }
            pacMan.move();
            checkCollision();
            blinky.move();
            pinky.move();
            inky.move();
            clyde.move();
            checkCollision();
            if(waiting < 60) { // This means pacman died or finished level, initial delay started so don't repaint
                repaint();
            }
        }else if(waiting > 0){
            waiting--;
            if(waiting < 60){
                repaint();
            }else if(waiting == 60){
                respawn();
            }
        }
    }

    /**
     *  Toggles paused state
     */
    private void togglePause(){
        isPaused = !isPaused;
    }

    /**
     * Checks collision with any ghosts and calls die() if any collide with a ghost that is not dead or frightened
     */
    private void checkCollision(){
        if(pacMan.getLocation().equals(blinky.getLocation())){
            switch(blinky.getMode()){
                case FRIGHTENED: blinky.die(); break;
                case DEAD: break;
                default: die(); return;
            }
        }
        if(pacMan.getLocation().equals(clyde.getLocation())){
            switch(clyde.getMode()){
                case FRIGHTENED: clyde.die(); break;
                case DEAD: break;
                default: die(); return;
            }
        }
        if(pacMan.getLocation().equals(pinky.getLocation())){
            switch(pinky.getMode()){
                case FRIGHTENED: pinky.die(); break;
                case DEAD: break;
                default: die(); return;
            }
        }
        if(pacMan.getLocation().equals(inky.getLocation())){
            switch(inky.getMode()){
                case FRIGHTENED: inky.die(); break;
                case DEAD: break;
                default: die();
            }
        }
    }

    /**
     * Adds 10 points per food pellet eaten
     */
    void eatFood(){
        points += 10;
    }

    /**
     * Starts frightened mode for ~6 seconds TODO change this value as level increases
     */
    void eatPower(){
        blinky.setMode(Mode.FRIGHTENED);
        pinky.setMode(Mode.FRIGHTENED);
        inky.setMode(Mode.FRIGHTENED);
        clyde.setMode(Mode.FRIGHTENED);
        points += 190;
        frightTimer = 180;
    }

    /**
     * Causes game to pause and decreases lives
     */
    void die(){
        waiting = 100;
        lives--;
        if(lives == 0){
            saveScore();
            grid.restart();
            points = 0;
            lives = 3;
        }
    }

    /**
     * Respawns everyone
     */
    private void respawn(){
        pacMan.restart();
        blinky.respawn();
        inky.respawn();
        pinky.respawn();
        clyde.respawn();
    }

    /**
     * Sets delay and restarts the grid
     */
    void nextLevel(){
        waiting = 100;
        grid.restart();
    }

    private void saveScore(){
        try {
            PrintWriter writer = new PrintWriter("highScores.txt", "UTF-8");
            writer.println("AAA " + points);
            writer.close();
        }catch(IOException e){}
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
                {1, 7, 1, 1, 1, 1, 2, 1, 1, 1, 1, 1, 2, 1, 1, 2, 1, 1, 1, 1, 1, 2, 1, 1, 1, 1, 7, 1},
                {1, 2, 1, 1, 1, 1, 2, 1, 1, 1, 1, 1, 2, 1, 1, 2, 1, 1, 1, 1, 1, 2, 1, 1, 1, 1, 2, 1},
                {1, 3, 2, 2, 2, 2, 3, 2, 2, 3, 2, 2, 3, 2, 2, 3, 2, 2, 3, 2, 2, 3, 2, 2, 2, 2, 3, 1},
                {1, 2, 1, 1, 1, 1, 2, 1, 1, 2, 1, 1, 1, 1, 1, 1, 1, 1, 2, 1, 1, 2, 1, 1, 1, 1, 2, 1},
                {1, 2, 1, 1, 1, 1, 2, 1, 1, 2, 1, 1, 1, 1, 1, 1, 1, 1, 2, 1, 1, 2, 1, 1, 1, 1, 2, 1},
                {1, 3, 2, 2, 2, 2, 3, 1, 1, 3, 2, 2, 3, 1, 1, 3, 2, 2, 3, 1, 1, 3, 2, 2, 2, 2, 3, 1},
                {1, 1, 1, 1, 1, 1, 2, 1, 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 2, 1, 1, 1, 1, 1, 1},
                {0, 0, 0, 0, 0, 1, 2, 1, 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 2, 1, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 1, 2, 1, 1, 4, 0, 0, 4, 4, 4, 4, 0, 0, 4, 1, 1, 2, 1, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 1, 2, 1, 1, 0, 1, 1, 1, 5, 5, 1, 1, 1, 0, 1, 1, 2, 1, 0, 0, 0, 0, 0},
                {1, 1, 1, 1, 1, 1, 2, 1, 1, 0, 1, 6, 6, 6, 6, 6, 6, 1, 0, 1, 1, 2, 1, 1, 1, 1, 1, 1},
                {0, 0, 0, 0, 0, 0, 3, 0, 0, 4, 1, 6, 6, 6, 6, 6, 6, 1, 4, 0, 0, 3, 0, 0, 0, 0, 0, 0},
                {1, 1, 1, 1, 1, 1, 2, 1, 1, 0, 1, 6, 6, 6, 6, 6, 6, 1, 0, 1, 1, 2, 1, 1, 1, 1, 1, 1},
                {0, 0, 0, 0, 0, 1, 2, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 2, 1, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 1, 2, 1, 1, 4, 0, 0, 0, 0, 0, 0, 0, 0, 4, 1, 1, 2, 1, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 1, 2, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 2, 1, 0, 0, 0, 0, 0},
                {1, 1, 1, 1, 1, 1, 2, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 2, 1, 1, 1, 1, 1, 1},
                {1, 3, 2, 2, 2, 2, 3, 2, 2, 3, 2, 2, 3, 1, 1, 3, 2, 2, 3, 2, 2, 3, 2, 2, 2, 2, 3, 1},
                {1, 2, 1, 1, 1, 1, 2, 1, 1, 1, 1, 1, 2, 1, 1, 2, 1, 1, 1, 1, 1, 2, 1, 1, 1, 1, 2, 1},
                {1, 2, 1, 1, 1, 1, 2, 1, 1, 1, 1, 1, 2, 1, 1, 2, 1, 1, 1, 1, 1, 2, 1, 1, 1, 1, 2, 1},
                {1, 7, 2, 3, 1, 1, 3, 2, 2, 3, 2, 2, 3, 2, 2, 3, 2, 2, 3, 2, 2, 3, 1, 1, 3, 2, 7, 1},
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
                case KeyEvent.VK_UP:  // Up Arrow
                    pacMan.setDirection(Direction.UP);
                    break;
                case KeyEvent.VK_S: // S, try to go downward
                case KeyEvent.VK_DOWN:
                    pacMan.setDirection(Direction.DOWN);
                    break;
                case KeyEvent.VK_A: // A, try to go left
                case KeyEvent.VK_LEFT:
                    pacMan.setDirection(Direction.LEFT);
                    break;
                case KeyEvent.VK_D: // D, try to go right
                case KeyEvent.VK_RIGHT:
                    pacMan.setDirection(Direction.RIGHT);
                    break;
                case KeyEvent.VK_SPACE: togglePause(); break; // Space, toggle pause
            }
        }

        /**
         *  Not used
         * @param e Key Released Event
         */
        @Override
        public void keyReleased(KeyEvent e) {}
    }
}

