package com.redhat.lightblue.client.enums;

public enum MetadataStatus {
    ACTIVE("active"), DISABLED("disabled"), DEPRECATED("deprecated");

    private final String status;

    public String getStatus() {
        return status;
    }

    private MetadataStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return getStatus();
    }
}
