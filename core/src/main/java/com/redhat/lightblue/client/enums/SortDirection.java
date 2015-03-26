package com.redhat.lightblue.client.enums;

public enum SortDirection {
    ASC, DESC, ASCENDING, DESCENDING;

    @Override
    public String toString() {
        switch (this) {
            case ASC:
                return "$asc";
            case DESC:
                return "$desc";
            case ASCENDING:
                return "$asc";
            case DESCENDING:
                return "$desc";
            default:
                throw new IllegalArgumentException();
        }
    }

}
