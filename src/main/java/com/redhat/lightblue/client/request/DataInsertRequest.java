package com.redhat.lightblue.client.request;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.redhat.lightblue.client.enums.RequestType;
import com.redhat.lightblue.client.projection.Projection;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Collection;

public class DataInsertRequest extends AbstractLightblueRequest {

    private Projection[] projections;
    private Object[] objects;
    private static ObjectMapper mapper = new ObjectMapper();
    private static JsonFactory jf = new JsonFactory();

    public DataInsertRequest() { }

    public DataInsertRequest(String entityName, String entityVersion){
        this.setEntityName(entityName);
        this.setEntityVersion(entityVersion);
    }

	@Override
	public RequestType getRequestType() {
		return RequestType.DATA_INSERT;
	}

    public void returns(Projection... projection){
        this.projections = projection;
    }

    public void returns(Collection<Projection> projections) {
        this.projections = projections.toArray( new Projection[ projections.size() ] );
    }

    public void create(Object... objects){
        this.objects = objects;
    }

    public void create(Collection<Object> objects){
        this.objects = objects.toArray(new Object[objects.size()]);
    }

    @Override
    public String getBody() {
        StringBuffer sb = new StringBuffer();
        sb.append("{\"data\":[");
        sb.append(toJson(objects[0]));
        for (int i = 1; i < objects.length; i++) {
            sb.append(",").append(toJson(objects[i]));
        }
        sb.append("],\"projection\":[");
        sb.append(projections[0].toJson());

        for (int i = 1; i < projections.length; i++) {
            sb.append(",").append(projections[i].toJson());
        }

        sb.append("]");

        sb.append("}");

        return sb.toString();
    }

    private static String toJson(Object obj) {
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
