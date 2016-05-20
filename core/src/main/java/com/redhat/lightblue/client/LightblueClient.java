package com.redhat.lightblue.client;

import com.redhat.lightblue.client.request.DataBulkRequest;
import com.redhat.lightblue.client.request.LightblueDataRequest;
import com.redhat.lightblue.client.request.LightblueRequest;
import com.redhat.lightblue.client.request.AbstractLightblueMetadataRequest;
import com.redhat.lightblue.client.response.LightblueBulkDataResponse;
import com.redhat.lightblue.client.response.LightblueDataResponse;
import com.redhat.lightblue.client.response.LightblueMetadataResponse;

public interface LightblueClient {

    Locking getLocking(String domain);

    LightblueMetadataResponse metadata(AbstractLightblueMetadataRequest lightblueRequest) throws LightblueException;

    LightblueDataResponse data(LightblueDataRequest lightblueRequest) throws LightblueException;

    LightblueBulkDataResponse bulkData(DataBulkRequest requests) throws LightblueException;

    <T> T data(LightblueDataRequest lightblueRequest, Class<T> type) throws LightblueException;

}
