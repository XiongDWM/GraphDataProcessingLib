package com.xiongdwm.graphstructure;


import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Stack;
import java.util.Vector;

public class DepthOrBreadthFirstSearch {
    private boolean[] wasVisited;
    private Object[] edgeTo;
    private final GraphStructure G;

    public enum Manipulate{
        BREADTH_FIRST("Breadth First"),
        DEPTH_FIRST("Depth First");

        private final String text;
        Manipulate(String text){
            this.text=text;
        }
        private String getName(){
            return text;
        }
    }

    public DepthOrBreadthFirstSearch(GraphStructure G, Object root, Manipulate manipulate){
        this.wasVisited=new boolean[G.getNodesNum()];
        this.edgeTo=new Object[G.getNodesNum()];
        this.G=G;
        for(int i=0;i<G.getNodesNum();i++){
            wasVisited[i]=false;
            edgeTo[i]=-1;
        }
        switch (manipulate){
            case BREADTH_FIRST:
                bfs(root);
                break;
            case DEPTH_FIRST:
                dfs(root);
                break;
        }
    }

    private void dfs(Object root){
        wasVisited[G.getIndexOfObject(root)]=true;
        for(Object o:G.get(root)){
            if(!wasVisited[G.getIndexOfObject(o)]){
                edgeTo[G.getIndexOfObject(o)]=root;
                dfs(o);
            }
        }
    }

    private void bfs(Object root){
        Queue<Integer> queue=new ArrayDeque<Integer>();
        queue.add(G.getIndexOfObject(root));
        wasVisited[G.getIndexOfObject(root)]=true;
        while(!queue.isEmpty()){
            int index=queue.poll();
            for(Object o:G.getRow(index)){
                if(!wasVisited[G.getIndexOfObject(o)]){
                    edgeTo[G.getIndexOfObject(o)]=G.getNodes()[index];
                    wasVisited[G.getIndexOfObject(o)]=true;
                    queue.add(G.getIndexOfObject(o));
                }
            }
        }
    }

    public boolean hasPathTo(Object target){
        return wasVisited[G.getIndexOfObject(target)];
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

}
