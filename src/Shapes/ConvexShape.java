package Shapes;

import main.MyMouseAdapterSingleton;
import utils.Point;

import java.util.Stack;

/**
 * Created by whallas on 20/04/17.
 */
public class ConvexShape extends Polygon {

    public ConvexShape(MyMouseAdapterSingleton myMouseAdapterSingleton, Stack<utils.Point> s) {
        super(myMouseAdapterSingleton);
        setShapePoints(s);
    }

    private void setShapePoints(Stack<utils.Point> s) {
        setNumSizes(s.size());
        int i = 0;

        while (!s.isEmpty()) {
            shapePoints.put(i, s.pop());
            i++;
        }

        setIsDone(true);
        formClicksNum = 3;
        setCenter();
        //myMouseAdapterSingleton.putShapeToList(getKey(), this);
    }

    private double getArea() {
        Object[] points = shapePoints.values().toArray();
        double area = 0;

        for (int i = 0; i < points.length - 1; i++) {
            Point p1 = (Point) points[i];
            Point p2 = (Point) points[i + 1];

            area += p1.getX() * p2.getY() - p2.getX() * p1.getY();
        }

        return area / 2;
    }

    private void setCenter() {
        Object[] points = shapePoints.values().toArray();
        double cx = 0, cy = 0;

        for (int i = 0; i < points.length - 1; i++) {
            Point p1 = (Point) points[i];
            Point p2 = (Point) points[i + 1];
            double c = p1.getX() * p2.getY() - p2.getX() * p1.getY();

            cx += (p1.getX() + p2.getX()) * c;
            cy += (p1.getY() + p2.getY()) * c;
        }

        double a = getArea() * 6;
        cx /= a;
        cy /= a;

        center.setPoint(cx, cy);
    }

    @Override
    protected void drawPrimitiveOperation() {
    }
}
