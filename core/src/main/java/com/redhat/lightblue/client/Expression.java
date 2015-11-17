package com.redhat.lightblue.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ContainerNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Base class for all expressions. Contains a container json node,
 * either an object, or an array
 */
public abstract class Expression extends ExpressionPart {

    /**
     * Construct expression with the given object or array node
     */
    protected Expression(ContainerNode node) {
        super(node);
    }

    /**
     * Construct an expression with an empty array or object node
     */
    protected Expression(boolean arrayNode) {
        super(arrayNode?JsonNodeFactory.instance.arrayNode():
              JsonNodeFactory.instance.objectNode());
    }

    /**
     * Add field:value
     */
    public Expression add(String field,JsonNode value) {
        try {
            ((ObjectNode)node).set(field,value);
            return this;
        } catch (ClassCastException e) {
            throw new RuntimeException("Object node expected while adding "+field);
        }
    }

    /**
     * Add field:value
     */
    public Expression add(String field,String value) {
        return add(field,JsonNodeFactory.instance.textNode(value));
    }

    /**
     * Add field:value
     */
    public Expression add(String field,boolean value) {
        return add(field,JsonNodeFactory.instance.booleanNode(value));
    }
    
}
