package com.redhat.lightblue.client.request.data;

import java.util.Collection;

import com.redhat.lightblue.client.projection.Projection;
import com.redhat.lightblue.client.request.AbstractLightblueDataRequest;
import com.redhat.lightblue.client.util.JSON;

public class DataInsertRequest extends AbstractLightblueDataRequest {

    private Projection[] projections;
    private Object[] objects;

    public DataInsertRequest() {

    }

    public DataInsertRequest(String entityName, String entityVersion) {
        this.setEntityName(entityName);
        this.setEntityVersion(entityVersion);
    }

    public void returns(Projection... projection) {
        this.projections = projection;
    }

    public void returns(Collection<Projection> projections) {
        this.projections = projections.toArray(new Projection[projections.size()]);
    }

    public void create(Object... objects) {
        if (objects[0] instanceof java.util.Collection<?>) {
            this.objects = ((Collection<?>) objects[0]).toArray();
        } else {
            this.objects = objects;
        }
    }

    @Override
    public String getBody() {
        StringBuilder sb = new StringBuilder();
        sb.append("{\"data\":[");
        sb.append(JSON.toJson(objects[0]));
        for (int i = 1; i < objects.length; i++) {
            sb.append(",").append(JSON.toJson(objects[i]));
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

    @Override
    public String getOperationPathParam() {
        return Operation.INSERT.getPathParam();
    }
}
