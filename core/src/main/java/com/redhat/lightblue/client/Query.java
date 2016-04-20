package com.redhat.lightblue.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ContainerNode;

/**
 * A query expression
 *
 * Usage:
 * <pre>
 * // { field: x, op: =, rvalue: "value" }
 * Query.withValue("x",Query.eq,"value")
 * Query.withValue("x",Query.eq,Literal.value("value"))
 * Query.withValue("x = value")
 *
 * // { field:x op:=, rfield: y }
 * Query.withField("x",Query.eq,"y")
 * Query.withField("x=y")
 * 
 * // { field:x, op:$in, values=[1,2,3] }
 * Query.withValues("x",Query.in,Literal.values(1,2,3))
 * Query.withValues("x $in [1,2,3]")
 *
 * // { field:x, op:$in, rfield:y
 * Query.withFieldValues("x",Query.in,"y")
 * Query.withFieldValues("x $in y")
 *
 * // { field:x, regex: pattern, caseInsensitive: true, extended: false, multiline: false, dotall:true  }
 * Query.regex(x,pattern,true,false,false,true);
 * Query.regex(x,pattern,Query.CASE_INSENSITIVE|Query.DOTALL)
 *
 * // { array: <array>, contains: <op>, values: [values] }
 * Query.arrayContains("array",Query.all,Literal.values(1,2,3))
 *
 * // { array: <array>, elemMatch:{field:x, op:=, rvalue:1 } }
 * Query.arrayMatch("array",Query.withValue("x=1"))
 *
 * // Logical connectors:
 * Query.and(Query.withValue("x=1"),Query.withValue("y=2"),Query.not(Query.withValue("z=3")))
 * Query.or(Query.withValue("x=1"),Query.withValue("y=2"),Query.not(Query.withValue("z=3")))
 * Query.logical(Query.and,Query.withValue("x=1"),Query.withValue("y=2"),Query.not(Query.withValue("z=3")))
 * 
 * // Literal query:
 * Query.query("{ \"field\":\"x\", \"op\":\"=\", \"rvalue\":1}")
 * 
 * </pre>
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

    public static final int CASE_INSENSITIVE=1;
    public static final int EXTENDED=2;
    public static final int MULTILINE=4;
    public static final int DOTALL=8;

    public enum BinOp {
        eq("="),
        neq("!="),
        lt("<"),
        gt(">"),
        lte("<="),
        gte(">=");
        
        private String s;
        
        private BinOp(String s) {
            this.s=s;
        }
        
        public String toString() {
            return s;
        }

        public static BinOp getOp(String x) {
            for(BinOp v:values())
                if(v.toString().equals(x))
                    return v;
            return null;
        }
    }

    public enum NaryOp {
        in("$in"),
        nin("$nin");
        
        private String s;
        
        private NaryOp(String s) {
            this.s=s;
        }
        
        @Override
        public String toString() {
            return s;
        }

        public static NaryOp getOp(String x) {
            for(NaryOp v:values())
                if(v.toString().equals(x))
                    return v;
            return null;
        }
    }

    public enum ArrOp {
        any("$any"),
        all("$all"),
        none("$none");
        
        private String s;
        
        private ArrOp(String s) {
            this.s=s;
        }
        
        @Override
        public String toString() {
            return s;
        }
    }

    public enum LogOp {
        and("$and"),
        or("$or");
        
        private String s;
        
        private LogOp(String s) {
            this.s=s;
        }
        
        @Override
        public String toString() {
            return s;
        }
    }

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
    public static Query withValue(String field,
                                  BinOp op,
                                  Literal value) {
        Query q=new Query(false);
        q.add("field",field).add("op",op.toString()).add("rvalue",value.toJson());
        return q;
    }
    
    /**
     * <pre>
     *   { field: <field>, regex: <pattern>, caseInsensitive: <caseInsensitive>, ... }
     * </pre>
     */
    public static Query withMatchingString(String field, String value, boolean caseInsensitive) {
        return caseInsensitive ? regex(field, escapeRegExPattern(value), caseInsensitive, false, false, false) : withValue(field, eq, value);
    }

    /**
     * <pre>
     *   { field: <field>, regex: <^string$>, caseInsensitive: <caseInsensitive>, ... }
     * </pre>
     */
    public static Query withString(String field, String value, boolean caseInsensitive) {
        return caseInsensitive ? regex(field, "^"+escapeRegExPattern(value)+"$", caseInsensitive, false, false, false) : withValue(field, eq, value);
    }

    /**
     * <pre>
     *   { field: <field>, regex: <^string$>, caseInsensitive: true, ... }
     * </pre>
     */
    public static Query withStringIgnoreCase(String field, String value) {
        return Query.withString(field, value, true);
    }

    /**
     * <pre>
     *   { "$or": [{ field: <field>, regex: <^string$>, caseInsensitive: <caseInsensitive>, ... }, ... ]}
     * </pre>
     */
    public static Query withStrings(String field, String[] values, boolean caseInsensitive) {
        if (caseInsensitive) {
            List<Query> regexList = new ArrayList<Query>();
            for (String value: values) {
                regexList.add(withString(field, value, true));
            }
            return Query.or(regexList);
        } else {
            return Query.withValues(field, Query.in, Literal.values(values));
        }
    }
    
    /**
     * <pre>
     *   { "$or": [{ field: <field>, regex: <^string$>, caseInsensitive: true, ... }, ... ]}
     * </pre>
     */
    public static Query withStringsIgnoreCase(String field, String[] values) {
        return Query.withStrings(field, values, true);
    }

    /**
     * <pre>
     *   { field: <field>, op: <op>, rvalue: <value> }
     * </pre>
     */
    public static Query withValue(String field,
                                  BinOp op,
                                  Object value) {
        return withValue(field,op,Literal.value(value));
    }
    
    /**
     * <pre>
     *   { field: <field>, op: <op>, rvalue: <value> }
     * </pre>
     */
    public static Query withValue(String field,
                                  BinOp op,
                                  int value) {
        return withValue(field,op,Literal.value(value));
    }

    /**
     * <pre>
     *   { field: <field>, op: <op>, rvalue: <value> }
     * </pre>
     */
    public static Query withValue(String field,
                                  BinOp op,
                                  long value) {
        return withValue(field,op,Literal.value(value));
    }

    /**
     * <pre>
     *   { field: <field>, op: <op>, rvalue: <value> }
     * </pre>
     */
    public static Query withValue(String field,
                                  BinOp op,
                                  double value) {
        return withValue(field,op,Literal.value(value));
    }

    /**
     * <pre>
     *   { field: <field>, op: <op>, rvalue: <value> }
     * </pre>
     */
    public static Query withValue(String field,
                                  BinOp op,
                                  boolean value) {
        return withValue(field,op,Literal.value(value));
    }
    
    /**
     * <pre>
     *   { field: <field>, op: <op>, rfield: <rfield> }
     * </pre>
     */
    public static Query withField(String field,
                                  BinOp op,
                                  String rfield) {
        Query q=new Query(false);
        q.add("field",field).add("op",op.toString()).add("rfield",rfield);
        return q;
    }

    /**
     * <pre>
     *   { field:<field>, op:<op>, rvalue:<value> }
     *   { field:<field>, op:<op>, values:[values] }
     * </pre>
     */
    public static Query withValue(String expression) {
        String[] parts=split(expression);
        if(parts!=null) {
            String field = parts[0];
            String operator = parts[1];
            String value = parts[2];

            BinOp binOp=BinOp.getOp(operator);
            if (binOp!=null) {
                return withValue(field,binOp,value);
            }
            NaryOp naryOp=NaryOp.getOp(operator);
            if (naryOp!=null) {
                Literal[] values = Literal.values(value.substring(1, value.length() - 1).split("\\s*,\\s*"));
                return withValues(field,naryOp,values);
            } 
        }
        throw new IllegalArgumentException("'" + expression + "' is incorrect");
    }
    
    /**
     * <pre>
     *   { field:<field>, op:<op>, rfield:<rfield> }
     * </pre>
     */
    public static Query withField(String expression) {
        String[] parts=split(expression);
        if(parts!=null) {
            String field = parts[0];
            String operator = parts[1];
            String rfield = parts[2];

            BinOp binOp=BinOp.getOp(operator);
            if (binOp!=null) {
                return withField(field,binOp,rfield);
            }
            NaryOp naryOp=NaryOp.getOp(operator);
            if (naryOp!=null) {
                return withFieldValues(field,naryOp,rfield);
            } 
        }
        throw new IllegalArgumentException("'" + expression + "' is incorrect");
    }

    private static String[] split(String expression) {
        int opIndex=-1;
        String operator=null;
        for(BinOp x:BinOp.values()) {
            int ix=expression.indexOf(x.toString());
            if(ix!=-1)
                if(opIndex==-1||ix<opIndex) {
                    opIndex=ix;
                    operator=x.toString();
                }
        }
        if(opIndex==-1) {
            for(NaryOp x:NaryOp.values()) {
                int ix=expression.indexOf(x.toString());
                if(ix!=-1)
                    if(opIndex==-1||ix<opIndex) {
                        opIndex=ix;
                        operator=x.toString();
                    }
            }
        }
        
        if(opIndex!=-1) 
            return new String[] {expression.substring(0,opIndex).trim(),
                                 operator,
                                 expression.substring(opIndex+operator.length()).trim() };
        else
            return null;
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
     *   { field: <field>, regex: <pattern>, ... }
     * </pre>
     */
    public static Query regex(String field,
                              String pattern,
                              int options) {
        return regex(field,pattern,is(options,CASE_INSENSITIVE),is(options,EXTENDED), is(options,MULTILINE), is(options,DOTALL));
    }

    private static boolean is(int options,int value) {
        return (options&value)==value;
    }

    /**
     * <pre>
     *   { field: <field>, op: <in/nin>, values: [ values ] }
     * </pre>
     */
    public static Query withValues(String field,
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
    public static Query withFieldValues(String field,
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
     *    { $and : [ expressions ] }
     *</pre>
     */
    public static Query and(Collection<Query> expressions) {
        return logical(LogOp.and, expressions);
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
     *    { $or : [ expressions ] }
     *</pre>
     */
    public static Query or(Collection<Query> expressions) {
        return logical(LogOp.or, expressions);
    }

    /**
     * <pre>
     *    { $and : [ expressions ] }
     *    { $or : [ expressions ] }
     *</pre>
     */
    public static Query logical(LogOp op,Query...expressions) {
        return logical(op, Arrays.asList(expressions));
    }

    /**
     * <pre>
     *    { $and : [ expressions ] }
     *    { $or : [ expressions ] }
     *</pre>
     */
    public static Query logical(LogOp op, Collection<Query> expressions) {
        Query q = new Query(true);
        for (Query x : expressions)
            ((ArrayNode) q.node).add(x.toJson());
        Query a = new Query(false);
        a.add(op.toString(), q.toJson());
        return a;
    }
    
    private static final String ESCAPECHARS=".^$*+?()[{\\|";

    public static String escapeRegExPattern(String s) {
        StringBuilder bld = new StringBuilder();
        int n = s.length();
        for (int i = 0; i < n; i++) {
            char c = s.charAt(i);
            if (ESCAPECHARS.indexOf(c) != -1)
                bld.append("\\\\");
            bld.append(c);
        }
        return bld.toString();
    }
}

