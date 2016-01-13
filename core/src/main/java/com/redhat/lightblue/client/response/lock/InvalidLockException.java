package com.redhat.lightblue.client.response.lock;

import com.redhat.lightblue.client.response.LightblueErrorResponse;
import com.redhat.lightblue.client.response.LightblueResponseException;

public class InvalidLockException extends LightblueResponseException {

    private static final long serialVersionUID = -3582328259746921702L;

    public InvalidLockException(String message, LightblueErrorResponse lightblueResponse) {
        super(message, lightblueResponse);
    }

    public InvalidLockException(String message, LightblueErrorResponse lightblueResponse, Throwable cause) {
        super(message, lightblueResponse, cause);
    }

}
