/*
 Copyright 2013 Red Hat, Inc. and/or its affiliates.

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
package com.redhat.lightblue.client;

import org.junit.Assert;
import org.junit.Test;

import com.redhat.lightblue.client.request.DataFindRequest;
import com.redhat.lightblue.client.request.LightblueRequest;
import com.redhat.lightblue.client.request.MetadataRequest;


public class LightblueClientMetadataTest {
  
	@Test
	public void testGetAllMetadata() {
		LightblueClient client = new LightblueClient();
		LightblueRequest request = new MetadataRequest();
		String response = client.metadata(request);
		Assert.assertNotNull(response);
		Assert.assertEquals("{\"entities\":[\"termsAcknowledgement\",\"terms\"]}",response);
		System.out.println("testGetMetadata() response: " +  response);
	}
	
	@Test
	public void testGetTermsMetadata() {
		LightblueClient client = new LightblueClient();
		LightblueRequest request = new MetadataRequest();
		request.setEntityName("terms");
		String response = client.metadata(request);
		Assert.assertEquals("[{\"version\":\"0.8.0-SNAPSHOT\",\"changelog\":\"Initial version\",\"extendsVersions\":[],\"status\":\"active\",\"defaultVersion\":false},{\"version\":\"0.9.0-SNAPSHOT\",\"changelog\":\"Initial version\",\"extendsVersions\":[],\"status\":\"active\",\"defaultVersion\":false},{\"version\":\"0.10.0-SNAPSHOT\",\"changelog\":\"Initial version\",\"extendsVersions\":[],\"status\":\"active\",\"defaultVersion\":false},{\"version\":\"0.11.0-SNAPSHOT\",\"changelog\":\"Initial version\",\"extendsVersions\":[],\"status\":\"active\",\"defaultVersion\":false},{\"version\":\"0.12.0-SNAPSHOT\",\"changelog\":\"Initial version\",\"extendsVersions\":[],\"status\":\"active\",\"defaultVersion\":false},{\"version\":\"0.13.0-SNAPSHOT\",\"changelog\":\"Initial version\",\"extendsVersions\":[],\"status\":\"active\",\"defaultVersion\":false},{\"version\":\"0.14.0-SNAPSHOT\",\"changelog\":\"Initial version\",\"extendsVersions\":[],\"status\":\"active\",\"defaultVersion\":true},{\"version\":\"0.14.1-SNAPSHOT\",\"changelog\":\"Initial version\",\"extendsVersions\":[],\"status\":\"active\",\"defaultVersion\":false}]", response);
		Assert.assertNotNull(response);
		System.out.println("testGetTermsMetadata() response: " +  response);
	}
	
	@Test
	public void testGetTermsVersionMetadata() {
		LightblueClient client = new LightblueClient();
		LightblueRequest request = new MetadataRequest();
		request.setEntityName("terms");
		request.setEntityVersion("0.14.0-SNAPSHOT");
		String response = client.metadata(request);
		Assert.assertNotNull(response);
		Assert.assertEquals("{\"entityInfo\":{\"name\":\"terms\",\"defaultVersion\":\"0.14.0-SNAPSHOT\",\"indexes\":[{\"name\":null,\"unique\":true,\"fields\":[{\"field\":\"localeCode\",\"dir\":\"$desc\"},{\"field\":\"version\",\"dir\":\"$asc\"},{\"field\":\"termsVerbiage.uid\",\"dir\":\"$asc\"}]}],\"enums\":[{\"name\":\"statusCode\",\"values\":[\"active\",\"inactive\"]},{\"name\":\"termsCategoryCode\",\"values\":[\"customer\",\"indemnification\",\"user\"]},{\"name\":\"termsTypeCode\",\"values\":[\"site\",\"T7\",\"application\",\"subscription\",\"NDA\",\"indemnification\",\"purchase\"]},{\"name\":\"operator\",\"values\":[\"start_with\",\"contains\",\"not_contains\",\"not_equals\",\"equals\",\"end_with\"]}],\"datastore\":{\"datasource\":\"mongodata\",\"collection\":\"terms\",\"backend\":\"mongo\"}},\"schema\":{\"name\":\"terms\",\"version\":{\"value\":\"0.14.0-SNAPSHOT\",\"changelog\":\"Initial version\"},\"status\":{\"value\":\"active\"},\"access\":{\"insert\":[\"anyone\"],\"update\":[\"anyone\"],\"find\":[\"anyone\"],\"delete\":[\"anyone\"]},\"fields\":{\"startDate\":{\"type\":\"date\"},\"optionalFlag\":{\"type\":\"boolean\"},\"hostname\":{\"type\":\"string\"},\"endDate\":{\"type\":\"date\"},\"termsVerbiage\":{\"type\":\"array\",\"items\":{\"type\":\"object\",\"fields\":{\"statusCode\":{\"type\":\"string\",\"constraints\":{\"enum\":\"statusCode\",\"required\":true}},\"uid\":{\"type\":\"uid\"},\"startDate\":{\"type\":\"date\"},\"termsVerbiageTranslation\":{\"type\":\"array\",\"items\":{\"type\":\"object\",\"fields\":{\"statusCode\":{\"type\":\"string\",\"constraints\":{\"enum\":\"statusCode\",\"required\":true}},\"uid\":{\"type\":\"uid\",\"constraints\":{\"required\":true}},\"startDate\":{\"type\":\"date\"},\"versionText\":{\"type\":\"string\"},\"localeCode\":{\"type\":\"string\"},\"pdf\":{\"type\":\"binary\"},\"localeText\":{\"type\":\"string\"},\"endDate\":{\"type\":\"date\"},\"bodyText\":{\"type\":\"string\"},\"URL\":{\"type\":\"string\"},\"publishedFlag\":{\"type\":\"boolean\"},\"version\":{\"type\":\"integer\"}}}},\"versionText\":{\"type\":\"string\"},\"description\":{\"type\":\"string\"},\"name\":{\"type\":\"string\"},\"endDate\":{\"type\":\"date\"},\"termsVerbiageTranslation#\":{\"type\":\"integer\",\"access\":{\"find\":[\"anyone\"]}}}}},\"siteCode\":{\"type\":\"string\"},\"productAttribute\":{\"type\":\"string\"},\"lastUpdateDate\":{\"type\":\"date\",\"constraints\":{\"required\":true}},\"statusCode\":{\"type\":\"string\",\"constraints\":{\"enum\":\"statusCode\",\"required\":true}},\"creationDate\":{\"type\":\"date\",\"constraints\":{\"required\":true}},\"termsCategoryCode\":{\"type\":\"string\",\"constraints\":{\"enum\":\"termsCategoryCode\"}},\"createdBy\":{\"type\":\"string\",\"constraints\":{\"required\":true}},\"_id\":{\"type\":\"uid\"},\"termsTypeCode\":{\"type\":\"string\",\"constraints\":{\"enum\":\"termsTypeCode\",\"required\":true}},\"lastUpdatedBy\":{\"type\":\"string\",\"constraints\":{\"required\":true}},\"termsVerbiage#\":{\"type\":\"integer\",\"access\":{\"find\":[\"anyone\"]}},\"objectType\":{\"type\":\"string\",\"access\":{\"find\":[\"anyone\"],\"update\":[\"noone\"]},\"constraints\":{\"minLength\":1,\"required\":true}}}}}", response);
		System.out.println("testGetTermsMetadata() response: " +  response);
	}
	
}
