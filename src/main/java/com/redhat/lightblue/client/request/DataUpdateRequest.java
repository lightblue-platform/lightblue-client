package com.redhat.lightblue.client.request;

import com.redhat.lightblue.client.enums.RequestType;
import com.redhat.lightblue.client.expression.query.Query;
import com.redhat.lightblue.client.expression.update.Update;
import com.redhat.lightblue.client.projection.Projection;

import java.util.Collection;

public class DataUpdateRequest extends AbstractLightblueRequest {

    private Projection[] projections;
    private Update[] updates;
    private Query query;

    public DataUpdateRequest() { }
    
    public DataUpdateRequest( String entityName, String entityVersion ) {
        this.setEntityName(entityName);
        this.setEntityVersion(entityVersion);
    }
    
	@Override
	public RequestType getRequestType() {
		return RequestType.DATA_UPDATE;
	}

    public void returns(Projection... projections){
        this.setProjections( projections );
    }
    
    public void returns( Collection<Projection> projections) {
        this.setProjections( projections );
    }
    
    public void where( Query query ) {
        this.setQuery(query);
    }
    
    public void updates( Update... updates ) {
        this.setUpdates( updates );
    }
    
    public void updates( Collection<Update> updates ) {
        this.setUpdates( updates );
    }
    
    public void setQuery( Query query ) {
        this.query = query;
    }
    
    public void setUpdates( Update... updates ) {
        this.updates = updates;
    }
    
    public void setUpdates( Collection<Update> updates ) {
        this.updates = updates.toArray( new Update[updates.size()] );
    }
    
    public void setProjections( Projection... projections ) {
        this.projections = projections;
    }
    
    public void setProjections( Collection<Projection> projections ) {
        this.projections = projections.toArray( new Projection[projections.size()] );
    }
    
    @Override
    public String getBody() {
        
        /*
         * {
         *   <common stuff>,
         *   query: <query_expression,
         *   update: <update_expression>
         *   projection : <projection>
         * }
         */
        StringBuffer sb = new StringBuffer();
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

}
