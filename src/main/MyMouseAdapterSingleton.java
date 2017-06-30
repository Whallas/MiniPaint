package main;

import Shapes.ConvexShape;
import Shapes.PickedShapes;
import Shapes.Polygon;
import Shapes.ShapeStrategy;
import com.jogamp.opengl.awt.GLCanvas;
import utils.Point;
import utils.PointComparatorAdapter;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Stack;

import static utils.ShapeActions.*;

/**
 * Created by whallas on 18/04/17.
 */
public class MyMouseAdapterSingleton extends MouseAdapter {

    private static MyMouseAdapterSingleton singleton;
    private ArrayList<ShapeStrategy> shapes = new ArrayList<>();
    private PickedShapes pickedShapes = new PickedShapes();
    private GLCanvas glCanvas;
    private MyGLEventListenerSingleton myGLEventListenerSingleton;
    private JPanel jpOptions;
    private ShapeStrategy shapeStrategy;
    private int clicks = 0;
    private boolean actionIsConvexHull = false;

    private MyMouseAdapterSingleton(GLCanvas glCanvas, MyGLEventListenerSingleton myGLEventListenerSingleton,
                                    JPanel jpOptions) {
        this.glCanvas = glCanvas;
        this.myGLEventListenerSingleton = myGLEventListenerSingleton;
        this.jpOptions = jpOptions;
    }

    static MyMouseAdapterSingleton getInstance(GLCanvas glCanvas, MyGLEventListenerSingleton myGLEventListenerSingleton,
                                               JPanel jpOptions) {
        if (singleton == null)
            singleton = new MyMouseAdapterSingleton(glCanvas, myGLEventListenerSingleton, jpOptions);
        return singleton;
    }

    static MyMouseAdapterSingleton getInstance() {
        return singleton;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (shapeStrategy != null) {
            shapeStrategy.mouseClicked(e.getPoint());

            if (!actionIsConvexHull && shapeStrategy.actionIsNull() && shapeStrategy.isDone()) {
                if (clicks > 1) {
                    pick(e.getPoint());
                } else if (clicks == 1) {
                    jpOptions.setVisible(true);
                    reInit(shapeStrategy.getKey());
                }
            } else {
                /*System.out.printf("!actionIsConvexHull:%s\tshapeStrategy.actionIsNull():%s\t" +
                                "shapeStrategy.isDone():%s\n\n", !actionIsConvexHull, shapeStrategy.actionIsNull(),
                        shapeStrategy.isDone());*/
                pickShapes(e.getPoint());
            }

            clicks++;
        } else pick(e.getPoint());
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (shapeStrategy != null) {
            shapeStrategy.mouseMoved(e.getPoint());

            if (!shapeStrategy.isDone() && shapeStrategy.actionIsNull() && clicks > 0) {
                reInit(shapeStrategy.getKey());
            }
        }
    }

    private void pickShapes(java.awt.Point ptMouse) {
        //for (int key : shapes.keySet()) {
        for (ShapeStrategy shapeStrategy : shapes) {
            //ShapeStrategy shapeStrategy = shapes.get(key);
            int key = shapeStrategy.getKey();

            if (shapeStrategy.pick(ptMouse)) {
                if (!pickedShapes.containsKey(key))
                    pickedShapes.add(shapeStrategy);
                else
                    pickedShapes.remove(shapeStrategy);

                reInit(pickedShapes.keyList());
                break;
            }
        }
    }

    private void pick(java.awt.Point ptMouse) {
        boolean picked = false;

        //for (int key : shapes.keySet()) {
        for (ShapeStrategy shapeStrategy : shapes) {
            //ShapeStrategy shapeStrategy = shapes.get(key);
            int key = shapeStrategy.getKey();

            if (shapeStrategy.pick(ptMouse)) {
                picked = true;
                this.shapeStrategy = shapeStrategy;
                jpOptions.setVisible(true);
                reInit(key);
                break;
            }
        }

        if (picked) {
            System.out.println("pick!");
        } else {
            reInit(-1);
            jpOptions.setVisible(false);
            System.out.println("nenhum shape selecionado!");
        }
    }

