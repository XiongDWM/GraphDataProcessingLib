package org.xiongdwm.graphstructure.connectGraph;

import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author xiong
 * @version 1.06 01/06/2023
 * @overall-description put the data into a graph structure 图结构
 * @ver-description store weight x保存边权值
 */
public class GraphStructure<T> {
    private  int nodesNum;
    private T[] nodes;
    private  T[][] matrix;
    private  List<Edge<T>> edges;
    private  Class<?> clazz;
    private Map<String, Integer> weightMap; // 新增的 Map 对象

    @SuppressWarnings("unchecked")
    public GraphStructure(T[] nodes){
        this.nodes=nodes;
        this.nodesNum= nodes.length;
        this.edges=new ArrayList<>();
        this.clazz=nodes[0].getClass();
        matrix= (T[][]) Array.newInstance(clazz,nodesNum,nodesNum);
        weightMap = new HashMap<>(); // 初始化 Map 对象
        for(int i=0;i<nodesNum;i++){
            matrix[i]= (T[]) Array.newInstance(clazz,nodesNum);
        }
    }

    public GraphStructure() {
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

    @SuppressWarnings("unchecked")
    public void make(T node,T[] context,int[] weight){
        List<T>list=new ArrayList<>();
        for(T o:context){
            if(!isNodeIn(o)) list.add(o);
        }
        T[] e=(T[])Array.newInstance(clazz,0);
        Array.set(matrix,getIndexOfObject(node), list.toArray(e));
        list=null;
        e=null;
        for (int i = 0; i < context.length; i++) {
            edges.add(new Edge<>(node, context[i], weight[i]));
            weightMap.put(node.toString() + "-" + context[i].toString(), weight[i]); // 将边的权值存储到 Map 中
        }
    }

    public Map<String,Integer>getWeightMap(){
        return weightMap;
    }

    public void setWeightMap(Map<String,Integer>weightMap){
        this.weightMap=weightMap;
    }

    // 修改后的 getWeight 方法
    public int getWeight(T v1,T v2){
        String key = v1.toString() + "-" + v2.toString();
        String key1 = v2.toString() + "-" + v1.toString();
        Integer weight=weightMap.get(key);
        Integer weight1=weightMap.get(key1);
        if (weight == null && weight1 == null) {
            return 0;
        } else if (weight == null) {
            return weight1;
        } else if (weight1 == null) {
            return weight;
        }
        return Math.min(weight1,weight);
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
    //最小连通子图
    public List<Edge<T>> minTailed(){
        List<Edge<T>> edges=tailed();
        List<Edge<T>> min=new ArrayList<>();
        for(Edge<T> edge:edges){
            if(!min.contains(edge)&&!min.contains(edge.reverse())){
                min.add(edge);
            }
        }
        return min;
    }


}
