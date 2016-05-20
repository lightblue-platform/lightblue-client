package com.redhat.lightblue.client.request;

import java.util.List;
import java.util.ArrayList;
import java.util.Collection;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import com.redhat.lightblue.client.http.HttpMethod;

/**
 * @author bvulaj
 *
 */
public class DataBulkRequest extends LightblueRequest {

    protected List<CRUDRequest> requests=new ArrayList<>();

    public DataBulkRequest() {
        super(HttpMethod.POST);
    }

    /**
     * Adds a request to the end of the current request chain.
     *
     * @param request
     * @return
     */
    public DataBulkRequest add(CRUDRequest request) {
        requests.add(request);
        return this;
    }

    /**
     * Adds a collection of requests to the end of the current request chain in
     * iteration order.
     *
     * <p>
     * As the order of responses depends on the order of requests, use a
     * collection with a reliable iteration order if you need to get a specific
     * response for a specific request by index. Otherwise, you can also
     * retrieve the response for a specific request using
     * {@link com.redhat.lightblue.client.response.LightblueBulkDataResponse#getResponse(LightblueRequest)}.
     *
     * @param requests
     * @return
     */
    public DataBulkRequest addAll(Collection<? extends CRUDRequest> requests) {
        this.requests.addAll(requests);
        return this;
    }

    /**
     * Inserts a request at the given index. This method should not be preferred
     * over the before / after methods.
     *
     * @param request
     * @param index
     * @return
     */
    public DataBulkRequest insert(CRUDRequest request, int index) {
        this.requests.add(index, request);
        return this;
    }

    /**
     * Inserts a request before another specified request. This guarantees that
     * the first request parameter will be executed, sequentially, before the
     * second request parameter. It does not guarantee consecutive execution.
     *
     * @param request
     * @param before
     * @return
     */
    public DataBulkRequest insertBefore(CRUDRequest request, CRUDRequest before) {
        this.requests.add(requests.indexOf(before), request);
        return this;
    }

    /**
     * Inserts a request after another specified request. This guarantees that
     * the first request parameter will be executed, sequentially, after the
     * second request parameter. It does not guarantee consecutive execution.
     *
     * @param request
     * @param after
     * @return
     */
    public DataBulkRequest insertAfter(CRUDRequest request, CRUDRequest after) {
        this.requests.add(requests.indexOf(after) + 1, request);
        return this;
    }

    public String getRestURI(String baseServiceURI) {
        StringBuilder requestURI = new StringBuilder();
        requestURI.append(baseServiceURI);
        LightblueRequest.appendToURI(requestURI, "bulk");
        return requestURI.toString();
    }

    public List<? extends CRUDRequest> getRequests() {
        return this.requests;
    }

    public void setRequests(List<CRUDRequest> requests) {
        this.requests = requests;
    }

    @Override
    public String toString() {
        return "POST" + getRestURI("/") + ", body: " + getBody();
    }
    
    public JsonNode getBodyJson() {
        ObjectNode root = JsonNodeFactory.instance.objectNode();
        ArrayNode reqs = JsonNodeFactory.instance.arrayNode();
        for (CRUDRequest req : requests) {
            if (req == null) {
                continue;
            }
            ObjectNode seqNode = JsonNodeFactory.instance.objectNode();
            seqNode.set("seq", JsonNodeFactory.instance.numberNode(reqs.size()));
            if (req.getOperation() != null) {
                seqNode.set("op", JsonNodeFactory.instance.textNode(req.getOperation().name().toLowerCase()));
            }
            ObjectNode request = (ObjectNode) req.getBodyJson();
            if (request != null) {
                request.set("entity", JsonNodeFactory.instance.textNode(req.getEntityName()));
                request.set("entityVersion", JsonNodeFactory.instance.textNode(req.getEntityVersion()));
                seqNode.set("request", request);
            }
            reqs.add(seqNode);
        }
        root.set("requests", reqs);
        return root;
    }
}
