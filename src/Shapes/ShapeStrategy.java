package Shapes;

import com.jogamp.opengl.GL2;
import main.MyMouseAdapterSingleton;
import utils.Point;
import utils.ShapeActions;

import java.util.HashMap;

import static java.lang.Math.*;

/**
 * Created by whallas on 03/04/17.
 */
public abstract class ShapeStrategy {

    private static int PATTERN_KEY = -1;
    HashMap<Integer, Point> shapePoints;
    MyMouseAdapterSingleton myMouseAdapterSingleton;
    Point movingPoint, center;
    int formClicksNum = 0;
    private boolean alreadyPut = false;
    private int key;
    private Point movingActionPoint, p1, p2;
    private ShapeActions shapeAction;
    private int actionClicksNum/*, hashCode*/;
    private boolean shapeIsDone;

    ShapeStrategy(MyMouseAdapterSingleton myMouseAdapterSingleton) {
        this.myMouseAdapterSingleton = myMouseAdapterSingleton;

        shapePoints = new HashMap<>();
        movingPoint = new Point();
        center = new Point();
        movingActionPoint = new Point();
        p1 = new Point();
        p2 = new Point();
        shapeAction = ShapeActions.NULL;
        shapeIsDone = false;
        key = ++PATTERN_KEY;
        clear();
    }

    public final void mouseClicked(java.awt.Point ptMouse) {
        mouseClick(ptMouse);

        if (shapeIsDone) {
            switch (shapeAction) {
                case ROTATE:
                    if (actionClicksNum == 1) {
                        shapeAction = ShapeActions.NULL;
                    }
                    break;
                case TRANSLATE:
                    if (actionClicksNum == 1) {
                        shapeAction = ShapeActions.NULL;
                    }
                    break;
                case REFLECT:
                    if (actionClicksNum == 0) {
                        myMouseAdapterSingleton.matchPoint(p1, ptMouse);
                    } else if (actionClicksNum == 1) {
                        shapeAction = ShapeActions.NULL;
                        reflect();
                    }
                    break;
            }
            if (!actionIsNull()) actionClicksNum++;
        }
    }

    public final void mouseMoved(java.awt.Point ptMouse) {
        if (formClicksNum == 1) {
            myMouseAdapterSingleton.matchPoint(movingPoint, ptMouse);
            drawTemplateMethod();
        }

        if (shapeIsDone) {
            switch (shapeAction) {
                case ROTATE:
                    if (actionClicksNum == 0) {
                        rotate(ptMouse);
                    }
                    break;
                case TRANSLATE:
                    if (actionClicksNum == 0) {
                        translate(ptMouse);
                    }
                    break;
                case REFLECT:
                    if (actionClicksNum == 1) {
                        myMouseAdapterSingleton.matchPoint(p2, ptMouse);
                        myMouseAdapterSingleton.setReflect(p1, p2);
                        myMouseAdapterSingleton.repaint();
                    }
                    break;
            }
        }
    }

    protected void rotate(java.awt.Point ptMouse) {
        myMouseAdapterSingleton.matchPoint(movingActionPoint, ptMouse);

        double delta = getAngle(movingActionPoint) - getAngle(movingPoint);
        boolean /*centerFound = false,*/ movingFound = false;

        for (int i = 0; i < shapePoints.size(); i++) {
            Point p = shapePoints.get(i);
            setRotationPoint(delta, p);

            if (p.equals(movingPoint)) movingFound = true;
        }

        if (!movingFound) setRotationPoint(delta, movingPoint);

        myMouseAdapterSingleton.repaint();
    }

    private void setRotationPoint(double delta, Point p) {
        double radius = getDistance(p);
        double angle = getAngle(p) + delta;
        double x = center.getX() + (cos(angle) * radius);
        double y = center.getY() + (sin(angle) * radius);
        p.setPoint(x, y);
    }

    protected void translate(java.awt.Point ptMouse) {
        myMouseAdapterSingleton.matchPoint(movingActionPoint, ptMouse);

        double deltaX = movingActionPoint.getX() - center.getX();
        double deltaY = movingActionPoint.getY() - center.getY();
        boolean movingFound = false;

        for (int i = 0; i < shapePoints.size(); i++) {
            Point p = shapePoints.get(i);
            double x = p.getX() + deltaX, y = p.getY() + deltaY;
            p.setPoint(x, y);

            if (p.equals(movingPoint)) movingFound = true;
        }

        center.setPoint(movingActionPoint);

        if (!movingFound) {
            double x = movingPoint.getX() + deltaX, y = movingPoint.getY() + deltaY;
            movingPoint.setPoint(x, y);
        }
        myMouseAdapterSingleton.repaint();
    }

    public void scale(double scaleX, double scaleY) {
        boolean centerFound = false, movingFound = false;

        for (int i = 0; i < shapePoints.size(); i++) {
            Point p = shapePoints.get(i);
            setEscalationPoint(scaleX, scaleY, p);

            if (p.equals(movingPoint)) movingFound = true;
            else if (p.equals(center)) centerFound = true;
        }

        if (!centerFound) setEscalationPoint(scaleX, scaleY, center);
        if (!movingFound) setEscalationPoint(scaleX, scaleY, movingPoint);

        shapeAction = ShapeActions.NULL;
        myMouseAdapterSingleton.repaint();
    }

