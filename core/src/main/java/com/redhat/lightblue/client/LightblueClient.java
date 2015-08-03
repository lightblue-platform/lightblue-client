package com.redhat.lightblue.client;

import com.redhat.lightblue.client.request.AbstractLightblueDataRequest;
import com.redhat.lightblue.client.request.LightblueRequest;
import com.redhat.lightblue.client.response.LightblueException;
import com.redhat.lightblue.client.response.LightblueResponse;

public interface LightblueClient {

    Locking getLocking(String domain);
    
    LightblueResponse metadata(LightblueRequest lightblueRequest);

    LightblueResponse data(LightblueRequest lightblueRequest) throws LightblueException;

    <T> T data(AbstractLightblueDataRequest lightblueRequest, Class<T> type) throws LightblueException;

}
