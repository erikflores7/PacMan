package me.erikflores.pacman.Entity.Ghosts;

import me.erikflores.pacman.*;
import me.erikflores.pacman.Entity.Entity;

import java.awt.*;

/**
 * Ghost super class, handles almost everything except movement so far
 */
public abstract class Ghost extends Entity {

    private Mode mode = Mode.CHASE;
    private Image[] sprites;
    private Location spriteLocation, tileLocation, spawnLocation;
    private int spriteCounter = 0;
    private Direction direction = Direction.LEFT;
    private boolean moving = false;
    private int size = 20;
    private int speed = 4;

    public Ghost(String name, Image[] sprites, int size, Location startLocation){
        super(name);
        this.sprites = sprites;
        this.size = size;
        this.spawnLocation = new Location(startLocation);
        this.spriteLocation = new Location(startLocation.getColumn() * size - 5, startLocation.getRow() * size - 5);
        setLocation(startLocation);
    }

    @Override
    public Location getLocation() {
        return this.tileLocation;
    }

    void setLocation(Location location){
        this.tileLocation = new Location(location);
    }

    @Override
    public void move() {}

    @Override
    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    @Override
    public Direction getDirection() {
        return this.direction;
    }

    /***
     * @return the image of the ghost based on what direction they're facing
     */
    @Override
    public Image getImage(){

        int index = 0;

        if(getMode() == Mode.DEAD){
            return sprites[40];
        }
        if(getMode() == Mode.FRIGHTENED){
            if (spriteCounter % 4 == 0 || spriteCounter % 4 == 3) {
                return sprites[44];
            }
            return sprites[45];
        }
        switch (getName()) {
            case "Blinky":
                switch (getDirection()) {
                    case UP: index = 12; break;
                    case DOWN: index = 14; break;
                    case LEFT: index = 10; break;
                    case RIGHT: index = 8; break;
                }
                if (spriteCounter % 4 == 0 || spriteCounter % 4 == 3) {
                    return sprites[index];
                }
                return sprites[index + 1];
            case "Pinky":
                switch (getDirection()) {
                    case UP: index = 20; break;
                    case DOWN: index = 22; break;
                    case LEFT: index = 18; break;
                    case RIGHT: index = 16; break;
                }
                if (spriteCounter % 4 == 0 || spriteCounter % 4 == 3) {
                    return sprites[index];
                }
                return sprites[index + 1];
            case "Inky":
                switch (getDirection()) {
                    case UP: index = 28; break;
                    case DOWN: index = 30; break;
                    case LEFT: index = 26; break;
                    case RIGHT: index = 24; break;
                }
                if (spriteCounter % 4 == 0 || spriteCounter % 4 == 3) {
                    return sprites[index];
                }
                return sprites[index + 1];
            case "Clyde":
                switch (getDirection()) {
                    case UP: index = 36; break;
                    case DOWN: index = 38; break;
                    case LEFT: index = 34; break;
                    case RIGHT: index = 32; break;
                }
                if (spriteCounter % 4 == 0 || spriteCounter % 4 == 3) { // Iterate between the 2 different states
                    return sprites[index];
                }
                return sprites[index + 1];
        }
        return sprites[0];
    }

    public int getX(){
        return spriteLocation.getColumn();
    }

    public int getY(){
        return spriteLocation.getRow();
    }

    public void animate(){
        if(moving){
            switch(getDirection()){
                case UP: spriteLocation.move(0, -speed);
                    break;
                case DOWN: spriteLocation.move(0, speed);
                    break;
                case LEFT: spriteLocation.move(-speed, 0);
                    break;
                case RIGHT: spriteLocation.move(speed, 0);
                    break;
            }
            spriteCounter++;

            Location destination = new Location(getLocation().getColumn() * size - 5, getLocation().getRow() * size - 5);
            if(destination.equals(spriteLocation)){
                setMoving(false);
            }
        }
    }

    public boolean isMoving(){
        return this.moving;
    }

    void setMoving(boolean moving){
        this.moving = moving;
    }

    /**
     * Ghosts can't reverse when chasing or in scatter mode
     *
     * @return Gets the opposite direction of Ghost
     */
    Direction getOppositeDirection(){
        switch (getDirection()){
            case UP: return Direction.DOWN;
            case DOWN: return Direction.UP;
            case LEFT: return Direction.RIGHT;
            case RIGHT: return Direction.LEFT;
        }
        return Direction.RIGHT;
    }

    /**
     * When multiply directions have the same distance to PacMan, there are priority and RIGHT is not possible
     *
     * @param dir1 First direction to compare
     * @param dir2 Second direcion to compare
     * @return Direction with higher priority
     */
    Direction getBestDirection(Direction dir1, Direction dir2){
        if(dir2 == Direction.RIGHT && dir1 != Direction.RIGHT){ return dir1; }
        if(dir1 == Direction.RIGHT && dir2 != Direction.RIGHT){ return dir2; }
        if(dir1 == Direction.UP){ return dir1; }
        if(dir2 == Direction.UP){ return dir2; }
        if(dir1 == Direction.LEFT){ return dir1; }
        if(dir2 == Direction.LEFT){ return dir2; }
        if(dir1 == Direction.DOWN){ return dir1; }
        return dir2;
    }

    public Mode getMode(){
        return mode;
    }

    public void setMode(Mode newMode){
        mode = newMode;
        if(isMoving()) {
            // Wait for animation to end
            Thread t = new Thread(() -> {
                while(isMoving()){
                    try { Thread.sleep(10); } catch(InterruptedException e) { /* we tried */}

                }
                if (!isMoving()) {
                    switch(getMode()){
                        case FRIGHTENED: setSpeed(2); setDirection(getOppositeDirection()); break;
                        case DEAD: setSpeed(10); break;
                        default: setSpeed(4);
                    }
                }
            });
            t.start();
        }else{
            switch(getMode()){
                case FRIGHTENED: setSpeed(2); setDirection(getOppositeDirection()); break;
                case DEAD: setSpeed(5); break;
                default: setSpeed(4);
            }
        }
    }

    void setSpeed(int speed){
        this.speed = speed;
    }

    public int getSpriteCounter(){
        return this.spriteCounter;
    }

    void setSprite(Location location){
        this.spriteLocation = new Location(location);
    }

    Location getSpawn(){
        return this.spawnLocation;
    }

    public void respawn(){
        setMode(Mode.CHASE);
        setMoving(false);
        spriteCounter = 0;
        setDirection(Direction.LEFT);
        setLocation(spawnLocation);
        this.spriteLocation = new Location(getLocation().getColumn() * size - 5, getLocation().getRow() * size - 5);
    }

    public void die(){
        setMode(Mode.DEAD);
        spriteCounter = 0;
    }

    @Override
    public String toString(){
        return super.toString() + ": Moving? " + isMoving() + " " + getDirection().toString() + " Mode: " + getMode()
                + " Tile: (" + getLocation().toString() + ")";
    }
}
