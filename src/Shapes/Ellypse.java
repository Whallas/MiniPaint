package Shapes;

import com.jogamp.opengl.GL2;
import main.MyMouseAdapterSingleton;
import utils.Point;

import static java.lang.Math.*;

/**
 * Created by whallas on 03/04/17.
 */
public class Ellypse extends ShapeStrategy {
    public Ellypse(MyMouseAdapterSingleton myMouseAdapterSingleton) {
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
            //myGLEventListener.addGlForm(GL2.GL_LINE_LOOP);
        } else if (formClicksNum == 1) {
            setIsDone(true);
        }
        formClicksNum++;
    }

    @Override
    protected void drawPrimitiveOperation() {
        double angle = (double) 2 * PI / 32;
        double c = cos(angle);//precalculate the sine and cosine
        double s = sin(angle);
        double x = 1;//we start at angle = 0
        double t, y = 0;

        for (int i = 0; i < 32; i++) {
            double X = x * movingPoint.getX() + center.getX();
            double Y = y * movingPoint.getY() + center.getY();
            shapePoints.put(i, new Point(X, Y));

            t = x;
            x = c * x - s * y;
            y = s * t + c * y;
        }
    }

    @Override
    public int getGl() {
        return GL2.GL_LINE_LOOP;
    }
}
