package org.xiongdwm.graphstructure;

import org.xiongdwm.graphstructure.discrete.Node;
import org.xiongdwm.graphstructure.utils.geometry.GeoAbstract;

import java.util.List;

public class TestGeoAbstract extends GeoAbstract {
    @Override
    public Node<?, Number> getEquilibriumPoint(Number x1, Number y1, Number x2, Number y2) {
        System.out.println(1);
        return null;
    }

    @Override
    public Node<?, Number> getEquilibriumPoint(List<Node<?, Number>> nodes) {
        return null;
    }

    @Override
    public Node<?, Number> getCoordinate(Number x, Number y, Double dis) {
        return null;
    }

    @Override
    public double calDis(Number x1, Number y1, Number x2, Number y2) {
        return 0;
    }
}
