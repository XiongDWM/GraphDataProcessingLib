package org.xiongdwm.graphstructure;

import org.xiongdwm.graphstructure.discrete.WeightedNode;
import org.xiongdwm.graphstructure.utils.geometry.GeoAbstract;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class TestGeoAbstract extends GeoAbstract {
    @Override
    public WeightedNode<?> getEquilibriumPoint(Number x1, Number y1, Number x2, Number y2) {
        double y=Math.abs(y1.doubleValue()+y2.doubleValue())/2;
        double x=Math.abs(x1.doubleValue()+x2.doubleValue())/2;
        return new WeightedNode<>(x,y);
    }

    @Override
    public WeightedNode<?> getEquilibriumPoint(List<WeightedNode<?>> nodes) {
        double sumx=0d;
        double sumy=0d;
        for(WeightedNode<?> n:nodes){
            sumx+=n.getX().doubleValue();
            sumy+=n.getY().doubleValue();
        }
        int count=nodes.size();
        BigDecimal bigDecimal=new BigDecimal(sumx/count);
        double x=bigDecimal.setScale(3, RoundingMode.HALF_UP).doubleValue();
        bigDecimal=new BigDecimal(sumy/count);
        double y=bigDecimal.setScale(3, RoundingMode.HALF_UP).doubleValue();
        return new WeightedNode<>(x,y);
    }

    @Override
    public WeightedNode<?> getCoordinate(Number x, Number y, Double dis) {
        return null;
    }

    @Override
    public double calDis(Number x1, Number y1, Number x2, Number y2) {
        double h=Math.abs(y1.doubleValue()-y2.doubleValue());
        double w=Math.abs(x1.doubleValue()-x2.doubleValue());
        return h+w;
    }

    public double getDisSqrt(Number x1, Number y1, Number x2, Number y2) {
        return Math.sqrt(Math.pow(x1.doubleValue() - x2.doubleValue(), 2) + Math.pow(y1.doubleValue() - y2.doubleValue(), 2));
    }

    @Override
    public double calculateVariance(WeightedNode<?>[] points) {
        double meanX = 0.0;
        double meanY = 0.0;

        // Calculate means
        for (WeightedNode<?> point : points) {
            meanX += point.getX().doubleValue();
            meanY += point.getY().doubleValue();
        }
        meanX /= points.length;
        meanY /= points.length;

        // Calculate variance
        double variance = 0.0;
        for (WeightedNode<?> point : points) {
            variance += Math.pow(point.getX().doubleValue() - meanX, 2) + Math.pow(point.getY().doubleValue() - meanY, 2);
        }
        variance /= points.length;

        return variance;
    }
}
