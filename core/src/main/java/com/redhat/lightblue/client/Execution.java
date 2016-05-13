package com.redhat.lightblue.client;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;

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
            nearest, primaryPreferred, secondary, secondaryPreferred;
        }

        public static Execution withReadPreference(ReadPreference readPreference) {
            Execution e = new Execution();
            e.add(READ_PREFERENCE, readPreference.toString());
            return e;
        }

        public static Execution withWriteConcern(String writeConcern) {
            Execution e = new Execution();
            e.add(WRITE_CONCERN, writeConcern);
            return e;
        }

        public static Execution withMaxQueryTime(int maxQueryTimeMS) {
            Execution e = new Execution();
            e.add(MAX_QUERY_TIME_MS, JsonNodeFactory.instance.numberNode(maxQueryTimeMS));
            return e;
        }
    }

    public Execution() {
        super(false);
    }

}
