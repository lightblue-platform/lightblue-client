package com.redhat.lightblue.client;

import java.io.Closeable;
import java.io.IOException;
import java.util.UUID;

import com.redhat.lightblue.client.response.LightblueException;

public abstract class Locking {

    private final String domain;

    private String callerId=UUID.randomUUID().toString();

    public Locking(String domain) {
        this.domain=domain;
    }

    public abstract boolean acquire(String callerId,String resourceId,Long ttl) throws LightblueException;
    public abstract boolean release(String callerId,String resourceId) throws LightblueException;
    public abstract int getLockCount(String callerId,String resourceId) throws LightblueException;
    public abstract boolean ping(String callerId,String resourceId) throws LightblueException;

    public String getCallerId() {
        return callerId;
    }

    public void setCallerId(String s) {
        callerId=s;
    }

    public String getDomain() {
        return domain;
    }

    public boolean acquire(String resourceId,Long ttl) throws LightblueException {
        return acquire(callerId,resourceId,ttl);
    }

    public boolean acquire(String resourceId) throws LightblueException {
        return acquire(callerId,resourceId,null);
    }

    public boolean release(String resourceId) throws LightblueException {
        return release(callerId,resourceId);
    }

    public int getLockCount(String resourceId) throws LightblueException {
        return getLockCount(callerId,resourceId);
    }

    public boolean ping(String resourceId) throws LightblueException {
        return ping(callerId,resourceId);
    }

    public Lock lock(String resourceId) throws LightblueException {
        return lock(resourceId, null);
    }

    public Lock lock(String resourceId, Long ttl) throws LightblueException {
        acquire(resourceId, ttl);
        return new Lock(resourceId);
    }

    public class Lock implements Closeable {

        private final String resourceId;

        public Lock(String resourceId) {
            this.resourceId = resourceId;
        }

        public boolean ping() throws LightblueException {
            return Locking.this.ping(resourceId);
        }

        public int getLockCount() throws LightblueException {
            return Locking.this.getLockCount(resourceId);
        }

        public boolean release() throws LightblueException {
            return Locking.this.release(resourceId);
        }

        @Override
        public void close() throws IOException {
            try {
                release();
            } catch (LightblueException e) {
                throw new IOException("Unable to release lock: " + resourceId, e);
            }
        }

    }
}
