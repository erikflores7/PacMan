import java.awt.*;

public class Tile{

    private int pixels;
    private Location location;
    private Color color;
    private Rectangle tile;
    private Entity entity;
    private boolean wall = false;

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
        if(hasFood()){
            ((Food) this.entity).eat();
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

    public boolean hasFood(){
        return entity instanceof Food;
    }

    public boolean hasGhost(){
        return false;
    }

    @Override
    public String toString(){
        return "Tile: " + location.toString();
    }

}
