package com.redhat.lightblue.client.response.lock;

public class InvalidLockException extends LockException {

    private static final long serialVersionUID = -3582328259746921702L;

    public InvalidLockException(String resourceId) {
        super(resourceId);
    }

    public InvalidLockException(String resourceId, Throwable cause) {
        super(resourceId, cause);
    }

}
