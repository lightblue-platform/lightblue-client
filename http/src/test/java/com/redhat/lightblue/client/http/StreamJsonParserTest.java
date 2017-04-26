package com.redhat.lightblue.client.http;

import org.junit.Assert;
import org.junit.Test;

import java.io.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

public class StreamJsonParserTest {

    @Test
    public void testSingleObj() throws Exception {
        ObjectNode n=JsonNodeFactory.instance.objectNode();
        n.set("field",JsonNodeFactory.instance.textNode("value"));
        String json=n.toString();
        System.out.println("Parse "+json);
        StreamJsonParser parser=new StreamJsonParser(new StringBufferInputStream(json));
        parser.parse();
    }

    @Test
    public void testMultipleObj() throws Exception {
        ObjectNode n1=JsonNodeFactory.instance.objectNode();
        n1.set("field",JsonNodeFactory.instance.textNode("value"));
        ObjectNode n2=JsonNodeFactory.instance.objectNode();
        n2.set("field2",JsonNodeFactory.instance.textNode("value2"));
        String json=n1+"   "+n2;
        System.out.println("Parse "+json);
        StreamJsonParser parser=new StreamJsonParser(new StringBufferInputStream(json));
        parser.parse();
    }
}
