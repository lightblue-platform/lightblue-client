package com.redhat.lightblue.client.http;

import org.junit.Assert;
import org.junit.Test;

import java.io.*;
import java.util.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

public class StreamJsonParserTest {

    private static class TestStreamJsonParser extends StreamJsonParser {

        List<ObjectNode> nodes=new ArrayList<ObjectNode>();
        
        public TestStreamJsonParser(InputStream input) {
            super(input);
        }
        
        @Override
        public boolean documentCompleted(ObjectNode n) {
            nodes.add(n);
            return true;
        }
    }

    @Test
    public void testSingleObj() throws Exception {
        ObjectNode n=JsonNodeFactory.instance.objectNode();
        n.set("field",JsonNodeFactory.instance.textNode("value"));
        String json=n.toString();
        System.out.println("Parse "+json);
        TestStreamJsonParser parser=new TestStreamJsonParser(new StringBufferInputStream(json));
        parser.parse();
        Assert.assertEquals(1,parser.nodes.size());
        Assert.assertEquals("value",parser.nodes.get(0).get("field").asText());
    }

    @Test
    public void testMultipleObj() throws Exception {
        ObjectNode n1=JsonNodeFactory.instance.objectNode();
        n1.set("field",JsonNodeFactory.instance.textNode("value"));
        ObjectNode n2=JsonNodeFactory.instance.objectNode();
        n2.set("field2",JsonNodeFactory.instance.textNode("value2"));
        String json=n1+"   "+n2;
        System.out.println("Parse "+json);
        TestStreamJsonParser parser=new TestStreamJsonParser(new StringBufferInputStream(json));
        parser.parse();
        Assert.assertEquals(2,parser.nodes.size());
        Assert.assertEquals("value",parser.nodes.get(0).get("field").asText());
        Assert.assertEquals("value2",parser.nodes.get(1).get("field2").asText());
    }
}
