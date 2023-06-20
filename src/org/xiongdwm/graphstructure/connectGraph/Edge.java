package org.xiongdwm.graphstructure.connectGraph;

import java.util.Objects;

public class Edge<T> {
    private T vertex1;
    private T vertex2;
    private int weight = 1;

    public Edge(){

    }

    public Edge(T vertex1, T vertex2) {
        this.vertex1 = vertex1;
        this.vertex2 = vertex2;
    }
    //constructor with weight  
    public Edge(T vertex1, T vertex2, int weight) {
        this.vertex1 = vertex1;
        this.vertex2 = vertex2;
        this.weight = weight;
    }

    public Object getVertex1() {
        return vertex1;
    }

    public void setVertex1(T vertex1) {
        this.vertex1 = vertex1;
    }

    public Object getVertex2() {
        return vertex2;
    }

    public void setVertex2(T vertex2) {
        this.vertex2 = vertex2;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        int a=0;
        int b=0;
        if(vertex1!=null)a=vertex1.hashCode();
        if(vertex2!=null)b=vertex2.hashCode();
        result = prime * result + (a + b)>>>32;
        return result;

    }

    @Override
    public boolean equals(Object obj) {
        if(this==obj)return true;
        if(obj == null || getClass() != obj.getClass())return false;
        Edge<?> that=(Edge<?>) obj;
        if(that.getVertex1()==null&&that.getVertex2()==null||vertex1==null&&vertex2==null)return false;
        if(that.getVertex1().getClass()==vertex2.getClass()&&Objects.equals(that.vertex1,vertex2)&&Objects.equals(that.vertex2,vertex1)){
            return true;
        }

        return Objects.equals(that.getVertex1(),vertex1) && Objects.equals(that.getVertex2(),vertex2);
    }

    @Override
    public String toString() {
        return "Edge{" +
                "vertex1=" + vertex1 +
                ", vertex2=" + vertex2 +
                '}';
    }

    public Edge<T> reverse() {
        return new Edge<>(vertex2,vertex1);
    }

}
