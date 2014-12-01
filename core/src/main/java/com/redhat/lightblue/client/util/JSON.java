package com.redhat.lightblue.client.util;

import java.io.IOException;
import java.io.StringWriter;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 *
 * @author mpatercz
 *
 */
public abstract class JSON {

    private static ObjectMapper mapper = new ObjectMapper();
    private static JsonFactory jf = new JsonFactory();
    static {
        mapper.setDateFormat(ClientConstants.getDateFormat());
    }

    /**
     * Convert object to json. If object contains fields of type date, they will be converted to strings using lightblue date format.
     *
     * @param obj
     * @return
     */
    public static String toJson(Object obj) {
        StringWriter sw = new StringWriter();
        try {
            JsonGenerator jg = jf.createGenerator(sw);
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

}
