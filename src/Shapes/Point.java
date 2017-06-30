package Shapes;

import com.jogamp.opengl.GL2;
import main.MyMouseAdapterSingleton;

/**
 * Created by whallas on 03/04/17.
 */
public class Point extends ShapeStrategy {

    public Point(MyMouseAdapterSingleton myMouseAdapterSingleton) {
        super(myMouseAdapterSingleton);
    }

    @Override
    protected void mouseClick(java.awt.Point ptMouse) {
        formClicksNum++;
        if (formClicksNum == 2) {
            setIsDone(true);
        }
    }

    @Override
    protected void drawPrimitiveOperation() {
        shapePoints.put(0, movingPoint);
    }

    @Override
    public int getGl() {
        return GL2.GL_POINTS;
    }

    @Override
    public boolean pick(java.awt.Point ptMouse) {
        utils.Point p = new utils.Point();
        myMouseAdapterSingleton.matchPoint(p, ptMouse);

        double x = movingPoint.getX(), y = movingPoint.getY(), t = 0.02d;
        double xMax = p.getX() + t, xMin = p.getX() - t;
        double yMax = p.getY() + t, yMin = p.getY() - t;

        return (x > xMin && x < xMax && y > yMin && y < yMax);
    }

    @Override
    protected void rotate(java.awt.Point ptMouse) {
    }

    @Override
    protected void translate(java.awt.Point ptMouse) {
        myMouseAdapterSingleton.matchPoint(movingPoint, ptMouse);
        myMouseAdapterSingleton.repaint();
    }
}
