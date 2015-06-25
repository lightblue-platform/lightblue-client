package com.redhat.lightblue.client.expression.update;

import java.util.Collection;

import com.redhat.lightblue.client.expression.query.Query;

public class UpdateExpressionList implements Update {

    private final Update[] update;

    public UpdateExpressionList(Update... u) {
        update=u;
    }

    public UpdateExpressionList(Collection<Update> c) {
        update=c.toArray(new Update[c.size()]);
    }

    @Override
    public String toJson() {
        if(update.length==1)
            return update[0].toJson();
        else {
            StringBuilder json = new StringBuilder();
            json.append('[');
            boolean first=true;
            for(Update u:update) {
                if(first)
                    first=false;
                else
                    json.append(',');
                json.append(u.toJson());
            }
            json.append(']');
            return json.toString();
        }
    }

}
