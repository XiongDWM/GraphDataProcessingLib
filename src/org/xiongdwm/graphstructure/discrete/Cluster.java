package org.xiongdwm.graphstructure.discrete;

import org.xiongdwm.graphstructure.exception.WrongMemberException;
import org.xiongdwm.graphstructure.utils.geometry.GeoAbstract;

import java.lang.reflect.Array;
import java.util.*;

public class Cluster {
    private final GeoAbstract geoMethods;
    //private List<Node<?,Number>>nodes;
    private static int random_record=0;
    private final Node<?,Number>[] nodeArray;
    private static int k_num=0;
    private final int nodesCount;
    private final static Class<?>clazz=Node.class;

    @SuppressWarnings("unchecked")
    public Cluster(GeoAbstract geoMethods, List<Node<?,Number>>nodes) {
        //this.nodes=nodes;
        this.geoMethods = geoMethods;
        this.nodesCount=nodes.size();
        k_num=nodesCount/4;

        this.nodeArray=(Node<?, Number>[]) Array.newInstance(clazz,nodesCount);
        for(int i=0;i<nodesCount;i++){
            this.nodeArray[i]=nodes.get(i);
        }

    }

    @SuppressWarnings("unchecked")
    public Node<?,Number>[] randomPoints(int amount){
        if(0>=amount)throw new WrongMemberException("amount is a positive integer");
        Class<?>clazz=Node.class;
        Node<?,Number>[] result=(Node<?, Number>[]) Array.newInstance(clazz,amount);
        Random random=new Random();
        int p=0;
        while(amount>0){
            int rn=random.nextInt(this.nodesCount); //number in [0,range)
            if(rn==random_record)continue;
            result[p]=this.nodeArray[rn];
            random_record=rn;
            amount--;
            p++;
        }
        return result;
    }
    @SuppressWarnings("unchecked")
    public Object runClustering(){
        Node<?,Number>[] k = randomPoints(k_num);
        Node<?,Number>[] bk=(Node<?, Number>[]) Array.newInstance(clazz,k_num);
        System.arraycopy(k,0,bk,0,k_num);
        Hashtable<Integer,List<Node<?,Number>>> c=new Hashtable<>();
        do{
            //c=clustering(k);

        }while(k==bk);

        return null;
    }
    @SuppressWarnings("unchecked")
    public Hashtable<Node<?,Number>,List<Node<?,Number>>> clustering(Node<?,Number>[] k){
        //Node<?,Number>[] k = randomPoints(k_num);
        Node<?,Number>[] back=(Node<?, Number>[]) Array.newInstance(clazz,k_num);
        System.arraycopy(k,0,back,0,k_num);
        Hashtable<Node<?,Number>,Integer>hashtable = new Hashtable<>();
        for (Node<?, Number> numberNode : nodeArray) {
            Number x1 = numberNode.getX();
            Number y1 = numberNode.getY();
            double[] array = new double[k_num];
            for (int j = 0; j < k_num; j++) {
                Number x2 = k[j].getX();
                Number y2 = k[j].getY();
                double dis = geoMethods.calDis(x1, y1, x2, y2);
                array[j] = dis;
            }
            int located = minValue(array); //locate in position of k
            hashtable.put(numberNode, located);
        }
        Hashtable<Node<?,Number>,List<Node<?,Number>>> cluster=new Hashtable<>();
        List<Node<?,Number>>nodes=new ArrayList<>();
        Enumeration<Node<?,Number>> es=hashtable.keys();
        Node<?,Number> newCenter;
        boolean flag=true;
        for(int i=0; i<k.length;i++){
            while(es.hasMoreElements()){
                Node<?,Number>n=es.nextElement();
                if(hashtable.get(n)==i)nodes.add(n);
            }
            newCenter=geoMethods.getEquilibriumPoint(nodes);
            if(!Objects.equals(k[i].getX(), newCenter.getX())
                    || !Objects.equals(k[i].getY(), newCenter.getY())){
                k[i]=newCenter;
                continue;
            }
            cluster.put(k[i], nodes);
            newCenter=null;
            nodes.clear();
        }
        if(k!=back){
            clustering(k);
        }
        return cluster;
    }

    static void updateK(Node<?,Number>[] source,Node<?,Number>[] compare){
        if(source.length!=compare.length)throw new WrongMemberException("params need to have the same size");
        for(int i=0;i<source.length;i++){
            if(Objects.equals(source[i].getX(), compare[i].getX())
                    && Objects.equals(source[i].getY(), compare[i].getY()))continue;
            source[i]=compare[i];
        }
    }

    public static void main(String[] args) {
        int[] a={1,2,3,4,5,6,7,13,15,613,312,321,21};
        int[] b=new int[a.length];
        //System.arraycopy(a,0,b,0,a.length);
        //System.out.println(a==b);
        int c=2;
        boolean flag=true;
        do{
            System.arraycopy(a,0,b,0,a.length);
            for(int i=0;i<a.length;i++){
                if(a[i]%2==0){
                    a[i]=0;
                }
                flag=a[i]==b[i];
                System.out.println(a[i]==b[i]);
            }
            System.out.println(Arrays.toString(a));
            System.out.println(Arrays.toString(b));
            System.out.println("+1");
            c--;
        }while (flag&&c>=0);
        //System.out.println(a==b);
    }
    private int minValue(double[] array){
        int res=0;
        for(int i=0;i<array.length;i++){
            if(array[res]>array[i]){
                res=i;
            }
        }
        return res;
    }

}
