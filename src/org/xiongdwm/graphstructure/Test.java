package org.xiongdwm.graphstructure;

import org.xiongdwm.graphstructure.connectGraph.GraphSearch;
import org.xiongdwm.graphstructure.connectGraph.GraphStructure;
import org.xiongdwm.graphstructure.discrete.Cluster;
import org.xiongdwm.graphstructure.discrete.Node;
import org.xiongdwm.graphstructure.utils.geometry.GeoAbstract;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Test {
    public static void main(String[] args) {
        //test dfs with weight
        Long[] nodes = {0L, 1L, 2L, 0L, 3L, 4L, 5L};
        Long[] nodes0 = {1L};
        Long[] nodes1 = {0L, 2L, 3L,5L};
        Long[] nodes2 = {1L, 3L};
        Long[] nodes3 = {1L,2L, 4L};
        Long[] node4 = {3L, 5L};
        Long[] node5 = {1L, 4L};
        GraphStructure<Long> graphStructure = new GraphStructure<>(nodes);
        graphStructure.make(0L, nodes0, new int []{1});
        graphStructure.make(1L, nodes1, new int []{1,1,6,0});
        graphStructure.make(2L, nodes2, new int []{1,1});
        graphStructure.make(3L, nodes3, new int []{0,1,5});
        graphStructure.make(4L, node4, new int []{5,5});
        graphStructure.make(5L, node5,new int []{0,5});
        GraphSearch<Long> dfs = new GraphSearch<Long>(graphStructure, 1L,GraphSearch.Manipulate.DEPTH_FIRST,null , 3L, 0, null,11);
        System.out.println(dfs.getAllPaths(true));

        Map<String,Integer>tesMap=new HashMap<>();
        tesMap.put("1-b",1);
        tesMap.put("b-1",0);
        tesMap.put("2-b",1);
        System.out.println(tesMap.get("3-0"));
        //System.out.println(dfs.getMinConnection());



    }
}
