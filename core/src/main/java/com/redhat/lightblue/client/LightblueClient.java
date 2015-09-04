package com.redhat.lightblue.client;

import com.redhat.lightblue.client.request.AbstractBulkLightblueRequest;
import com.redhat.lightblue.client.request.AbstractLightblueDataRequest;
import com.redhat.lightblue.client.request.LightblueRequest;
import com.redhat.lightblue.client.response.BulkLightblueResponse;
import com.redhat.lightblue.client.response.LightblueException;
import com.redhat.lightblue.client.response.LightblueResponse;

public interface LightblueClient {

	Locking getLocking(String domain);

	LightblueResponse metadata(LightblueRequest lightblueRequest);

	LightblueResponse data(LightblueRequest lightblueRequest) throws LightblueException;

	BulkLightblueResponse data(AbstractBulkLightblueRequest<AbstractLightblueDataRequest> bulkLightblueRequest) throws LightblueException;

	<T> T data(AbstractLightblueDataRequest lightblueRequest, Class<T> type) throws LightblueException;

}
