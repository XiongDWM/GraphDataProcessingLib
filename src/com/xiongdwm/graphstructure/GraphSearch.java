package com.xiongdwm.graphstructure;


import java.util.*;

public class GraphSearch {
    private final boolean[] wasVisited;
    private final Object[] edgeTo;
    private final GraphStructure G;
    private final Object theTarget;
    private final LinkedList<List<Object>> allPaths=new LinkedList<>(); // collection of all recorded paths 所有路径集合
    private final Stack<Object> path=new Stack<>(); //a single path 一条路径
    private int maximumOutDegree; // to limit the path length, 规定路径出度，也就时最多跳数

    public enum Manipulate{
        BREADTH_FIRST("广度优先"),
        DEPTH_FIRST("深度优先"),
        DJKSTRA("Djkstra"),
        NONE_STRUCTURE("无结构");

        private final String text;
        Manipulate(String text){
            this.text=text;
        }
        private String getName(){
            return text;
        }
    }

    public GraphSearch(GraphStructure G, Object root, Manipulate manipulate,Object[] nodesAbandon,Object target,int maximumOutDegree){
        this.wasVisited=new boolean[G.getNodesNum()];
        this.edgeTo=new Object[G.getNodesNum()];
        this.maximumOutDegree =maximumOutDegree;
        theTarget = target;
        this.G=G;
        for(int i=0;i<G.getNodesNum();i++){
            wasVisited[i]=false;
            edgeTo[i]=-1;
        }
        if(nodesAbandon!=null){
            for(Object o:nodesAbandon){
                setWasVisited(o);
            }
        }
        switch (manipulate){
            case BREADTH_FIRST:
                bfs(root);
                break;
            case DEPTH_FIRST:
                dfs(root);
                break;
            case NONE_STRUCTURE:
                break;
        }
    }

    private void dfs(Object root){
        if(root.equals(theTarget)) {
            path.push(root);
            List<Object> list = new ArrayList<>(path); //store path 转储
            allPaths.add(list);
            path.pop();
            wasVisited[G.getIndexOfObject(root)]=false;
            return;
        }
        Object[] nodes=G.get(root); //adjacency matrix of root at this term 所有子节点
        wasVisited[G.getIndexOfObject(root)]=true;
        path.push(root);
        for(int i=0;i< nodes.length;i++){
            int index=G.getIndexOfObject(nodes[i]);
            if(maximumOutDegree ==0) maximumOutDegree =path.size()+1;
            if(!wasVisited[index]&&path.size()< maximumOutDegree){
                dfs(nodes[i]);
            }
            if(i==nodes.length-1){
                path.pop();
                wasVisited[G.getIndexOfObject(root)]=false;
                return;
            }
        }
    }

    private void bfs(Object root){
        int rootPtr=G.getIndexOfObject(root);
        Queue<Integer> queue=new ArrayDeque<Integer>();
        queue.add(rootPtr);
        wasVisited[rootPtr]=true;
        while(!queue.isEmpty()){
            int index=queue.poll();
            for(Object o:G.getRow(index)){
                if(o==null)continue;
                int ptr= G.getIndexOfObject(o);
                if(!wasVisited[ptr]){
                    edgeTo[ptr]=G.getNodes()[index];
                    wasVisited[ptr]=true;
                    queue.add(ptr);
                }
            }
        }
    }
    public void djkstra(){

    }

    public boolean hasPathTo(Object target){
        if(!G.isNodeIn(target))return false;
        return wasVisited[G.getIndexOfObject(target)];
    }

    public void setWasVisited(Object o){
        wasVisited[G.getIndexOfObject(o)]=true;
    }


    public Vector<Object> pathTo(Object target){
        if(!hasPathTo(target)){
            return null;
        }
        Stack<Integer> wait=new Stack<>();
        int p= G.getIndexOfObject(target);
        while(p!=-1){
            wait.push(p);
            p= G.getIndexOfObject(edgeTo[p]);
        }
        Stack<Object> path= new Stack<>();
        while(!wait.isEmpty()){
            path.add(G.getNodes()[wait.pop()]);
        }
        return path;
    }

    public List<Object[]> printAllEdge(Object target){
        List<Object[]> rs=new ArrayList<>();
        Object[]roots=G.getNodes();

        return rs;
    }

    public void showPath(Object target){
        assert hasPathTo(target);
        Vector<Object> vector=pathTo(target);
        for(int i=0;i<vector.size();i++){
            System.out.println(vector.elementAt(i));
            if(i==vector.size()-1){
                System.out.println();
            }else {
                System.out.println("->");
            }
        }
    }

    public LinkedList<List<Object>> getAllPaths() {
        return allPaths;
    }

}
