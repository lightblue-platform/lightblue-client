/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.redhat.lightblue.client.hystrix;

import com.redhat.lightblue.client.LightblueClient;
import com.redhat.lightblue.client.LightblueException;
import com.redhat.lightblue.client.Locking;
import com.redhat.lightblue.client.request.DataBulkRequest;
import com.redhat.lightblue.client.request.LightblueDataRequest;
import com.redhat.lightblue.client.request.LightblueMetadataRequest;
import com.redhat.lightblue.client.response.LightblueBulkDataResponse;
import com.redhat.lightblue.client.response.LightblueDataResponse;
import com.redhat.lightblue.client.response.LightblueMetadataResponse;
import org.junit.Assert;
import org.junit.Test;

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
        boolean dataBulk = false;

        @Override
        public LightblueMetadataResponse metadata(LightblueMetadataRequest lightblueRequest) {
            metadata = true;
            return null;
        }

        @Override
        public LightblueDataResponse data(LightblueDataRequest lightblueRequest) {
            data = true;
            return null;
        }

        @Override
        public <T> T data(LightblueDataRequest lightblueRequest, Class<T> type) throws LightblueException {
            dataType = true;
            return null;
        }
        
        @Override
        public LightblueBulkDataResponse bulkData(DataBulkRequest bulkLightblueRequest) throws LightblueException {
            dataBulk = true;
            return null;
        }

        @Override
        public Locking getLocking(String domain) {
            return null;
        }

    }

    @Test
    public void metadata() {
        TestLightblueClient client = new TestLightblueClient();
        LightblueHystrixClient hystrixClient = new LightblueHystrixClient(client, "group", "command");

        Assert.assertFalse(client.metadata);
        Assert.assertFalse(client.data);
        Assert.assertFalse(client.dataBulk);
        Assert.assertFalse(client.dataType);

        hystrixClient.metadata(null);

        Assert.assertTrue(client.metadata);
        Assert.assertFalse(client.data);
        Assert.assertFalse(client.dataBulk);
        Assert.assertFalse(client.dataType);
    }

    @Test
    public void data() {
        TestLightblueClient client = new TestLightblueClient();
        LightblueHystrixClient hystrixClient = new LightblueHystrixClient(client, "group", "command");

        Assert.assertFalse(client.metadata);
        Assert.assertFalse(client.data);
        Assert.assertFalse(client.dataBulk);
        Assert.assertFalse(client.dataType);

        hystrixClient.data(null);

        Assert.assertFalse(client.metadata);
        Assert.assertTrue(client.data);
        Assert.assertFalse(client.dataBulk);
        Assert.assertFalse(client.dataType);
    }

    @Test
    public void dataBulk() throws LightblueException {
        TestLightblueClient client = new TestLightblueClient();
        LightblueHystrixClient hystrixClient = new LightblueHystrixClient(client, "group", "command");

        Assert.assertFalse(client.metadata);
        Assert.assertFalse(client.data);
        Assert.assertFalse(client.dataBulk);
        Assert.assertFalse(client.dataType);

        hystrixClient.bulkData(null);

        Assert.assertFalse(client.metadata);
        Assert.assertFalse(client.data);
        Assert.assertTrue(client.dataBulk);
        Assert.assertFalse(client.dataType);
    }

    @Test
    public void dataType() throws LightblueException {
        TestLightblueClient client = new TestLightblueClient();
        LightblueHystrixClient hystrixClient = new LightblueHystrixClient(client, "group", "command");

        Assert.assertFalse(client.metadata);
        Assert.assertFalse(client.data);
        Assert.assertFalse(client.dataBulk);
        Assert.assertFalse(client.dataType);

        hystrixClient.data(null, Object.class);

        Assert.assertFalse(client.metadata);
        Assert.assertFalse(client.data);
        Assert.assertFalse(client.dataBulk);
        Assert.assertTrue(client.dataType);
    }
}
