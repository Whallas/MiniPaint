package utils;

import java.util.Comparator;

/**
 * Created by whallas on 19/04/17.
 */
public class PointComparatorAdapter implements Comparator<Point> {

    private ComparadorDePontos comparadorDePontos;

    public PointComparatorAdapter(Point p0) {
        comparadorDePontos = new ComparadorDePontos(p0);
    }

    @Override
    public int compare(Point p1, Point p2) {
        return comparadorDePontos.compareDoisPontos(p1, p2);
    }

    public int orientation(Point p, Point q, Point r) {
        return comparadorDePontos.orientation(p, q, r);
    }
}
