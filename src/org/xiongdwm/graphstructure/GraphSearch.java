package org.xiongdwm.graphstructure;


import java.lang.reflect.Array;
import java.util.*;

public class GraphSearch<T> {
    private final boolean[] wasVisited;
    private final T[] edgeTo;
    private final GraphStructure<T> G;
    private final T theTarget;
    private final LinkedList<List<T>> allPaths = new LinkedList<>(); // collection of all recorded paths 所有路径集合
    private final Stack<T> path = new Stack<>(); //a single path 一条路径
    private int maximumOutDegree; // to limit the path length, 规定路径出度，也就时最多跳数
    private final T dominator; // dominator 必经节点

    public enum Manipulate {
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
                djkstra();
                break;
            case NONE_STRUCTURE:
                break;
        }
    }

    private void dfs(T root) {
        if (G.isNodeIn(root)) return;
        if(dominator!=null&&G.isNodeIn(dominator))return;
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
        T[] nodes = G.get(root); //adjacency matrix of root at this term 所有子节点
        wasVisited[G.getIndexOfObject(root)] = true;
        path.push(root);
        for (int i = 0; i < nodes.length; i++) {
            T x = nodes[i];
            if (x == null) continue;
            int index = G.getIndexOfObject(x);
            if (maximumOutDegree == 0) maximumOutDegree = path.size() + 1;
            if (!wasVisited[index] && path.size() < maximumOutDegree) {
                dfs(nodes[i]);
            }
            if (i == nodes.length - 1) {
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

    public void djkstra() {

    }

    public boolean hasPathTo(T target) {
        if (G.isNodeIn(target)) return false;
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

    public LinkedList<List<T>> getAllPaths() {
        return allPaths;
    }

    public static void main(String[] args) {
        Long[] nodes = {0L, 1L, 2L, 0L, 3L, 4L, 5L};
        Long[] nodes0 = {1L, 2L, 3L, 4L};
        Long[] nodes1 = {0L, 2L, 3L};
        Long[] nodes2 = {0L,1L};
        Long[] nodes3 = {0L, 1L};
        Long[] node4 = {0L, 5L};
        Long[] node5 = {4L};
        GraphStructure<Long> graphStructure = new GraphStructure<>(nodes);
        int a = graphStructure.getIndexOfObject(0L);
        System.out.println(a);
        graphStructure.make(0L, nodes0);
        graphStructure.make(1L, nodes1);
        graphStructure.make(2L, nodes2);
        graphStructure.make(3L, nodes3);
        graphStructure.make(4L, node4);
        graphStructure.make(5L, node5);
        GraphSearch<Long> dfs = new GraphSearch<>(graphStructure, 0L, GraphSearch.Manipulate.DEPTH_FIRST, null, 4L, 10,null);
        /*GraphSearch<Long> bfs=new GraphSearch<>(graphStructure,2L,GraphSearch.Manipulate.BREADTH_FIRST,null,null,0);
        List<Long> path = bfs.pathTo(3L);
        System.out.println(path);*/
        List<Edge<Long>> eb = graphStructure.getEdges();
        System.out.println(eb);
        System.out.println(dfs.getAllPaths());
        List<Edge<Long>> edges = graphStructure.tailed();
        System.out.println(edges);
        System.out.println("--!!!!!!!-------------------------------------------------");
        System.out.println(dfs.getAllPaths());
        LinkedList<Object> lst = new LinkedList<>();
        List<Object> l0 = new ArrayList<>(Arrays.asList(nodes0));
        List<Object> l1 = new ArrayList<>(Arrays.asList(nodes1));
        //List<Object> l2=new ArrayList<>(Arrays.asList(nodes2));
        List<Object> l3 = new ArrayList<>(Arrays.asList(nodes3));
        lst.add(l0);
        lst.add(l1);
        //lst.add(l2);
        lst.add(l3);
        System.out.println("---------------------------------------------------");
        System.out.println(lst);

    }

}
