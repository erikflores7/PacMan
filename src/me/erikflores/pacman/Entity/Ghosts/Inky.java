package me.erikflores.pacman.Entity.Ghosts;

import me.erikflores.pacman.Entity.PacMan;
import me.erikflores.pacman.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Inky extends Ghost {

    private PacMan pacMan;
    private Blinky blinky;
    private Grid grid;
    private boolean atIntersection = true;
    private static final Location SCATTER_TARGET = new Location(27, 35);

    public Inky(Image[] sprites, PacMan pacMan, Blinky blinky, Grid grid, int size){
        super("Inky", sprites, size, new Location(12, 16));
        this.pacMan = pacMan;
        this.blinky = blinky;
        this.grid = grid;
    }

    /**
     * Inky chases PacMan and uses Blinky's distance to decide where to move
     */
    @Override
    public void move(){

        if(!isMoving()) {

            if(checkTunnel()){return;} // Check if in tunnel to change location to other side

            if (atIntersection){ // Check if have to make decision on new direction
                changeDirection();
            }

            Location newLocation = new Location(getLocation());
            newLocation.move(getDirection().getX(), getDirection().getY());
            Tile newTile = grid.getTileAt(newLocation.getColumn(), newLocation.getRow());

            if (newTile != null && newTile.addEntity(this)) { // Add to new tile and move
                grid.getTileAt(getLocation().getColumn(), getLocation().getRow()).removeEntity();
                atIntersection = newTile.isIntersection();
                setLocation(newLocation);
                setMoving(true);
                animate();
            }
        }else{
            animate();
        }
    }

    /**
     * Checks if Inky is in tunnel and heading outside map to teleport to other side
     *
     * @return True if heading outside map and in tunnel
     */
    private boolean checkTunnel(){
        if(getLocation().getColumn() == 27 && getDirection() == Direction.RIGHT && getLocation().getRow() == 16){
            grid.getTileAt(getLocation().getColumn(), getLocation().getRow()).removeEntity();
            setLocation(new Location(0, 16));
            grid.getTileAt(0, 16).addEntity(this);
            setSprite(new Location(getLocation().getColumn() * 20 - 9, getLocation().getRow() * 20 - 5));
            setMoving(true);
            return true;
        }else if(getLocation().getColumn() == 0 && getDirection() == Direction.LEFT && getLocation().getRow() == 16){
            grid.getTileAt(getLocation().getColumn(), getLocation().getRow()).removeEntity();
            setLocation(new Location(27, 16));
            grid.getTileAt(27, 16).addEntity(this);
            setSprite(new Location(getLocation().getColumn() * 20 - 1, getLocation().getRow() * 20 - 5));
            setMoving(true);
            return true;
        }

        return false;
    }

    private void changeDirection(){
        List<Direction> possibleDirections = new ArrayList<>(Arrays.asList(Direction.values()));

        if(getMode() == Mode.FRIGHTENED){
            while(true) {
                setDirection(possibleDirections.get(new Random().nextInt(4)));
                Tile next = grid.getTileAt(getLocation().getColumn() + getDirection().getX(), getLocation().getRow() + getDirection().getY());
                if(!next.isWall() && !next.isGhostDoor()) {
                    return;
                }
            }
        }

        if(getMode() == Mode.GHOST_HOUSE){
            setDirection(getOppositeDirection());
            return;
        }

        possibleDirections.remove(getOppositeDirection()); // Can't reverse
        Direction bestDirection = possibleDirections.get(0);
        double smallestDistance = 100000;

        for(Direction possible : possibleDirections){ // Check directions available and compare distance
            Tile nextTile = grid.getTileAt(getLocation().getColumn() + possible.getX(), getLocation().getRow() + possible.getY());
            if(nextTile == null || nextTile.isWall() || (nextTile.isGhostDoor() && possible == Direction.DOWN && getMode() != Mode.DEAD)){
                continue; // Can't move to it, skip it
            }
            double x, y;
            if(grid.getTileAt(getLocation().getColumn(), getLocation().getRow()).isGhostHouse()) { // get out of it
                x = getSpawn().getColumn()  + 2 - nextTile.getLocation().getColumn();
                y = getSpawn().getRow() - 6 - nextTile.getLocation().getRow();
                setMode(Mode.CHASE);
            }else if(getMode() == Mode.CHASE) { // Set target tile
                Location blinkyLocation = blinky.getLocation();
                x = blinkyLocation.getColumn() + (2 * (pacMan.getLocation().getColumn() + (pacMan.getDirection().getX() * 2) - blinkyLocation.getColumn()));
                y = blinkyLocation.getRow() + (2 * (pacMan.getLocation().getRow() + (pacMan.getDirection().getY() * 2) - blinkyLocation.getRow()));
                x = (x - nextTile.getLocation().getColumn());
                y = (y - nextTile.getLocation().getRow());
            }else if(getMode() == Mode.SCATTER){ // Go to scatter mode target tile
                x = (SCATTER_TARGET.getColumn()) - (nextTile.getLocation().getColumn());
                y = (SCATTER_TARGET.getRow()) - (nextTile.getLocation().getRow());
            }else{ // DEAD
                x = (getSpawn().getColumn() - (nextTile.getLocation().getColumn()));
                y = (getSpawn().getRow() - (nextTile.getLocation().getRow()));
            }

            double distance = Math.sqrt((x * x) + (y * y)); // Distance to target location
            if(distance < smallestDistance){
                bestDirection = possible;
                smallestDistance = distance;
            }else if(distance == smallestDistance){ // Get one with priority
                bestDirection = getBestDirection(possible, bestDirection);
            }
        }
        setDirection(bestDirection);
    }

}
