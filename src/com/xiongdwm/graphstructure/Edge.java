package com.xiongdwm.graphstructure;

import java.util.Objects;

public class Edge<T> {
    private T vertex1;
    private T vertex2;

    public Edge(){

    }

    public Edge(T vertex1, T vertex2) {
        this.vertex1 = vertex1;
        this.vertex2 = vertex2;
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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (vertex1.hashCode() + vertex2.hashCode())>>>32;
        return result;

    }

    @Override
    public boolean equals(Object obj) {
        if(this==obj)return true;
        if(obj == null || getClass() != obj.getClass())return false;
        Edge<?> that=(Edge<?>) obj;
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
}
