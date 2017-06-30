package main;

import Shapes.*;

/**
 * Created by whallas on 19/06/17.
 */
class FactoryShape {

    ShapeStrategy getShape(int item) {
        MyMouseAdapterSingleton m = MyMouseAdapterSingleton.getInstance();
        ShapeStrategy shapeStrategy;

        switch (item) {
            case 1:
                shapeStrategy = new Point(m);
                break;
            case 2:
                shapeStrategy = new Line(m);
                break;
            case 3:
                shapeStrategy = new Polygon(m);
                break;
            case 4:
                shapeStrategy = new Ellypse(m);
                break;
            default:
                shapeStrategy = null;
        }

        return shapeStrategy;
    }
}
