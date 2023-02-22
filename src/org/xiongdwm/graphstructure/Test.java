package org.xiongdwm.graphstructure;

import org.xiongdwm.graphstructure.connectGraph.GraphSearch;
import org.xiongdwm.graphstructure.connectGraph.GraphStructure;
import org.xiongdwm.graphstructure.discrete.Cluster;
import org.xiongdwm.graphstructure.discrete.Node;
import org.xiongdwm.graphstructure.utils.geometry.GeoAbstract;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        //System.out.println(dfs.getMinConnection());
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
        List<Node<?>>listse=new ArrayList<>();
        Random random=new Random();
        BigDecimal bigDecimal;
        Node<Integer>nss;
        for(int i=0;i<10000;i++){
            bigDecimal= BigDecimal.valueOf(random.nextInt(1000) / 13.3);
            double x=bigDecimal.setScale(3, RoundingMode.HALF_UP).doubleValue();
            bigDecimal= BigDecimal.valueOf(random.nextInt(1000) / 13.3);
            double y=bigDecimal.setScale(3, RoundingMode.HALF_UP).doubleValue();
            nss=new Node<>(x,y);
            Integer id=i;
            nss.setUniqueTag(id);
            nss.setWeight(0);
            listse.add(nss);
        }
        Node<?>[] list= Stream
                .of(new Node<>(1d,2d),new Node<>(2d,5d),new Node<>(2d,10d),new Node<>(4d,9d),new Node<>(5d,8d),new Node<>(4d,3d),new Node<>(6d,4d),new Node<>(7d,5d),new Node<>(8d,4d))
                .toArray(Node<?>[]::new);
        Node<?>[] hs= listse.toArray(new Node<?>[0]);
        Cluster cluster =new Cluster(eg,hs);
        //cluster.t();
        Hashtable<Node<?>,List<Node<?>>>table= cluster.clustering();
        System.out.println(table.toString());

/*        int[] ar={1,2,3,4,5,6,7,13,15,613,312,321,21};
        int[] b=new int[ar.length];
        //System.arraycopy(a,0,b,0,a.length);
        //System.out.println(a==b);
        int c=2;
        boolean flag;
        do{
            flag=false;
            System.out.println(flag);
            System.arraycopy(ar,0,b,0,ar.length);
            for(int i=0;i<ar.length;i++){
                if(ar[i]%2==0){
                    ar[i]=-1;
                    flag=true;
                }
            }
            System.out.println(Arrays.toString(ar));
            System.out.println(Arrays.toString(b));
            //System.out.println("+1");
            System.out.println(flag);
            c--;
        }while (flag);
        System.out.println(ar==b);*/
    }
}
