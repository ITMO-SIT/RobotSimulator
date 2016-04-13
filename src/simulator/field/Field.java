package simulator.field;

abstract public class Field {
    protected int height, width;

    public int getHeight() {return height;}
    public int getWidth() {return width;}

    public void setHeight(int height) {this.height = height;}
    public void setWidth (int width)  {this.width = width;}
    public void setSize(int width, int height) {
        this.width  = width;
        this.height = height;
    }


    public boolean contains(double X, double Y) {return 0 < X && X < width && 0 < Y && Y <  height;}
}
