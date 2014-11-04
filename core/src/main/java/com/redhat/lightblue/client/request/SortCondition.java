package com.redhat.lightblue.client.request;

import com.redhat.lightblue.client.enums.SortDirection;

/**
 * Created by bmiller and vkumar on 10/10/14.
 */
public class SortCondition {
    public final String field;
    public final SortDirection direction;

    public SortCondition(String field, SortDirection direction) {
          this.field = field;
          this.direction = direction;
    }

    public String toJson() {
        StringBuilder json = new StringBuilder("{");
        json.append("\"").append(field).append(("\":"));
        json.append("\"").append(direction.toString()).append(("\""));
        json.append("}");

        return json.toString();
    }
}
