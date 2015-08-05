package com.redhat.lightblue.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ContainerNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ArrayNode;

/**
 * A query expression
 */
public class Query extends Expression
    implements Update.UpdateQuery {

    public static final BinOp eq=BinOp.eq;
    public static final BinOp neq=BinOp.neq;
    public static final BinOp lt=BinOp.lt;
    public static final BinOp gt=BinOp.gt;
    public static final BinOp lte=BinOp.lte;
    public static final BinOp gte=BinOp.gte;

    public static final NaryOp in=NaryOp.in;
    public static final NaryOp nin=NaryOp.nin;

    public static final ArrOp any=ArrOp.any;
    public static final ArrOp all=ArrOp.all;
    public static final ArrOp non=ArrOp.none;

    public static final LogOp and=LogOp.and;
    public static final LogOp or=LogOp.or;
    
    /**
     * Constructs a query object from a json array or object
     */
    public Query(ContainerNode node) {
        super(node);
    }
    
    private Query(boolean arrayNode) {
        super(arrayNode);
    }

    /**
     * <pre>
     *   { field: <field>, op: <op>, rvalue: <value> }
     * </pre>
     */
    public static Query cmp(String field,
                            BinOp op,
                            Literal value) {
        Query q=new Query(false);
        q.add("field",field).add("op",op.toString()).add("rvalue",value.toJson());
        return q;
    }
    
    /**
     * <pre>
     *   { field: <field>, op: <op>, rvalue: <value> }
     * </pre>
     */
    public static Query cmp(String field,
                            BinOp op,
                            Object value) {
        return cmp(field,op,Literal.value(value));
    }

    /**
     * <pre>
     *   { field: <field>, op: <op>, rvalue: <value> }
     * </pre>
     */
    public static Query cmp(String field,
                            BinOp op,
                            int value) {
        return cmp(field,op,Literal.value(value));
    }

    /**
     * <pre>
     *   { field: <field>, op: <op>, rvalue: <value> }
     * </pre>
     */
    public static Query cmp(String field,
                            BinOp op,
                            long value) {
        return cmp(field,op,Literal.value(value));
    }

    /**
     * <pre>
     *   { field: <field>, op: <op>, rvalue: <value> }
     * </pre>
     */
    public static Query cmp(String field,
                            BinOp op,
                            double value) {
        return cmp(field,op,Literal.value(value));
    }

    /**
     * <pre>
     *   { field: <field>, op: <op>, rvalue: <value> }
     * </pre>
     */
    public static Query cmp(String field,
                            BinOp op,
                            boolean value) {
        return cmp(field,op,Literal.value(value));
    }

        /**
     * <pre>
     *   { field: <field>, op: <op>, rfield: <rfield> }
     * </pre>
     */
    public static Query cmpFields(String field,
                                  BinOp op,
                                  String rfield) {
        Query q=new Query(false);
        q.add("field",field).add("op",op.toString()).add("rfield",rfield);
        return q;
    }
    
    /**
     * <pre>
     *   { field: <field>, regex: <pattern>, ... }
     * </pre>
     */
    public static Query regex(String field,
                              String pattern,
                              boolean caseInsensitive,
                              boolean extended,
                              boolean multiline,
                              boolean dotall) {
        Query q=new Query(false);
        q.add("field",field).add("regex",pattern).
            add("caseInsensitive",caseInsensitive).
            add("extended",extended).
            add("multiline",multiline).
            add("dotall",dotall);
        return q;
    }
    
    /**
     * <pre>
     *   { field: <field>, op: <in/nin>, values: [ values ] }
     * </pre>
     */
    public static Query incl(String field,
                             NaryOp op,
                             Literal...values) {
        Query q=new Query(false);
        q.add("field",field).add("op",op.toString()).add("values",Literal.toJson(values));
        return q;
    }
    
    /**
     * <pre>
     *   { field: <field>, op: <in/nin>, rfield: <rfield> }
     * </pre>
     */
    public static Query inclField(String field,
                                  NaryOp op,
                                  String rfield) {
        Query q=new Query(false);
        q.add("field",field).add("op",op.toString()).add("rfield",rfield);
        return q;
    }
    
    /**
     * <pre>
     *   { array: <array>, contains: <op>, values: [values] }
     * </pre>
     */
    public static Query arrayContains(String array,
                                      ArrOp op,
                                      Literal...values) {
        Query q=new Query(false);
        q.add("array",array).add("contains",op.toString()).add("values",Literal.toJson(values));
        return q;
    }
    
    /**
     * <pre>
     *   { array: <array>, elemMatch:<x> }
     * </pre>
     */
    public static Query arrayMatch(String array,
                                   Query x) {
        Query q=new Query(false);
        q.add("array",array).add("elemMatch",x.toJson());
        return q;
    }
    
    /**
     * Return a query from a json node
     */
    public static Query query(ContainerNode query) {
        return new Query(query);
    }

    /**
     * <pre>
     *    { $not : { query } }
     *</pre>
     */
    public static Query not(Query query) {
        Query q=new Query(false);
        q.add("$not",query.toJson());
        return q;
    }

    /**
     * <pre>
     *    { $and : [ expressions ] }
     *</pre>
     */
    public static Query and(Query...expressions) {
        return logical(LogOp.and,expressions);
    }

    /**
     * <pre>
     *    { $or : [ expressions ] }
     *</pre>
     */
    public static Query or(Query...expressions) {
        return logical(LogOp.or,expressions);
    }

    /**
     * <pre>
     *    { $and : [ expressions ] }
     *    { $or : [ expressions ] }
     *</pre>
     */
    public static Query logical(LogOp op,Query...expressions) {
        Query q=new Query(true);
        for(Query x:expressions)
            ((ArrayNode)q.node).add(x.toJson());
        Query a=new Query(false);
        a.add(op.toString(),q.toJson());
        return a;
    }
}

