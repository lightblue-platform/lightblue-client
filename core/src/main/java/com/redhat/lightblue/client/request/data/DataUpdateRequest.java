package com.redhat.lightblue.client.request.data;

import java.util.Collection;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.redhat.lightblue.client.Operation;
import com.redhat.lightblue.client.Projection;
import com.redhat.lightblue.client.Query;
import com.redhat.lightblue.client.Update;
import com.redhat.lightblue.client.http.HttpMethod;
import com.redhat.lightblue.client.request.AbstractLightblueDataRequest;

public class DataUpdateRequest extends AbstractLightblueDataRequest {

    private Projection projection;
    private Update update;
    private Query query;

    public DataUpdateRequest() {
        super();
    }

    public DataUpdateRequest(String entityName, String entityVersion) {
        super(entityName, entityVersion);
    }

    public DataUpdateRequest(String entityName) {
        super(entityName);
    }

    public void returns(Projection... projection) {
        this.projection = Projection.project(projection);
    }

    @Deprecated
    public void returns(com.redhat.lightblue.client.projection.Projection... projection) {
        Projection[] p = new Projection[projection.length];
        for (int i = 0; i < p.length; i++)
            p[i] = top(projection[i]);
        returns(p);
    }

    @Deprecated
    public void returns(Collection<com.redhat.lightblue.client.projection.Projection> projections) {
        returns(projections.toArray(new com.redhat.lightblue.client.projection.Projection[projections.size()]));
    }

    public void where(Query queryExpression) {
        this.query = queryExpression;
    }

    @Deprecated
    public void where(com.redhat.lightblue.client.expression.query.Query queryExpression) {
        this.query = toq(queryExpression);
    }

    public void updates(Update... updates) {
        update = Update.update(updates);
    }

    @Deprecated
    public void updates(com.redhat.lightblue.client.expression.update.Update... updates) {
        Update[] u = new Update[updates.length];
        for (int i = 0; i < u.length; i++)
            u[i] = tou(updates[i]);
        updates(u);
    }

    @Deprecated
    public void updates(Collection<com.redhat.lightblue.client.expression.update.Update> updates) {
        updates(updates.toArray(new com.redhat.lightblue.client.expression.update.Update[updates.size()]));
    }

    public void setQuery(Query query) {
        where(query);
    }

    @Deprecated
    public void setQuery(com.redhat.lightblue.client.expression.query.Query query) {
        where(query);
    }

    public void setUpdates(Update... updates) {
        updates(updates);
    }

    @Deprecated
    public void setUpdates(com.redhat.lightblue.client.expression.update.Update... updates) {
        updates(updates);
    }

    @Deprecated
    public void setUpdates(Collection<com.redhat.lightblue.client.expression.update.Update> updates) {
        updates(updates.toArray(new com.redhat.lightblue.client.expression.update.Update[updates.size()]));
    }

    public void setProjections(Projection... projections) {
        returns(projections);
    }

    @Deprecated
    public void setProjections(com.redhat.lightblue.client.projection.Projection... projections) {
        returns(projections);
    }

    public void setProjections(Collection<com.redhat.lightblue.client.projection.Projection> projections) {
        returns(projections.toArray(new com.redhat.lightblue.client.projection.Projection[projections.size()]));
    }

    @Override
    public JsonNode getBodyJson() {
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        if (projection != null)
            node.set("projection", projection.toJson());
        node.set("query", query.toJson());
        node.set("update", update.toJson());
        return node;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

    @Override
    public String getOperationPathParam() {
        return "update";
    }

    @Override
    public String getOperation() {
        return Operation.UPDATE.toString();
    }

}
