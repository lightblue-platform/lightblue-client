package com.redhat.lightblue.client.expression.update;

import java.util.Collection;

import com.redhat.lightblue.client.util.JSON;

/**
 * created by Michael White 10/10/2014
 */
public class SetUpdate implements Update {
    PathValuePair[] pathValuePairs;

    public SetUpdate(PathValuePair... statements) {
        pathValuePairs = statements;
    }

    public SetUpdate(Collection<PathValuePair> statements) {
        if (statements != null) {
            pathValuePairs = statements.toArray(new PathValuePair[statements.size()]);
        }
    }

    @Override
    public String toJson() {
        StringBuilder json = new StringBuilder("{");
        json.append(JSON.toJson("$set")).append(":{");
        for (int index = 0; index < pathValuePairs.length; index++) {
            json.append(pathValuePairs[index].toJson());
            if ((this.pathValuePairs.length - index) > 1) {
                json.append(", ");
            }
        }
        json.append("}}");
        return json.toString();
    }

}
