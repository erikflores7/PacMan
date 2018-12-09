package me.erikflores.pacman.Entity;

import me.erikflores.pacman.Direction;
import me.erikflores.pacman.Location;

import java.awt.*;

public abstract class Entity {

    private String name;

    public Entity(String name){
        setName(name);
    }

    public abstract Location getLocation();
    public abstract void move();

    public abstract void setDirection(Direction direction);

    public abstract Direction getDirection();

    public abstract Shape getShape();

    private void setName(String name){
        this.name = name;
    }

    public String getName(){
        return this.name;
    }

    @Override
    public String toString(){
        return this.getName();
    }


}
