package com.redhat.lightblue.client.expression.update;

import com.redhat.lightblue.client.expression.query.Query;

/**
 * created by Michael White 10/10/2014
 */

public class ForeachUpdateRemove implements Update {
    
    private String path;
    private Query queryExpression;
    
    public ForeachUpdateRemove( String path, Query queryExpression){
        this.path = path;
        this.queryExpression = queryExpression;
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
        json.append( this.queryExpression.toJson() );
        json.append( ", $update : $remove } }");
        return json.toString();
    }
    
}