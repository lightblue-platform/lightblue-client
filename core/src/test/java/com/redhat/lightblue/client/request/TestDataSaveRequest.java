package com.redhat.lightblue.client.request;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

import com.redhat.lightblue.client.MongoExecution;
import com.redhat.lightblue.client.Projection;
import com.redhat.lightblue.client.request.data.DataSaveRequest;

public class TestDataSaveRequest {

    @Test
    public void testSaveToString() {
        DataSaveRequest request = new DataSaveRequest("fake");
        request.returns(Arrays.asList(Projection.includeField("*")));
        request.create("");

        assertEquals(request.toString(), "POST /save/fake, body: {\"projection\":{\"field\":\"*\",\"include\":true,\"recursive\":false},\"data\":\"\"}");
    }

    @Test
    public void testProjectionsAsList() {
        DataSaveRequest request = new DataSaveRequest("fake");
        request.returns(Arrays.asList(Projection.includeField("*")));
        request.create("");

        assertTrue(request.getBody(), request.getBody().contains(
                "\"projection\":{\"field\":\"*\",\"include\":true,\"recursive\":false}"));
    }

    @Test
    public void testProjectionsAsArray() {
        DataSaveRequest request = new DataSaveRequest("fake");
        request.returns(Projection.includeField("*"));
        request.create("");

        assertTrue(request.getBody(), request.getBody().contains(
                "\"projection\":{\"field\":\"*\",\"include\":true,\"recursive\":false}"));
    }

    @Test
    public void testExecutionReadPreference() {
        DataSaveRequest request = new DataSaveRequest("fake");
        request.execution(MongoExecution.withReadPreference(MongoExecution.ReadPreference.primaryPreferred));
        request.create("");

        assertTrue(request.getBody(), request.getBody().contains(
                "\"execution\":{\"readPreference\":\"primaryPreferred\"}"));
    }

    @Test
    public void testUpdateIfCurrent() {
        DataSaveRequest request = new DataSaveRequest("fake");
        request.ifCurrent("blah");
        request.create("");

        assertTrue(request.getBody(), request.getBody().contains("\"onlyIfCurrent\":true"));
        assertTrue(request.getBody(), request.getBody().contains("\"documentVersions\":[\"blah\"]"));
                   
    }

    @Test
    public void testExecutionWriteConcern() {
        DataSaveRequest request = new DataSaveRequest("fake");
        request.execution(MongoExecution.withWriteConcern("majority"));
        request.create("");

        assertTrue(request.getBody(), request.getBody().contains(
                "\"execution\":{\"writeConcern\":\"majority\"}"));
    }

    @Test
    public void testExecutionMaxQueryTimeMS() {
        DataSaveRequest request = new DataSaveRequest("fake");
        request.execution(MongoExecution.withMaxQueryTimeMS(1000));
        request.create("");

        assertTrue(request.getBody(), request.getBody().contains(
                "\"execution\":{\"maxQueryTimeMS\":1000"));
    }
}
