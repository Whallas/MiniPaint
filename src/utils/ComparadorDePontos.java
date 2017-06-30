package utils;

/**
 * Created by whallas on 19/06/17.
 */
final class ComparadorDePontos {
    private Point p0;

    ComparadorDePontos(Point p0) {
        this.p0 = p0;
    }

    int compareDoisPontos(Point p1, Point p2) {
        // Find orientation
        int o = orientation(p0, p1, p2);
        if (o == 0)
            return (dist(p0, p2) >= dist(p0, p1)) ? -1 : 1;

        return (o == 2) ? -1 : 1;
    }

    int orientation(Point p, Point q, Point r) {
        double val = (q.getY() - p.getY()) * (r.getX() - q.getX()) - (q.getX() - p.getX()) * (r.getY() - q.getY());

        if (val == 0d)
            return 0; // colinear
        return (val > 0) ? 1 : 2; // clock or counterclock wise
    }

    // A utility function to return square of distance between p1 and p2
    private double dist(Point p1, Point p2) {
        return (p1.getX() - p2.getX()) * (p1.getX() - p2.getX()) + (p1.getY() - p2.getY()) * (p1.getY() - p2.getY());
    }
}