    private void setEscalationPoint(double scaleX, double scaleY, Point p) {
        double x = p.getX() * scaleX;
        double y = p.getY() * scaleY;
        p.setPoint(x, y);
    }

    private void reflect() {
        double p1X = p1.getX(), p1Y = p1.getY();
        double deltaX = p2.getX() - p1X, deltaY = p2.getY() - p1Y;
        boolean centerFound = false, movingFound = false;

        myMouseAdapterSingleton.setReflect();
        if (deltaX == 0 || deltaY == 0) {
            myMouseAdapterSingleton.repaint();
            return;
        }

        double m = deltaY / deltaX;

        for (int i = 0; i < shapePoints.size(); i++) {
            Point p = shapePoints.get(i);
            setReflectionPoint(p1X, p1Y, m, p);

            if (p.equals(movingPoint)) movingFound = true;
            else if (p.equals(center)) centerFound = true;
        }

        if (!centerFound) setReflectionPoint(p1X, p1Y, m, center);
        if (!movingFound) setReflectionPoint(p1X, p1Y, m, movingPoint);

        myMouseAdapterSingleton.repaint();
    }

    private void setReflectionPoint(double p1X, double p1Y, double m, Point p) {
        double px = p.getX();
        double py = p.getY();
        double intersectionY = (p1Y + m * (px + (m * py) - p1X)) / (m * m + 1);
        double intersectionX = (intersectionY - p1Y + m * p1X) / m;
        double distance = hypot(intersectionX - px, intersectionY - py);
        double a = atan2(intersectionY - py, intersectionX - px);

        double x = intersectionX + (cos(a) * distance);
        double y = intersectionY + (sin(a) * distance);

        p.setPoint(x, y);
    }

    public void shear(double kx, double ky) {
        boolean centerFound = false, movingFound = false;

        for (int i = 0; i < shapePoints.size(); i++) {
            Point p = shapePoints.get(i);
            setShearPoint(kx, ky, p);

            if (p.equals(movingPoint)) movingFound = true;
            else if (p.equals(center)) centerFound = true;
        }

        if (!centerFound) setShearPoint(kx, ky, center);
        if (!movingFound) setShearPoint(kx, ky, movingPoint);

        shapeAction = ShapeActions.NULL;
        myMouseAdapterSingleton.repaint();
    }

    private void setShearPoint(double kx, double ky, Point p) {
        double x = p.getX() + kx * p.getY();
        double y = p.getY() + ky * p.getX();
        p.setPoint(x, y);
    }

    void clear() {
        setShapeAction(ShapeActions.NULL);
        formClicksNum = 0;
        setIsDone(false);
    }

    public void setShapeAction(ShapeActions shapeAction) {
        this.shapeAction = shapeAction;
        actionClicksNum = 0;
    }

    private double getAngle(Point p) {
        return atan2(p.getY() - center.getY(), p.getX() - center.getX());
    }

    private double getDistance(Point p) {
        return hypot(p.getX() - center.getX(), p.getY() - center.getY());
    }

    public HashMap<Integer, utils.Point> getShapePoints() {
        return shapePoints;
    }

    private void drawTemplateMethod() {
        drawPrimitiveOperation();

        if (alreadyPut)
            myMouseAdapterSingleton.repaint();
        else {
            alreadyPut = true;
        }
    }

    protected abstract void drawPrimitiveOperation();

    protected abstract void mouseClick(java.awt.Point ptMouse);

    public abstract boolean pick(java.awt.Point ptMouse);

    public int getGl() {
        return GL2.GL_LINE_LOOP;
    }

    public boolean isDone() {
        return shapeIsDone;
    }

    void setIsDone(boolean b) {
        shapeIsDone = b;
        shapeAction = ShapeActions.NULL;
        actionClicksNum = 0;
    }

    public int getKey() {
        return key;
    }

    public boolean actionIsNull() {
        return shapeAction == ShapeActions.NULL;
    }

    boolean defaultPick(java.awt.Point ptMouse) {
        if (shapePoints.size() == 0) return false;

        int ni = 0, fst;
        Point p = new Point();
        myMouseAdapterSingleton.matchPoint(p, ptMouse);
        double x = p.getX(), y = p.getY(), xc;

        Object[] keys = shapePoints.keySet().toArray();
        fst = (int) keys[shapePoints.size() - 1];

        for (Object key : keys) {
            int i = (int) key;
            double p1X = shapePoints.get(i).getX(), p2X = shapePoints.get(fst).getX();
            double p1Y = shapePoints.get(i).getY(), p2Y = shapePoints.get(fst).getY();

            if (!(p1Y == p2Y) && !((p1Y > y) && (p2Y > y)) && !((p1Y < y) && (p2Y < y)) && !((p1X < x) && (p2X < x))) {
                if (p1Y == y) {
                    if ((p1X > x) && (p2Y > y))
                        ni++;
                } else {
                    if (p2Y == y) {
                        if ((p2X > x) && (p1Y > y))
                            ni++;
                    } else {
                        if ((p1X > x) && (p2X > x))
                            ni++;
                        else {
                            double dx = p1X - p2X;
                            xc = p1X;
                            if (dx != 0d)
                                xc += (y - p1Y) * dx / (p1Y - p2Y);
                            if (xc > x)
                                ni++;
                        }
                    }
                }
            }
            fst = i;
        }

        return (ni % 2 == 1);
    }
}
