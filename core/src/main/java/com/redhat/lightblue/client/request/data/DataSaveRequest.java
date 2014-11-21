package com.redhat.lightblue.client.request.data;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Collection;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.redhat.lightblue.client.projection.Projection;
import com.redhat.lightblue.client.request.AbstractLightblueDataRequest;
import com.redhat.lightblue.client.types.DateType;

public class DataSaveRequest extends AbstractLightblueDataRequest {

    private Projection[] projections;
    private Object[] objects;
    private Boolean upsert;
    private static ObjectMapper mapper = new ObjectMapper();
    private static JsonFactory jf = new JsonFactory();
    static {
        mapper.setDateFormat(DateType.getDateFormat());
    }

	public DataSaveRequest() {

	}

	public DataSaveRequest(String entityName, String entityVersion) {
		this.setEntityName(entityName);
		this.setEntityVersion(entityVersion);
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

    public Boolean isUpsert() {
        return upsert;
    }

    public void setUpsert(Boolean upsert) {
        this.upsert = upsert;
    }

	@Override
    public String getOperationPathParam() {
	  return PATH_PARAM_SAVE;
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
        if (upsert!=null) {
            sb.append(",\"upsert\":");
            sb.append(upsert);
        }

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
