package com.redhat.lightblue.client.expression.update;

import java.util.Collection;

/**
 * created by Michael White 10/10/2014
 */
@Deprecated
public class InsertUpdate implements Update {

    private final String path;
    private final Integer index;
    private final RValue[] expressions;

    public InsertUpdate(String path, Integer index, RValue... expressions) {
        this.path = path;
        this.index = index;
        this.expressions = expressions;
    }

    public InsertUpdate(String path, Integer index, Collection<RValue> expressions) {
        this.path = path;
        this.index = index;
        this.expressions = expressions.toArray(new RValue[expressions.size()]);
    }

    @Override
    public String toJson() {
        // http://jewzaam.gitbooks.io/lightblue-specifications/content/language_specification/update.html#array-updates
        StringBuilder json = new StringBuilder("{");
        json.append("$insert:{");
        json.append(this.path).append(".").append(this.index.toString()).append(":");
        if (expressions.length > 1) {
            json.append("[");
        }
        for (int i = 0; i < expressions.length; i++) {
            json.append(expressions[i].toJson());
            if ((this.expressions.length - i) > 1) {
                json.append(","); // append a comma
            }
        }
        if (expressions.length > 1) {
            json.append("]");
        }
        json.append("}");
        return json.toString();
    }

}
