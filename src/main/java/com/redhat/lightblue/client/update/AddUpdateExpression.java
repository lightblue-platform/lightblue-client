package com.redhat.lightblue.client.update;

import java.lang.StringBuffer;
import java.util.Collection;

/**
 * created by Michael White 10/10/2014
 */

public class AddUpdateExpression implements UpdateExpression {
    
    private PathValuePair[] statements;
    
    public AddUpdateExpression(PathValuePair... statements ){
        this.statements = statements;
    }
    
    public AddUpdateExpression(Collection<PathValuePair> statements ){
        this.statements = statements.toArray( new PathValuePair[statements.size()] );
    }
    
    public String toJson() {
        // { "$set" : { path : value } }
        StringBuffer builder = new StringBuffer();
        
        builder.append("{\"$add\":");
        if( this.statements.length > 1 ){
            builder.append("[");
        }
        for( int index = 0; index < this.statements.length; index++ ){
            builder.append( this.statements[index].toJson() );
            // if there's more than one element left...
            if( ( this.statements.length - index ) > 1 ){
                builder.append(","); // append a comma
            }
        }
        if( this.statements.length > 1 ){
            builder.append("]");
        }
        builder.append("}");
        
        return builder.toString();
    }
    
}