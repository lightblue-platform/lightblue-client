package com.redhat.lightblue.client.request;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

import com.redhat.lightblue.client.MongoExecution;
import com.redhat.lightblue.client.Projection;
import com.redhat.lightblue.client.Sort;
import com.redhat.lightblue.client.request.data.DataFindRequest;

public class TestDataFindRequest {

    @Test
    public void testFindToString() {
        DataFindRequest request = new DataFindRequest("fake");
        request.select(Arrays.asList(Projection.includeField("*")));

        assertEquals(request.toString(), "POST /find/fake, body: {\"projection\":{\"field\":\"*\",\"include\":true,\"recursive\":false}}");
    }


    @Test
    public void testProjectionsAsList() {
        DataFindRequest request = new DataFindRequest("fake");
        request.select(Arrays.asList(Projection.includeField("*")));

        assertTrue(request.getBody(), request.getBody().contains(
                "\"projection\":{\"field\":\"*\",\"include\":true,\"recursive\":false}"));
    }

    @Test
    public void testProjectionsAsArray() {
        DataFindRequest request = new DataFindRequest("fake");
        request.select(Projection.includeField("*"));

        assertTrue(request.getBody(), request.getBody().contains(
                "\"projection\":{\"field\":\"*\",\"include\":true,\"recursive\":false}"));
    }

    @Test
    public void testSortAsList() {
        DataFindRequest request = new DataFindRequest("fake");
        request.sort(Arrays.asList(Sort.asc("fakeField")));

        assertTrue(request.getBody(), request.getBody().contains(
                "\"sort\":{\"fakeField\":\"$asc\"}"));
    }

    @Test
    public void testSortAsArray() {
        DataFindRequest request = new DataFindRequest("fake");
        request.sort(Sort.asc("fakeField"));

        assertTrue(request.getBody(), request.getBody().contains(
                "\"sort\":{\"fakeField\":\"$asc\"}"));
    }

    @Test
    public void testExecutionReadPreference() {
        DataFindRequest request = new DataFindRequest("fake");
        request.execution(MongoExecution.withReadPreference(MongoExecution.ReadPreference.primaryPreferred));

        assertTrue(request.getBody(), request.getBody().contains(
                "\"execution\":{\"readPreference\":\"primaryPreferred\"}"));
    }

    @Test
    public void testExecutionWriteConcern() {
        DataFindRequest request = new DataFindRequest("fake");
        request.execution(MongoExecution.withWriteConcern("majority"));

        assertTrue(request.getBody(), request.getBody().contains(
                "\"execution\":{\"writeConcern\":\"majority\"}"));
    }

    @Test
    public void testExecutionMaxQueryTimeMS() {
        DataFindRequest request = new DataFindRequest("fake");
        request.execution(MongoExecution.withMaxQueryTimeMS(1000));

        assertTrue(request.getBody(), request.getBody().contains(
                "\"execution\":{\"maxQueryTimeMS\":1000"));
    }

}
