/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.redhat.lightblue.client.hystrix;

import com.redhat.lightblue.client.LightblueClient;
import com.redhat.lightblue.client.request.LightblueRequest;
import com.redhat.lightblue.client.response.LightblueResponse;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

/**
 *
 * @author nmalik
 */
public class LightblueHystrixClientTest {
    /**
     * Mock LightblueClient for testing.
     */
    private class TestLightblueClient implements LightblueClient {
        boolean metadata = false;
        boolean data = false;
        boolean dataType = false;

        @Override
        public LightblueResponse metadata(LightblueRequest lightblueRequest) {
            metadata = true;
            return null;
        }

        @Override
        public LightblueResponse data(LightblueRequest lightblueRequest) {
            data = true;
            return null;
        }

        @Override
        public <T> T data(LightblueRequest lightblueRequest, Class<T> type) throws IOException {
            dataType = true;
            return null;
        }
    }

    @Test
    public void metadata() {
        TestLightblueClient client = new TestLightblueClient();
        LightblueHystrixClient hystrixClient = new LightblueHystrixClient(client, "group", "command");

        Assert.assertFalse(client.metadata);
        Assert.assertFalse(client.data);
        Assert.assertFalse(client.dataType);

        hystrixClient.metadata(null);

        Assert.assertTrue(client.metadata);
        Assert.assertFalse(client.data);
        Assert.assertFalse(client.dataType);
    }

    @Test
    public void data() {
        TestLightblueClient client = new TestLightblueClient();
        LightblueHystrixClient hystrixClient = new LightblueHystrixClient(client, "group", "command");

        Assert.assertFalse(client.metadata);
        Assert.assertFalse(client.data);
        Assert.assertFalse(client.dataType);

        hystrixClient.data(null);

        Assert.assertFalse(client.metadata);
        Assert.assertTrue(client.data);
        Assert.assertFalse(client.dataType);
    }

    @Test
    public void dataType() throws IOException {
        TestLightblueClient client = new TestLightblueClient();
        LightblueHystrixClient hystrixClient = new LightblueHystrixClient(client, "group", "command");

        Assert.assertFalse(client.metadata);
        Assert.assertFalse(client.data);
        Assert.assertFalse(client.dataType);

        hystrixClient.data(null, Object.class);

        Assert.assertFalse(client.metadata);
        Assert.assertFalse(client.data);
        Assert.assertTrue(client.dataType);
    }
}
