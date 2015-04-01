package com.redhat.lightblue.client.request;

import org.apache.commons.lang.StringUtils;

public abstract class AbstractLightblueDataRequest extends AbstractLightblueRequest {
    public enum Operation {
        INSERT(""),
        SAVE("save"),
        UPDATE("update"),
        DELETE("delete"),
        FIND("find");

        private final String pathParam;

        public String getPathParam() {
            return pathParam;
        }

        private Operation(String pathParam) {
            this.pathParam = pathParam;
        }
    }

    @Override
    public String getRestURI(String baseServiceURI) {
        StringBuilder requestURI = new StringBuilder();

        requestURI.append(baseServiceURI);

        if (StringUtils.isNotBlank(this.getOperationPathParam())) {
            appendToURI(requestURI, getOperationPathParam());
        }

        if (StringUtils.isNotBlank(this.getEntityName())) {
            appendToURI(requestURI, this.getEntityName());
        }

        if (StringUtils.isNotBlank(this.getEntityVersion())) {
            appendToURI(requestURI, this.getEntityVersion());
        }
        return requestURI.toString();
    }
}
