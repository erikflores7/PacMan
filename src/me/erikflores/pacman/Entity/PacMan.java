package me.erikflores.pacman.Entity;

import me.erikflores.pacman.Direction;
import me.erikflores.pacman.Grid;
import me.erikflores.pacman.Location;
import me.erikflores.pacman.Tile;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 *  Actual PacMan, handles its movement
 */
public class PacMan extends Entity {

    private Location location, spriteLocation;
    private Direction direction, nextDirection;
    private Grid grid;
    private int size, counter;
    private boolean moving = false;
    private Image[] sprites;

    public PacMan(){
        this(null, 0, 0, 0, Direction.RIGHT, null);
    }

    public PacMan(Image[] sprites, int column, int row, int size, Direction direction, Grid grid){
        this(sprites, new Location(column, row), size, direction, grid);
    }

    public PacMan(Image[] sprites, Location location, int size, Direction direction, Grid grid){
        super("PacMan");
        setLocation(location);
        spriteLocation = new Location(getLocation().getColumn() * size - 5, getLocation().getRow() * size - 5);
        setDirection(direction);
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

        if(!moving) {

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
                    moving = true;
                    animate();
                    return;
                }
            }

            // Check for this direction
            Location newLocation = new Location(getLocation());
            newLocation.move(getDirection().getX(), getDirection().getY());
            Tile newTile = grid.getTileAt(newLocation.getColumn(), newLocation.getRow());

            if (newTile != null && newTile.addEntity(this)) {
                grid.getTileAt(getLocation().getColumn(), getLocation().getRow()).removeEntity();
                setLocation(newLocation);
                moving = true;
                animate();
            }
        }else{
            animate();
        }

    }

    private void animate(){
        if(moving){

            Direction direction = getDirection();

            switch(direction){
                case UP: spriteLocation.move(0, -5);
                    break;
                case DOWN: spriteLocation.move(0, 5);
                    break;
                case LEFT: spriteLocation.move(-5, 0);
                    break;
                case RIGHT: spriteLocation.move(5, 0);
                    break;
            }
            counter++;

            Location destination = new Location(getLocation().getColumn() * size - 5, getLocation().getRow() * size - 5);
            if(destination.equals(spriteLocation)){
                moving = false;
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

    @Override
    public Direction getDirection() {
        return this.direction;
    }

    private Direction getNextDirection(){
        return this.nextDirection;
    }

    @Override
    public Shape getShape() {
        return null;
    }

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
