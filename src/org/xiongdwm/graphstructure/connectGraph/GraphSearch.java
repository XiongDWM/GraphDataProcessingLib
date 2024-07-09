package org.xiongdwm.graphstructure.connectGraph;



import org.xiongdwm.graphstructure.exception.WrongMemberException;

import java.lang.reflect.Array;
import java.util.*;
import java.util.concurrent.*;

public class GraphSearch<T> {
    private final boolean[] wasVisited;
    private final T[] edgeTo;
    private final GraphStructure<T> G;
    private T theTarget;
//    private final ConcurrentLinkedQueue<List<T>> allPaths = new ConcurrentLinkedQueue<>();
    Set<List<T>> allPaths = Collections.synchronizedSet(new HashSet<>());
    private final Stack<T> path = new Stack<>(); //get a path 一条路径
    private int maximumRouteCount; // to limit the path length, 规定路径出度，也就时最多跳数
    private T dominator; // dominator 必经节点
    private final List<Edge<T>> minConnection = new ArrayList<>();
    private int weightLimit;
    private final Object lock=new Object();
    private ForkJoinPool forkJoinPool;
    private T gRoot;
//    private final List<CompletableFuture<Void>> allFutures = Collections.synchronizedList(new ArrayList<>());

    private static class NodeState<T> {
        T node;
        int weight;
        int depth;
        ConcurrentLinkedDeque<T> dfsStack;

        NodeState(T node, int weight, int depth, ConcurrentLinkedDeque<T> dfsStack) {
            this.node = node;
            this.weight = weight;
            this.depth = depth;
            this.dfsStack = dfsStack;
        }
    }

    public enum Manipulate {
        BREADTH_FIRST("广度优先"),
        DEPTH_FIRST("深度优先"),
        DJKSTRA("Djkstra"),
        PRIME("联通分量"),
        NONE_STRUCTURE("无结构");

        private final String text;

        Manipulate(String text) {
            this.text = text;
        }
    }

