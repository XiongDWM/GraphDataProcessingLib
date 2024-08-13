package org.xiongdwm.graphstructure.utils.geometry;

import org.xiongdwm.graphstructure.discrete.WeightedNode;

import java.util.List;

public abstract class GeoAbstract {
    public abstract WeightedNode<?> getEquilibriumPoint(Number x1, Number y1, Number x2, Number y2);
    public abstract WeightedNode<?> getEquilibriumPoint(List<WeightedNode<?>> nodes);
    public abstract WeightedNode<?> getCoordinate(Number x, Number y, Double dis);
    public abstract double calDis(Number x1,Number y1,Number x2,Number y2);
    public abstract double calculateVariance(WeightedNode<?>[] points);
}
