package com.redhat.lightblue.client.update;

import com.redhat.lightblue.client.query.QueryExpression;

/**
 * created by Michael White 10/10/2014
 */

public class ForeachUpdate implements UpdateExpression {
    
    private String path;
    private QueryExpression query;
    private UpdateExpression update;
    
    public ForeachUpdate( String path, QueryExpression queryExpression, UpdateExpression updateExpression ){
        this.path = path;
        this.query = queryExpression;
        this.update = updateExpression;
    }
    
    public String toJson() {
        /*
         * { $foreach : { path : update_query_expression, $update : foreach_update_expression } }
         * update_query_expression := $all | query_expression
         * foreach_update_expression := $remove | update_expression
         */
        StringBuilder json = new StringBuilder("{");
        json.append("$foreach:{");
        json.append(this.path);
        json.append(":");
        json.append( this.query.toJson() );
        json.append( ", $update :");
        json.append( this.update.toJson() );
        json.append("} }");
        return json.toString();
    }
    
}