package com.redhat.lightblue.client.request;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

import com.redhat.lightblue.client.Execution;
import com.redhat.lightblue.client.Projection;
import com.redhat.lightblue.client.Update;
import com.redhat.lightblue.client.request.data.DataUpdateRequest;

public class TestDataUpdateRequest {

    @Test
    public void testUpdatesAsList() {
        DataUpdateRequest request = new DataUpdateRequest("fake");
        request.updates(Arrays.asList(Update.set("fakeField1", true)));

        assertTrue(request.getBody(), request.getBody().contains("\"fakeField1\":true"));
    }

    @Test
    public void testUpdatesAsArray() {
        DataUpdateRequest request = new DataUpdateRequest("fake");
        request.updates(Update.set("fakeField1", true));

        assertTrue(request.getBody(), request.getBody().contains("\"fakeField1\":true"));
    }

    @Test
    public void testProjectionsAsList(){
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
        request.execution(Execution.MongoController.withReadPreference(Execution.MongoController.ReadPreference.primaryPreferred));

        assertTrue(request.getBody(), request.getBody().contains(
                "\"execution\":{\"readPreference\":\"primaryPreferred\"}"));
    }

    @Test
    public void testExecutionWriteConcern() {
        DataUpdateRequest request = new DataUpdateRequest("fake");
        request.execution(Execution.MongoController.withWriteConcern("majority"));

        assertTrue(request.getBody(), request.getBody().contains(
                "\"execution\":{\"writeConcern\":\"majority\"}"));
    }

    @Test
    public void testExecutionMaxQueryTimeMS() {
        DataUpdateRequest request = new DataUpdateRequest("fake");
        request.execution(Execution.MongoController.withMaxQueryTimeMS(1000));

        assertTrue(request.getBody(), request.getBody().contains(
                "\"execution\":{\"maxQueryTimeMS\":1000"));
    }

}
