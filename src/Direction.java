public enum Direction {

    UP(0, -1), DOWN(0, 1), LEFT(-1, 0), RIGHT(1, 0);

    private int x, y;

    Direction(int x, int y){
        this.x = x;
        this.y = y;
    }

    public int getX(){
        return this.x;
    }

    public int getY(){
        return this.y;
    }

    @Override
    public String toString(){
        return "Direction: " + getClass().getName();
    }

}
