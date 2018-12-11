package me.erikflores.pacman.Entity;

import me.erikflores.pacman.Direction;
import me.erikflores.pacman.Entity.Entity;
import me.erikflores.pacman.Grid;
import me.erikflores.pacman.Location;
import me.erikflores.pacman.Tile;

import java.awt.*;

public class Food extends Entity {

    private Rectangle food;
    private Grid grid;


    public Food(Tile tile, Grid grid){
        super("Food");
        this.grid = grid;
        food = new Rectangle(tile.getLocation().getColumn() * 20 + 8, tile.getLocation().getRow() * 20 + 8, 4, 4);
    }

    /**
     * Passes on whether there are more food pellets left or not
     * @return True if there are more food pellets left
     */
    public boolean eat(){
        return grid.removeFood(this);
    }

    @Override
    public Location getLocation() {
        return null;
    }

    @Override
    public void move() {}

    @Override
    public void setDirection(Direction direction) {}

    @Override
    public Direction getDirection() {
        return null;
    }

    public Shape getShape(){
        return food;
    }

}
