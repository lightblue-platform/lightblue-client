package com.redhat.lightblue.client;

import java.util.List;

import com.fasterxml.jackson.databind.node.ContainerNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

/**
 * A sort expression
 */
public class Sort extends Expression {

    /**
     * Creates a sort node using the array or object node
     */
    public Sort(ContainerNode node) {
        super(node);
    }
    
    private Sort(boolean arrayNode) {
        super(arrayNode);
    }

    public static Sort sort(ContainerNode node) {
        return new Sort(node);
    }
    
    /**
     * <pre>
     *   { field: asc }
     * </pre>
     */
    public static Sort sort(String field,boolean asc) {
        Sort s=new Sort(false);
        s.add(field,asc?"$asc":"$desc");
        return s;
    }

    public static Sort asc(String field) {
        return sort(field,true);
    }

    public static Sort desc(String field) {
        return sort(field,false);
    }

    /**
     * <pre>
     *  [ sort,...]
     * </pre>
     */
    public static Sort sort(Sort...sort) {
        if(sort.length==1) {
            return sort[0];
        } else {
            Sort s=new Sort(true);
            for(Sort x:sort)
                ((ArrayNode)s.node).add(x.toJson());
            return s;
        }
    }

    public static Sort sort(List<Sort> sort) {
        if(sort.size()==1) {
            return sort.get(0);
        } else {
            Sort s=new Sort(true);
            for(Sort x:sort)
                ((ArrayNode)s.node).add(x.toJson());
            return s;
        }
    }
}

