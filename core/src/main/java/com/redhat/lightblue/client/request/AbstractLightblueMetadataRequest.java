package com.redhat.lightblue.client.request;

import org.apache.commons.lang.StringUtils;

public abstract class AbstractLightblueMetadataRequest extends AbstractLightblueRequest implements LightblueRequest {
    public enum MetadataOperation {
        GET_ENTITY_NAMES(""),
        GET_ENTITY_VERSIONS(""),
        GET_ENTITY_METADATA(""),
        GET_ENTITY_DEPENDENCIES("dependencies"),
        GET_ENTITY_ROLES("roles"),
        CREATE_METADATA(""),
        CREATE_SCHEMA(""),
        UPDATE_SCHEMA_STATUS(""),
        UPDATE_ENTITY_INFO(""),
        SET_DEFAULT_VERSION("default"),
        REMOVE_ENTITY(""),
        CLEAR_DEFAULT_VERSION("");

        private final String pathParam;

        public String getPathParam() {
            return pathParam;
        }

        private MetadataOperation(String pathParam) {
            this.pathParam = pathParam;
        }
    }

    public abstract MetadataOperation getOperation();

    @Override
    public String getOperationPathParam() {
        return getOperation().getPathParam();
    }

    @Override
    public String getRestURI(String baseServiceURI) {
        StringBuilder requestURI = new StringBuilder();

        requestURI.append(baseServiceURI);

        if (StringUtils.isNotBlank(this.getEntityName())) {
            appendToURI(requestURI, this.getEntityName());
        }

        if (StringUtils.isNotBlank(this.getEntityVersion())) {
            appendToURI(requestURI, this.getEntityVersion());
        }

        if (StringUtils.isNotBlank(this.getOperationPathParam())) {
            appendToURI(requestURI, getOperationPathParam());
        }

        return requestURI.toString();
    }
}
