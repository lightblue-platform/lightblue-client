package com.redhat.lightblue.client.expression.update;

import java.util.Collection;

/**
 * created by Michael White 10/10/2014
 */
public class AppendUpdate implements Update {
    private final String path;
    private final RValue[] expressions;

    public AppendUpdate(String path, RValue... expressions) {
        this.path = path;
        this.expressions = expressions;
    }

    public AppendUpdate(String path, Collection<RValue> expressions) {
        this.path = path;
        this.expressions = expressions.toArray(new RValue[expressions.size()]);
    }

    @Override
    public String toJson() {
        // http://jewzaam.gitbooks.io/lightblue-specifications/content/language_specification/update.html#array-updates
        StringBuilder json = new StringBuilder("{");
        json.append("\"$append\":{");
        json.append("\"").append(this.path).append("\":");
        if (expressions.length > 1) {
            json.append("[");
        }
        for (int index = 0; index < expressions.length; index++) {
            json.append(expressions[index].toJson());
            if ((this.expressions.length - index) > 1) {
                json.append(","); // append a comma
            }
        }
        if (expressions.length > 1) {
            json.append("]");
        }
        json.append("}"); // close append
        json.append("}"); // close main block
        return json.toString();
    }

}