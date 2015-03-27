package com.redhat.lightblue.client.expression.update;

import com.redhat.lightblue.client.expression.query.Query;

/**
 * created by Michael White 10/10/2014
 */
public class ForeachUpdateRemove implements Update {
    private final String path;
    private final Query queryExpression;

    public ForeachUpdateRemove(String path, Query queryExpression) {
        this.path = path;
        this.queryExpression = queryExpression;
    }

    @Override
    public String toJson() {
        // http://jewzaam.gitbooks.io/lightblue-specifications/content/language_specification/update.html#examples-of-foreach
        StringBuilder json = new StringBuilder("{");
        json.append("\"$foreach\":{");
        json.append("\"").append(this.path).append("\"");
        json.append(":");
        json.append(this.queryExpression.toJson());
        json.append(", \"$update\" : \"$remove\" } }");
        return json.toString();
    }

}
