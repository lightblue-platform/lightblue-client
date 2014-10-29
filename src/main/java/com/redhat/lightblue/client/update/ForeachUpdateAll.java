package com.redhat.lightblue.client.update;

/**
 * created by Michael White 10/10/2014
 */

public class ForeachUpdateAll implements UpdateExpression {
    
    private String path;
    private UpdateExpression update;
    
    public ForeachUpdateAll( String path, UpdateExpression updateExpression ){
        this.path = path;
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
        json.append(": $all, $update :");
        json.append( this.update.toJson() );
        json.append("} }");
        return json.toString();
    }
    
}