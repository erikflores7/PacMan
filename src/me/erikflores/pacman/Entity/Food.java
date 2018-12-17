package me.erikflores.pacman.Entity;

import me.erikflores.pacman.Direction;
import me.erikflores.pacman.Grid;
import me.erikflores.pacman.Location;
import me.erikflores.pacman.Tile;

import java.awt.*;
import java.awt.geom.Ellipse2D;

public class Food extends Entity {

    private Shape food;
    private Grid grid;
    private boolean isPower = false;


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

    /**
     * Sets to power pellet which causes frightened mode to happen when eaten
     * Also visually bigger than regular food
     */
    public void setPower(){
        isPower = true;
        food = new Ellipse2D.Double(food.getBounds2D().getX() - 3, food.getBounds2D().getY() - 3, 10, 10);
    }

    /**
     * @return true if is power pellet
     */
    public boolean isPower(){
        return this.isPower;
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

    @Override
    public Image getImage() {
        return null;
    }

    public Shape getShape(){
        return food;
    }

}
