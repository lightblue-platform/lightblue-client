package com.redhat.lightblue.client;

import java.math.BigDecimal;
import java.math.BigInteger;

import java.util.Date;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ArrayNode;

import com.redhat.lightblue.client.util.ClientConstants;
import com.redhat.lightblue.client.util.JSON;

/**
 * A Literal value, backed by a JsonNode
 */
public class Literal extends ExpressionPart implements
                                                Update.SetLiteral,
                                                Update.AddLiteral,
                                                Update.AppendInsertLiteral {

    public Literal(int i) {
        super(JsonNodeFactory.instance.numberNode(i));
    }

    public Literal(long l) {
        super(JsonNodeFactory.instance.numberNode(l));
    }

    public Literal(String s) {
        super(JsonNodeFactory.instance.textNode(s));
    }

    public Literal(double d) {
        super(JsonNodeFactory.instance.numberNode(d));
    }

    public Literal(Date d) {
        this((Object)d);
    }

    public Literal(Object x) {
        super(valueToJson(x));
    }

    public static JsonNode valueToJson(Object x) {
        JsonNode node;
        if(x==null) {
            node=JsonNodeFactory.instance.nullNode();
        } else if(x instanceof Date) {
            node=JsonNodeFactory.instance.textNode(ClientConstants.getDateFormat().format((Date)x));
        }  else if(x instanceof Number) {
            if (x instanceof BigDecimal) {
                node=JsonNodeFactory.instance.numberNode((BigDecimal) x);
            } else if (x instanceof BigInteger) {
                node=JsonNodeFactory.instance.numberNode((BigInteger) x);
            } else if (x instanceof Double) {
                node=JsonNodeFactory.instance.numberNode((Double) x);
            } else if (x instanceof Float) {
                node=JsonNodeFactory.instance.numberNode((Float) x);
            } else if (x instanceof Long) {
                node=JsonNodeFactory.instance.numberNode((Long) x);
            } else {
                node=JsonNodeFactory.instance.numberNode(((Number) x).intValue());
            }
        } else if(x instanceof Boolean) {
            node=JsonNodeFactory.instance.booleanNode((Boolean)x);
        } else if(x instanceof JsonNode) {
            node=(JsonNode)x;
        } else if(x instanceof Literal) {
            node=((Literal)x).node;
        } else {
            node=JsonNodeFactory.instance.textNode(x.toString());
        }
        return node;
    }

    /**
     * Create a JSON literal from a pojo
     */
    public static Literal pojo(Object x) {
        return new Literal(JSON.toJsonNode(x));
    }

    public static Literal value(Object x) {
        return new Literal(x);
    }

    public static Literal value(int i) {
        return new Literal(i);
    }

    public static Literal value(long l) {
        return new Literal(l);
    }

    public static Literal value(double d) {
        return new Literal(d);
    }

    public static Literal value(boolean b) {
        return new Literal(b);
    }

    public static Literal[] values(int...v) {
        Literal[] ret=new Literal[v.length];
        for(int i=0;i<ret.length;i++)
            ret[i]=Literal.value(v[i]);
        return ret;
    }

    public static Literal[] values(long...v) {
        Literal[] ret=new Literal[v.length];
        for(int i=0;i<ret.length;i++)
            ret[i]=Literal.value(v[i]);
        return ret;
    }

    public static Literal[] numbers(Number...v) {
        Literal[] ret=new Literal[v.length];
        for(int i=0;i<ret.length;i++)
            ret[i]=Literal.value(v[i]);
        return ret;
    }

    public static Literal[] values(String...v) {
        Literal[] ret=new Literal[v.length];
        for(int i=0;i<ret.length;i++)
            ret[i]=Literal.value(v[i]);
        return ret;
    }

    public static EmptyArray emptyArray() {
        return new EmptyArray();
    }

    public static EmptyObject  emptyObject() {
        return new EmptyObject();
    }

    public static JsonNode toJson(Literal[] arr) {
        ArrayNode node=JsonNodeFactory.instance.arrayNode();
        for(Literal x:arr)
            node.add(x.toJson());
        return node;
    }
}