    @SuppressWarnings("unchecked")
    public GraphSearch(GraphStructure<T> G, T root, Manipulate manipulate, T[] nodesAbandon, T target, int maximumRouteCount, T dominator) {
        Class<?> clazz = root.getClass();
        this.gRoot = root;
        this.wasVisited = new boolean[G.getNodesNum()];
        this.edgeTo = (T[]) Array.newInstance(clazz, G.getNodesNum());
        this.maximumRouteCount = maximumRouteCount;
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
                dfsV0(root);
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
        for (int i = 0; i < G.getNodesNum(); ++i) {
            this.wasVisited[i] = false;
            this.edgeTo[i] = null;
        }
        switch (manipulate) {
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
    public GraphSearch(GraphStructure<T> G, T root, Manipulate manipulate, T[] nodesAbandon, T target, int maximumRouteCount, T dominator, int weightLimit) {
        Class<?> clazz = root.getClass();
        this.gRoot = root;
        this.wasVisited = new boolean[G.getNodesNum()];
        this.edgeTo = (T[]) Array.newInstance(clazz, G.getNodesNum());
        this.dominator = dominator;
        theTarget = target;
        this.maximumRouteCount = maximumRouteCount;
        this.G = G;
        this.weightLimit = weightLimit;
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
                forkJoinPool=new ForkJoinPool(32);
                break;
            case DJKSTRA:
                djkstra(root);
                break;
            case PRIME:
                prime(root);
                break;
            case NONE_STRUCTURE:
                break;
        }
    }

    public CompletableFuture<Void> startRetrieveNonRecursive() throws ExecutionException, InterruptedException {
        CompletableFuture<Void> allTasks = nonRecursiveDFS(gRoot);
        allTasks.thenRun(this::shutdownExecutorService).join(); // wait for all futures
        return allTasks;

    }

    public CompletableFuture<Void> startRetrieve() throws ExecutionException, InterruptedException {
        ConcurrentLinkedDeque<T>initial=new ConcurrentLinkedDeque<>();
        CompletableFuture<Void> allTasks = dfs(gRoot, 0, initial);
//        CompletableFuture<Void> allCompletableFuture = CompletableFuture.allOf(allFutures.toArray(new CompletableFuture[0]));
//        allTasks.thenRun(this::shutdownExecutorService).join(); // wait for all futures
        return allTasks;
    }

    public void shutdownExecutorService() {
        System.out.println("===================pool shutting down========================");
        forkJoinPool.shutdown(); // Disable new tasks from being submitted
        clear();
    }
    public void clear(){
        forkJoinPool=null;
    }

    private CompletableFuture<Void> dfs(T node, int currentWeight, ConcurrentLinkedDeque<T>dfsStack) {
        if (allPaths.size() >=maximumRouteCount)return CompletableFuture.completedFuture(null);
        if (node == null || G.isNodeNotIn(node)) return CompletableFuture.completedFuture(null);
//        if (currentDepth > maximumRouteCount &&!dfsStack.contains(theTarget)) return CompletableFuture.completedFuture(null);
        if (currentWeight > weightLimit &&!dfsStack.contains(theTarget)) return CompletableFuture.completedFuture(null);
        dfsStack.push(node); // push root

        if (node.equals(theTarget) && currentWeight <= weightLimit) {
            List<T> path = new ArrayList<>(dfsStack);
            Collections.reverse(path);
            allPaths.add(path);// add to path 转储路径
            System.out.println(path);
            return CompletableFuture.completedFuture(null);
        } else {
            List<CompletableFuture<Void>> futures = new ArrayList<>();
            for (T neighbor : G.get(node)) { // adjacency list of node at this term 所有子节点
                if (dfsStack.contains(neighbor))continue;
                int newWeight = currentWeight + G.getWeight(node, neighbor);
                if (newWeight <= weightLimit) {
                    ConcurrentLinkedDeque<T> newPath = new ConcurrentLinkedDeque<>(dfsStack); // copy of path stack创建路径栈的副本
                    CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
//                        System.out.println(Thread.currentThread().getName() + "================>>" + newPath.peek()+"->"+neighbor + "::" + newPath);
                        dfs(neighbor, newWeight, newPath).join();
                    }, forkJoinPool);
                    futures.add(future);
//                    allFutures.add(future);
                }
            }
            CompletableFuture<Void>allFuturesForNode= CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
            return allFuturesForNode.thenRun(dfsStack::pop);
        }
    }


    private CompletableFuture<Void> nonRecursiveDFS(T root) {
        Stack<NodeState<T>> stack = new Stack<>(); // param stack
        stack.push(new NodeState<>(root, 0, 0, new ConcurrentLinkedDeque<>()));
        CompletableFuture<Void> rootFuture = CompletableFuture.completedFuture(null);
        while (!stack.isEmpty()) {
            NodeState<T> currentState = stack.pop();
            T currentNode = currentState.node;
            int currentWeight = currentState.weight;
            int currentDepth = currentState.depth;
            ConcurrentLinkedDeque<T> dfsStack = currentState.dfsStack;

            if (currentNode == null || G.isNodeNotIn(currentNode)) continue;

            if (currentDepth > maximumRouteCount) continue;

            dfsStack.push(currentNode);
            if (currentNode.equals(theTarget) && currentWeight <= weightLimit) {
                List<T> path = new ArrayList<>(dfsStack);
                Collections.reverse(path);
                System.out.println(path);
                allPaths.add(path);
            } else {
                List<CompletableFuture<Void>> futures = new ArrayList<>();
                for (T neighbor : G.get(currentNode)) {
                    if (dfsStack.contains(neighbor)) continue;
                    int newWeight = currentWeight + G.getWeight(currentNode, neighbor);
                    if (newWeight <= weightLimit) {
                        ConcurrentLinkedDeque<T> newPath = new ConcurrentLinkedDeque<>(dfsStack);
                        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                            System.out.println(Thread.currentThread().getName() + "================>>" + newPath.peek()+"->"+neighbor + "::" + newPath);
                        }, forkJoinPool).thenRun(() -> {
                            stack.push(new NodeState<>(neighbor, newWeight, currentDepth + 1, newPath));
                        });
                        futures.add(future);
                        synchronized (lock){
//                            allFutures.add(future);
                        }
                    }
                }
                CompletableFuture<Void> allFuturesForNode = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
                try {
                    allFuturesForNode.get();  // Wait for all futures to complete before proceeding to the next node
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
            dfsStack.pop();
        }
        return rootFuture;
    }

    private void dfsV0(T root) {
        if (G.isNodeNotIn(root)) return;
        if (dominator != null && G.isNodeNotIn(dominator)) return;
        if (root.equals(theTarget)) {
            path.push(root);
            List<T> list = new ArrayList<>(path); //store path 转储
            if (dominator != null) {
                if (list.contains(dominator)) allPaths.add(list);
            } else {
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
                if (maximumRouteCount == 0 || path.size() < maximumRouteCount) dfsV0(nodes.get(i));
            }
            if (i == nodes.size() - 1) {
                path.pop();
                wasVisited[G.getIndexOfObject(root)] = false;
                return;
            }
        }
    }

    private void dfsV1(T root, int weight) {
        if (G.isNodeNotIn(root)) return;
        if (dominator != null && G.isNodeNotIn(dominator)) return;
        if (weight == weightLimit && !root.equals(theTarget)) return;
        if (root.equals(theTarget)) {
            if (weight > weightLimit) return;// check if weight is within limit
            path.push(root);
            List<T> list = new ArrayList<>(path); //store path 转储
            if (dominator != null) {
                if (list.contains(dominator)) allPaths.add(list);
            } else {
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
                int currentWeight = weight + G.getWeight(root, x);
                if (maximumRouteCount == 0 || path.size() < maximumRouteCount)
                    dfsV1(nodes.get(i), currentWeight); // pass weight to next node
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

    private void djkstra(T root) {
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

    private void prime(T root) {
        List<T> targets = G.get(root);
        int outDegree = targets.size();
        if (outDegree < 2) return;

        List<T> record = new ArrayList<>();
        record.add(root);
        for (T target : targets) {
            //either every single node has unless 1 path to its previous node or it cannot add to path set
            record.add(target);
            this.theTarget = root;
            this.dfsV0(target);
            int step = this.getAllPaths(false).size();
            if (step < 2) continue;
            minConnection.add(new Edge<>(root, target));
            if (record.contains(target)) continue;
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

    public List<Edge<T>> getMinConnection() {
        return minConnection;
    }

    public List<List<T>> getAllPaths(boolean sortedOrNot) {
        List<List<T>>paths=new LinkedList<>(allPaths);
        if (sortedOrNot) pathOrderByWeight(paths);
        return Collections.unmodifiableList(paths);
    }

    private void pathOrderByWeight(List<List<T>> p) {
        p.sort(new Comparator<List<T>>() {
            @Override
            public int compare(List<T> o1, List<T> o2) {
                int weight1 = 0;
                int weight2 = 0;
                for (int i = 0; i < o1.size() - 1; i++) {
                    weight1 += G.getWeight(o1.get(i), o1.get(i + 1));
                }
                for (int i = 0; i < o2.size() - 1; i++) {
                    weight2 += G.getWeight(o2.get(i), o2.get(i + 1));
                }
                return weight1 - weight2;
            }
        });
    }

    //        try {
//            if (!executorService.awaitTermination(30, TimeUnit.SECONDS)) { // Wait a while for existing tasks to terminate
//                executorService.shutdownNow(); // Cancel currently executing tasks
//                if (!executorService.awaitTermination(30, TimeUnit.SECONDS)) {
//                    System.err.println("ThreadPool did not terminate");
//                }
//            }
//        } catch (InterruptedException ie) {
//            executorService.shutdownNow();
//            Thread.currentThread().interrupt();
//        }
}
