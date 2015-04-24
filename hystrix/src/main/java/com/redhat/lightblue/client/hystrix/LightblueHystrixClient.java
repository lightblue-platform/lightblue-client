package com.redhat.lightblue.client.hystrix;

import java.io.IOException;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.redhat.lightblue.client.LightblueClient;
import com.redhat.lightblue.client.request.AbstractLightblueDataRequest;
import com.redhat.lightblue.client.request.AbstractLightblueMetadataRequest;
import com.redhat.lightblue.client.response.LightblueResponse;
import com.redhat.lightblue.hystrix.ServoGraphiteSetup;

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
        private final AbstractLightblueMetadataRequest request;

        public MetadataHystrixCommand(AbstractLightblueMetadataRequest request, String groupKey, String commandKey) {
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
        private final AbstractLightblueDataRequest request;

        public DataHystrixCommand(AbstractLightblueDataRequest request, String groupKey, String commandKey) {
            super(HystrixCommand.Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey(groupKey)).
                    andCommandKey(HystrixCommandKey.Factory.asKey(groupKey + ":" + commandKey)));

            this.request = request;
        }

        @Override
        protected LightblueResponse run() throws Exception {
            return client.data(request);
        }
    }

    protected class DataTypeHystrixCommand<T> extends HystrixCommand<T> {
        private final AbstractLightblueDataRequest request;
        private final Class<T> type;

        public DataTypeHystrixCommand(AbstractLightblueDataRequest request, Class<T> type, String groupKey, String commandKey) {
            super(HystrixCommand.Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey(groupKey)).
                    andCommandKey(HystrixCommandKey.Factory.asKey(groupKey + ":" + commandKey)));

            this.request = request;
            this.type = type;
        }

        @Override
        protected T run() throws Exception {
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
    public LightblueResponse metadata(AbstractLightblueMetadataRequest lightblueRequest) {
        return new MetadataHystrixCommand(lightblueRequest, groupKey, commandKey).execute();
    }

    @Override
    public LightblueResponse data(AbstractLightblueDataRequest lightblueRequest) {
        return new DataHystrixCommand(lightblueRequest, groupKey, commandKey).execute();
    }

    @Override
    public <T> T data(AbstractLightblueDataRequest lightblueRequest, Class<T> type) throws IOException {
        return new DataTypeHystrixCommand<T>(lightblueRequest, type, groupKey, commandKey).execute();
    }
}
