package com.redhat.lightblue.client;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.BooleanNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

import com.redhat.lightblue.client.model.Error;
import com.redhat.lightblue.client.model.DataError;
import com.redhat.lightblue.client.LightblueException;
import com.redhat.lightblue.client.response.ResultMetadata;

/**
 * This class is used to stream results from a lightblue
 * server. Instead of retrieving the results in a single server
 * response, this class allows the clients to process results as they
 * arrive. After obtaining an instance of ResultStream, call the
 * run(f) method, and f.processDocument will be called when a new
 * document is received from the server.
 */
public class ResultStream {

    /**
     * Contains the fields in a response other than the documents
     * themselves. An instance of ResponseHeader is passed to
     * ForEachDoc when it is received from the server
     */
    public static class ResponseHeader {
        public final String entityName;
        public final String version;
        public final String status;
        public final String hostName;
        public final Integer matchCount;
        public final List<Error> errors;

        public ResponseHeader(String entityName,
                              String version,
                              String status,
                              String hostName,
                              Integer matchCount,
                              List<Error> errors) {
            this.entityName=entityName;
            this.version=version;
            this.status=status;
            this.hostName=hostName;
            this.matchCount=matchCount;
            this.errors=errors;
        }

        public static ResponseHeader fromJson(ObjectNode node) {
            ResponseHeader ret=null;
            if(node!=null) {
                String entityName=null;
                String entityVersion=null;
                String status=null;
                String hostName=null;
                Integer matchCount=null;
                List<Error> errors=new ArrayList<Error>();
                JsonNode x=node.get("entity");
                if(x!=null)
                    entityName=x.asText();
                x=node.get("entityVersion");
                if(x!=null)
                    entityVersion=x.asText();
                x=node.get("status");
                if(x!=null)
                    status=x.asText();
                x=node.get("hostname");
                if(x!=null)
                   hostName=x.asText();
                x=node.get("matchCount");
                if(x!=null)
                    matchCount=x.asInt();
                x=node.get("errors");
                if(x!=null) {
                    if(x instanceof ArrayNode) {
                        for(int i=0;i<x.size();i++) {
                            errors.add(Error.fromJson(x.get(i)));
                        }
                    } else if(x instanceof ObjectNode) {
                        errors.add(Error.fromJson(x));
                    }
                }
                
                ret=new ResponseHeader(entityName,entityVersion,status,hostName,matchCount,errors);
            }
            return ret;
        }
    }
    
    /**
     * An instance of StreamDoc represents a JSON document received
     * from the stream. Contains the JSON document, result metadata,
     * and the flag that determines hwether this is the last document
     * received or not
     */
    public static class StreamDoc {
        public boolean lastDoc;
        public ResultMetadata resultMetadata;
        public ObjectNode doc;


        /**
         * Construct a StreamDoc from the json node
         */
        public static StreamDoc fromJson(ObjectNode node) {
            StreamDoc ret=null;
            if(node!=null) {
                ret=new StreamDoc();
                JsonNode x=node.get("processed");
                if(x instanceof ObjectNode)
                    ret.doc=(ObjectNode)x;
                x=node.get("resultMetadata");
                if(x instanceof ObjectNode) {
                    ret.resultMetadata=ResultMetadata.fromJson((ObjectNode)x);
                }
                x=node.get("last");
                if(x instanceof BooleanNode)
                    ret.lastDoc=x.asBoolean();
            }
            return ret;
        }
    }

    /**
     * If the client is retrieving objects instead of documents (using
     * an instance of ForEachObj), then this contains the object
     * constructed from the json document
     */
    public static class StreamObj<T>  extends StreamDoc {
        private T obj;

        /**
         * The POJO read from the stream
         */
        public T getObj() {
            return obj;
        }
    }

    /**
     * Pass an instanceof of ForEachDoc by implementing
     * processDocument function to run() to process documents. For
     * processDocument function will be called for every document
     * received. The processResponseHeader will be called when the
     * response header is received.
     */
    public static abstract class ForEachDoc {
        protected ResponseHeader responseHeader;
        
        public void processResponseHeader(ResponseHeader r) {
            responseHeader=r;
        }

        public ResponseHeader getResponseHeader() {
            return responseHeader;
        }

        /**
         * Override this to recewie an object from the strema
         *
         * Return true for continue processing, false to terminate
         */
        public abstract boolean processDocument(StreamDoc doc);
    }

    /**
     * Pass an instance of ForEachObj by implementing processObjct to
     * run() to process objects. processObject will be called for each
     * object received from the stream.
     */
    public static abstract class ForEachObj<T> extends ForEachDoc {
        protected final Class<T> type;
        protected ObjectMapper objectMapper;
        
        public ForEachObj(Class<T> type) {
            this.type=type;
        }

        @Override
        public boolean processDocument(StreamDoc doc) {
            StreamObj<T> obj=new StreamObj<T>();
            obj.lastDoc=doc.lastDoc;
            obj.resultMetadata=doc.resultMetadata;
            try {
                obj.obj=objectMapper.treeToValue(doc.doc,type);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return processObject(obj);
        }

        /**
         * Override this to receive a POJO from the stream
         *
         * Return true for continue processing, false to terminate
         */
        public abstract boolean processObject(StreamObj<T> obj);
    }

    /**
     * Submit the query, and call <code>f</code> for each document received. 
     */
    public interface RequestCl {
        void submitAndIterate(ForEachDoc f) throws LightblueException;
    }

    public final RequestCl requestClosure;
    public final ObjectMapper mapper;
    
    public ResultStream(RequestCl req,ObjectMapper mapper) {
        this.requestClosure=req;
        this.mapper=mapper;
    }

    /**
     * Submit the query and stream results. Call <code>f</code> for
     * each document received. If <code>f</code> is instance of
     * ForEacObj, each object will be translated to a POJO before
     * <code>f</code> is notified.
     */
    public void run(ForEachDoc f) throws LightblueException {
        if(f instanceof ForEachObj&&mapper!=null) {
            ((ForEachObj)f).objectMapper=mapper;
        }
        requestClosure.submitAndIterate(f);
    }
}
