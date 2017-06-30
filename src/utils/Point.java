package utils;

/**
 * Created by whallas on 22/03/17.
 */
public class Point {
    private double x, y;

    public Point(double x, double y) {
        setPoint(x, y);
    }

    public Point() {
    }

    public Point(Point p) {
        setPoint(p);
    }

    public void setPoint(Point p) {
        x = p.getX();
        y = p.getY();
    }

    public void setPoint(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}
