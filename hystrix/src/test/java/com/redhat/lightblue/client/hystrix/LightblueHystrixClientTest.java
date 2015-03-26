/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/*
 Copyright 2015 Red Hat, Inc. and/or its affiliates.

 This file is part of lightblue.

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.redhat.lightblue.client.hystrix;

import com.redhat.lightblue.client.LightblueClient;
import com.redhat.lightblue.client.request.LightblueRequest;
import com.redhat.lightblue.client.response.LightblueResponse;
import java.io.IOException;
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
