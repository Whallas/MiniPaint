package main;

import Shapes.ShapeStrategy;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import utils.Point;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by whallas on 20/03/17.
 */
class MyGLEventListenerSingleton implements GLEventListener {
    private static MyGLEventListenerSingleton singleton;
    private ArrayList<ShapeStrategy> shapesAttributeList = null;
    private Point p1, p2;
    private boolean reflect = false;
    private int[] picks = new int[]{-1};

    private MyGLEventListenerSingleton() {
    }

    static MyGLEventListenerSingleton getInstance() {
        if (singleton == null)
            singleton = new MyGLEventListenerSingleton();
        return singleton;
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        final GL2 gl = drawable.getGL().getGL2();
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
        gl.glPointSize(7);

        if (shapesAttributeList != null && !shapesAttributeList.isEmpty()) {

            //for (int key : shapesAttributeList.keySet()) {
            for (ShapeStrategy shape : shapesAttributeList) {
                //Shape shape = shapesAttributeList.get(key);
                HashMap<Integer, Point> shapePoints = shape.getShapePoints();
                int key = shape.getKey();
                boolean pick = false;
                //System.out.println("key: " + key);

                gl.glMatrixMode(GL2.GL_MODELVIEW);
                gl.glLoadIdentity();
                gl.glPushMatrix();
                gl.glBegin(shape.getGl());

                if (picksContainsKey(key)) pick = true;

                //for (int i = 0; i < shapePoints.size(); i++) {
                for (int i : shapePoints.keySet()) {
                    if (!pick) {
                        gl.glColor3d(1d, 1d, 1d);
                    } else {
                        gl.glColor3d(0d, 1d, 0d);
                    }

                    Point p = shapePoints.get(i);
                    gl.glVertex2d(p.getX(), p.getY());
                }

                gl.glEnd();

                if (!reflect) gl.glPopMatrix();
                else {
                    gl.glBegin(GL2.GL_LINES);
                    gl.glColor3d(255, 255, 255);
                    gl.glVertex2d(p1.getX(), p1.getY());
                    gl.glVertex2d(p2.getX(), p2.getY());
                    gl.glEnd();

                    gl.glPopMatrix();
                }
            }
        } else {
            gl.glBegin(GL2.GL_LINES);
            gl.glVertex2d(0, 0);
            gl.glVertex2d(0, 0);
            gl.glEnd();
        }

        gl.glFlush();
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        /*final GL2 gl = drawable.getGL().getGL2();

        // Compute aspect ratio of the new window
        if (height == 0) height = 1;                // To prevent divide by 0
        double aspect = (double) width / height;

        // Set the viewport to cover the new window
        gl.glViewport(0, 0, width, height);

        // Set the aspect ratio of the clipping area to match the viewport
        gl.glMatrixMode(GL2.GL_PROJECTION);  // To operate on the Projection matrix
        gl.glLoadIdentity();             // Reset the projection matrix
        GLU glu = GLU.createGLU(gl);
        if (width >= height) {
            // aspect >= 1, set the height from -1 to 1, with larger width
            glu.gluOrtho2D(-1.0 * aspect, 1.0 * aspect, -1.0, 1.0);
            //glu.gluOrtho2D(0, 500, 500, 0);
        } else {
            // aspect < 1, set the width to -1 to 1, with larger height
            glu.gluOrtho2D(-1.0, 1.0, -1.0 / aspect, 1.0 / aspect);
        }*/
    }

    @Override
    public void init(GLAutoDrawable drawable) {
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
    }

    void setReflect(Point p1, Point p2) {
        this.p1 = p1;
        this.p2 = p2;
        setReflect(true);
    }

    void setReflect(boolean reflect) {
        this.reflect = reflect;
    }

    public void setShapes(ArrayList<ShapeStrategy> shapesAttributesList) {
        this.shapesAttributeList = shapesAttributesList;
    }

    private boolean picksContainsKey(int key) {
        if (picks.length == 1 && picks[0] == -1) return false;

        for (int pick : picks) {
            if (pick == key) return true;
        }
        return false;
    }

    void setPicks(int pick) {
        this.picks = new int[]{pick};
    }

    void setPicks(int[] picks) {
        this.picks = picks;
    }
}
