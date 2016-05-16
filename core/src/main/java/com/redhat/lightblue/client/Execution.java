package com.redhat.lightblue.client;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.redhat.lightblue.client.Execution.MongoController.ReadPreference;

/**
 * https://jewzaam.gitbooks.io/lightblue-specifications/content/language_specification/execution.html
 *
 * @author mpatercz
 *
 */
public class Execution extends Expression {

    public static class MongoController {

        public static final String READ_PREFERENCE = "readPreference",
                WRITE_CONCERN = "writeConcern",
                MAX_QUERY_TIME_MS = "maxQueryTimeMS";

        public enum ReadPreference {
            nearest, primaryPreferred, primary, secondary, secondaryPreferred;
        }

        public static Execution withReadPreference(ReadPreference readPreference) {
            return new Execution().addReadPreference(readPreference);
        }

        public static Execution withWriteConcern(String writeConcern) {
            return new Execution().addWriteConcern(writeConcern);
        }

        public static Execution withMaxQueryTimeMS(int maxQueryTimeMS) {
            return new Execution().addMaxQueryTimeMS(maxQueryTimeMS);
        }
    }

    public Execution addReadPreference(ReadPreference readPreference) {
        this.add(MongoController.READ_PREFERENCE, readPreference.toString());
        return this;
    }

    public Execution addWriteConcern(String writeConcern) {
        this.add(MongoController.WRITE_CONCERN, writeConcern);
        return this;
    }

    public Execution addMaxQueryTimeMS(int maxQueryTimeMS) {
        this.add(MongoController.MAX_QUERY_TIME_MS, JsonNodeFactory.instance.numberNode(maxQueryTimeMS));
        return this;
    }

    private Execution() {
        super(false);
    }

}
