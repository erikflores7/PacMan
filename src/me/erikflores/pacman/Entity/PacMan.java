package me.erikflores.pacman.Entity;

import me.erikflores.pacman.Direction;
import me.erikflores.pacman.Grid;
import me.erikflores.pacman.Location;
import me.erikflores.pacman.Tile;

import java.awt.*;
import java.util.ArrayList;

/**
 *  Actual PacMan, handles its movement
 */
public class PacMan extends Entity {

    private Location location, spriteLocation, spawn;
    private Direction direction, nextDirection;
    private Grid grid;
    private int size, counter;
    private boolean moving = false;
    private Image[] sprites;

    public PacMan(){
        this(null, 0, 0, 0, null);
    }

    public PacMan(Image[] sprites, int column, int row, int size, Grid grid){
        this(sprites, new Location(column, row), size, grid);
    }

    public PacMan(Image[] sprites, Location location, int size, Grid grid){
        super("PacMan");
        setLocation(location);
        spriteLocation = new Location(getLocation().getColumn() * size - 5, getLocation().getRow() * size - 5);
        spawn = new Location(getLocation());
        setDirection(Direction.LEFT);
        this.sprites = sprites;
        this.size = size;
        this.grid = grid;
        grid.getTileAt(getLocation().getColumn(), getLocation().getRow()).addEntity(this);
    }

    @Override
    public Location getLocation() {
        return this.location;
    }

    /**
     * Move function, tries to move to queue'd up direction before trying to move to regular direction
     */
    @Override
    public void move() {

        if(!isMoving()) {

            if(checkTunnel()){ return;}

            // If there is a queue'd up direction, check if we can finally move to it
            if (getNextDirection() != null) {
                Location newLocation = new Location(getLocation());
                newLocation.move(getNextDirection().getX(), getNextDirection().getY());
                Tile newTile = grid.getTileAt(newLocation.getColumn(), newLocation.getRow()); // Tile to check if can move

                if (newTile != null && newTile.addEntity(this)) { // If can move, move to it and delete queue
                    grid.getTileAt(getLocation().getColumn(), getLocation().getRow()).removeEntity();
                    setLocation(newLocation);
                    this.direction = getNextDirection();
                    this.nextDirection = null;
                    setMoving(true);
                    animate();
                    return;
                }
            }

            // Check for this direction
            Location newLocation = new Location(getLocation());
            newLocation.move(getDirection().getX(), getDirection().getY());
            Tile newTile = grid.getTileAt(newLocation.getColumn(), newLocation.getRow());

            if (newTile != null) {
                if(newTile.addEntity(this)) {
                    grid.getTileAt(getLocation().getColumn(), getLocation().getRow()).removeEntity();
                    setLocation(newLocation);
                    setMoving(true);
                    animate();
                }
            }
        }else{
            animate();
        }

    }

    /**
     * Moves pacman in increments to next tile until it reaches it
     */
    private void animate(){

        if(isMoving()){
            switch(getDirection()){
                case UP: spriteLocation.move(0, -5);
                    break;
                case DOWN: spriteLocation.move(0, 5);
                    break;
                case LEFT: spriteLocation.move(-5, 0);
                    break;
                case RIGHT: spriteLocation.move(5, 0);
                    break;
            }
            counter++; // Sprite counter for switching image

            Location destination = new Location(getLocation().getColumn() * size - 5, getLocation().getRow() * size - 5);
            if(destination.equals(spriteLocation)){
                setMoving(false);
            }
        }
    }

    /**
     * Queues up direction as nextDirection and will try to change direction each move
     *
     * @param direction Direction to add to queue, will change to it once it can
     */
    @Override
    public void setDirection(Direction direction) {

        // Check if this is the first time setting a direction
        if(getDirection() == null){
            this.direction = direction;
            return;
        }

        // If input is same direction, will get rid of queue
        if(getDirection() == direction){
            this.nextDirection = null;
            return;
        }

        // If input is different direction, will add to queue
        if(getNextDirection() != direction){
            this.nextDirection = direction;
        }
    }

    private void setLocation(Location location){
        this.location = new Location(location);
    }

    /**
     * @return Current direction of Pacman
     */
    @Override
    public Direction getDirection() {
        return this.direction;
    }

    /**
     * @return Queue'd up direction
     */
    private Direction getNextDirection(){
        return this.nextDirection;
    }

    @Override
    public Image getImage(){
        int index = 0;
        switch(getDirection()){
            case RIGHT: break;
            case LEFT: index = 2; break;
            case DOWN: index = 6; break;
            case UP: index = 4; break;
        }
        if(counter % 4 == 0 || counter % 4 == 3){
            return sprites[index + 1];
        }
        return sprites[index];
    }

    public void restart(){
        counter = 0;
        setLocation(spawn);
        setMoving(false);
        spriteLocation = new Location(getLocation().getColumn() * size - 5, getLocation().getRow() * size - 5);
        grid.getTileAt(getLocation().getColumn(), getLocation().getRow()).addEntity(this);
        this.direction = null;
        this.nextDirection = null;
        setDirection(Direction.LEFT);
    }

    private boolean checkTunnel(){
        if(getLocation().getColumn() == 27 && getDirection() == Direction.RIGHT && getLocation().getRow() == 16){
            grid.getTileAt(getLocation().getColumn(), getLocation().getRow()).removeEntity();
            setLocation(new Location(0, 16));
            grid.getTileAt(0, 16).addEntity(this);
            this.spriteLocation = new Location(getLocation().getColumn() * 20 - 10, getLocation().getRow() * 20 - 5);
            setMoving(true);
            return true;
        }else if(getLocation().getColumn() == 0 && getDirection() == Direction.LEFT && getLocation().getRow() == 16){
            grid.getTileAt(getLocation().getColumn(), getLocation().getRow()).removeEntity();
            setLocation(new Location(27, 16));
            grid.getTileAt(27, 16).addEntity(this);
            this.spriteLocation = new Location(getLocation().getColumn() * 20, getLocation().getRow() * 20 - 5);
            setMoving(true);
            return true;
        }

        return false;
    }

    private boolean isMoving(){
        return this.moving;
    }

    private void setMoving(boolean moving){
        this.moving = moving;
    }

    public int getX(){
        return spriteLocation.getColumn();
    }

    public int getY(){
        return spriteLocation.getRow();
    }

    @Override
    public String toString(){
        return super.toString() + ": " + location.toString() + " " + direction.toString();
    }
}
