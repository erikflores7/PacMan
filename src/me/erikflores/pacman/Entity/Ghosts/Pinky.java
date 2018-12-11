package me.erikflores.pacman.Entity.Ghosts;

import me.erikflores.pacman.*;
import me.erikflores.pacman.Entity.PacMan;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Pinky extends Ghost {

    private PacMan pacMan;
    private final Location SCATTER_TARGET = new Location(0, 0);
    private Grid grid;
    private boolean atIntersection = true;

    public Pinky(Image[] sprites, PacMan pacMan, Grid grid, int size){
        super("Pinky", sprites, size, new Location(14, 13));
        this.pacMan = pacMan;
        this.grid = grid;
    }

    /**
     * Pinky targets 4 tiles ahead of PacMan when chasing
     *
     */
    @Override
    public void move(){

        if(!isMoving()) { // If not moving to a tile,

            if(checkTunnel()){return;} // Check if in tunnel to change location to other side

            if (atIntersection){ // Get best direction if at intersection
                List<Direction> possibleDirections = new ArrayList<>(Arrays.asList(Direction.values()));
                possibleDirections.remove(getOppositeDirection());
                Direction bestDirection = possibleDirections.get(0);
                double smallestDistance = 100000;
                for(Direction possible : possibleDirections){
                    Tile nextTile = grid.getTileAt(getLocation().getColumn() + possible.getX(), getLocation().getRow() + possible.getY());
                    if(nextTile == null || nextTile.isWall() || (nextTile.isGhostDoor() && getMode() != Mode.FRIGHTENED)){
                        continue;
                    }
                    double x, y;
                    if(getMode() == Mode.CHASE) {
                        x = (pacMan.getX() + (pacMan.getDirection().getX() * 80)) - (nextTile.getLocation().getColumn() * 20);
                        y = (pacMan.getY() + (pacMan.getDirection().getY() * 80)) - (nextTile.getLocation().getRow() * 20);
                    }else{
                        x = (SCATTER_TARGET.getColumn()) - (nextTile.getLocation().getColumn());
                        y = (SCATTER_TARGET.getRow()) - (nextTile.getLocation().getRow());
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

            // Move if possible
            Location newLocation = new Location(getLocation());
            newLocation.move(getDirection().getX(), getDirection().getY());
            Tile newTile = grid.getTileAt(newLocation.getColumn(), newLocation.getRow());

            if (newTile != null && newTile.addEntity(this)) {
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

}
