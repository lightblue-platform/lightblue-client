package com.redhat.lightblue.client.expression.query;

/**
 * This is what you can use when you have a JSON query fragment and
 * insert that into an existing query, or use it as your query string.
 */
@Deprecated
public class LiteralQuery implements Query {
    private String q;

    public LiteralQuery() {}
    
    public LiteralQuery(String q) {
        this.q=q;
    }

    public String getQuery() {
        return q;
    }

    public void setQuery(String q) {
        this.q=q;
    }

    @Override
    public String toJson() {
        return q;
    }
}
