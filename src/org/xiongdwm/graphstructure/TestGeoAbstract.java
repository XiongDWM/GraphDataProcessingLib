package org.xiongdwm.graphstructure;

import org.xiongdwm.graphstructure.discrete.Node;
import org.xiongdwm.graphstructure.utils.geometry.GeoAbstract;

import java.util.List;

public class TestGeoAbstract extends GeoAbstract {
    @Override
    public Node<?> getEquilibriumPoint(Number x1, Number y1, Number x2, Number y2) {
        double y=Math.abs(y1.doubleValue()+y2.doubleValue())/2;
        double x=Math.abs(x1.doubleValue()+x2.doubleValue())/2;
        return new Node<>(x,y);
    }

    @Override
    public Node<?> getEquilibriumPoint(List<Node<?>> nodes) {
        double sumx=0d;
        double sumy=0d;
        for(Node<?>n:nodes){
            sumx+=n.getX().doubleValue();
            sumy+=n.getY().doubleValue();
        }
        int count=nodes.size();
        return new Node<>(sumx/count,sumy/count);
    }

    @Override
    public Node<?> getCoordinate(Number x, Number y, Double dis) {
        return null;
    }

    @Override
    public double calDis(Number x1, Number y1, Number x2, Number y2) {
        double h=Math.abs(y1.doubleValue()-y2.doubleValue());
        double w=Math.abs(x1.doubleValue()-x2.doubleValue());
        return h+w;
    }
}
