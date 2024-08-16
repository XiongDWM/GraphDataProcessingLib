package org.xiongdwm.graphstructure.discrete;


import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class KDTree {
    private KDTreeNode root;

    public KDTree() {
    }

    public KDTree(List<ContextNode> points) {


        if (points != null && !points.isEmpty()) {
            root = buildTree(points, 0, points.size(), true); // Initially split by X-axis
        }
    }

    private KDTreeNode buildTree(List<ContextNode> points, int start, int end, boolean splitByX) {
        if (start >= end) {
            return null;
        }

        points.subList(start, end).sort(Comparator.comparingDouble(p -> p.get(splitByX))); // true for X-axis, false for Y-axis
        int medianIndex = (start + end) / 2;
        ContextNode medianPoint = points.get(medianIndex);
        KDTreeNode node = new KDTreeNode(medianPoint, splitByX);

        node.setLeft(buildTree(points, start, medianIndex, !splitByX));
        node.setRight( buildTree(points, medianIndex + 1, end, !splitByX));

        return node;
    }

    public ContextNode nearest(ContextNode target) {
        return nearestSearch(root, target, Double.MAX_VALUE).getPoint();
    }

    private KDTreeNode nearestSearch(KDTreeNode node, ContextNode target, double bestDistSq) {
        if (node == null) {
            return null;
        }

        double d = distanceSquared(node.getPoint(), target);
        KDTreeNode bestNode = node;

        if (d < bestDistSq) {
            bestDistSq = d;
        }

        double dx = target.getX() - node.getPoint().getX();
        double dy = target.getY() - node.getPoint().getY();
        double splitAxisDistSq;
        KDTreeNode nearBranch, farBranch;

        if (node.splitByX) {
            splitAxisDistSq = dx * dx;
            nearBranch = (dx > 0) ? node.getRight() : node.getLeft();
            farBranch = (dx > 0) ? node.getLeft() : node.getRight();
        } else {
            splitAxisDistSq = dy * dy;
            nearBranch = (dy > 0) ? node.getRight() : node.getLeft();
            farBranch = (dy > 0) ? node.getLeft() : node.getRight();
        }

        KDTreeNode bestNearBranchNode = nearestSearch(nearBranch, target, bestDistSq);
        if (bestNearBranchNode != null && distanceSquared(bestNearBranchNode.getPoint(), target) < bestDistSq) {
            bestNode = bestNearBranchNode;
            bestDistSq = distanceSquared(bestNode.getPoint(), target);
        }

        if (splitAxisDistSq < bestDistSq) {
            KDTreeNode bestFarBranchNode = nearestSearch(farBranch, target, bestDistSq);
            if (bestFarBranchNode != null && distanceSquared(bestFarBranchNode.getPoint(), target) < bestDistSq) {
                bestNode = bestFarBranchNode;
            }
        }

        return bestNode;
    }

    public double distanceSquared(ContextNode p1, ContextNode p2) {
        double dx = p1.getX() - p2.getX();
        double dy = p1.getY() - p2.getY();
        return dx * dx + dy * dy;
    }

    //recursively clear the tree
    public void clear() {
        root = null;
    }


    //in order, left, root, right
    public List<ContextNode> inOrder(KDTreeNode node) {
        if (node==null)return Collections.emptyList();
        LinkedList<ContextNode> list = new LinkedList<>(inOrder(node.getLeft()));
        list.add(node.getPoint());
        list.addAll(inOrder(node.getRight()));
        return list;
    }

    //pre order, root, left, right
    public List<ContextNode> preOrder(KDTreeNode node) {
        if (node==null)return Collections.emptyList();
        LinkedList<ContextNode> list= new LinkedList<>();
        list.add(node.getPoint());
        list.addAll(postOrder(node.getLeft()));
        list.addAll(postOrder(node.getRight()));
        return list;
    }

    //post order, left, right, root
    public List<ContextNode> postOrder(KDTreeNode node) {
        if(node==null)return Collections.emptyList();
        LinkedList<ContextNode> list= new LinkedList<>();
        list.addAll(postOrder(node.getLeft()));
        list.addAll(postOrder(node.getRight()));
        list.add(node.getPoint());
        return list;
    }

    public int getTreeHeight(KDTreeNode node) {
        if (node == null) {
            return 0;
        } else {
            int leftHeight = getTreeHeight(node.getLeft());
            int rightHeight = getTreeHeight(node.getRight());
            return Math.max(leftHeight, rightHeight) + 1;
        }
    }


    public ContextNode getRootPoints() {
        return root.getPoint();
    }

    public KDTreeNode getRoot() {
        return root;
    }

    // group1{1，1；2，2；3，1;4,5;5,2.3}    group2{6L,4,7;7L,4,5;8L,4,0.8;9L,5,2}
    public static void main(String[] args) {
        //将{1L,1，1；2L,2，2；3L,3，1;4L,4,5;5L,5,2.3} 放入一个pipenode的集合
        List<ContextNode> points = Stream.of(
                new ContextNode(5L, 5, 2.3),
                new ContextNode(1L, 1, 1),
                new ContextNode(2L, 2, 2),
                new ContextNode(3L, 3, 1),
                new ContextNode(4L, 4, 5)

        ).collect(Collectors.toList());
        //将第二个points表 {6L,4,7;7L,4,5;8L,4,0.8;9L,5,2}放入一个pipenode的集合
        List<ContextNode> points2 = Stream.of(
                new ContextNode(6L, 4, 7),
                new ContextNode(7L, 4, 5),
                new ContextNode(8L, 4, 0.8),
                new ContextNode(9L, 5, 2)
        ).collect(Collectors.toList());

        double min = Double.MAX_VALUE;
        ContextNode nearestFromPoints = null;
        ContextNode nearestFromPoints2 = null;

        // points 创建kdtree
        KDTree tree = new KDTree(points);
        //循环points2 找到points2和points之间最近的一组点
        for (ContextNode point : points2) {
            ContextNode nearest = tree.nearest(point);
            double distance = tree.distanceSquared(nearest, point);
            if (distance < min) {
                min = distance;
                nearestFromPoints = nearest;
                nearestFromPoints2 = point;
            }
        }
        System.out.println("closest point between p1 and p2: " + nearestFromPoints + " and " + nearestFromPoints2 + " distance: " + min);
        System.out.println("postOrder: " + tree.postOrder(tree.getRoot()));
        System.out.println("preOrder: " + tree.preOrder(tree.getRoot()));
        System.out.println("inOrder: " + tree.inOrder(tree.getRoot()));
    }
}
