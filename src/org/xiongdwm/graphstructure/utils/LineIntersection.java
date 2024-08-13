package org.xiongdwm.graphstructure.utils;

import org.xiongdwm.graphstructure.discrete.Node;

import java.util.List;

public class LineIntersection {

    public static boolean doIntersect(Node p1, Node q1, Node p2, Node q2) {
        int d1 = direction(p1, q1, p2);
        int d2 = direction(p1, q1, q2);
        int d3 = direction(p2, q2, p1);
        int d4 = direction(p2, q2, q1);
        return d1 != d2 && d3 != d4;
    }

    private static int direction(Node a, Node b, Node c) {
        double val = (b.getY().doubleValue() - a.getY().doubleValue()) * (c.getX().doubleValue() - b.getX().doubleValue())-
                (b.getX().doubleValue() - a.getX().doubleValue()) * (c.getY().doubleValue() - b.getY().doubleValue());
        if (val == 0) return 0;  // collinear
        return (val > 0) ? 1 : 2; // clockwise or counterclockwise
    }

    public static boolean doesPolylineIntersectHull(List<Node> polyline, List<Node> hull) {
        for (int i = 0; i < polyline.size() - 1; i++) {
            Node p1 = polyline.get(i);
            Node q1 = polyline.get(i + 1);

            for (int j = 0; j < hull.size(); j++) {
                Node p2 = hull.get(j);
                Node q2 = hull.get((j + 1) % hull.size());

                if (doIntersect(p1, q1, p2, q2)) {
                    return true;
                }
            }
        }
        return false;
    }
}
