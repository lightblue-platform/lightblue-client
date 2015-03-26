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

import com.redhat.lightblue.client.expression.query.Query;
import com.redhat.lightblue.client.expression.update.Update;
import com.redhat.lightblue.client.projection.Projection;
import com.redhat.lightblue.client.request.AbstractLightblueDataRequest;

import java.util.Collection;

public class DataUpdateRequest extends AbstractLightblueDataRequest {

    private Projection[] projections;
    private Update[] updates;
    private Query query;

    public DataUpdateRequest() {

    }

    public DataUpdateRequest(String entityName, String entityVersion) {
        this.setEntityName(entityName);
        this.setEntityVersion(entityVersion);
    }

    public void returns(Projection... projections) {
        this.setProjections(projections);
    }

    public void returns(Collection<Projection> projections) {
        this.setProjections(projections);
    }

    public void where(Query query) {
        this.setQuery(query);
    }

    public void updates(Update... updates) {
        this.setUpdates(updates);
    }

    public void updates(Collection<Update> updates) {
        this.setUpdates(updates);
    }

    public void setQuery(Query query) {
        this.query = query;
    }

    public void setUpdates(Update... updates) {
        this.updates = updates;
    }

    public void setUpdates(Collection<Update> updates) {
        this.updates = updates.toArray(new Update[updates.size()]);
    }

    public void setProjections(Projection... projections) {
        this.projections = projections;
    }

    public void setProjections(Collection<Projection> projections) {
        this.projections = projections.toArray(new Projection[projections.size()]);
    }

    @Override
    public String getBody() {
        // http://jewzaam.gitbooks.io/lightblue-specifications/content/language_specification/data.html#update
        StringBuilder sb = new StringBuilder();
        sb.append("{\"query\":");
        sb.append(query.toJson());

        // one or more update expressions
        sb.append(",\"update\":[");
        sb.append(updates[0].toJson());

        for (int i = 1; i < updates.length; i++) {
            sb.append(",").append(updates[i].toJson());
        }

        sb.append("]");

        // one or more projection expressions
        sb.append(",\"projection\":[");
        sb.append(projections[0].toJson());

        for (int i = 1; i < projections.length; i++) {
            sb.append(",").append(projections[i].toJson());
        }

        sb.append("]");

        // end top level
        sb.append("}");

        return sb.toString();
    }

    @Override
    public String getOperationPathParam() {
        return PATH_PARAM_UPDATE;
    }

}
