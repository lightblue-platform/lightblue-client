package com.redhat.lightblue.client.http;

import java.io.IOException;
import java.io.InputStream;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.TreeNode;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

public class StreamJsonParser {
    private final InputStream input;
    private final JsonFactory factory;

    public StreamJsonParser(InputStream input) {
        this(input,new ObjectMapper());
    }

    public StreamJsonParser(InputStream input,ObjectMapper mapper) {
        this.input=input;
        this.factory=new JsonFactory();
        factory.setCodec(mapper);
    }

    public boolean parse() throws IOException, JsonProcessingException {
        JsonParser parser=factory.createParser(input);
        boolean done=false;
        do {
            TreeNode t=parser.readValueAsTree();
            if(t==null) {
                done=true;
            } else {
                if(t instanceof ObjectNode) {
                    if(!documentCompleted((ObjectNode)t))
                        return false;
                } else if(t instanceof ArrayNode) {
                    int n=t.size();
                    for(int i=0;i<n;i++) {
                        TreeNode elem=t.path(i);
                        if(elem!=null) {
                            if(!documentCompleted((ObjectNode)elem))
                                return false;
                        }
                    }
                } else {
                    throw new RuntimeException("Ill formed stream");
                }
            }
        } while(!done);
        return true;
    }
    
    public boolean documentCompleted(ObjectNode n) {return true;}

}
