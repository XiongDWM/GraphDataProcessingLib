package org.xiongdwm.graphstructure.utils.geometry;

import org.xiongdwm.graphstructure.discrete.Node;

public abstract class GeoAbstract {
    public abstract Node<?,Number> getEquilibriumPoint(Number x1,Number y1,Number x2,Number y2);
    public abstract Node<?,Number> getCoordinate(Number x,Number y,Double dis);
}
