package org.xiongdwm.graphstructure.discrete;


public class ContextNode {
    private Long id;
    private boolean isPoi;
    private double x;
    private double y;
    private int groupId;
    private Long fiberId;

    public double get(boolean compareAxis) {
        if(compareAxis)return x;
        return y;
    }

    public ContextNode() {
    }

    public ContextNode(Long id, double x, double y) {
        this.id = id;
        this.x = x;
        this.y = y;
    }

    public ContextNode(Long id, boolean isPoi, double x, double y, int groupId, Long fiberId) {
        this.id = id;
        this.isPoi = isPoi;
        this.x = x;
        this.y = y;
        this.groupId = groupId;
        this.fiberId = fiberId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public boolean isPoi() {
        return isPoi;
    }

    public void setPoi(boolean poi) {
        isPoi = poi;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public Long getFiberId() {
        return fiberId;
    }

    public void setFiberId(Long fiberId) {
        this.fiberId = fiberId;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof ContextNode){
            ContextNode node = (ContextNode) obj;
            return node.getId().equals(this.id)&&node.isPoi()==this.isPoi();
        }
        return false;
    }

    @Override
    public String toString() {
        return "PipeNode{" +
                "id=" + id +
                ", isPoi=" + isPoi +
                ", x=" + x +
                ", y=" + y +
                ", groupId=" + groupId +
                ", fiberId=" + fiberId +
                '}';
    }
}
