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

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.redhat.lightblue.client.LightblueClient;
import com.redhat.lightblue.client.request.LightblueRequest;
import com.redhat.lightblue.client.response.LightblueResponse;
import com.redhat.lightblue.hystrix.ServoGraphiteSetup;
import java.io.IOException;

/**
 * An implementation of LightblueClient that uses hystrix commands to execute
 * operations.
 *
 * @author nmalik
 */
public class LightblueHystrixClient implements LightblueClient {
    static {
        ServoGraphiteSetup.initialize();
    }

    protected class MetadataHystrixCommand extends HystrixCommand<LightblueResponse> {
        private final LightblueRequest request;

        public MetadataHystrixCommand(LightblueRequest request, String groupKey, String commandKey) {
            super(HystrixCommand.Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey(groupKey)).
                    andCommandKey(HystrixCommandKey.Factory.asKey(groupKey + ":" + commandKey)));

            this.request = request;
        }

        @Override
        protected LightblueResponse run() throws Exception {
            return client.metadata(request);
        }
    }

    protected class DataHystrixCommand extends HystrixCommand<LightblueResponse> {
        private final LightblueRequest request;

        public DataHystrixCommand(LightblueRequest request, String groupKey, String commandKey) {
            super(HystrixCommand.Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey(groupKey)).
                    andCommandKey(HystrixCommandKey.Factory.asKey(groupKey + ":" + commandKey)));

            this.request = request;
        }

        @Override
        protected LightblueResponse run() throws Exception {
            return client.data(request);
        }
    }

    protected class DataTypeHystrixCommand extends HystrixCommand<Object> {
        private final LightblueRequest request;
        private final Class type;

        public DataTypeHystrixCommand(LightblueRequest request, Class type, String groupKey, String commandKey) {
            super(HystrixCommand.Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey(groupKey)).
                    andCommandKey(HystrixCommandKey.Factory.asKey(groupKey + ":" + commandKey)));

            this.request = request;
            this.type = type;
        }

        @Override
        protected Object run() throws Exception {
            return client.data(request, type);
        }
    }

    private final LightblueClient client;
    private final String groupKey;
    private final String commandKey;

    public LightblueHystrixClient(LightblueClient client, String groupKey, String commandKey) {
        this.client = client;
        this.groupKey = groupKey;
        this.commandKey = commandKey;
    }

    @Override
    public LightblueResponse metadata(LightblueRequest lightblueRequest) {
        return new MetadataHystrixCommand(lightblueRequest, groupKey, commandKey).execute();
    }

    @Override
    public LightblueResponse data(LightblueRequest lightblueRequest) {
        return new DataHystrixCommand(lightblueRequest, groupKey, commandKey).execute();
    }

    @Override
    public <T> T data(LightblueRequest lightblueRequest, Class<T> type) throws IOException {
        return (T) new DataTypeHystrixCommand(lightblueRequest, type, groupKey, commandKey).execute();
    }
}
