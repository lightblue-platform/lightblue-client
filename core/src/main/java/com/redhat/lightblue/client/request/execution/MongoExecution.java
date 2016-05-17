package com.redhat.lightblue.client.request.execution;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;

public class MongoExecution extends Execution {

    public static final String READ_PREFERENCE = "readPreference";
    public static final String WRITE_CONCERN = "writeConcern";
    public static final String MAX_QUERY_TIME_MS = "maxQueryTimeMS";

    public enum ReadPreference {
        nearest, primaryPreferred, primary, secondary, secondaryPreferred;
    }

    public static MongoExecution withReadPreference(ReadPreference readPreference) {
        return new MongoExecution().addReadPreference(readPreference);
    }

    public static MongoExecution withWriteConcern(String writeConcern) {
        return new MongoExecution().addWriteConcern(writeConcern);
    }

    public static MongoExecution withMaxQueryTimeMS(int maxQueryTimeMS) {
        return new MongoExecution().addMaxQueryTimeMS(maxQueryTimeMS);
    }

    public MongoExecution addReadPreference(ReadPreference readPreference) {
        this.add(READ_PREFERENCE, readPreference.toString());
        return this;
    }

    public MongoExecution addWriteConcern(String writeConcern) {
        this.add(WRITE_CONCERN, writeConcern);
        return this;
    }

    public MongoExecution addMaxQueryTimeMS(int maxQueryTimeMS) {
        this.add(MAX_QUERY_TIME_MS, JsonNodeFactory.instance.numberNode(
                maxQueryTimeMS));
        return this;
    }

    private MongoExecution() {
        super();
    }

}
