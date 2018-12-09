package me.erikflores.pacman.Entity.Ghosts;

import me.erikflores.pacman.*;
import me.erikflores.pacman.Entity.Entity;

import java.awt.*;

public abstract class Ghost extends Entity {

    private Image[] sprites;
    private Location spriteLocation, tileLocation;
    private int spriteCounter = 0;
    private Direction direction = Direction.DOWN;
    private boolean moving = false;
    private int size = 20;

    public Ghost(String name, Image[] sprites, int size, Location startLocation){
        super(name);
        this.sprites = sprites;
        this.size = size;
        this.spriteLocation = new Location(startLocation.getColumn() * size - 5, startLocation.getRow() * size - 5);
        this.tileLocation = startLocation;
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

    @Override
    public Shape getShape() { return null; }

    public Image getImage(){

        if(super.getName().equals("Blinky")) {
            if (spriteCounter % 4 == 0 || spriteCounter % 4 == 3) {
                return sprites[8];
            }
            return sprites[9];
        }else if(super.getName().equals("Pinky")){
            if (spriteCounter % 4 == 0 || spriteCounter % 4 == 3) {
                return sprites[10];
            }
            return sprites[11];
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

            Direction direction = getDirection();

            switch(direction){
                case UP: spriteLocation.move(0, -4);
                    break;
                case DOWN: spriteLocation.move(0, 4);
                    break;
                case LEFT: spriteLocation.move(-4, 0);
                    break;
                case RIGHT: spriteLocation.move(4, 0);
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

    public int getSpriteCounter(){
        return this.spriteCounter;
    }

    @Override
    public String toString(){
        return super.toString() + " Moving: " + isMoving() + " Direction: " + getDirection().toString();
    }
}
