package com.redhat.lightblue.client.expression.update;

import com.redhat.lightblue.client.expression.query.Query;

/**
 * created by Michael White 10/10/2014
 */
@Deprecated
public class ForeachUpdate implements Update {
    private final String path;
    private final Query query;
    private final Update update;

    public ForeachUpdate(String path, Query queryExpression, Update updateExpression) {
        this.path = path;
        this.query = queryExpression;
        this.update = updateExpression;
    }

    @Override
    public String toJson() {
        // http://jewzaam.gitbooks.io/lightblue-specifications/content/language_specification/update.html#examples-of-foreach
        StringBuilder json = new StringBuilder("{");
        json.append("\"$foreach\":{");
        json.append("\"").append(this.path).append("\"");
        json.append(":");
        json.append(this.query.toJson());
        json.append(", \"$update\" :");
        json.append(this.update.toJson());
        json.append("} }");
        return json.toString();
    }

}
