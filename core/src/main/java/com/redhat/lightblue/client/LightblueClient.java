package com.redhat.lightblue.client;

import java.io.IOException;

import com.redhat.lightblue.client.request.AbstractLightblueDataRequest;
import com.redhat.lightblue.client.request.AbstractLightblueMetadataRequest;
import com.redhat.lightblue.client.response.LightblueResponse;

public interface LightblueClient {

    public abstract LightblueResponse metadata(AbstractLightblueMetadataRequest lightblueRequest);

    public abstract LightblueResponse data(AbstractLightblueDataRequest lightblueRequest);

    public abstract <T> T data(AbstractLightblueDataRequest lightblueRequest, Class<T> type) throws IOException;

}
