package org.xiongdwm.graphstructure.connectGraph;



import org.xiongdwm.graphstructure.exception.WrongMemberException;

import java.lang.reflect.Array;
import java.util.*;

public class GraphSearch<T> {
    private final boolean[] wasVisited;
    private final T[] edgeTo;
    private final GraphStructure<T> G;
    private T theTarget;
    private final LinkedList<List<T>> allPaths = new LinkedList<>(); // collection of all recorded paths 所有路径集合
    private final Stack<T> path = new Stack<>(); //get a path 一条路径
    private int maximumOutDegree; // to limit the path length, 规定路径出度，也就时最多跳数
    private T dominator; // dominator 必经节点
    private final List<Edge<T>>minConnection= new ArrayList<>();
    private int weightLimit;

    public enum Manipulate {
        BREADTH_FIRST("广度优先"),
        DEPTH_FIRST("深度优先"),
        DJKSTRA("Djkstra"),
        PRIME("联通分量"),
        NONE_STRUCTURE("无结构");

        private final String text;
        Manipulate(String text){
            this.text=text;
        }
        private String getName(){
            return text;
        }
    }

    @SuppressWarnings("unchecked")
    public GraphSearch(GraphStructure<T> G, T root, Manipulate manipulate, T[] nodesAbandon, T target, int maximumOutDegree,T dominator) {
        Class<?> clazz = root.getClass();
        this.wasVisited = new boolean[G.getNodesNum()];
        this.edgeTo = (T[]) Array.newInstance(clazz, G.getNodesNum());
        this.maximumOutDegree = maximumOutDegree;
        this.dominator = dominator;
        theTarget = target;
        this.G = G;
        for (int i = 0; i < G.getNodesNum(); i++) {
            wasVisited[i] = false;
            edgeTo[i] = null;
        }
        if (nodesAbandon != null) {
            for (T o : nodesAbandon) {
                setWasVisited(o);
            }
        }
        switch (manipulate) {
            case BREADTH_FIRST:
                bfs(root);
                break;
            case DEPTH_FIRST:
                dfs(root);
                break;
            case DJKSTRA:
                djkstra(root);
                break;
            case PRIME:
                prime(root);
            case NONE_STRUCTURE:
                break;
        }
    }

    @SuppressWarnings("unchecked")
    public GraphSearch(GraphStructure<T> G, T root, GraphSearch.Manipulate manipulate) {
        Class<?> clazz = root.getClass();
        this.wasVisited = new boolean[G.getNodesNum()];
        this.edgeTo = (T[]) Array.newInstance(clazz, G.getNodesNum());
        this.G = G;

        for(int i = 0; i < G.getNodesNum(); ++i) {
            this.wasVisited[i] = false;
            this.edgeTo[i] = null;
        }

        switch(manipulate) {
            case BREADTH_FIRST:
                this.bfs(root);
                break;
            case DEPTH_FIRST:
                throw new WrongMemberException("misusing constructor,need param `target`");
            case DJKSTRA:
                this.djkstra(root);
                break;
            case PRIME:
                this.prime(root);
            case NONE_STRUCTURE:
        }

    }

    @SuppressWarnings("unchecked")
    public GraphSearch(GraphStructure<T> G, T root, Manipulate manipulate, T[] nodesAbandon, T target,int maximumOutDegree,T dominator,int weightLimit) {
        Class<?> clazz = root.getClass();
        this.wasVisited = new boolean[G.getNodesNum()];
        this.edgeTo = (T[]) Array.newInstance(clazz, G.getNodesNum());
        this.dominator = dominator;
        theTarget = target;
        this.maximumOutDegree = maximumOutDegree;
        this.G = G;
        this.weightLimit=weightLimit;
        for (int i = 0; i < G.getNodesNum(); i++) {
            wasVisited[i] = false;
            edgeTo[i] = null;
        }
        if (nodesAbandon != null) {
            for (T o : nodesAbandon) {
                setWasVisited(o);
            }
        }
        switch (manipulate) {
            case BREADTH_FIRST:
                bfs(root);
                break;
            case DEPTH_FIRST:
                dfs(root,0);
                break;
            case DJKSTRA:
                djkstra(root);
                break;
            case PRIME:
                prime(root);
            case NONE_STRUCTURE:
                break;
        }
    }

    private void dfs(T root) {
        if (G.isNodeNotIn(root)) return;
        if(dominator!=null&&G.isNodeNotIn(dominator))return;
        if (root.equals(theTarget)) {
            path.push(root);
            List<T> list = new ArrayList<>(path); //store path 转储
            if(dominator!=null){
             if(list.contains(dominator))allPaths.add(list);
            }else {
                allPaths.add(list);
            }
            path.pop();
            wasVisited[G.getIndexOfObject(root)] = false;
            return;
        }
        List<T> nodes = G.get(root); //adjacency matrix of root at this term 所有子节点
        wasVisited[G.getIndexOfObject(root)] = true;
        path.push(root);
        for (int i = 0; i < nodes.size(); i++) {
            T x = nodes.get(i);
            if (x == null) continue;
            int index = G.getIndexOfObject(x);
            if (!wasVisited[index]) {
                if (maximumOutDegree==0||path.size() < maximumOutDegree) dfs(nodes.get(i));
            }
            if (i == nodes.size() - 1) {
                path.pop();
                wasVisited[G.getIndexOfObject(root)] = false;
                return;
            }
        }
    }

