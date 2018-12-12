package me.erikflores.pacman.Entity.Ghosts;

import me.erikflores.pacman.*;
import me.erikflores.pacman.Entity.PacMan;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Pinky extends Ghost {

    private PacMan pacMan;
    private final Location SCATTER_TARGET = new Location(0, 0);
    private Grid grid;
    private boolean atIntersection = true;

    public Pinky(Image[] sprites, PacMan pacMan, Grid grid, int size){
        super("Pinky", sprites, size, new Location(14, 16));
        this.pacMan = pacMan;
        this.grid = grid;
    }

    /**
     * Pinky targets 4 tiles ahead of PacMan when chasing
     */
    @Override
    public void move(){

        if(!isMoving()) { // If not moving to a tile,

            if(checkTunnel()){return;} // Check if in tunnel to change location to other side

            if (atIntersection){ // Get best direction if at intersection
                changeDirection();
            }

            // Move if possible
            Location newLocation = new Location(getLocation());
            newLocation.move(getDirection().getX(), getDirection().getY());
            Tile newTile = grid.getTileAt(newLocation.getColumn(), newLocation.getRow());

            if (newTile != null && newTile.addEntity(this)) { // Check if possible movement
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
     * Checks if is heading into tunnel to transport to other side
     * @return true if went into tunnel
     */
    private boolean checkTunnel(){
        int column = getLocation().getColumn();
        int row = getLocation().getRow();
        if(column == 27 && getDirection() == Direction.RIGHT && row == 16){ // RIGHT
            grid.getTileAt(column, row).removeEntity();
            setLocation(new Location(0, 16));
            grid.getTileAt(0, 16).addEntity(this);
            setSprite(new Location(getLocation().getColumn() * 20 - 9, getLocation().getRow() * 20 - 5));
            setMoving(true);
            return true;
        }else if(column == 0 && getDirection() == Direction.LEFT && row == 16){ // LEFT
            grid.getTileAt(column, row).removeEntity();
            setLocation(new Location(27, 16));
            grid.getTileAt(27, 16).addEntity(this);
            setSprite(new Location(getLocation().getColumn() * 20 - 1, getLocation().getRow() * 20 - 5));
            setMoving(true);
            return true;
        }
        return false;
    }

    /**
     * Sets direction to one closest to target tile if in chase mode
     */
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

        possibleDirections.remove(getOppositeDirection());
        Direction bestDirection = possibleDirections.get(0);
        double smallestDistance = 100000;
        for(Direction possible : possibleDirections){
            Tile nextTile = grid.getTileAt(getLocation().getColumn() + possible.getX(), getLocation().getRow() + possible.getY());
            if(nextTile == null || nextTile.isWall() || (nextTile.isGhostDoor() && possible == Direction.DOWN && getMode() != Mode.DEAD)){
                continue;
            }
            double x, y;
            if(grid.getTileAt(getLocation().getColumn(), getLocation().getRow()).isGhostHouse()) { // get out of it
                x = getSpawn().getColumn() - nextTile.getLocation().getColumn();
                y = getSpawn().getRow() - 3 - nextTile.getLocation().getRow();
                setMode(Mode.CHASE);
            }else if(getMode() == Mode.CHASE) {
                x = (pacMan.getX() + (pacMan.getDirection().getX() * 80)) - (nextTile.getLocation().getColumn() * 20);
                y = (pacMan.getY() + (pacMan.getDirection().getY() * 80)) - (nextTile.getLocation().getRow() * 20);
            }else if(getMode() == Mode.SCATTER){
                x = (SCATTER_TARGET.getColumn()) - (nextTile.getLocation().getColumn());
                y = (SCATTER_TARGET.getRow()) - (nextTile.getLocation().getRow());
            }else{ // DEAD
                x = (getSpawn().getColumn() - (nextTile.getLocation().getColumn()));
                y = (getSpawn().getRow() - (nextTile.getLocation().getRow()));
            }
            double distance = Math.sqrt((x * x) + (y * y));
            if(distance < smallestDistance){
                bestDirection = possible;
                smallestDistance = distance;
            }else if(distance == smallestDistance){
                bestDirection = getBestDirection(possible, bestDirection);
            }
        }
        setDirection(bestDirection);
    }

}
