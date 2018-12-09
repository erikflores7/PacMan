package me.erikflores.pacman.Entity.Ghosts;

import me.erikflores.pacman.Direction;
import me.erikflores.pacman.Entity.PacMan;
import me.erikflores.pacman.Grid;
import me.erikflores.pacman.Location;
import me.erikflores.pacman.Tile;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Pinky extends Ghost {

    private PacMan pacMan;
    private Grid grid;
    private boolean atIntersection = true;

    public Pinky(Image[] sprites, PacMan pacMan, Grid grid, int size){
        super("Pinky", sprites, size, new Location(14, 16));
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

            if (atIntersection){ // Get best direction if at intersection
                List<Direction> possibleDirections = new ArrayList<>(Arrays.asList(Direction.values()));
                possibleDirections.remove(getOppositeDirection());
                Direction bestDirection = possibleDirections.get(0);
                double smallestDistance = 100000;
                for(Direction possible : possibleDirections){
                    Tile nextTile = grid.getTileAt(getLocation().getColumn() + possible.getX(), getLocation().getRow() + possible.getY());
                    if(nextTile == null || nextTile.isWall()){
                        continue;
                    }
                    double x = (pacMan.getX() + (pacMan.getDirection().getX() * 80)) - (nextTile.getLocation().getColumn() * 20 - 5);
                    double y = (pacMan.getY() + (pacMan.getDirection().getY() * 80)) - (nextTile.getLocation().getRow() * 20 - 5);
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

}
