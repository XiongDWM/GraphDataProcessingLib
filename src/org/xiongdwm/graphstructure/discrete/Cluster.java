package org.xiongdwm.graphstructure.discrete;

import jdk.nashorn.internal.ir.annotations.Ignore;
import org.xiongdwm.graphstructure.exception.WrongMemberException;
import org.xiongdwm.graphstructure.utils.geometry.GeoAbstract;

import java.lang.reflect.Array;
import java.util.*;

public class Cluster {
    private final GeoAbstract geoMethods;
    private static Node<?>[] nodeArray = new Node[0];
    private static int k_num = 0;
    private final int nodesCount;
    private final static Class<?> clazz = Node.class;
    private final boolean[] unChange;

    public Cluster(GeoAbstract geoMethods, Node<?>[] nodes) {
        nodeArray = nodes;
        this.geoMethods = geoMethods;
        this.nodesCount = nodes.length;
        k_num = nodesCount / 3;

        unChange = new boolean[nodesCount];
        for (int i = 0; i < k_num; i++) {
            unChange[i] = false;
        }

    }

    //random 可能有重复的
    public Node<?>[] randomPoints(int amount) {
        if (0 >= amount) throw new WrongMemberException("amount is a positive integer");
        Class<?> clazz = Node.class;
        Node<?>[] result = (Node<?>[]) Array.newInstance(clazz, amount);
        Random random = new Random();
        int p = 0;
        while (amount > 0) {
            int rn = random.nextInt(this.nodesCount); //number in [0,range)
            if (unChange[rn]) continue;
            result[p] = nodeArray[rn];
            unChange[rn] = true;
            amount--;
            p++;
        }
        return result;
    }

    @Ignore
    public void t() {
        Node<?> o = geoMethods.getEquilibriumPoint(1, 1, 3, 4);
        System.out.println(o);
    }

    public Object runClustering() {
        Node<?>[] k = randomPoints(k_num);
        Node<?>[] bk = (Node<?>[]) Array.newInstance(clazz, k_num);
        System.arraycopy(k, 0, bk, 0, k_num);
        Hashtable<Integer, List<Node<?>>> c = new Hashtable<>();
        return null;
    }

    @SuppressWarnings("unchecked")
    public Hashtable<Node<?>, List<Node<?>>> clustering() {
        Node<?>[] k = randomPoints(k_num); //numbers of cluster
        boolean flag= false; //感知器
        List<Node<?>>[] rc;
        List<Node<?>> lst = new ArrayList<>();
        Node<?> center;

        rc = (List<Node<?>>[]) Array.newInstance(List.class, k_num);
        int located;
        for (int i = 0; i < nodesCount; i++) {
            Number x1 = nodeArray[i].getX();
            Number y1 = nodeArray[i].getY();
            double[] array = new double[k_num];
            for (int j = 0; j < k_num; j++) {
                Number x2 = k[j].getX();
                Number y2 = k[j].getY();
                double dis = geoMethods.calDis(x1, y1, x2, y2);
                array[j] = dis;
            }
            located = minValue(array); //locate in position of k
            lst = rc[located];
            if (null == lst) lst = new ArrayList<>();
            if (nodeArray[i] == null) {
                continue;
            }
            lst.add(nodeArray[i]);
            rc[located] = lst;
            List<Node<?>>[] copy = (List<Node<?>>[]) Array.newInstance(List.class, k_num);
            System.arraycopy(rc, 0, copy, 0, k_num);
            for (int ki = 0; ki < k_num; ki++) {
                center = geoMethods.getEquilibriumPoint(copy[ki]);
                if (!Objects.equals(k[ki].getX().doubleValue(), center.getX().doubleValue())
                        || !Objects.equals(k[ki].getY().doubleValue(), center.getY().doubleValue())) {
                    k[ki] = center;
                    flag = true;
                }
            }
        }
        //re-cal clusters' centers n fill 'k'

        Hashtable<Node<?>, List<Node<?>>> cluster = new Hashtable<>(); //result
        for (int i = 0; i < k_num; i++) {
            cluster.put(k[i], rc[i]);
        }
        return cluster;
    }

    static void updateK(Node<?>[] source, Node<?>[] compare) {
        if (source.length != compare.length) throw new WrongMemberException("params need to have the same size");
        for (int i = 0; i < source.length; i++) {
            if (Objects.equals(source[i].getX(), compare[i].getX())
                    && Objects.equals(source[i].getY(), compare[i].getY())) continue;
            source[i] = compare[i];
        }
    }

    private int minValue(double[] array) {
        int res = 0;
        for (int i = 0; i < array.length; i++) {
            if (array[res] > array[i]) {
                res = i;
            }
        }
        return res;
    }

}
