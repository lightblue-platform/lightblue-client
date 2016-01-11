package com.redhat.lightblue.client;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ContainerNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Update expression
 *
 * Usage:
 * <pre>
 * Update.set("x",1).more("y","value").more("x",Literal.value(new Date())).more(Literal.emptyObject());
 * Update.unset("x").more("y").more("z");
 * Update.addValue("x",Literal.value(1)).more("y",Literal.value(-1)).more("z",new ValueOf("w"));
 * Update.append("array",Literal.emptyObject());
 * Update.insert("array",Literal.emptyArray());
 * Update.forEach("array",Query..., Update.set(...) )
 * Update.forEach("array",Update.ALL, Update.set(...) )
 * Update.forEach("array",Query..., Update.REMOVE)
 * Update.update(Update.set("x",1),Update.unset("y"), Update.addValue("z",Literal.value(1)));
 * </pre>
 */
public class Update extends Expression implements ForEachUpdate {

    public interface AddLiteral extends JsonObj {}
    public interface AppendInsertLiteral extends JsonObj {}
    public interface SetLiteral extends JsonObj {}
    public interface UpdateQuery extends JsonObj {}

    public static final UpdateQuery ALL=new All();
    public static final ForEachUpdate REMOVE=new Remove();

    public static class All extends ExpressionPart implements UpdateQuery {
        public All() {
            super(JsonNodeFactory.instance.textNode("$all"));
        }
    }

    public static class Remove extends ExpressionPart implements ForEachUpdate {
        public Remove() {
            super(JsonNodeFactory.instance.textNode("$remove"));
        }
    }

    /**
     * Represents a $set expression
     */
    public static class Set extends Update {
        public Set() {
            super(JsonNodeFactory.instance.objectNode());
        }

        public Set(ObjectNode node) {
            super(node);
        }

        /**
         * Add a new field/value to $set
         */
        public Set more(String field,JsonNode value) {
            JsonNode x=((ObjectNode)node).get("$set");
            if(x==null) {
                x=JsonNodeFactory.instance.objectNode();
                ((ObjectNode)node).set("$set",x);
            }
            ((ObjectNode)x).set(field,value);
            return this;
        }

        /**
         * Add a new field/value to $set
         */
        public Set more(String field,SetLiteral value) {
            return more(field,value.toJson());
        }

        public Set more(String field,int i) {
            return more(field,Literal.value(i));
        }

        public  Set more(String field,String i) {
            return more(field,Literal.value(i));
        }

        public  Set more(String field,boolean i) {
            return more(field,Literal.value(i));
        }

   }

    /**
     * Represents an $add expression
     */
    public static class Add extends Update {
        public Add() {
            super(JsonNodeFactory.instance.objectNode());
        }

        public Add(ObjectNode node) {
            super(node);
        }

        /**
         * Adds a new field/value to add
         */
        public Add more(String field,JsonNode value) {
            JsonNode x=((ObjectNode)node).get("$add");
            if(x==null) {
                x=JsonNodeFactory.instance.objectNode();
                ((ObjectNode)node).set("$add",x);
            }
            ((ObjectNode)x).set(field,value);
            return this;
        }

        /**
         * Adds a new field/value to add
         */
        public Add more(String field,AddLiteral value) {
            return more(field,value.toJson());
        }
    }

    /**
     * Represents an $append or $insert expression
     */
    public static class AppendInsert extends Update {
        public AppendInsert(String op,String field) {
            super(JsonNodeFactory.instance.objectNode());
            ObjectNode x=JsonNodeFactory.instance.objectNode();
            x.set(field,JsonNodeFactory.instance.arrayNode());
            ((ObjectNode)node).set(op,x);
        }

        /**
         * Adds a new value to append/insert
         */
        public AppendInsert more(JsonNode value) {
            ((ArrayNode)((ObjectNode)((ObjectNode)node).elements().next()).elements().next()).add(value);
            return this;
        }

        /**
         * Adds a new value to append/insert
         */
        public AppendInsert more(AppendInsertLiteral value) {
            return more(value.toJson());
        }
    }

    /**
     * Represents an $unset expression
     */
    public static class Unset extends Update {
        public Unset() {
             super(JsonNodeFactory.instance.objectNode());
        }

        public Unset(ObjectNode node) {
            super(node);
        }

        /**
         * Adds a new field to unset
         */
        public Unset more(String field) {
            JsonNode x=((ObjectNode)node).get("$unset");
            if(x==null) {
                x=JsonNodeFactory.instance.textNode(field);
                ((ObjectNode)node).set("$unset",x);
            } else {
                ArrayNode arr;
                if(!(x instanceof ArrayNode)) {
                    arr=JsonNodeFactory.instance.arrayNode();
                    arr.add(x);
                    ((ObjectNode)node).set("$unset",arr);
                } else {
                    arr=(ArrayNode)x;
                }
                arr.add(JsonNodeFactory.instance.textNode(field));
            }
            return this;
        }
    }

    /**
     * Creates an update node with the given array or object node
     */
    public Update(ContainerNode node) {
        super(node);
    }

    private Update(boolean arrayNode) {
        super(arrayNode);
    }

    public static Update update(ContainerNode node) {
        return new Update(node);
    }

    /**
     * <pre>
     *  [ update,... ]
     * </pre>
     */
    public static Update update(Update...update) {
        if(update.length==1) {
            return update[0];
        } else {
            Update x=new Update(true);
            for(Update u:update) {
                ((ArrayNode)x.node).add(u.toJson());
            }
            return x;
        }
    }

    public static Update update(List<? extends Update> update) {
        if(update.size()==1) {
            return update.get(0);
        } else {
            Update x=new Update(true);
            for(Update u:update) {
                ((ArrayNode)x.node).add(u.toJson());
            }
            return x;
        }
    }

    /**
     * <pre>
     *  { $set : { field: l } }
     * </pre>
     */
    public static Set set(String field,SetLiteral l) {
        return new Set().more(field,l);
    }

    public static Set set(String field,int i) {
        return new Set().more(field,Literal.value(i));
    }

    public static Set set(String field,String i) {
        return new Set().more(field,Literal.value(i));
    }

    public static Set set(String field,boolean i) {
        return new Set().more(field,Literal.value(i));
    }

    public static Set set(String field, Date date){
        return new Set().more(field, Literal.value(date));
    }

    /**
     * <pre>
     *   { $unset : [ fields... ]
     * </pre>
     */
    public static Unset unset(String...fields) {
        Unset x=new Unset();
        for(String f:fields) {
            x.more(f);
        }
        return x;
    }

    /**
     * <pre>
     *  { $add : { field: literal } }
     * </pre>
     */
    public static Add addValue(String field,AddLiteral literal) {
        return new Add().more(field,literal);
    }

    /**
     * <pre>
     *  { $append : { path: value } }
     * </pre>
     */
    public static AppendInsert append(String field,AppendInsertLiteral value) {
        return new AppendInsert("$append",field).more(value);
    }

    /**
     * <pre>
     *  { $insert : { path: value } }
     * </pre>
     */
    public static AppendInsert insert(String field,AppendInsertLiteral value) {
        return new AppendInsert("$insert",field).more(value);
    }

    /**
     * <pre>
     *  { $foreach : { field: { q }, $update: u } }
     * </pre>
     */
    public static Update forEach(String field,
                                 UpdateQuery q,
                                 ForEachUpdate u) {
        Update x=new Update(false);
        ObjectNode fe=JsonNodeFactory.instance.objectNode();
        fe.set(field,q.toJson());
        fe.set("$update",u.toJson());
        ((ObjectNode)x.node).set("$foreach",fe);
        return x;
    }
}
