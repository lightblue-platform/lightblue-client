package com.redhat.lightblue.client.response;

import java.util.List;

import com.redhat.lightblue.client.request.AbstractLightblueRequest;
import com.redhat.lightblue.client.request.LightblueRequest;

public interface LightblueBulkDataResponse extends LightblueResponse {

    LightblueDataResponse getResponse(LightblueRequest lbr);

    LightblueDataResponse getResponse(int seq);

    List<LightblueDataResponse> getResponses();

    List<? extends AbstractLightblueRequest> getRequests();

}
