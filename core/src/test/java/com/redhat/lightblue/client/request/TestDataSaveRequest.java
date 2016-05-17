package com.redhat.lightblue.client.request;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

import com.redhat.lightblue.client.Execution;
import com.redhat.lightblue.client.Projection;
import com.redhat.lightblue.client.request.data.DataSaveRequest;

public class TestDataSaveRequest {

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
        request.execution(Execution.withReadPreference(Execution.ReadPreference.primaryPreferred));
        request.create("");

        assertTrue(request.getBody(), request.getBody().contains(
                "\"execution\":{\"readPreference\":\"primaryPreferred\"}"));
    }

    @Test
    public void testExecutionWriteConcern() {
        DataSaveRequest request = new DataSaveRequest("fake");
        request.execution(Execution.withWriteConcern("majority"));
        request.create("");

        assertTrue(request.getBody(), request.getBody().contains(
                "\"execution\":{\"writeConcern\":\"majority\"}"));
    }

    @Test
    public void testExecutionMaxQueryTimeMS() {
        DataSaveRequest request = new DataSaveRequest("fake");
        request.execution(Execution.withMaxQueryTimeMS(1000));
        request.create("");

        assertTrue(request.getBody(), request.getBody().contains(
                "\"execution\":{\"maxQueryTimeMS\":1000"));
    }
}
