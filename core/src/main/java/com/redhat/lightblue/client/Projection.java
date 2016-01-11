package com.redhat.lightblue.client;

import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ContainerNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;

/**
 * A Projection expression
 *
 * Usage:
 * <pre>
 * // { field: <pattern>, include: <include>, recursive: <recursive> }
 * Projection.field("*",true.true)
 * // include field, non-recursive
 * Projection.includeField("*")
 * // include field, recursive
 * Projection.includeFieldRecursively("*")
 * // exclude field, non-recursive
 * Projection.excludeField("*")
 * // exclude field, recursive
 * Projection.excludeFieldRecursively("*")
 *
 * // Array match projection
 * Projection.array("field",Query.withValue("x=1"),true,Projection.includeFieldRecursively("*"),Sort.asc("x"))
 * Projection.array("field",Query.withValue("x=1"),Projection.includeFieldRecursively("*"))
 * Projection.array("field",Query.withValue("x=1"),Sort.asc("x"))
 * Projection.array("field",Query.withValue("x=1"))
 *
 * // Array range
 * Projection.array("field",0,10,true,Projection.includeFieldRecursively("*"),Sort.asc("x"))
 * Projection.array("field",0,10,Projection.includeFieldRecursively("*"))
 * Projection.array("field",0,10,Sort.asc("x"))
 * Projection.array("field",0,10);
 *
 * // Projection lists
 * Projection.project(p1,p2,...)
 *
 * List<Projection> l=new List<>();
 * .../
 * Projection.project(l)
 *
 * // Literal projection
 * Projection.project("{\"field\":\"*\"}");
 * </pre>
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

    public static Projection includeField(String pattern) {
        return field(pattern,true,false);
    }

    public static Projection excludeField(String pattern) {
        return field(pattern,false,false);
    }

    public static Projection includeFieldRecursively(String pattern) {
        return field(pattern,true,true);
    }

    public static Projection excludeFieldRecursively(String pattern) {
        return field(pattern,false,true);
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
        if(projection!=null) {
            p.add("projection",projection.toJson());
        }
        if(sort!=null) {
            p.add("sort",sort.toJson());
        }
        return p;
    }

    public static Projection array(String pattern,
                                   Query match,
                                   Projection projection) {
        return array(pattern,match,true,projection,null);
    }

    public static Projection array(String pattern,
                                   Query match,
                                   Sort sort) {
        return array(pattern,match,true,null,sort);
    }

    public static Projection array(String pattern,
                                   Query match) {
        return array(pattern,match,true,null,null);
    }

    /**
     * <pre>
     *   { field: <pattern>, include: <include>, range: [from,to], projection: <projection>, sort: <sort> }
     * </pre>
     */
    public static Projection array(String pattern,
                                   Integer from,
                                   Integer to,
                                   boolean include,
                                   Projection projection,
                                   Sort sort) {
        Projection p=new Projection(false);
        ArrayNode a=JsonNodeFactory.instance.arrayNode();
        a.add(JsonNodeFactory.instance.numberNode(from));
        if(to!=null) {
            a.add(JsonNodeFactory.instance.numberNode(to));
        } else {
            a.add(JsonNodeFactory.instance.nullNode());
        }
        p.add("field",pattern).add("include",include).add("range",a);
        if(projection!=null) {
            p.add("projection",projection.toJson());
        }
        if(sort!=null) {
            p.add("sort",sort.toJson());
        }
        return p;
    }

    public static Projection array(String pattern,
                                   Integer from,
                                   Integer to,
                                   Projection projection) {
        return array(pattern,from,to,true,projection,null);
    }

    public static Projection array(String pattern,
                                   Integer from,
                                   Integer to,
                                   Sort sort) {
        return array(pattern,from,to,true,null,sort);
    }

    public static Projection array(String pattern,
                                   Integer from,
                                   Integer to) {
        return array(pattern,from,to,true,null,null);
    }

    /**
     * <pre>
     *   [ projection ... ]
     * </pre>
     */
    public static Projection project(Projection...projection) {
        if(projection.length==1) {
            return projection[0];
        } else {
            Projection x=new Projection(true);
            for(Projection p:projection) {
                x.addToArray(p.toJson());
            }
            return x;
        }
    }

    public static Projection project(List<? extends Projection> projection) {
        if(projection.size()==1) {
            return projection.get(0);
        } else {
            Projection x=new Projection(true);
            for(Projection p:projection) {
                x.addToArray(p.toJson());
            }
            return x;
        }
    }

    /**
     * Adds p into this array projection
     */
    private void addToArray(JsonNode j) {
        if(j instanceof ArrayNode) {
            for(Iterator<JsonNode> itr=((ArrayNode)j).elements();itr.hasNext();) {
                addToArray(itr.next());
            }
        } else {
            ((ArrayNode)node).add(j);
        }
    }


    /**
     * Returns a projection based on an array or object node
     */
    public static Projection project(ContainerNode node) {
        return new Projection(node);
    }

}

