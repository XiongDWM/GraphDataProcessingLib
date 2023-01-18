package org.xiongdwm.graphstructure;

import org.xiongdwm.graphstructure.connectGraph.GraphSearch;
import org.xiongdwm.graphstructure.connectGraph.GraphStructure;
import org.xiongdwm.graphstructure.discrete.Pan;
import org.xiongdwm.graphstructure.utils.geometry.GeoAbstract;

import java.util.ArrayList;

public class Test {
    public static void main(String[] args) {
        Long[] nodes = {0L, 1L, 2L, 0L, 3L, 4L, 5L};
        Long[] nodes0 = {1L};
        Long[] nodes1 = {0L, 2L, 5L};
        Long[] nodes2 = {1L,3L};
        Long[] nodes3 = {2L, 4L};
        Long[] node4 = {3L, 5L};
        Long[] node5 = {1L,4L};
        GraphStructure<Long> graphStructure = new GraphStructure<>(nodes);
        int a = graphStructure.getIndexOfObject(0L);
        //System.out.println(a);
        graphStructure.make(0L, nodes0);
        graphStructure.make(1L, nodes1);
        graphStructure.make(2L, nodes2);
        graphStructure.make(3L, nodes3);
        graphStructure.make(4L, node4);
        graphStructure.make(5L, node5);
        GraphSearch<Long> dfs = new GraphSearch<>(graphStructure, 1L, GraphSearch.Manipulate.PRIME, null, 5L, 10,null);
        //System.out.println(dfs.getAllPaths().size());
        System.out.println(dfs.getMinConnection());
        /*        *//*GraphSearch<Long> bfs=new GraphSearch<>(graphStructure,2L,GraphSearch.Manipulate.BREADTH_FIRST,null,null,0);
        List<Long> path = bfs.pathTo(3L);
        System.out.println(path);*//*
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
        System.out.println(lst);*/
        GeoAbstract eg=new TestGeoAbstract();
        Pan pan=new Pan(eg,new ArrayList<>());

    }
}
