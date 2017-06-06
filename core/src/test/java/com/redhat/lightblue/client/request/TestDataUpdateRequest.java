package com.redhat.lightblue.client.request;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

import com.redhat.lightblue.client.MongoExecution;
import com.redhat.lightblue.client.MongoExecution.WriteConcern;
import com.redhat.lightblue.client.Projection;
import com.redhat.lightblue.client.Update;
import com.redhat.lightblue.client.request.data.DataUpdateRequest;

public class TestDataUpdateRequest {

    @Test
    public void testUpdateToString() {
        DataUpdateRequest request = new DataUpdateRequest("fake");
        request.updates(Arrays.asList(Update.set("fakeField1", true)));

        assertEquals(request.toString(), "POST /update/fake, body: {\"update\":{\"$set\":{\"fakeField1\":true}}}");
    }

    @Test
    public void testUpdatesAsList() {
        DataUpdateRequest request = new DataUpdateRequest("fake");
        request.updates(Arrays.asList(Update.set("fakeField1", true)));

        assertTrue(request.getBody(), request.getBody().contains("\"fakeField1\":true"));
    }

    @Test
    public void testUpdateIfCurrent() {
        DataUpdateRequest request = new DataUpdateRequest("fake");
        request.ifCurrent("blah");

        assertTrue(request.getBody(), request.getBody().contains("\"onlyIfCurrent\":true"));
        assertTrue(request.getBody(), request.getBody().contains("\"documentVersions\":[\"blah\"]"));
                   
    }

    @Test
    public void testUpdatesAsArray() {
        DataUpdateRequest request = new DataUpdateRequest("fake");
        request.updates(Update.set("fakeField1", true));

        assertTrue(request.getBody(), request.getBody().contains("\"fakeField1\":true"));
    }

    @Test
    public void testProjectionsAsList() {
        DataUpdateRequest request = new DataUpdateRequest("fake");
        request.returns(Arrays.asList(Projection.includeField("*")));

        assertTrue(request.getBody(), request.getBody().contains(
                "\"projection\":{\"field\":\"*\",\"include\":true,\"recursive\":false}"));
    }

    @Test
    public void testProjectionsAsArray() {
        DataUpdateRequest request = new DataUpdateRequest("fake");
        request.returns(Projection.includeField("*"));

        assertTrue(request.getBody(), request.getBody().contains(
                "\"projection\":{\"field\":\"*\",\"include\":true,\"recursive\":false}"));
    }

    @Test
    public void testExecutionReadPreference() {
        DataUpdateRequest request = new DataUpdateRequest("fake");
        request.execution(MongoExecution.withReadPreference(MongoExecution.ReadPreference.primaryPreferred));

        assertTrue(request.getBody(), request.getBody().contains(
                "\"execution\":{\"readPreference\":\"primaryPreferred\"}"));
    }

    @Test
    public void testExecutionWriteConcern() {
        DataUpdateRequest request = new DataUpdateRequest("fake");
        request.execution(MongoExecution.withWriteConcern(WriteConcern.majority));

        assertTrue(request.getBody(), request.getBody().contains(
                "\"execution\":{\"writeConcern\":\"majority\"}"));
    }

    @Test
    public void testExecutionMaxQueryTimeMS() {
        DataUpdateRequest request = new DataUpdateRequest("fake");
        request.execution(MongoExecution.withMaxQueryTimeMS(1000));

        assertTrue(request.getBody(), request.getBody().contains(
                "\"execution\":{\"maxQueryTimeMS\":1000"));
    }

}
