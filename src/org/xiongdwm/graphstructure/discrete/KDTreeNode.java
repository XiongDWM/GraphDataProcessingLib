package org.xiongdwm.graphstructure.discrete;


public class KDTreeNode{
    private ContextNode point;
    private KDTreeNode left;
    private KDTreeNode right;
    boolean splitByX;

    public KDTreeNode() {
    }

    public KDTreeNode(ContextNode point) {
        this.point = point;
    }

    public KDTreeNode(ContextNode point, boolean splitByX) {
        this.point = point;
        this.splitByX = splitByX;
    }

    public ContextNode getPoint() {
        return point;
    }

    public void setPoint(ContextNode point) {
        this.point = point;
    }

    public KDTreeNode getLeft() {
        return left;
    }

    public void setLeft(KDTreeNode left) {
        this.left = left;
    }

    public KDTreeNode getRight() {
        return right;
    }

    public void setRight(KDTreeNode right) {
        this.right = right;
    }

    public boolean isSplitByX() {
        return splitByX;
    }

    public void setSplitByX(boolean splitByX) {
        this.splitByX = splitByX;
    }

    public static void main(String[] args) {
//        for (float y = 1.5f; y > -1.5f; y -= 0.1f) {
//            for (float x = -1.5f; x < 1.5f; x += 0.05f) {
//                float a = x * x + y * y - 1;
//                if ((a * a * a - x * x * y * y * y) <= 0.0f) {
//                    System.out.print("*");
//                } else {
//                    System.out.print(" ");
//                }
//            }
//            System.out.println();
//        }
        System.out.println();
    }
}
