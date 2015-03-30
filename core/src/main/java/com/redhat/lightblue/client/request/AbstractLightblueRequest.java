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

public abstract class AbstractLightblueRequest implements LightblueRequest {

    static String PATH_SEPARATOR = "/";
    protected static String PATH_PARAM_ENTITY = "entity";
    protected static String PATH_PARAM_VERSION = "version";

    private String entityName;
    private String entityVersion;
    private String body;

    @Override
    public String getEntityName() {
        return entityName;
    }

    @Override
    public String getEntityVersion() {
        return entityVersion;
    }

    @Override
    public String getBody() {
        return body;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public void setEntityVersion(String entityVersion) {
        this.entityVersion = entityVersion;
    }

    public void setBody(String body) {
        this.body = body;
    }

    protected void appendToURI(StringBuilder restOfURI, String pathParam) {
        if (!StringUtils.endsWith(restOfURI.toString(), PATH_SEPARATOR)) {
            restOfURI.append(PATH_SEPARATOR);
        }
        restOfURI.append(pathParam);
    }

}