    // Prints convex hull of a set of n points.
    void convexHullFacade() {
        if (pickedShapes.length() < 2) return;

        //aqui eu pego os pontos dos shapes que quero transformar no feixo convexo
        ArrayList<Point> pickedPoints = pickedShapes.getAllPoints();
        if (pickedPoints.size() < 3) return;

        //deleto os shapes que não vou mais precisar
        for (ShapeStrategy ss : pickedShapes.cloneList()) {
            //pickedShapes.remove(ss);
            shapes.remove(ss);
        }
        pickedShapes.clear();
        toShapeAdapterList();

        // Find the bottommost point
        Point po = pickedPoints.get(0);
        double ymin = po.getY();
        int min = 0;

        for (int i = 1; i < pickedPoints.size(); i++) {
            Point pi = pickedPoints.get(i);
            Point pMin = pickedPoints.get(min);
            double y = pi.getY();

            // Pick the bottom-most or chose the left most point in case of tie
            if ((y < ymin) || (ymin == y && pi.getX() < pMin.getX())) {
                ymin = pi.getY();
                min = i;
            }
        }

        // Place the bottom-most point at first position
        swap(po, pickedPoints.get(min));

        // Sort n-1 points with respect to the first point.  A point p1 comes
        // before p2 in sorted ouput if p2 has larger polar angle (in
        // counterclockwise direction) than p1

        //aqui faço uma ordenação
        PointComparatorAdapter pointComparator = new PointComparatorAdapter(po);
        pickedPoints.remove(po);
        pickedPoints.sort(pointComparator);

        // Create an empty stack and push first three points to it.
        Stack<Point> s = new Stack<>();
        s.push(po);
        s.push(pickedPoints.get(0));
        s.push(pickedPoints.get(1));

        // Process remaining n-3 points
        for (int i = 2; i < pickedPoints.size(); i++) {
            // Keep removing top while the angle formed by points next-to-top,
            // top, and points[i] makes a non-left turn
            while (pointComparator.orientation(nextToTop(s), s.peek(), pickedPoints.get(i)) != 2)
                s.pop();

            s.push(pickedPoints.get(i));
        }

        actionIsConvexHull = false;

        //aqui crio um novo shapeStrategy pra pôr na tela, os antigos ja foram apagados
        shapeStrategy = new ConvexShape(this, s);
        putShapeToList(shapeStrategy);
    }

    // A utility function to find next to top in a stack
    private Point nextToTop(Stack<Point> s) {
        Point p = s.pop();
        Point res = s.peek();
        s.push(p);

        return res;
    }

    // A utility function to swap two points
    private void swap(utils.Point p1, utils.Point p2) {
        utils.Point p_aux = p1;
        p1 = p2;
        p2 = p_aux;
    }

    void setPolygonNumSides(int numSides) {
        if (shapeStrategy instanceof Polygon)
            ((Polygon) shapeStrategy).setNumSizes(numSides);
    }

    void shearShape(double kx, double ky) {
        if (shapeStrategy != null)
            shapeStrategy.shear(kx, ky);
    }

    void scaleShape(double x, double y) {
        if (shapeStrategy != null)
            shapeStrategy.scale(x, y);
    }

    //public void putShapeToList(int SHAPE_ID, ShapeStrategy shapeStrategy) {
    private void putShapeToList(ShapeStrategy shapeStrategy) {
        //shapes.put(SHAPE_ID, shapeStrategy);
        shapes.add(shapeStrategy);
        reInit(shapeStrategy.getKey());
    }

    public void repaint() {
        glCanvas.repaint();
    }

    void setShapeAction(int item) {
        pickedShapes.clear();

        if (shapeStrategy != null) {
            switch (item) {
                case 1:
                    shapeStrategy.setShapeAction(ROTATE);
                    break;
                case 2:
                    shapeStrategy.setShapeAction(TRANSLATE);
                    break;
                case 5:
                    shapeStrategy.setShapeAction(REFLECT);
                    break;
                case 6:
                    actionIsConvexHull = true;
                    clicks = 0;
                    reInit(-1);
                    break;
                default:
                    shapeStrategy.setShapeAction(NULL);
            }

            if (item != 6) actionIsConvexHull = false;
        }
    }

    private void reInit(int key) {
        myGLEventListenerSingleton.setPicks(key);
        if (key != -1)
            toShapeAdapterList();
        repaint();
    }

    private void reInit(int[] keys) {
        myGLEventListenerSingleton.setPicks(keys);
        toShapeAdapterList();
        repaint();
    }

    private void toShapeAdapterList() {
        myGLEventListenerSingleton.setShapes(shapes);
    }

    private void removeShape(ShapeStrategy shapeStrategy) {
        pickedShapes.remove(shapeStrategy);
        shapes.remove(shapeStrategy);
        toShapeAdapterList();
    }

    void changeCurrentShape(int item) {
        pickedShapes.clear();
        reInit(-1);
        clicks = 0;

        if (shapeStrategy != null && !shapeStrategy.isDone()) {
            removeShape(shapeStrategy);
            repaint();
        }

        FactoryShape factoryShape = new FactoryShape();
        shapeStrategy = factoryShape.getShape(item);

        if (shapeStrategy != null) {
            putShapeToList(shapeStrategy);
            //System.out.println("shapeStrategy adicionada");
        }
    }

    public void matchPoint(Point p, java.awt.Point ptMouse) {
        //para x e y positivos
        double newX = (2 * ptMouse.getX()) / glCanvas.getWidth() - 1;
        double newY = (2 * ptMouse.getY()) / glCanvas.getHeight();

        //transforma no Y correto para a interface
        if (newY < 1) newY = 1 - newY;
        else newY = (newY - 1) * -1;

        p.setPoint(newX, newY);
    }

    public void setReflect(Point p1, Point p2) {
        myGLEventListenerSingleton.setReflect(p1, p2);
    }

    public void setReflect() {
        myGLEventListenerSingleton.setReflect(false);
    }
}
