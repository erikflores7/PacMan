package me.erikflores.pacman;

import me.erikflores.pacman.Entity.Entity;
import me.erikflores.pacman.Entity.Food;
import me.erikflores.pacman.Entity.Ghosts.Ghost;
import me.erikflores.pacman.Entity.PacMan;

import java.awt.*;

public class Tile{

    private Location location;
    private Entity entity;
    private Food food;
    private boolean wall, intersection, ghostDoor, ghostHouse = false;

    public Tile(Location location){
        this.location = new Location(location);
    }

    public Location getLocation(){
        return this.location;
    }

    // TODO update this, most of it is no longer needed
    public boolean addEntity(Entity entity){
        if(isWall()) {
            return false;
        }else if(entity instanceof PacMan) {
            if(isGhostDoor()){
                return false;
            }
            if (hasFood()) {
                if(!food.eat()){ return false; }
                food = null;
            }
        }else if(entity instanceof Food){
            this.food = (Food) entity;
            return true;
        }

        this.entity = entity;
        return true;
    }

    public void removeEntity(){
        entity = null;
    }

    public boolean hasPacman(){
        return (entity  instanceof PacMan);
    }

    public void setWall(boolean wall){
        this.wall = wall;
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

    public void setGhostDoor(boolean door){
        this.ghostDoor = door;
    }

    public boolean isGhostDoor(){
        return this.ghostDoor;
    }

    public void setGhostHouse(boolean house){ this.ghostHouse = house; }

    public boolean isGhostHouse(){ return this.ghostHouse; }

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
