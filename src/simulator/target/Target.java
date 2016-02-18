package simulator.target;

import java.awt.*;

abstract public class Target {
    protected double x, y;
    protected double width, height;
    protected Color color = new Color(250, 50, 50, 150);

    public void draw(Graphics g) {
        g.setColor(color);
        g.fillRect((int)x, (int)y, (int)width, (int)height);
    }

    public boolean contains(double X, double Y) {
        return x < X && X < x + width && y < Y && Y < y + height;
    }

    public double getCenterX() {return x + width / 2;}
    public double getCenterY() {return y + height / 2;}

    public double getX() {return x;}
    public double getY() {return y;}
    @Deprecated
    public double getSize() {return width;}

    public double getWidth() {return width;}
    public double getHeight() {return height;}

    public void setX(double x) {this.x = x;}
    public void setY(double y) {this.y = y;}
    public void setSize(double size) {this.width = this.height = size;}
    public void setSize(double width, double height) {
        this.width = width;
        this.height = height;
    }
    public void setColor(Color color) {this.color = color;}
}
