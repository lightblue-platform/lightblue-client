package com.redhat.lightblue.client;

import java.io.IOException;

import com.redhat.lightblue.client.request.AbstractLightblueDataRequest;
import com.redhat.lightblue.client.request.AbstractLightblueMetadataRequest;
import com.redhat.lightblue.client.request.LightblueRequest;
import com.redhat.lightblue.client.response.LightblueResponse;

public interface LightblueClient {

    LightblueResponse metadata(LightblueRequest lightblueRequest);

    LightblueResponse data(LightblueRequest lightblueRequest);

    <T> T data(AbstractLightblueDataRequest lightblueRequest, Class<T> type) throws IOException;

}
