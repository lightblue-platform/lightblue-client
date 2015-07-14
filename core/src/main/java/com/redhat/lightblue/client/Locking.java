package com.redhat.lightblue.client;

import java.util.UUID;

public abstract class Locking {

    private final String domain;

    private String callerId=UUID.randomUUID().toString();

    public Locking(String domain) {
        this.domain=domain;
    }
    
    public abstract boolean acquire(String callerId,String resourceId,Long ttl);
    public abstract boolean release(String callerId,String resourceId);
    public abstract int getLockCount(String callerId,String resourceId);
    public abstract boolean ping(String callerId,String resourceId);

    public String getCallerId() {
        return callerId;
    }

    public void setCallerId(String s) {
        callerId=s;
    }

    public String getDomain() {
        return domain;
    }
    
    public boolean acquire(String resourceId,Long ttl) {
        return acquire(callerId,resourceId,ttl);
    }

    public boolean acquire(String resourceId) {
        return acquire(callerId,resourceId,null);
    }

    public boolean release(String resourceId) {
        return release(callerId,resourceId);
    }

    public int getLockCount(String resourceId) {
        return getLockCount(callerId,resourceId);
    }

    public boolean ping(String resourceId) {
        return ping(callerId,resourceId);
    }
}
