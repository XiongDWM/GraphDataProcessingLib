package org.xiongdwm.graphstructure.discrete;

import org.xiongdwm.graphstructure.TestGeoAbstract;
import org.xiongdwm.graphstructure.exception.PolyShapeBuildException;
import org.xiongdwm.graphstructure.utils.LineIntersection;
import org.xiongdwm.graphstructure.utils.geometry.GeoAbstract;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class DBSCAN {
    private final List<Node> points;
    private static final int NOISE = -1;

    public DBSCAN(List<Node> points) {
        this.points=points;
    }

    public List<List<Node>> fit(double eps, int minPts,boolean convexHull) {
        int clusterId = 0;
        for (Node point : points) {
            if (!point.isWasVisited()) {
                point.setWasVisited(true);
                List<Node> neighbors = getNeighbors(point, points, eps);
                if (neighbors.size() >= minPts) {
                    expandCluster(point, neighbors, clusterId, points, eps, minPts);
                    clusterId++;
                } else {
                    point.setGroupId(NOISE);
                }
            }
        }
        List<List<Node>> clusters=extractClusters();
        if(convexHull)return extractOutline(clusters);
        return clusters;
    }

    private List<Node> getNeighbors(Node point, List<Node> points, double eps) {
        List<Node> neighbors = new ArrayList<>();
        for (Node neighbor : points) {
            double distance=getDisSqrt(point.getX(),point.getY(),neighbor.getX(),neighbor.getY());
            if (distance<= eps) {
                neighbors.add(neighbor);
            }
        }
        return neighbors;
    }
    private double getDisSqrt(Number x1,Number y1,Number x2,Number y2){
        return Math.sqrt(Math.pow(x1.doubleValue() - x2.doubleValue(), 2) + Math.pow(y1.doubleValue() - y2.doubleValue(), 2));

    }

    private void expandCluster(Node point, List<Node> neighbors, int clusterId, List<Node> points, double eps, int minPts) {
        point.setGroupId(clusterId);
        int index = 0;
        while (index < neighbors.size()) {
            Node neighbor = neighbors.get(index);
            if (!neighbor.isWasVisited()) {
                neighbor.setWasVisited(true);
                List<Node> neighborNeighbors = getNeighbors(neighbor, points, eps);
                if (neighborNeighbors.size() >= minPts) {
                    neighbors.addAll(neighborNeighbors);
                }
            }
            if (neighbor.getGroupId() == NOISE) {
                neighbor.setGroupId(clusterId);
            }
            index++;
        }
    }

    private List<List<Node>> extractClusters() {
        return new ArrayList<>(points.stream().collect(Collectors.groupingBy(Node::getGroupId)).values());
    }

    private List<List<Node>> extractOutline(List<List<Node>> clusters){
        List<List<Node>> result=new ArrayList<>();
        for(List<Node>c:clusters){
            try {
                List<Node>shape=grahamScan(c);
                shape.add(shape.get(0));
                result.add(shape);
            }catch (Exception e){
                System.out.println(e.getLocalizedMessage());
            }
        }
        return result;
    }

    public double findEpsilon(int k) {  // generally, k's value is equal to minPts's value
        List<Double> distances = new ArrayList<>();

        for (Node point : points) {
            List<Double> pointDistances = new ArrayList<>();
            for (Node other : points) {
                if (point != other) {
                    pointDistances.add(getDisSqrt(point.getX(),point.getY(),other.getX(),other.getY()));
                }
            }
            Collections.sort(pointDistances);
            distances.add(pointDistances.get(k - 1));
        }

        distances.sort(Comparator.reverseOrder());

        double maxDiff = 0;
        int elbowIndex = 0;
        for (int i = 1; i < distances.size(); i++) {
            double diff = distances.get(i - 1) - distances.get(i);
            if (diff > maxDiff) {
                maxDiff = diff;
                elbowIndex = i;
            }
        }
        return distances.get(elbowIndex);
    }

    //getConvexhull
    public List<Node> grahamScan(List<Node> nodes) throws Exception {
        // points less than 3 cannot combine as a shape;
        if (nodes.size() < 3) throw new PolyShapeBuildException("points less than 3 cannot combine as a shape");

        nodes.sort(Comparator.comparing(it->it.getX().doubleValue()));

        // Check if all points are collinear
        if (areCollinear(nodes)) throw new PolyShapeBuildException("points are collinear");

        // lower hull
        List<Node> lower = new ArrayList<>();
        for (Node p : nodes) {
            while (lower.size() >= 2 && cross(lower.get(lower.size() - 2), lower.get(lower.size() - 1), p) <= 0) {
                lower.remove(lower.size() - 1);
            }
            lower.add(p);
        }

        // upper hull
        List<Node> upper = new ArrayList<>();
        for (int i = nodes.size() - 1; i >= 0; i--) {
            Node p = nodes.get(i);
            while (upper.size() >= 2 && cross(upper.get(upper.size() - 2), upper.get(upper.size() - 1), p) <= 0) {
                upper.remove(upper.size() - 1);
            }
            upper.add(p);
        }
        lower.remove(lower.size() - 1);
        upper.remove(upper.size() - 1);

        // make the full hull
        lower.addAll(upper);
        return lower;
    }

    private double cross(Node o, Node a, Node b) {
        return (a.getX().doubleValue() - o.getX().doubleValue()) * (b.getY().doubleValue()
                - o.getY().doubleValue()) - (a.getY().doubleValue() - o.getY().doubleValue()) * (b.getX().doubleValue() - o.getX().doubleValue());
    }

    private boolean areCollinear(List<Node> nodes) {
        Node p0 = nodes.get(0);
        Node p1 = nodes.get(1);
        for (int i = 2; i < nodes.size(); i++) {
            if (cross(p0, p1, nodes.get(i)) != 0) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) throws Exception {
        List<Node> points = new ArrayList<>();
        points.add(new Node(30, 140));
        points.add(new Node(30, 142));
        points.add(new Node(30, 147));
        points.add(new Node(31,150));
        points.add(new Node(32, 142));
        points.add(new Node(28, 147));
        points.add(new Node(18, 122));
        points.add(new Node(18, 123));
        points.add(new Node(18, 127));
        points.add(new Node(20, 123));
        points.add(new Node(20, 127));
        points.add(new Node(80, 160));
        points.add(new Node(80, 157));
        points.add(new Node(83, 153));
        points.add(new Node(88, 163));
        points.add(new Node(86, 155));
        DBSCAN dbscan = new DBSCAN(points);
        int minPts = 3;
        double eps = dbscan.findEpsilon(minPts);
        List<Node>polyLine=new ArrayList<>();
        polyLine.add(new Node(10, 70));
        polyLine.add(new Node(29, 141));
        polyLine.add(new Node(31, 151));
//        polyLine.add(new Node(60, 157));
        List<List<Node>> clusters = dbscan.fit(eps, minPts,true);
        int i=1;
        List<Node>hull=clusters.get(0);
        boolean isCross=LineIntersection.doesPolylineIntersectHull(polyLine,hull);
        System.out.println(isCross);
        for (List<Node> cluster : clusters) {
            System.out.println("Cluster: "+i);
            System.out.println(cluster);
            i++;
        }
    }
}
