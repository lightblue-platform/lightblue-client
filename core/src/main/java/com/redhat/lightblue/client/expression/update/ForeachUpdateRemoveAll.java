package com.redhat.lightblue.client.expression.update;

/**
 * created by Michael White 10/10/2014
 */
public class ForeachUpdateRemoveAll implements Update {
    private final String path;

    public ForeachUpdateRemoveAll(String path) {
        this.path = path;
    }

    @Override
    public String toJson() {
        // http://jewzaam.gitbooks.io/lightblue-specifications/content/language_specification/update.html#examples-of-foreach
        StringBuilder json = new StringBuilder("{");
        json.append("$foreach:{");
        json.append(this.path);
        json.append(": $all, $update : $remove } }");
        return json.toString();
    }

}
