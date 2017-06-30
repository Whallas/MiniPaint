package Shapes;

import main.MyMouseAdapterSingleton;

import java.awt.Point;

/**
 * Created by whallas on 03/04/17.
 */
public class Line extends ShapeStrategy {

    public Line(MyMouseAdapterSingleton myMouseAdapterSingleton) {
        super(myMouseAdapterSingleton);
    }

    @Override
    protected void mouseClick(java.awt.Point ptMouse) {
        if (formClicksNum == 0) {
            myMouseAdapterSingleton.matchPoint(center, ptMouse);
            shapePoints.put(0, center);
        } else if (formClicksNum == 1) {
            setIsDone(true);
        }
        formClicksNum++;
    }

    @Override
    protected void drawPrimitiveOperation() {
        shapePoints.put(1, movingPoint);
    }

    @Override
    public boolean pick(Point ptMouse) {
        utils.Point p = new utils.Point();
        myMouseAdapterSingleton.matchPoint(p, ptMouse);

        CohenSutherland cohenSutherland = new CohenSutherland(p);
        return cohenSutherland.clip();
    }

    private class CohenSutherland {

        private static final int INSIDE = 0;
        private static final int LEFT = 1;
        private static final int RIGHT = 2;
        private static final int BOTTOM = 4;
        private static final int TOP = 8;
        private double xMin, xMax, yMin, yMax;

        CohenSutherland(utils.Point p) {
            double delta = 0.02d;
            xMax = p.getX() + delta;
            xMin = p.getX() - delta;
            yMax = p.getY() + delta;
            yMin = p.getY() - delta;
        }

        /**
         * Computes OutCode for given point (x,y)
         *
         * @param x Horizontal coordinate
         * @param y Vertical coordinate
         * @return Computed OutCode
         */
        private int computeOutCode(double x, double y) {
            int code = INSIDE;

            if (x < xMin) {
                code |= LEFT;
            } else if (x > xMax) {
                code |= RIGHT;
            }
            if (y < yMin) {
                code |= BOTTOM;
            } else if (y > yMax) {
                code |= TOP;
            }

            return code;
        }

        /**
         * Execute line clipping using Cohen-Sutherland
         * Taken from: http://en.wikipedia.org/wiki/Cohen-Sutherland
         *
         * @return Clipped line
         */
        boolean clip() {
            double x0 = center.getX(), x1 = movingPoint.getX();
            double y0 = center.getY(), y1 = movingPoint.getY();
            int outCode0 = computeOutCode(x0, y0);
            int outCode1 = computeOutCode(x1, y1);
            boolean accept = false;

            while (true) {
                if ((outCode0 | outCode1) == 0) { // Bitwise OR is 0. Trivially accept
                    accept = true;
                    break;
                } else if ((outCode0 & outCode1) != 0) { // Bitwise AND is not 0. Trivially reject
                    break;
                } else {
                    double x, y;
                    // Pick at least one point outside rectangle
                    int outCodeOut = (outCode0 != 0) ? outCode0 : outCode1;

                    // Now find the intersection point;
                    // use formulas y = y0 + slope * (x - x0), x = x0 + (1 / slope) * (y - y0)
                    if ((outCodeOut & TOP) != 0) {
                        x = x0 + (x1 - x0) * (yMax - y0) / (y1 - y0);
                        y = yMax;
                    } else if ((outCodeOut & BOTTOM) != 0) {
                        x = x0 + (x1 - x0) * (yMin - y0) / (y1 - y0);
                        y = yMin;
                    } else if ((outCodeOut & RIGHT) != 0) {
                        y = y0 + (y1 - y0) * (xMax - x0) / (x1 - x0);
                        x = xMax;
                    } else {
                        y = y0 + (y1 - y0) * (xMin - x0) / (x1 - x0);
                        x = xMin;
                    }

                    // Now we move outside point to intersection point to clip
                    if (outCodeOut == outCode0) {
                        x0 = x;
                        y0 = y;
                        outCode0 = computeOutCode(x0, y0);
                    } else {
                        x1 = x;
                        y1 = y;
                        outCode1 = computeOutCode(x1, y1);
                    }
                }
            }

            return accept;
        }
    }
}
