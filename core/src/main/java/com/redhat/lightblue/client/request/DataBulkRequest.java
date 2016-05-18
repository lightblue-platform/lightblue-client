/**
 *
 */
package com.redhat.lightblue.client.request;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * @author bvulaj
 *
 */
public class DataBulkRequest extends AbstractDataBulkRequest<AbstractLightblueDataRequest> {

    public DataBulkRequest() {
        super();
    }

    @Override
    public JsonNode getBodyJson() {
        ObjectNode root = JsonNodeFactory.instance.objectNode();
        ArrayNode reqs = JsonNodeFactory.instance.arrayNode();
        for (AbstractLightblueDataRequest req : requests) {
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
