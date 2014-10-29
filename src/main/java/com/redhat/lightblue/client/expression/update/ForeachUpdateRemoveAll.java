package com.redhat.lightblue.client.expression.update;

/**
 * created by Michael White 10/10/2014
 */

public class ForeachUpdateRemoveAll implements Update {
    
    private String path;
    
    public ForeachUpdateRemoveAll( String path ){
        this.path = path;
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
        json.append(": $all, $update : $remove } }");
        return json.toString();
    }
    
}