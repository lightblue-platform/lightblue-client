/*
 Copyright 2015 Red Hat, Inc. and/or its affiliates.

 This file is part of lightblue.

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.redhat.lightblue.client.request.data;

import java.util.Collection;

import com.redhat.lightblue.client.projection.Projection;
import com.redhat.lightblue.client.request.AbstractLightblueDataRequest;
import com.redhat.lightblue.client.util.JSON;

public class DataSaveRequest extends AbstractLightblueDataRequest {

    private Projection[] projections;
    private Object[] objects;
    private Boolean upsert;

    public DataSaveRequest() {

    }

    public DataSaveRequest(String entityName, String entityVersion) {
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
        if (upsert != null) {
            sb.append(",\"upsert\":");
            sb.append(upsert);
        }

        sb.append("}");

        return sb.toString();
    }

}
