import java.awt.*;
import java.awt.geom.Ellipse2D;

public class PacMan extends Entity {

    private Location location;
    private Direction direction;
    private Ellipse2D circle;
    private Grid grid;
    private int size;

    public PacMan(){
        this(0, 0, 0, Direction.RIGHT, null);
    }

    public PacMan(int column, int row, int size, Direction direction, Grid grid){
        this(new Location(column, row), size, direction, grid);
    }

    public PacMan(Location location, int size, Direction direction, Grid grid){
        super("PacMan");
        setLocation(location);
        setDirection(direction);
        this.size = size;
        this.grid = grid;

        circle = new Ellipse2D.Double(getLocation().getColumn() * size, getLocation().getRow() * size, size, size);
    }

    @Override
    public Location getLocation() {
        return this.location;
    }

    @Override
    public void move() {
        Location newLocation = new Location(getLocation());
        newLocation.move(getDirection().getX(), getDirection().getY());
        Tile newTile = grid.getTileAt(newLocation.getColumn(), newLocation.getRow());

        if(newTile != null && newTile.addEntity(this)){
            grid.getTileAt(getLocation().getColumn(), getLocation().getRow()).removeEntity();
            setLocation(newLocation);
            circle.setFrame(getLocation().getColumn() * size - 5, getLocation().getRow() * size - 5, size + 10, size + 10);
        }
    }

    @Override
    public void setDirection(Direction direction) {

        // Check if this is the first time setting a direction
        if(getDirection() == null){
            this.direction = direction;
            return;
        }

        // Check if change of direction is valid
        Location newLocation = new Location(getLocation());
        newLocation.move(direction.getX(), direction.getY());
        Tile newTile = grid.getTileAt(newLocation.getColumn(), newLocation.getRow());
        if(newTile != null && !newTile.isWall()){
            this.direction = direction;
        }
    }

    private void setLocation(Location location){
        this.location = new Location(location);
    }

    @Override
    public Direction getDirection() {
        return this.direction;
    }

    @Override
    public Shape getShape() {
        return this.circle;
    }

    @Override
    public String toString(){
        return super.toString() + ": " + location.toString() + " " + direction.toString();
    }
}
