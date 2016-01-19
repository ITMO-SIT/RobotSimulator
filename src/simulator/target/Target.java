package simulator.target;

import java.awt.*;

abstract public class Target {
    protected double x, y;
    protected double size;

    public void draw(Graphics g) {
        g.setColor(new Color(250, 50, 50, 150));
        g.fillRect((int)x, (int)y, (int)size, (int)size);
    }

    public boolean contains(double X, double Y) {
        return x < X && X < x + size && y < Y && Y < y + size;
    }

    public double getCenterX() {return x + size / 2;}
    public double getCenterY() {return y + size / 2;}

    public double getX() {return x;}
    public double getY() {return y;}
    public double getSize() {return size;}

    public void setX(double x) {this.x = x;}
    public void setY(double y) {this.y = y;}
    public void setSize(double size) {this.size = size;}
}
