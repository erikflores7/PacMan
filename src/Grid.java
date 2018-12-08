import java.awt.*;
import java.util.ArrayList;

public class Grid {

    private int columns, rows, pixels;
    private ArrayList<Tile> tiles = new ArrayList<>();
    private ArrayList<Food> foods = new ArrayList<>();

    public Grid(int[][] map, int pixels){
        setPixels(pixels);

        createTiles(map);
    }

    public Grid(int columns, int rows, int pixels){
        setColumns(columns);
        setRows(rows);
        setPixels(pixels);

        createTiles();
    }

    private void setColumns(int columns){
        this.columns = columns;
    }

    private void setRows(int rows){
        this.rows = rows;
    }

    private void setPixels(int pixels){
        this.pixels = pixels;
    }

    public int getColumns(){
        return this.columns;
    }

    public int getRows(){
        return this.rows;
    }

    private void createTiles(int[][] map){
        clear();
        setRows(map.length);
        setColumns(map[0].length);

        for(int r = 0; r < getRows(); r++){
            for(int c = 0; c < getColumns(); c++){
                Tile tile = new Tile(pixels, new Location(c, r));
                tiles.add(tile);
                switch(map[r][c]){
                    case 1: tile.setWall(true); break;
                    case 0: break;
                    case 2:
                        Food food = new Food(tile, this);
                        foods.add(food);
                        tile.addEntity(food);
                        break;
                    case 3: /*SPAWN*/ break;
                    case 4: /*SPECIAL*/ break;
                }
            }
        }
    }

    private void createTiles(){
        clear();
        for(int r = 0; r < getRows(); r++){
            for(int c = 0; c < getColumns(); c++){
                tiles.add(new Tile(pixels, new Location(c, r)));
            }
        }
    }

    public ArrayList<Tile> getTiles(){
        return this.tiles;
    }

    public ArrayList<Food> getFood(){
        return this.foods;
    }

    public void removeFood(Food food){
        if(food != null){
            foods.remove(food);
        }
    }

    public Tile getTileAt(int column, int row){
        if(column >= 0 && column < getColumns() && row >= 0 && row < getRows()){
            return tiles.get((row) * (getColumns()) + column );
        }
        return null;
    }

    public void clear(){
        tiles.clear();
    }


}
