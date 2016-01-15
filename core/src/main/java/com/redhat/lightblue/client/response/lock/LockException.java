package com.redhat.lightblue.client.response.lock;

import com.redhat.lightblue.client.LightblueException;

public class LockException extends LightblueException {

    private static final long serialVersionUID = 258836165000267435L;

    private final String resourceId;

    public String getResourceId() {
        return resourceId;
    }

    public LockException(String resourceId) {
        super();

        this.resourceId = resourceId;
    }

    public LockException(String resourceId, Throwable cause) {
        super(cause);

        this.resourceId = resourceId;
    }

    @Override
    public String getMessage() {
        return getResourceId();
    }

}
