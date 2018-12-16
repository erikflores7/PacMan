package me.erikflores.pacman;

import me.erikflores.pacman.Entity.Food;

import java.util.ArrayList;

/**
 *  Handles the Tiles in the game
 */
public class Grid {

    private PacManController controller;
    private int columns, rows, pixels;
    private ArrayList<Tile> tiles = new ArrayList<>();
    private ArrayList<Food> foods = new ArrayList<>();
    private int[][] map;
    /**
     * Creates a Grid of Tiles using the 2D array map
     *
     * @param map 2D array of information for tiles
     * @param pixels Amount of pixels per tile
     */
    public Grid(int[][] map, int pixels, PacManController controller){
        setPixels(pixels);
        createTiles(map);
        this.map = map;
        this.controller = controller;
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

    /**
     *  Creates the grid using the 2D array
     *
     * @param map 2D array with information on each tile
     */
    private void createTiles(int[][] map) {
        clear(); // Clears all tiles
        setRows(map.length);
        setColumns(map[0].length); // Sets Rows and Columns using map array

        for (int r = 0; r < getRows(); r++) {
            for (int c = 0; c < getColumns(); c++) {
                Tile tile = new Tile(new Location(c, r));
                tiles.add(tile); // Creates tile and adds it to list
                switch (map[r][c]) { // Checks the information provided by 2D array on this specific tile
                    case 1:
                        tile.setWall(true);
                        break;
                    case 0:
                        break; // NONE
                    case 2: // FOOD
                        Food food = new Food(tile, this);
                        foods.add(food);
                        tile.addEntity(food);
                        break;
                    case 3: // Food and Intersection
                        Food food2 = new Food(tile, this);
                        foods.add(food2);
                        tile.addEntity(food2);
                        tile.setIntersection(true);
                        break;
                    case 4: // Intersection
                        tile.setIntersection(true);
                        break;
                    case 5:
                        tile.setGhostDoor(true);
                        break; // Ghost door
                    case 6:
                        tile.setGhostHouse(true);
                        tile.setIntersection(true);
                        break; // Ghost house
                    case 7:
                        Food food1 = new Food(tile, this);
                        food1.setPower();
                        foods.add(food1);
                        tile.addEntity(food1);
                        tile.setIntersection(true);
                        break;
                }
            }
        }
    }
    /**
     * Creating board without 2D array, no longer used
     */
    private void createTiles(){
        clear();
        for(int r = 0; r < getRows(); r++){
            for(int c = 0; c < getColumns(); c++){
                tiles.add(new Tile(new Location(c, r)));
            }
        }
    }

    public ArrayList<Tile> getTiles(){
        return this.tiles;
    }

    /**
     *
     * @return List of Food objects on map
     */
    public ArrayList<Food> getFood(){
        return this.foods;
    }

    /**
     *  Removes Food from list, need to call PacManController to take care of scoring/level reset/etc.
     *
     * @param food Food object to remove
     *
     * @return True if there are still more food pellets to collect
     */
    public boolean removeFood(Food food){
        if(food != null){
            if(food.isPower()){
                controller.eatPower();
            }
            foods.remove(food);
            controller.eatFood();
            if(foods.size() == 0){
                controller.nextLevel();
                return false;
            }
        }
        return true;
    }

    public void collided(){
        controller.die();
    }

    /**
     *
     * Gets tile at provided column, row
     *
     * @param column Column of tile to get
     * @param row Row of tile to get
     * @return Specified tile if it exists, null if it doesn't
     */
    public Tile getTileAt(int column, int row){
        if(column >= 0 && column < getColumns() && row >= 0 && row < getRows()){
            return tiles.get((row) * (getColumns()) + column );
        }
        return null;
    }

    /**
     * Clears the tiles and food
     */
    public void clear(){
        tiles.clear();
        foods.clear();
    }

    public void restart(){
        createTiles(map);
    }


}
