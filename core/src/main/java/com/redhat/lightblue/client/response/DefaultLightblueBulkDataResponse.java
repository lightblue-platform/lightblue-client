package com.redhat.lightblue.client.response;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.redhat.lightblue.client.request.AbstractDataBulkRequest;
import com.redhat.lightblue.client.request.AbstractLightblueRequest;
import com.redhat.lightblue.client.request.LightblueRequest;
import com.redhat.lightblue.client.util.JSON;

/**
 * @author bvulaj
 *
 */
public class DefaultLightblueBulkDataResponse extends AbstractLightblueResponse implements LightblueBulkDataResponse {

    private final Map<Integer, LightblueDataResponse> responses = new TreeMap<>();
    private final List<? extends AbstractLightblueRequest> requests;

    public DefaultLightblueBulkDataResponse(String responseText, AbstractDataBulkRequest<? extends AbstractLightblueRequest> reqs) throws LightblueParseException, LightblueBulkResponseException {
        this(responseText, JSON.getDefaultObjectMapper(), reqs);
    }

    public DefaultLightblueBulkDataResponse(String responseText, ObjectMapper mapper, AbstractDataBulkRequest<? extends AbstractLightblueRequest> reqs) throws LightblueParseException, LightblueBulkResponseException {
        super(responseText, mapper);
        requests = reqs.getRequests();

        JsonNode resps = getJson().get("responses");
        if (resps == null) {
            throw new LightblueParseException("Unable to parse 'responses' node.");
        }

        List<LightblueResponseException> exceptions = new ArrayList<>();

        if (resps.isArray()) {
            ArrayNode arrResps = (ArrayNode) resps;
            for (Iterator<JsonNode> it = arrResps.iterator(); it.hasNext();) {
                JsonNode resp = it.next();
                JsonNode seq = resp.get("seq");

                DefaultLightblueDataResponse response;
                try {
                    response = new DefaultLightblueDataResponse(resp.get("response"), mapper);
                } catch (LightblueResponseException e) {
                    exceptions.add(e);

                    //Append the response because it is still a valid response and seq needs to be kept.
                    response = (DefaultLightblueDataResponse) e.getLightblueResponse();
                }

                if (seq.isNumber()) {
                    responses.put(seq.intValue(), response);
                } else {
                    // A bad sequence should be rare (never?)
                    // Do not add a bad sequence to the responses because its location cannot be determined.
                    exceptions.add(new LightblueResponseException("Invalid sequence: " + seq.toString(), response));
                }
            }
        }
        else {
            throw new LightblueParseException("Unparseable bulk data 'responses' node: " + resps.toString());
        }

        if (!exceptions.isEmpty()) {
            throw new LightblueBulkResponseException("Errors returned in responses", this, exceptions);
        }
    }

    @Override
    public LightblueDataResponse getResponse(LightblueRequest lbr) {
        return getResponse(requests.indexOf(lbr));
    }

    @Override
    public LightblueDataResponse getResponse(int seq) {
        return responses.get(seq);
    }

    @Override
    public List<LightblueDataResponse> getResponses() {
        return new ArrayList<>(responses.values());
    }

    @Override
    public List<? extends AbstractLightblueRequest> getRequests() {
        return requests;
    }

}
