package org.xiongdwm.graphstructure;

import org.xiongdwm.graphstructure.connectGraph.GraphSearch;
import org.xiongdwm.graphstructure.connectGraph.GraphStructure;
import org.xiongdwm.graphstructure.discrete.Cluster;
import org.xiongdwm.graphstructure.discrete.Node;
import org.xiongdwm.graphstructure.utils.geometry.GeoAbstract;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Test {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        //test dfs with weight
        Long[] nodes = {0L, 1L, 2L, 0L, 3L, 4L, 5L};
        Long[] nodes0 = {1L};
        Long[] nodes1 = {0L, 2L, 3L,5L};
        Long[] nodes2 = {1L, 3L};
        Long[] nodes3 = {1L,2L, 4L};
        Long[] node4 = {3L, 5L};
        Long[] node5 = {1L, 4L};
        GraphStructure<Long> graphStructure = new GraphStructure<>(Long.class);
        graphStructure.make(0L, Arrays.asList(nodes0), new int []{1});
        graphStructure.make(1L, Arrays.asList(nodes1), new int []{1,1,6,0});
        graphStructure.make(2L, Arrays.asList(nodes2), new int []{1,1});
        graphStructure.make(3L, Arrays.asList(nodes3), new int []{0,1,5});
        graphStructure.make(4L, Arrays.asList(node4), new int []{5,5});
        graphStructure.make(5L, Arrays.asList(node5),new int []{0,5});
        graphStructure.init();
        GraphSearch<Long> dfs = new GraphSearch<Long>(graphStructure, 1L,GraphSearch.Manipulate.DEPTH_FIRST,null , 3L, 6, null,100);
        CompletableFuture<Void>c=dfs.startRetrieve();
        c.join();
        System.out.println(c.isDone());
        System.out.println(c.getNumberOfDependents());
        System.out.println(dfs.getAllPaths(true));
//        CompletableFuture<Void>c1=dfs.startRetrieveNonRecursive();
//        System.out.println(dfs.startRetrieve().isDone());

//        dfs.shutdownExecutorService();
        Map<String,Integer>tesMap=new HashMap<>();
        tesMap.put("1-b",1);
        tesMap.put("b-1",0);
        tesMap.put("2-b",1);
//        System.out.println(tesMap.get("3-0"));
//        System.out.println(dfs.getMinConnection());


    }
}
