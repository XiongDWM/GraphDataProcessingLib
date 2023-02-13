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

    @SuppressWarnings("unchecked")
    public Cluster(GeoAbstract geoMethods, List<Node<?,Number>>nodes) {
        //this.nodes=nodes;
        this.geoMethods = geoMethods;
        this.nodesCount=nodes.size();
        k_num=nodesCount/4;
        Class<?>clazz=Node.class;
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

    public Object clustering(){
        Node<?,Number>[] k = randomPoints(k_num);
        Hashtable<Node<?,Number>,Integer>hashtable = new Hashtable<>();
        for(int i=0;i< nodeArray.length;i++){
            Number x1=nodeArray[i].getX();
            Number y1=nodeArray[i].getY();
            double[] array=new double[k_num];
            for(int j=0;j<k_num;j++){
                Number x2=k[j].getX();
                Number y2=k[j].getY();
                double dis=geoMethods.calDis(x1,y1,x2,y2);
                array[j]=dis;
            }
            int located=minValue(array);
            hashtable.put(nodeArray[i],located);
        }
/*        Node
        hashtable.*/

        return null;
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
