package com.redhat.lightblue.client.expression.update;

import java.util.Collection;

/**
 * created by Michael White 10/10/2014
 */
public class AddUpdate implements Update {
    private final PathValuePair[] statements;

    public AddUpdate(PathValuePair... statements) {
        this.statements = statements;
    }

    public AddUpdate(Collection<PathValuePair> statements) {
        this.statements = statements.toArray(new PathValuePair[statements.size()]);
    }

    @Override
    public String toJson() {
        StringBuilder builder = new StringBuilder();

        builder.append("{\"$add\":");
        for (int index = 0; index < this.statements.length; index++) {
            builder.append(this.statements[index].toJson());
            // if there's more than one element left...
            if ((this.statements.length - index) > 1) {
                builder.append(","); // append a comma
            }
        }
        builder.append("}");

        return builder.toString();
    }

}