    private void dfs(T root, int weight) {
        System.out.println(weight);
        if (G.isNodeNotIn(root)) return;
        if(dominator!=null&&G.isNodeNotIn(dominator))return;
        if(weight==weightLimit&&!root.equals(theTarget))return;
        if (root.equals(theTarget)) {
            if(weight>weightLimit)return;// check if weight is within limit
            path.push(root);
            List<T> list = new ArrayList<>(path); //store path 转储
            if(dominator!=null){
                if(list.contains(dominator))allPaths.add(list);
            }else {
                allPaths.addLast(list);
            }
            path.pop();
            wasVisited[G.getIndexOfObject(root)] = false;
            return;
        }
        List<T> nodes = G.get(root); //adjacency matrix of root at this term 所有子节点
        wasVisited[G.getIndexOfObject(root)] = true;
        path.push(root);
        for (int i = 0; i < nodes.size(); i++) {
            T x = nodes.get(i);
            if (x == null) continue;
            int index = G.getIndexOfObject(x);
            if (!wasVisited[index]) {
                int currentWeight=weight+G.getWeight(root, x);
                if (maximumOutDegree==0||path.size() < maximumOutDegree) dfs(nodes.get(i),currentWeight); // pass weight to next node
            }
            if (i == nodes.size() - 1) {
                path.pop();
                wasVisited[G.getIndexOfObject(root)] = false;
                return;
            }
        }
    }

    private void bfs(T root) {
        int rootPtr = G.getIndexOfObject(root);
        Queue<Integer> queue = new ArrayDeque<>();
        queue.add(rootPtr);
        wasVisited[rootPtr] = true;
        while (!queue.isEmpty()) {
            int index = queue.poll();
            for (T o : G.getRow(index)) {
                if (o == null) continue;
                int ptr = G.getIndexOfObject(o);
                if (!wasVisited[ptr]) {
                    edgeTo[ptr] = G.getNodes()[index];
                    wasVisited[ptr] = true;
                    queue.add(ptr);
                }
            }
        }
    }

    private void djkstra(T root){
        int rootPtr = G.getIndexOfObject(root);
        Queue<Integer> queue = new ArrayDeque<>();
        queue.add(rootPtr);
        wasVisited[rootPtr] = true;
        while (!queue.isEmpty()) {
            int index = queue.poll();
            for (T o : G.getRow(index)) {
                if (o == null) continue;
                int ptr = G.getIndexOfObject(o);
                if (!wasVisited[ptr]) {
                    edgeTo[ptr] = G.getNodes()[index];
                    wasVisited[ptr] = true;
                    queue.add(ptr);
                }
            }
        }
    }
    
    private void prime(T root){
        //int pathCount=0;
        List<T> targets=G.get(root);
        int outDegree=targets.size();
        if(outDegree<2) return;

        List<T>record=new ArrayList<>();
        record.add(root);
        for (T target : targets) {
            //either every single node has unless 1 path to its previous node or it cannot add to path set
            record.add(target);
            this.theTarget = root;
            this.dfs(target);
            int step=this.getAllPaths(false).size();
            if(step<2)continue;
            minConnection.add(new Edge<>(root, target));
            if(record.contains(target))continue;
            prime(target);
        }

    }


    public boolean hasPathTo(T target) {
        if (G.isNodeNotIn(target)) return false;
        return wasVisited[G.getIndexOfObject(target)];
    }

    public void setWasVisited(T o) {
        wasVisited[G.getIndexOfObject(o)] = true;
    }


    public Vector<T> pathTo(T target) {
        if (!hasPathTo(target)) {
            return null;
        }
        Stack<Integer> wait = new Stack<>();
        int p = G.getIndexOfObject(target);
        while (p != -1) {
            wait.push(p);
            p = G.getIndexOfObject(edgeTo[p]);
        }
        Stack<T> path = new Stack<>();
        while (!wait.isEmpty()) {
            path.add(G.getNodes()[wait.pop()]);
        }
        return path;
    }

    public List<T[]> printAllEdge(T target) {
        List<T[]> rs = new ArrayList<>();
        T[] roots = G.getNodes();
        return rs;
    }

    public void showPath(T target) {
        assert hasPathTo(target);
        Vector<T> vector = pathTo(target);
        for (int i = 0; i < vector.size(); i++) {
            System.out.println(vector.elementAt(i));
            if (i == vector.size() - 1) {
                System.out.println();
            } else {
                System.out.println("->");
            }
        }
    }

    public LinkedList<List<T>> getAllPaths(boolean sortedOrNot) {
        if(sortedOrNot)pathOrderByWeight(allPaths);
        return allPaths;
    }

    public List<Edge<T>> getMinConnection() {
        return minConnection;
    }

    private void pathOrderByWeight(LinkedList<List<T>> p){
        p.sort(new Comparator<List<T>>() {
            @Override
            public int compare(List<T> o1, List<T> o2) {
                int weight1=0;
                int weight2=0;
                for (int i = 0; i < o1.size()-1; i++) {
                    weight1+=G.getWeight(o1.get(i),o1.get(i+1));
                }
                for (int i = 0; i < o2.size()-1; i++) {
                    weight2+=G.getWeight(o2.get(i),o2.get(i+1));
                }
                return weight1-weight2;
            }
        });
    }
}
