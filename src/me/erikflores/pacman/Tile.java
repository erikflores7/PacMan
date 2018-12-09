package me.erikflores.pacman;

import me.erikflores.pacman.Entity.Entity;
import me.erikflores.pacman.Entity.Food;
import me.erikflores.pacman.Entity.Ghosts.Ghost;
import me.erikflores.pacman.Entity.PacMan;

import java.awt.*;

public class Tile{

    private int pixels;
    private Location location;
    private Color color;
    private Rectangle tile;
    private Entity entity;
    private Food food;
    private boolean wall, intersection = false;

    public Tile(int pixels, Location location){
        this.pixels = pixels;
        this.location = new Location(location);

        color = Color.BLACK;

        tile = new Rectangle(location.getColumn() * pixels, location.getRow() * pixels, pixels, pixels);
    }

    public Shape getShape(){
        return tile;
    }

    public Color getColor(){
        return this.color;
    }

    public Location getLocation(){
        return this.location;
    }

    public boolean addEntity(Entity entity){
        if(isWall()) {
            return false;
        }
        if(entity instanceof PacMan) {
            if (hasFood()) {
                (food).eat();
                food = null;
            }
        }

        if(entity instanceof Food){
            this.food = (Food) entity;
        }

        this.entity = entity;
        return true;
    }

    public void removeEntity(){
        entity = null;
    }

    public boolean hasEntity(){
        return (entity  != null);
    }

    public void setWall(boolean wall){
        this.wall = wall;
        if(isWall()){
            color = Color.BLUE;
        }
    }

    public boolean isWall(){
        return this.wall;
    }

    public void setIntersection(boolean intersection){
        this.intersection = intersection;
    }

    public boolean isIntersection() {
        return this.intersection;
    }
    public boolean hasFood(){
        return (food != null);
    }

    public boolean hasGhost(){
        return entity instanceof Ghost;
    }

    @Override
    public String toString(){
        return "Tile: " + location.toString();
    }

}
