package com.redhat.lightblue.client;

public class LightblueClientConfigurationException extends RuntimeException {
    public LightblueClientConfigurationException(String message) {
        super(message);
    }

    public LightblueClientConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }
}
