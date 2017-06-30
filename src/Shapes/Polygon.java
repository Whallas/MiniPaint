package Shapes;

import main.MyMouseAdapterSingleton;
import utils.Point;

import static java.lang.Math.*;

/**
 * Created by whallas on 03/04/17.
 */
public class Polygon extends ShapeStrategy {

    private int numSizes = 5;

    public Polygon(MyMouseAdapterSingleton myMouseAdapterSingleton) {
        super(myMouseAdapterSingleton);
    }

    @Override
    public boolean pick(java.awt.Point ptMouse) {
        return defaultPick(ptMouse);
    }

    @Override
    public void mouseClick(java.awt.Point ptMouse) {
        if (formClicksNum == 0) {
            myMouseAdapterSingleton.matchPoint(center, ptMouse);
        } else if (formClicksNum == 1) {
            setIsDone(true);
        }
        formClicksNum++;
    }

    @Override
    protected void drawPrimitiveOperation() {
        double a = (double) 2 * PI / numSizes;
        double deltaX = movingPoint.getX() - center.getX();
        double deltaY = movingPoint.getY() - center.getY();
        double mvAngle = atan2(deltaY, deltaX);
        double radius = hypot(deltaX, deltaY);
        double X, Y;

        shapePoints.put(0, movingPoint);

        for (int i = 1; i < numSizes; i++) {
            double angle = i * a + mvAngle;
            X = center.getX() + (cos(angle) * radius);
            Y = center.getY() + (sin(angle) * radius);

            shapePoints.put(i, new Point(X, Y));
        }
    }

    public void setNumSizes(int numSizes) {
        this.numSizes = numSizes;
        clear();
    }
}
