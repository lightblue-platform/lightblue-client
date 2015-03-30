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


public class AbstractLightblueRequestTest {

	protected static final String entityName = "lightblueEntity";
	protected static final String entityVersion = "1.2.3";
	protected static final String body = "{\"name\":\"value\"}";
	protected static final String baseURI = "http://lightblue.io/rest/";
	protected static final String dataOperation = "dosomethingwithdata";
	protected static final String metadataOperation = "dosomethingwithmetadata";
	protected static final String finalDataURI = baseURI + dataOperation + "/" + entityName + "/" + entityVersion;
	protected static final String finalMetadataURI = baseURI + entityName + "/" + entityVersion + "/" + metadataOperation;
	
}
