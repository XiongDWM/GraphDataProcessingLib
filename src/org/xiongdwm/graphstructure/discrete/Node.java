package org.xiongdwm.graphstructure.discrete;

public class Node<T,Number>{
    private T uniqueTag;
    private Number x;
    private Number y;
    private Integer weight;

    public Node(){

    }

    public Node(T uniqueTag, Number x, Number y, Integer weight) {
        this.uniqueTag = uniqueTag;
        this.x = x;
        this.y = y;
        this.weight = weight;
    }

    public Node(T uniqueTag, Number x, Number y) {
        this.uniqueTag = uniqueTag;
        this.x = x;
        this.y = y;
    }

    public Node(Number x, Number y) {
        this.x = x;
        this.y = y;
    }

    public T getUniqueTag() {
        return uniqueTag;
    }

    public void setUniqueTag(T uniqueTag) {
        this.uniqueTag = uniqueTag;
    }

    public Number getX() {
        return x;
    }

    public void setX(Number x) {
        this.x = x;
    }

    public Number getY() {
        return y;
    }

    public void setY(Number y) {
        this.y = y;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
