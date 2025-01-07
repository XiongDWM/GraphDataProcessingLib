package org.xiongdwm.graphstructure.discrete;

public class Node {
    private boolean wasVisited=false;
    private int groupId=-1;
    private Number x;
    private Number y;

    public Node() {
    }

    public Node(Number x, Number y) {
        this.x = x;
        this.y = y;
    }

    public boolean isWasVisited() {
        return wasVisited;
    }

    public void setWasVisited(boolean wasVisited) {
        this.wasVisited = wasVisited;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
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

    @Override
    public String toString() {
        return "DBNode{" +
                "x=" + this.x +
                ", y=" + this.y +
                ", group="+ this.groupId+
                '}';
    }
}
