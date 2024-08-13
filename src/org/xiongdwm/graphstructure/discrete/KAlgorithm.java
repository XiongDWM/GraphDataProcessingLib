package org.xiongdwm.graphstructure.discrete;

import org.xiongdwm.graphstructure.exception.WrongMemberException;
import org.xiongdwm.graphstructure.utils.geometry.GeoAbstract;

import java.lang.reflect.Array;
import java.util.*;

public class KAlgorithm {
    private final GeoAbstract geoMethods;
    private static WeightedNode<?>[] nodeArray = new WeightedNode[0];
    private static int k_num = 0;
    private final int nodesCount;
    private final static Class<?> clazz = WeightedNode.class;
    private final boolean[] unChange;

    public KAlgorithm(GeoAbstract geoMethods, WeightedNode<?>[] nodes) {
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
    public WeightedNode<?>[] randomPoints(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("amount must be a positive integer");
        }
        WeightedNode<?>[] result = new WeightedNode<?>[amount];
        double maxVariance = 0.0;

        for (int i = 0; i < 10; i++) {
            WeightedNode<?>[] randomPoints = getRandomPoints(amount);

            // Calculate variance
            double variance = geoMethods.calculateVariance(randomPoints);

            // Store result with largest variance
            if (variance > maxVariance) {
                System.arraycopy(randomPoints, 0, result, 0, amount);
                maxVariance = variance;
            }
            //System.out.println(maxVariance);
        }

        return result;
    }
    private WeightedNode<?>[] getRandomPoints(int amount) {
        WeightedNode<?>[] points = new WeightedNode<?>[amount];
        Random random = new Random();
        int p = 0;
        while (amount > 0) {
            int rn = random.nextInt(this.nodesCount); //number in [0,range)
            if (unChange[rn]) continue;
            points[p] = nodeArray[rn];
            unChange[rn] = true;
            amount--;
            p++;
        }
        Arrays.fill(unChange, false); // Reset unChange array
        return points;
    }


    @SuppressWarnings("unchecked")
    public Hashtable<WeightedNode<?>, List<WeightedNode<?>>> clustering() {
        WeightedNode<?>[] k = randomPoints(k_num); //numbers of cluster
        boolean flag; //sensor 感知器
        List<WeightedNode<?>>[] rc=(List<WeightedNode<?>>[]) Array.newInstance(List.class, k_num);
        WeightedNode<?> center;
        do {
            flag= false;
            List<WeightedNode<?>>[] temprc = (List<WeightedNode<?>>[]) Array.newInstance(List.class, k_num);
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
                int located = minValue(array); //locate in position of k
                WeightedNode<?> t=nodeArray[i];
                if(null==temprc[located])temprc[located]=new ArrayList<>();
                temprc[located].add(t);
            }
            List<WeightedNode<?>>[] copy = (List<WeightedNode<?>>[]) Array.newInstance(List.class, k_num);
            System.arraycopy(temprc,0,copy,0,k_num);
            //re-cal clusters' centers n fill 'k'
            WeightedNode<?>[] kcopy= (WeightedNode<?>[]) Array.newInstance(WeightedNode.class,k_num);
            System.arraycopy(k,0,kcopy,0,k_num);
            for (int i = 0; i < k_num; i++) {
                if(temprc[i]==null)continue;
                center = geoMethods.getEquilibriumPoint(temprc[i]);
                if (!Objects.equals(k[i].getX().doubleValue(), center.getX().doubleValue())
                        || !Objects.equals(k[i].getY().doubleValue(), center.getY().doubleValue())) {
                    kcopy[i] = center;
                    flag = true;
                }
            }
            System.arraycopy(kcopy,0,k,0,k_num);
            if(!flag)System.arraycopy(temprc,0,rc,0,k_num);
        } while (flag);

        Hashtable<WeightedNode<?>, List<WeightedNode<?>>> cluster = new Hashtable<>(); //result
        for (int i = 0; i < k_num; i++) {
            if(null==rc[i])continue;
            cluster.put(k[i], rc[i]);
        }
        return cluster;
    }

    static void updateK(WeightedNode<?>[] source, WeightedNode<?>[] compare) {
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


    public static void main(String[] args) {
        Integer[] s1={1,2,53,4,5};
        Integer[] s2={1,2,3,4,5};
        System.arraycopy(s1,0,s2,0,s2.length);
        System.out.println(Arrays.toString(s2));
        String s="hello";
        String rs=new StringBuilder(s).reverse().toString();
        System.out.println(rs);
    }

}
