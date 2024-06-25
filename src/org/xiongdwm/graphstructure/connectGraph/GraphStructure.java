package org.xiongdwm.graphstructure.connectGraph;

import org.xiongdwm.graphstructure.exception.WrongMemberException;

import java.lang.reflect.Array;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author xiong
 * @version 1.06 01/06/2023
 * @overall-description form data into a graph structure 图结构
 * @ver-description store weight x保存边权值
 */
public class GraphStructure<T> {
    private int nodesNum;
    private T[] nodes;
    private final ConcurrentHashMap<T,List<T>> matrix;
    private final List<Edge<T>> edges;
    private Class<?> clazz;
    private ConcurrentHashMap<String, Integer> weightMap; // 新增的 Map 对象

    public GraphStructure(){
        this.edges=new ArrayList<>();
        this.clazz=null;
        matrix= new ConcurrentHashMap<>();
        weightMap = new ConcurrentHashMap<>(); // 初始化 Map 对象
    }

    public GraphStructure(Class<?> clazz){
        this.edges=new ArrayList<>();
        this.clazz=clazz;
        matrix= new ConcurrentHashMap<>();
        weightMap = new ConcurrentHashMap<>(); // 初始化 Map 对象
    }


    public GraphStructure(T[] nodes){
        this.nodes=nodes;
        this.nodesNum= nodes.length;
        this.edges=new ArrayList<>();
        this.clazz=nodes[0].getClass();
        matrix=new ConcurrentHashMap<>();
        weightMap =new ConcurrentHashMap<>(); // 初始化 Map 对象
    }

    @SuppressWarnings("unchecked")
    public void init(){
        if(null==matrix) throw new WrongMemberException("matrix is empty");
        if(null==clazz)matrix.keySet().stream().findAny().ifPresent(it->{
            this.clazz=it.getClass();
            System.out.println(clazz.getName());
        });
        if(null==clazz)throw new WrongMemberException("clazz undefine");
        nodes=matrix.keySet().toArray((T[])Array.newInstance(clazz,0));
        nodesNum=nodes.length;
    }


    public T[] getNodes() {
        return nodes;
    }

    public void setNodes(T[] nodes) {
        if(null==clazz)this.clazz=nodes[0].getClass();
        this.nodes = nodes;
    }

    //if T is not basic data type, need to rewrite methods 'equal'&'hashCode' 如果是对象则需要重写实体类的equals方法
    public boolean isNodeNotIn(T obj){
        return !Arrays.asList(this.nodes).contains(obj);
    }

    public List<T> get(T obj){
        if(isNodeNotIn(obj)) return Collections.emptyList();
        return matrix.get(obj);
    }

    public void make(T node,List<T> context,int[] weight){
        List<T> list = new ArrayList<>(context);
        matrix.put(node, list);
        list=null;
        for (int i = 0; i < context.size(); i++) {
            edges.add(new Edge<>(node, context.get(i), weight[i]));
            weightMap.put(node.toString() + "-" + context.get(i).toString(), weight[i]); // 将边的权值存储到 Map 中
        }
    }

    public void make(T node,List<T> context){
        List<T>list=new ArrayList<>();
        for(T o:context){
            if(!isNodeNotIn(o)) list.add(o);
        }
        matrix.put(node, list);
        list=null;
        for (T o : context) {
            edges.add(new Edge<>(node, o));
        }
    }

    public Map<String,Integer>getWeightMap(){
        return weightMap;
    }

    public void setWeightMap(Map<String,Integer>weightMap){
        this.weightMap=new ConcurrentHashMap<>(weightMap);
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

    public List<T> getRow(int index){
        return matrix.get(nodes[index]);
    }

    public int getNodesNum() {
        return nodesNum;
    }

    public Map<T, List<T>> getMatrix() {
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
        if(get(this.nodes[0]).size()==1)return Collections.emptyList(); //出度为1的直接返回;if out-degree for start node is 1, return empty
        List<T> nodesList=new ArrayList<>(Arrays.asList(this.nodes)); //参与的所有节点

        Queue<T> queue=new ArrayDeque<>(nodesList); //加入队列
        Set<T> trashBin=new HashSet<>(); //出度为1的点们；store nodes with out-degree =1
        //Object preVertex=new Object();??? 这里来一个前一个顶点的记录？
        do{
            T node=queue.poll();
            if(node==null)continue;
            List<T> out=get(node); //get方法是取到当前节点连接的节点
            List<T> temp=new ArrayList<>(out);
            temp.removeAll(trashBin); //去掉出度中已经计算出的单个连接边节点；get rid of connected nodes which already stored in trashBin
            int outDegree=temp.size();
            if(outDegree==1){
                queue.add(out.get(0));  //add the only connected-node to queue
                trashBin.add(node);
            }
        }while (!queue.isEmpty());
        nodesList.removeAll(trashBin); //倒了
        return nodesList.stream().flatMap(o->getEdgeByNode(o,trashBin).stream()).distinct().collect(Collectors.toList());
    }

    private List<Edge<T>> getEdgeByNode(T node,Set<T> trash){
        List<T> array=get(node);
        return array.stream().filter(it->!trash.contains(it)&&it!=null).map(o-> new Edge<>(node, o)).collect(Collectors.toList());
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
