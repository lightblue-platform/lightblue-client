package com.redhat.lightblue.client.expression.update;

/**
 * created by Michael White 10/10/2014
 */
@Deprecated
public class ForeachUpdateAll implements Update {
    private final String path;
    private final Update update;

    public ForeachUpdateAll(String path, Update updateExpression) {
        this.path = path;
        this.update = updateExpression;
    }

    @Override
    public String toJson() {
        // http://jewzaam.gitbooks.io/lightblue-specifications/content/language_specification/update.html#examples-of-foreach
        StringBuilder json = new StringBuilder("{");
        json.append("$foreach:{");
        json.append(this.path);
        json.append(": $all, $update :");
        json.append(this.update.toJson());
        json.append("} }");
        return json.toString();
    }

}
