/*
 Copyright 2015 Red Hat, Inc. and/or its affiliates.

 This file is part of lightblue.

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.redhat.lightblue.client.request;

import org.apache.commons.lang.StringUtils;

public abstract class AbstractLightblueMetadataRequest extends AbstractLightblueRequest implements LightblueRequest {
    public static final String PATH_PARAM_GET_ENTITY_NAMES = "";
    public static final String PATH_PARAM_GET_ENTITY_VERSIONS = "";
    public static final String PATH_PARAM_GET_ENTITY_METADATA = "";
    public static final String PATH_PARAM_GET_ENTITY_DEPENDENCIES = "dependencies";
    public static final String PATH_PARAM_GET_ENTITY_ROLES = "roles";
    public static final String PATH_PARAM_CREATE_METADATA = "";
    public static final String PATH_PARAM_CREATE_SCHEMA = "";
    public static final String PATH_PARAM_UPDATE_SCHEMA_STATUS = "";
    public static final String PATH_PARAM_UPDATE_ENTITY_INFO = "";
    public static final String PATH_PARAM_SET_DEFAULT_VERSION = "default";
    public static final String PATH_PARAM_REMOVE_ENTITY = "";
    public static final String PATH_PARAM_CLEAR_DEFAULT_VERSION = "";

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
