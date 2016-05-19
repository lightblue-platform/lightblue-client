package com.redhat.lightblue.client.hystrix;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.redhat.lightblue.client.LightblueClient;
import com.redhat.lightblue.client.LightblueException;
import com.redhat.lightblue.client.Locking;
import com.redhat.lightblue.client.hystrix.graphite.ServoGraphiteSetup;
import com.redhat.lightblue.client.request.AbstractDataBulkRequest;
import com.redhat.lightblue.client.request.AbstractLightblueDataRequest;
import com.redhat.lightblue.client.request.LightblueRequest;
import com.redhat.lightblue.client.response.LightblueBulkDataResponse;
import com.redhat.lightblue.client.response.LightblueDataResponse;
import com.redhat.lightblue.client.response.LightblueMetadataResponse;

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

    protected class MetadataHystrixCommand extends HystrixCommand<LightblueMetadataResponse> {
        private final LightblueRequest request;

        public MetadataHystrixCommand(LightblueRequest request, String groupKey, String commandKey) {
            super(HystrixCommand.Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey(groupKey)).andCommandKey(HystrixCommandKey.Factory.asKey(groupKey + ":" + commandKey)));

            this.request = request;
        }

        @Override
        protected LightblueMetadataResponse run() throws Exception {
            return client.metadata(request);
        }
    }

    protected class DataHystrixCommand extends HystrixCommand<LightblueDataResponse> {
        private final LightblueRequest request;

        public DataHystrixCommand(LightblueRequest request, String groupKey, String commandKey) {
            super(HystrixCommand.Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey(groupKey)).andCommandKey(HystrixCommandKey.Factory.asKey(groupKey + ":" + commandKey)));

            this.request = request;
        }

        @Override
        protected LightblueDataResponse run() throws Exception {
            return client.data(request);
        }
    }

    protected class BulkDataHystrixCommand extends HystrixCommand<LightblueBulkDataResponse> {

        private final AbstractDataBulkRequest<AbstractLightblueDataRequest> requests;

        protected BulkDataHystrixCommand(AbstractDataBulkRequest<AbstractLightblueDataRequest> requests, String groupKey, String commandKey) {
            super(HystrixCommand.Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey(groupKey)).andCommandKey(HystrixCommandKey.Factory.asKey(groupKey + ":" + commandKey)));
            this.requests = requests;

        }

        @Override
        protected LightblueBulkDataResponse run() throws Exception {
            return client.bulkData(requests);
        }
    }

    protected class DataTypeHystrixCommand<T> extends HystrixCommand<T> {
        private final LightblueRequest request;
        private final Class<T> type;

        public DataTypeHystrixCommand(LightblueRequest request, Class<T> type, String groupKey, String commandKey) {
            super(HystrixCommand.Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey(groupKey)).andCommandKey(HystrixCommandKey.Factory.asKey(groupKey + ":" + commandKey)));

            this.request = request;
            this.type = type;
        }

        @Override
        protected T run() throws Exception {
            return client.data(request, type);
        }
    }

    private abstract class LockingHystrixCommand<T> extends HystrixCommand<T> {
        protected final Locking delegate;
        protected final String callerId;
        protected final String resourceId;

        public LockingHystrixCommand(Class<T> type, String groupKey, String commandKey, Locking delegate, String callerId, String resourceId) {
            super(HystrixCommand.Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey(groupKey)).andCommandKey(HystrixCommandKey.Factory.asKey(groupKey + ":" + commandKey)));

            this.callerId = callerId;
            this.resourceId = resourceId;
            this.delegate = delegate;
        }
    }

    private class AcquireCommand extends LockingHystrixCommand<Boolean> {
        private final Long ttl;

        public AcquireCommand(String groupKey, String commandKey, Locking delegate, String callerId, String resourceId, Long ttl) {
            super(Boolean.class, groupKey, commandKey, delegate, callerId, resourceId);
            this.ttl = ttl;
        }

        @Override
        protected Boolean run() throws Exception {
            return delegate.acquire(callerId, resourceId, ttl);
        }
    }

    private class ReleaseCommand extends LockingHystrixCommand<Boolean> {
        public ReleaseCommand(String groupKey, String commandKey, Locking delegate, String callerId, String resourceId) {
            super(Boolean.class, groupKey, commandKey, delegate, callerId, resourceId);
        }

        @Override
        protected Boolean run() throws Exception {
            return delegate.release(callerId, resourceId);
        }
    }

    private class GetLockCountCommand extends LockingHystrixCommand<Integer> {
        public GetLockCountCommand(String groupKey, String commandKey, Locking delegate, String callerId, String resourceId) {
            super(Integer.class, groupKey, commandKey, delegate, callerId, resourceId);
        }

        @Override
        protected Integer run() throws Exception {
            return delegate.getLockCount(callerId, resourceId);
        }
    }

    private class PingCommand extends LockingHystrixCommand<Boolean> {
        public PingCommand(String groupKey, String commandKey, Locking delegate, String callerId, String resourceId) {
            super(Boolean.class, groupKey, commandKey, delegate, callerId, resourceId);
        }

        @Override
        protected Boolean run() throws Exception {
            return delegate.ping(callerId, resourceId);
        }
    }

    private final class LockingImpl extends Locking {
        private final Locking delegate;

        public LockingImpl(String domain, Locking delegate) {
            super(domain);
            this.delegate = delegate;
        }

        @Override
        public boolean acquire(String callerId, String resourceId, Long ttl) throws LightblueException {
            return new AcquireCommand(groupKey, commandKey, delegate, callerId, resourceId, ttl).execute();
        }

        @Override
        public boolean release(String callerId, String resourceId) throws LightblueException {
            return new ReleaseCommand(groupKey, commandKey, delegate, callerId, resourceId).execute();
        }

        @Override
        public int getLockCount(String callerId, String resourceId) throws LightblueException {
            return new GetLockCountCommand(groupKey, commandKey, delegate, callerId, resourceId).execute();
        }

        @Override
        public boolean ping(String callerId, String resourceId) throws LightblueException {
            return new PingCommand(groupKey, commandKey, delegate, callerId, resourceId).execute();
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
    public LightblueMetadataResponse metadata(LightblueRequest lightblueRequest) {
        return new MetadataHystrixCommand(lightblueRequest, groupKey, commandKey).execute();
    }

    @Override
    public LightblueDataResponse data(LightblueRequest lightblueRequest) {
        return new DataHystrixCommand(lightblueRequest, groupKey, commandKey).execute();
    }

    @Override
    public <T> T data(LightblueRequest lightblueRequest, Class<T> type) throws LightblueException {
        return new DataTypeHystrixCommand<T>(lightblueRequest, type, groupKey, commandKey).execute();
    }

    @Override
    public LightblueBulkDataResponse bulkData(AbstractDataBulkRequest<AbstractLightblueDataRequest> requests) throws LightblueException {
        return new BulkDataHystrixCommand(requests, groupKey, commandKey).execute();
    }

    @Override
    public Locking getLocking(String domain) {
        return new LockingImpl(domain, client.getLocking(domain));
    }

}
