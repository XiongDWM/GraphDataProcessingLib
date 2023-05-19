package org.xiongdwm.graphstructure.connectGraph;
/*
 * @description first time offer some api to the community even it seems not very useful lol
 *
 */

import org.xiongdwm.graphstructure.connectGraph.Edge;

import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author xiong
 * @version 1.06 01/06/2023
 * @overall-description put the data into a graph structure 图结构
 * @ver-description store weight 保存边权值
 */
public class GraphStructure<T> {
    private final int nodesNum;
    private T[] nodes;
    private final T[][] matrix;
    private final List<Edge<T>> edges;
    private final Class<?> clazz;

    @SuppressWarnings("unchecked")
    public GraphStructure(T[] nodes){
        this.nodes=nodes;
        this.nodesNum= nodes.length;
        this.edges=new ArrayList<>();
        this.clazz=nodes[0].getClass();
        matrix= (T[][]) Array.newInstance(clazz,nodesNum,nodesNum);
        for(int i=0;i<nodesNum;i++){
            matrix[i]= (T[]) Array.newInstance(clazz,nodesNum);
        }
    }

    public T[] getNodes() {
        return nodes;
    }

    public void setNodes(T[] nodes) {
        this.nodes = nodes;
    }

    //if T is not basic data type, need to rewrite methods 'equal'&'hashCode' 如果是对象则需要重写实体类的equals方法
    public boolean isNodeIn(T obj){
        return !Arrays.asList(this.nodes).contains(obj);
    }
    @SuppressWarnings("unchecked")
    public T[] get(T obj){
        T[] ex=(T[]) Array.newInstance(clazz,nodesNum);
        if(isNodeIn(obj)) return ex;
        int index=getIndexOfObject(obj);
        return matrix[index];
    }

    @SuppressWarnings("unchecked")
    public void make(T node,T[] context){
        List<T>list=new ArrayList<>();
        for(T o:context){
            if(!isNodeIn(o)) list.add(o);
        }
        T[] e=(T[])Array.newInstance(clazz,0);
        Array.set(matrix,getIndexOfObject(node), list.toArray(e));
        list=null;
        e=null;
        for (T o : context) {
            edges.add(new Edge<>(node, o));
        }
    }

    @SuppressWarnings("unchecked")
    public void make(T node, T[] context, int[] weights) { //添加边权参数weights
        List<T> list = new ArrayList<>();
        for (int i = 0; i < context.length; i++) {
            if (!isNodeIn(context[i])) {
                list.add(context[i]);
                edges.add(new Edge<>(node, context[i], weights[i])); //添加边权
            }
        }
        Array.set(matrix, getIndexOfObject(node), list.toArray((T[]) Array.newInstance(clazz, 0)));
    }

    @SuppressWarnings("unchecked")
    public void makeWithOutEdge(T node,T[] context){
        List<T>list=new ArrayList<>();
        for(T o:context){
            if(!isNodeIn(o)) list.add(o);
        }
        T[] e=(T[])Array.newInstance(clazz,0);
        Array.set(matrix,getIndexOfObject(node), list.toArray(e));
        list.clear();
        e=null;
    }

    public int getIndexOfObject(T obj){
        return Arrays.asList(nodes).indexOf(obj);
    }

    public T[] getRow(int index){
        return matrix[index];
    }

    public int getNodesNum() {
        return nodesNum;
    }

    public T[][] getMatrix() {
        return matrix;
    }

    public List<Edge<T>> getEdges(){
        return this.edges.stream().distinct().collect(Collectors.toList());
    }

    /**
     * cut the subfield
     * @return edge set，
     */
    public List<Edge<T>> tailed(){
        if(get(this.nodes[0]).length==1)return Collections.emptyList(); //出度为1的直接返回;if out-degree for start node is 1, return empty
        List<T> nodesList=new ArrayList<>(Arrays.asList(this.nodes)); //参与的所有节点

        Queue<T> queue=new ArrayDeque<>(nodesList); //加入队列
        Set<T> trashBin=new HashSet<>(); //出度为1的点们；store nodes with out-degree =1
        //Object preVertex=new Object();??? 这里来一个前一个顶点的记录？
        do{
            T node=queue.poll();
            if(node==null)continue;
            T[] out=get(node); //get方法是取到当前节点连接的节点
            List<T> temp=new ArrayList<>(Arrays.asList(out));
            temp.removeAll(trashBin); //去掉出度中已经计算出的单个连接边节点；get rid of connected nodes which already stored in trashBin
            int outDegree=temp.size();
            if(outDegree==1){
                queue.add(out[0]);  //add the only connected-node to queue
                trashBin.add(node);
            }
        }while (!queue.isEmpty());
        nodesList.removeAll(trashBin); //倒了
        return nodesList.stream().flatMap(o->getEdgeByNode(o,trashBin).stream()).distinct().collect(Collectors.toList());
    }

    private List<Edge<T>> getEdgeByNode(T node,Set<T> trash){
        T[] array=get(node);
        return Arrays.stream(array).filter(it->!trash.contains(it)&&it!=null).map(o-> new Edge<>(node, o)).collect(Collectors.toList());
    }


}
