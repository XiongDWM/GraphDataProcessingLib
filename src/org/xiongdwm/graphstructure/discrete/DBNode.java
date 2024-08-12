package org.xiongdwm.graphstructure.discrete;

public  class DBNode {
    private Number x;
    private Number y;
    private boolean wasVisited=false;
    private int groupId=-1;

    public DBNode() {
    }

    public DBNode(Number x, Number y) {
        this.x = x;
        this.y = y;
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

    @Override
    public String toString() {
        return "DBNode{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
