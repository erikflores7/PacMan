package me.erikflores.pacman.Entity.Ghosts;

import me.erikflores.pacman.Entity.PacMan;
import me.erikflores.pacman.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Blinky extends Ghost {

    private PacMan pacMan;
    private Grid grid;
    private boolean atIntersection = true;

    public Blinky(Image[] sprites, PacMan pacMan, Grid grid, int size){
        super("Blinky", sprites, size, new Location(13, 13));
        this.pacMan = pacMan;
        this.grid = grid;
    }

    /**
     * Blinky chases PacMan and compares distance to him when changing direction
     */
    @Override
    public void move(){

        if(!isMoving()) {

            if (atIntersection){
                List<Direction> possibleDirections = new ArrayList<>(Arrays.asList(Direction.values()));
                possibleDirections.remove(getOppositeDirection()); // Can't reverse
                Direction bestDirection = possibleDirections.get(0);
                double smallestDistance = 100000;
                for(Direction possible : possibleDirections){
                    Tile nextTile = grid.getTileAt(getLocation().getColumn() + possible.getX(), getLocation().getRow() + possible.getY());
                    if(nextTile == null || nextTile.isWall()){
                        continue;
                    }
                    double x = (pacMan.getLocation().getColumn()) - (nextTile.getLocation().getColumn());
                    double y = (pacMan.getLocation().getRow()) - (nextTile.getLocation().getRow());
                    double distance = Math.sqrt((x * x) + (y * y)); // Distance to PacMan
                    if(distance < smallestDistance){
                        bestDirection = possible;
                        smallestDistance = distance;
                    }else if(distance == smallestDistance){ // Get one with priority
                        bestDirection = getBestDirection(possible, bestDirection);
                    }
                }
                setDirection(bestDirection);
            }

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

}
