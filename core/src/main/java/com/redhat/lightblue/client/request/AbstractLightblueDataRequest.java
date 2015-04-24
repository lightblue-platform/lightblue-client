package com.redhat.lightblue.client.request;

import org.apache.commons.lang.StringUtils;

public abstract class AbstractLightblueDataRequest extends AbstractLightblueRequest implements LightblueRequest {
    public static final String PATH_PARAM_INSERT = "";
    public static final String PATH_PARAM_SAVE = "save";
    public static final String PATH_PARAM_UPDATE = "update";
    public static final String PATH_PARAM_DELETE = "delete";
    public static final String PATH_PARAM_FIND = "find";

    public AbstractLightblueDataRequest() {
        super();
    }

    public AbstractLightblueDataRequest(String entityName, String entityVersion) {
        super(entityName, entityVersion);
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
