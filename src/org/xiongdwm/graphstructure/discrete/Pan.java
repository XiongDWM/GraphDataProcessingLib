package org.xiongdwm.graphstructure.discrete;

import org.xiongdwm.graphstructure.exception.WrongMemberException;
import org.xiongdwm.graphstructure.utils.geometry.GeoAbstract;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Pan {
    private final GeoAbstract geoMethods;
    private List<Node<?,Number>>nodes;
    private static int random_record=0;

    public Pan(GeoAbstract geoMethods,List<Node<?,Number>>nodes) {
        this.nodes=nodes;
        this.geoMethods = geoMethods;
    }

    public List<Node<?,Number>> randomPoints(int amount){
        if(0>=amount)throw new WrongMemberException("amount is a positive integer");
        int range=this.nodes.size();
        List<Node<?,Number>>result=new ArrayList<>();
        Random random=new Random();
        while(amount>0){
            int rn=random.nextInt(range); //number in [0,range)
            if(rn==random_record)continue;
            result.add(this.nodes.get(rn));
            random_record=rn;
            amount--;
        }
        return result;
    }

    public List<Node<?, Number>> getNodes() {
        return nodes;
    }

    public void setNodes(List<Node<?, Number>> nodes) {
        this.nodes = nodes;
    }
}
