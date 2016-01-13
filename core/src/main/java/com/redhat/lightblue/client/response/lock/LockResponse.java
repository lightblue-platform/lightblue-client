package com.redhat.lightblue.client.response.lock;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.redhat.lightblue.client.LightblueException;
import com.redhat.lightblue.client.model.Error;
import com.redhat.lightblue.client.response.DefaultLightblueErrorResponse;
import com.redhat.lightblue.client.response.LightblueParseException;
import com.redhat.lightblue.client.response.LightblueResponseException;

public class LockResponse extends DefaultLightblueErrorResponse {

    private static final Pattern INVALID_LOCK = Pattern.compile(
            "^.*InvalidLockException: (.+)$");

    public LockResponse(String responseText)
            throws LightblueParseException, LightblueResponseException, LightblueException {
        super(responseText);
    }

    public LockResponse(String responseText, ObjectMapper mapper)
            throws LightblueParseException, LightblueResponseException, LightblueException {
        super(responseText, mapper);
    }

    public boolean parseAsBoolean() throws LightblueParseException {
        return parseResultNode().asBoolean();
    }

    public int parseAsInt() throws LightblueParseException {
        return parseResultNode().asInt();
    }

    private JsonNode parseResultNode() throws LightblueParseException {
        if (!(getJson() instanceof ObjectNode)) {
            throw new LightblueParseException("Unable to parse json: " + getJson());
        }

        return ((ObjectNode) getJson()).get("result");
    }

    @Override
    protected void assertNoErrors() throws LightblueResponseException, LightblueException {
        Error[] errors = getLightblueErrors();
        if (errors != null) {
            for(Error error : errors){
                Matcher m = INVALID_LOCK.matcher(error.getMsg());
                if (m.matches()) {
                    throw new InvalidLockException(m.group(1));
                }
            }
        }
        super.assertNoErrors();
    }

}
