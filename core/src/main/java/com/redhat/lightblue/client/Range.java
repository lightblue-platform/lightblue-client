package com.redhat.lightblue.client;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class Range {

    private final Integer begin;
    private final Integer end;

    public Range(Integer begin) {
        this(begin, null);
    }

    public Range(Integer begin, Integer end) {
        this.begin = begin;
        this.end = end;
    }

    public void appendToJson(ObjectNode node) {
        if (begin != null) {
            ArrayNode arr = JsonNodeFactory.instance.arrayNode();
            arr.add(JsonNodeFactory.instance.numberNode(begin));
            if (end != null) {
                arr.add(JsonNodeFactory.instance.numberNode(end));
            } else {
                arr.add(JsonNodeFactory.instance.nullNode());
            }
            node.set("range", arr);
        }
    }

    @Override
    public String toString() {
        return "Range [begin=" + begin + ", end=" + end + "]";
    }

}
