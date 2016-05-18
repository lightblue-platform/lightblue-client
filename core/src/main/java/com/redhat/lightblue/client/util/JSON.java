package com.redhat.lightblue.client.util;

import java.io.IOException;
import java.io.StringWriter;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 *
 * @author mpatercz
 *
 */
public final class JSON {

    /**
     * It is safe and encouraged to share the same mapper among threads. It is
     * thread safe. So, this default instance is static.
     *
     * @see <a href="http://stackoverflow.com/a/3909846">The developer of the
     * Jackson library's own quote.</a>
     */
    private static final ObjectMapper DEFAULT_MAPPER = new ObjectMapper()
            .setDateFormat(ClientConstants.getDateFormat())
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    private static ObjectMapper objectMapper;

    /**
     * <p>
     * Sets the default {@link ObjectMapper} that will be used throughout the
     * lightblue-client unless otherwise not specified.</p>
     * <p>
     * This is an optional setter, in that if not set a prepared default is
     * already available.</p>
     *
     * @param m
     */
    public static void setDefaultObjectMapper(ObjectMapper m) {
        objectMapper = m;
    }

    /**
     * @return default {@link ObjectMapper}.
     */
    public static ObjectMapper getDefaultObjectMapper() {
        if (objectMapper == null) {
            return DEFAULT_MAPPER;
        }
        return objectMapper;
    }

    public static JsonNode toJsonNode(String s) {
        try {
            return getDefaultObjectMapper().readTree(s);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Convert object to json. If object contains fields of type date, they will
     * be converted to strings using lightblue date format.
     *
     * @param obj
     * @return
     */
    public static String toJson(Object obj) {
        StringWriter sw = new StringWriter();
        ObjectMapper mapper = getDefaultObjectMapper();
        try {
            JsonGenerator jg = mapper.getFactory().createGenerator(sw);
            mapper.writeValue(jg, obj);
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonGenerationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sw.toString();
    }

    public static JsonNode toJsonNode(Object obj) {
        ObjectMapper mapper = getDefaultObjectMapper();
        return mapper.valueToTree(obj);
    }

    private JSON() {
    }

}
