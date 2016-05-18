package com.redhat.lightblue.client.request;

import org.junit.Assert;

import java.util.Arrays;

import org.junit.Test;

import com.redhat.lightblue.client.request.data.GenerateRequest;

public class TestGenerateRequest {

    @Test
    public void testGenerateCall() {
        GenerateRequest request = new GenerateRequest("fake");
        request.path("x.y.*.z").nValues(100);
        Assert.assertEquals("/generate/fake/x.y.*.z?n=100", request.getRestURI(""));
    }

}
