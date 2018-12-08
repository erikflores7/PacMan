public class Location {

    private int column, row;

    public Location(){
        this(0, 0);
    }

    public Location(Location location){
        setColumn(location.getColumn());
        setRow(location.getRow());
    }

    public Location(int column, int row){
        setColumn(column);
        setRow(row);
    }

    public void setColumn(int col){
        this.column = col;
    }

    public void setRow(int row){
        this.row = row;
    }

    public int getColumn(){
        return this.column;
    }

    public int getRow(){
        return this.row;
    }

    public void move(int x, int y){
        setColumn(getColumn() + x);
        setRow(getRow() + y);
    }

    @Override
    public String toString(){
        return "Location: (" + getColumn() + ", " + getRow() + ")";
    }


}
