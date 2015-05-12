package com.redhat.lightblue.client.test.request;

import com.redhat.lightblue.client.request.data.DataUpdateRequest;

/**
 * Created by jblashka on 10/20/14.
 */
public class DataUpdateRequestStub extends DataUpdateRequest {
    private final String body;

    public DataUpdateRequestStub(String entityName, String entityVersion, String body) {
        super(entityName, entityVersion);
        this.body = body;
    }

    @Override
    public String getBody() {
        return body;
    }

}
