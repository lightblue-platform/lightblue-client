package com.redhat.lightblue.client;

import com.redhat.lightblue.client.request.DataBulkRequest;
import com.redhat.lightblue.client.request.LightblueDataRequest;
import com.redhat.lightblue.client.request.LightblueHealthRequest;
import com.redhat.lightblue.client.request.LightblueMetadataRequest;
import com.redhat.lightblue.client.request.data.DataFindRequest;
import com.redhat.lightblue.client.response.LightblueBulkDataResponse;
import com.redhat.lightblue.client.response.LightblueDataResponse;
import com.redhat.lightblue.client.response.LightblueHealthResponse;
import com.redhat.lightblue.client.response.LightblueMetadataResponse;

public interface LightblueClient {

    Locking getLocking(String domain);

    LightblueMetadataResponse metadata(LightblueMetadataRequest lightblueRequest) throws LightblueException;

    LightblueDataResponse data(LightblueDataRequest lightblueRequest) throws LightblueException;

    LightblueBulkDataResponse bulkData(DataBulkRequest requests) throws LightblueException;

    <T> T data(LightblueDataRequest lightblueRequest, Class<T> type) throws LightblueException;

    /**
     * Prepares a resultset stream based on the find request. The find
     * request will be run when the client calls ResultStream.run
     */
    ResultStream prepareFind(DataFindRequest req) throws LightblueException;

    LightblueHealthResponse lightblueHealth(LightblueHealthRequest lightblueHealthRequest) throws LightblueException;
}
