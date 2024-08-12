package org.xiongdwm.graphstructure.discrete;

import org.xiongdwm.graphstructure.TestGeoAbstract;
import org.xiongdwm.graphstructure.utils.geometry.GeoAbstract;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class DBSCAN {
    private final GeoAbstract geoMethod;
    private final List<DBNode> points;
    private static final int NOISE = -1;

    public DBSCAN(GeoAbstract geoMethod,List<DBNode> points) {
        this.geoMethod = geoMethod;
        this.points=points;
    }

    public List<List<DBNode>> fit(List<DBNode> points, double eps, int minPts) {
        int clusterId = 0;
        for (DBNode point : points) {
            if (!point.isWasVisited()) {
                point.setWasVisited(true);
                List<DBNode> neighbors = getNeighbors(point, points, eps);
                if (neighbors.size() >= minPts) {
                    expandCluster(point, neighbors, clusterId, points, eps, minPts);
                    clusterId++;
                } else {
                    point.setGroupId(NOISE);
                }
            }
        }
        return extractClusters(points, clusterId);
    }

    private List<DBNode> getNeighbors(DBNode point, List<DBNode> points, double eps) {
        List<DBNode> neighbors = new ArrayList<>();
        for (DBNode neighbor : points) {
            double distance=geoMethod.getDisSqrt(point.getX(),point.getY(),neighbor.getX(),neighbor.getY());
            if (distance<= eps) {
                neighbors.add(neighbor);
            }
        }
        return neighbors;
    }

    private void expandCluster(DBNode point, List<DBNode> neighbors, int clusterId, List<DBNode> points, double eps, int minPts) {
        point.setGroupId(clusterId);
        int index = 0;
        while (index < neighbors.size()) {
            DBNode neighbor = neighbors.get(index);
            if (!neighbor.isWasVisited()) {
                neighbor.setWasVisited(true);
                List<DBNode> neighborNeighbors = getNeighbors(neighbor, points, eps);
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

    private List<List<DBNode>> extractClusters(List<DBNode> points, int clusterCount) {
        List<List<DBNode>> clusters = new ArrayList<>();
        for (int i = 0; i < clusterCount; i++) {
            clusters.add(new ArrayList<>());
        }
        for (DBNode point : points) {
            if (point.getGroupId() != NOISE) {
                clusters.get(point.getGroupId()).add(point);
            }
        }
        return clusters;
    }

    public double findEpsilon(int k) { //same as minpts in dbscan
        List<Double> distances = new ArrayList<>();

        for (DBNode point : points) {
            List<Double> pointDistances = new ArrayList<>();
            for (DBNode other : points) {
                if (point != other) {
                    pointDistances.add(geoMethod.getDisSqrt(point.getX(),point.getY(),other.getX(),other.getY()));
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

    public static void main(String[] args) {
        List<DBNode> points = new ArrayList<>();
        points.add(new DBNode(30, 140));
        points.add(new DBNode(30, 142));
        points.add(new DBNode(30, 147));
        points.add(new DBNode(31, 145));
        points.add(new DBNode(28, 147));
        points.add(new DBNode(18, 122));
        points.add(new DBNode(18, 123));
        points.add(new DBNode(18, 127));
        // Add more points as needed
        GeoAbstract geoMethod=new TestGeoAbstract();
        DBSCAN dbscan = new DBSCAN(geoMethod,points);
        int minPts = 3; // Adjust based on your data
//        double eps =4.0; // Adjust based on your data
        double eps = dbscan.findEpsilon(minPts); // Adjust based on your data

        System.out.println();

        List<List<DBNode>> clusters = dbscan.fit(points, eps, minPts);

        int i=1;
        for (List<DBNode> cluster : clusters) {
            System.out.println("Cluster: "+i);
            System.out.println(cluster);
            i++;
        }
    }
}
