package Shapes;

import utils.Point;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by whallas on 19/04/17.
 */

public class PickedShapes {
    private ArrayList<ShapeStrategy> pickedShapeList = new ArrayList<>();

    public void add(ShapeStrategy shapeStrategy) {
        pickedShapeList.add(shapeStrategy);
        //System.out.println("adicionando " + shapeStrategy.getKey());
    }

    public void remove(ShapeStrategy shapeStrategy) {
        if (pickedShapeList.contains(shapeStrategy))
            pickedShapeList.remove(shapeStrategy);
    }

    public boolean containsKey(int key) {
        for (ShapeStrategy attributes : pickedShapeList) {
            if (attributes.getKey() == key) {
                //System.out.println("contem " + key);
                return true;
            }
        }

        return false;
    }

    public int[] keyList() {
        int[] keys = new int[length()];

        for (int i = 0; i < pickedShapeList.size(); i++)
            keys[i] = pickedShapeList.get(i).getKey();

        return keys;
    }

    public ArrayList<ShapeStrategy> cloneList() {
        return new ArrayList<>(pickedShapeList);
    }

    public ArrayList<Point> getAllPoints() {
        ArrayList<Point> pickedPointsList = new ArrayList<>();

        for (ShapeStrategy attributes : pickedShapeList) {
            HashMap<Integer, Point> shapePoints = attributes.getShapePoints();
            pickedPointsList.addAll(shapePoints.values());
        }

        return pickedPointsList;
    }

    public int length() {
        return pickedShapeList.size();
    }

    public void clear() {
        if (!pickedShapeList.isEmpty()) pickedShapeList.clear();
    }
}
