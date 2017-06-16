package com.redhat.lightblue.client;

import org.junit.Assert;
import org.junit.Test;

import com.redhat.lightblue.client.MongoExecution.ReadPreference;
import com.redhat.lightblue.client.MongoExecution.WriteConcern;



public class MongoExecutionTest {

    @Test
    public void testWriteConcern() {
        eq("{'writeConcern':'w2'}", MongoExecution.withWriteConcern(WriteConcern.w2));
    }

    @Test
    public void testReSetWriteConcern() {
        MongoExecution e = new MongoExecution();
        e.addWriteConcern(WriteConcern.majority);
        e.addWriteConcern(WriteConcern.unacknowledged);

        eq("{'writeConcern':'unacknowledged'}", e);
    }

    @Test
    public void testAll() {
        MongoExecution e = new MongoExecution();
        e.addMaxQueryTimeMS(1000);
        e.addReadPreference(ReadPreference.nearest);
        e.addWriteConcern(WriteConcern.unacknowledged);

        eq("{'maxQueryTimeMS':1000,'readPreference':'nearest','writeConcern':'unacknowledged'}", e);
    }

    private void eq(String jsonStr, Execution execution) {
        Assert.assertEquals(jsonStr.replaceAll("'", "\""), execution.toString());
    }

}
