package com.xiongdwm.graphstructure;
/**
 * first time offer some api-like stuff to the community even it seems not useful lol
 */

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author xiong
 * @version 1.0
 *  to put the data into a graph structure
 */
public class GraphStructure {
    private int nodesNum;
    private Object[] nodes;
    private Object[][] matrix;
    private List<Edge<Object>> edges;

    public GraphStructure(Object[] nodes){
        this.nodes=nodes;
        this.nodesNum= nodes.length;
        this.edges=new ArrayList<>();
        matrix=new Object[nodesNum][nodesNum];
        for(int i=0;i<nodesNum;i++){
            matrix[i]=new Object[nodesNum];
        }
    }

    public Object[] getNodes() {
        return nodes;
    }

    public void setNodes(Object[] nodes) {
        this.nodes = nodes;
    }

    //如果是对象则需要重写实体类的equals方法
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

        for (Object o : array) {
            edges.add(new Edge<>(node, o));
        }
    }

    public<T> int getIndexOfObject(T obj){
        return Arrays.asList(nodes).indexOf(obj);
    }

    public Object[] getRow(int index){
        return matrix[index];
    }

    public int getNodesNum() {
        return nodesNum;
    }

    public Object[][] getMatrix() {
        return matrix;
    }

    public List<Edge<Object>> getEdges(){
        return this.edges.stream().distinct().collect(Collectors.toList());
    }

    /**
     * cut the subfield
     * @return edge set，
     */
    public List<Edge<?>> tailed(){
        if(get(this.nodes[0]).length==1)return Collections.emptyList(); //出度为1的直接返回;if out-degree for start node is 1, return empty
        List<Object> nodesList=new ArrayList<>(Arrays.asList(this.nodes)); //参与的所有节点

        Queue<Object> queue=new ArrayDeque<>(nodesList); //加入队列
        Set<Object> trashBin=new HashSet<>(); //出度为1的点们；store nodes with out-degree =1
        //Object preVertex=new Object();??? 这里来一个前一个顶点的记录？
        do{
            Object node=queue.poll();
            Object[] out=get(node); //get方法是取到当前节点连接的节点
            List<Object> temp=new ArrayList<>(Arrays.asList(out));
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

    private List<Edge<?>> getEdgeByNode(Object node,Set<Object> trash){
        Object[] array=get(node);
        return Arrays.stream(array).filter(it->!trash.contains(it)).map(o-> new Edge<>(node, o)).collect(Collectors.toList());
    }
}
