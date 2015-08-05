package com.redhat.lightblue.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ContainerNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ArrayNode;

/**
 * A Projection expression
 */
public class Projection extends Expression {

    /**
     * Constructs a projection node from an array or object node
     */
    public Projection(ContainerNode node) {
        super(node);
    }

    private Projection(boolean arrayNode) {
        super(arrayNode);
    }

    /**
     * <pre>
     *   { field: <pattern>, include: <include>, recursive: <recursive> }
     * </pre>
     */
    public static Projection field(String pattern,
                                   boolean include,
                                   boolean recursive) {
        Projection p=new Projection(false);
        p.add("field",pattern).add("include",include).add("recursive",recursive);
        return p;
    }
    
    /**
     * <pre>
     *   { field: <pattern>, include: <include>, match: <query>, projection: <projection>, sort: <sort> }
     * </pre>
     */    
    public static Projection array(String pattern,
                                   Query match,
                                   boolean include,
                                   Projection projection,
                                   Sort sort) {
        Projection p=new Projection(false);
        p.add("field",pattern).add("include",include).add("match",match.toJson());
        if(projection!=null)
            p.add("projection",projection.toJson());
        if(sort!=null)
            p.add("sort",sort.toJson());
        return p;
    }
    
    /**
     * <pre>
     *   { field: <pattern>, include: <include>, range: [from,to], projection: <projection>, sort: <sort> }
     * </pre>
     */        
    public static Projection array(String pattern,
                                   int from,
                                   int to,
                                   boolean include,
                                   Projection projection,
                                   Sort sort) {
        Projection p=new Projection(false);
        ArrayNode a=JsonNodeFactory.instance.arrayNode();
        a.add(JsonNodeFactory.instance.numberNode(from));
        a.add(JsonNodeFactory.instance.numberNode(to));
        p.add("field",pattern).add("include",include).add("range",a);
        if(projection!=null)
            p.add("projection",projection.toJson());
        if(sort!=null)
            p.add("sort",sort.toJson());
        return p;
    }

    /**
     * <pre>
     *   [ projection ... ]
     * </pre>
     */
    public static Projection project(Projection...projection) {
        if(projection.length==1)
            return projection[0];
        else {
            Projection x=new Projection(true);
            for(Projection p:projection)
                ((ArrayNode)x.node).add(p.toJson());
            return x;
        }
    }

    /**
     * Returns a projection based on an array or object node
     */
    public static Projection project(ContainerNode node) {
        return new Projection(node);
    }

}

