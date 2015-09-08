package com.redhat.lightblue.client.response;

import java.util.HashSet;
import java.util.Set;

import com.redhat.lightblue.client.model.DataError;
import com.redhat.lightblue.client.model.Error;

/**
 * Exception thrown when a response from lightblue has errors or data errors.
 * The client can call e.exists(ERR_REST_CRUD_REST_SAVE)
 *
 * @author ykoer
 */
public class LightblueException extends Exception {

    private static final long serialVersionUID = -5883690817679713469L;

    private LightblueResponse lightblueResponse;
    private Set<String> errorCodes;

    public LightblueException() {
        super();
    }

    public LightblueException(String message, LightblueResponse lightblueResponse) {
        super(message);
        this.lightblueResponse = lightblueResponse;
    }

    public LightblueException(String message, LightblueResponse lightblueResponse, Throwable cause) {
        super(message, cause);
        this.lightblueResponse = lightblueResponse;
    }

    public LightblueResponse getLightblueResponse() {
        return lightblueResponse;
    }

    public void setLightblueResponse(LightblueResponse lightblueResponse) {
        this.lightblueResponse = lightblueResponse;
    }

    public boolean exists(String errorCode) {
        return getErrorCodes().contains(errorCode);
    }

    public Set<String> getErrorCodes() {
        if (errorCodes == null || lightblueResponse == null) {
            errorCodes = new HashSet<>();
        } else {
            return errorCodes;
        }

        if (lightblueResponse.getErrors() != null) {
            for (Error e : lightblueResponse.getErrors()) {
                errorCodes.add(e.getErrorCode());
            }
        }
        if (lightblueResponse.getDataErrors() != null) {
            for (DataError de : lightblueResponse.getDataErrors()) {
                if (de.getErrors() != null) {
                    for (Error e : de.getErrors()) {
                        errorCodes.add(e.getErrorCode());
                    }
                }
            }
        }
        return errorCodes;
    }

    @Override
    public String getMessage() {
        if (lightblueResponse != null)
            return super.getMessage() + lightblueResponse.getText();
        else
            return super.getMessage();
    }
}
