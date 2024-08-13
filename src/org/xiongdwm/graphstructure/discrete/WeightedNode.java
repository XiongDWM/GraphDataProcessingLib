package org.xiongdwm.graphstructure.discrete;

public class WeightedNode<T>{
    private T uniqueTag;
    private Number x;
    private Number y;
    private Integer weight;


    public WeightedNode(){

    }

    public WeightedNode(T uniqueTag, Number x, Number y, Integer weight) {
        this.uniqueTag = uniqueTag;
        this.x = x;
        this.y = y;
        this.weight = weight;
    }

    public WeightedNode(T uniqueTag, Number x, Number y) {
        this.uniqueTag = uniqueTag;
        this.x = x;
        this.y = y;
    }

    public WeightedNode(Number x, Number y) {
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

    @Override
    public String toString() {
        return "Node{" +
                "uniqueTag=" + uniqueTag +
                ", x=" + x +
                ", y=" + y +
                ", weight=" + weight +
                '}';
    }
}
