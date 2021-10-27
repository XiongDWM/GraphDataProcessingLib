package com.xiongdwm.graphstructure;
/**
 * first time offer some api-like stuff to the community even it is not useful lol
 */

import java.util.Arrays;

/**
 * @author xiong
 * @version 0.1
 *  to put the data into a graph structure
 */
public class GraphStructure {
    private int nodesNum;
    private Object[] nodes;
    private Object[][] matrix;
    private boolean[] wasVisited;

    public GraphStructure(Object[] nodes){
        this.nodes=nodes;
        this.nodesNum= nodes.length;
        matrix=new Object[nodesNum][nodesNum];
        wasVisited=new boolean[nodesNum];
    }

    public Object[] getNodes() {
        return nodes;
    }

    public void setNodes(Object[] nodes) {
        this.nodes = nodes;
    }

    // 需要重写实体类的equals方法
    // need to overwrite the method 'equals' as well as the 'hashcode' of an entity
    public<T> boolean isNodeIn(T obj){
        return Arrays.asList(this.nodes).contains(obj);
    }

    public Object[] get(Object obj){
        Object[] ex={};
        if(!isNodeIn(obj)) return ex;
        int index=getIndexOfObject(obj);
        return matrix[index];
    }
    public void make(Object node,Object[] context){
        Object[] array=Arrays.stream(context).filter(this::isNodeIn).toArray();
        matrix[getIndexOfObject(node)]=array;
    }

    public<T> int getIndexOfObject(T obj){
        return Arrays.asList(nodes).indexOf(obj);
    }

    public Object[] getRow(int index){
        return matrix[index];
    }

    public boolean isNodeVisited(Object obj){
        return wasVisited[getIndexOfObject(obj)];
    }

    public int getNodesNum() {
        return nodesNum;
    }

     public static void main(String[] args) {
        Integer[] array={};
        Integer[] array1={2,3,6};
        Integer[] array2={3,4};
        Integer[] array3={3,4,5};
        //GraphStructure graph=new GraphStructure(array);
        //graph.make(1,array1);
        //System.out.println(Arrays.toString(graph.get(1)));
        //System.out.println(Arrays.deepToString(graph.matrix));
         System.out.println(array.length);
    }

}
