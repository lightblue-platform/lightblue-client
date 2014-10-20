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

import com.redhat.lightblue.client.expression.Expression;
import com.redhat.lightblue.client.projection.FieldProjection;
import com.redhat.lightblue.client.projection.Projection;
import com.redhat.lightblue.client.request.DataDeleteRequest;
import com.redhat.lightblue.client.request.DataFindRequest;
import com.redhat.lightblue.client.request.DataInsertRequest;
import com.redhat.lightblue.client.request.DataUpdateRequest;
import org.junit.Assert;
import org.junit.Test;


public class LightblueClientDataTest {
	
  private Expression insertExpression = new Expression() {
    public String toJson() {
        return "{\"data\":[{\"acknowledgedCode\":\"accepted\",\"acknowledgedDate\":\"20120328T03:19:34.295-0600\",\"objectType\":\"termsAcknowledgement\",\"termsId\":\"16049311\",\"termsVerbageTranslationUid\":\"8675309\",\"userId\":\"060378\"}]";
    }
};

private Expression testExpression = new Expression() {
  public String toJson() {
      return "{\"field\": \"termsId\",\"op\": \"=\",\"rvalue\": \"16049311\"}";
  }
};

private Projection testProjection = new Projection () {
    public String toJson() {
    	return "{\"field\":\"*\"}";
    }
};
	
	@Test
	public void testInsertData() {
		String response = insertData();
		Assert.assertNotNull(response);
		System.out.println("testInsertData() response: " +  response);
	}
	
	@Test
	public void testFindData() {
		insertData();
		String response = findData();
		Assert.assertNotNull(response);
		System.out.println("testFindData() response: " +  response);
		deleteData();
	}
	
	@Test
	public void testUpdateData() {
		insertData();
		String response = updateData();
		Assert.assertNotNull(response);
		System.out.println("testUpdateData() response: " +  response);
		deleteData();
	}
	
	@Test
	public void testDeleteData() {
		insertData();
		String response = deleteData();
		Assert.assertNotNull(response);
		System.out.println("testDeleteData() response: " +  response);
	}
	
	private String insertData() {
		LightblueClient client = new LightblueClient();
		DataInsertRequest request = new DataInsertRequest();
		
		request.setEntityName("termsAcknowledgement");
		request.setEntityVersion("0.5.0-SNAPSHOT");
        request.returns(new FieldProjection("*") );
		request.setBody("{\"data\":[{\"acknowledgedCode\":\"accepted\",\"acknowledgedDate\":\"20120328T03:19:34.295-0600\",\"objectType\":\"termsAcknowledgement\",\"termsId\":\"16049311\",\"termsVerbageTranslationUid\":\"8675309\",\"userId\":\"060378\"}],\"projection\":[{\"field\":\"*\",\"include\":\"true\"}]}");
		return client.data(request);
	}
	
	private String findData() {
		LightblueClient client = new LightblueClient();
		DataFindRequest request = new DataFindRequest();
		request.setEntityName("termsAcknowledgement");
		request.setEntityVersion("0.5.0-SNAPSHOT");
		request.select(testProjection);
        request.where(testExpression);
		return client.data(request);
	}
	
	private String updateData() {
		LightblueClient client = new LightblueClient();
		DataUpdateRequest request = new DataUpdateRequest();
		request.setEntityName("termsAcknowledgement");
		request.setEntityVersion("0.5.0-SNAPSHOT");
		request.setBody("{\"entity\": \"termsAcknowledgement\",\"entityVersion\": \"0.5.0-SNAPSHOT\",\"query\": {\"field\": \"termsId\",\"op\": \"=\",\"rvalue\": \"16049311\"},\"projection\" : [{  \"field\": \"termsId\", \"include\": true },{ \"field\": \"acknowledgedDate\", \"include\": true } ]}");
		return client.data(request);
	}
	
	private String deleteData() {
		LightblueClient client = new LightblueClient();
		DataDeleteRequest request = new DataDeleteRequest();
		request.setEntityName("termsAcknowledgement");
		request.setEntityVersion("0.5.0-SNAPSHOT");
		request.setBody("{\"entity\": \"termsAcknowledgement\",\"entityVersion\": \"0.5.0-SNAPSHOT\",\"query\": {\"field\": \"termsId\",\"op\": \"=\",\"rvalue\": \"16049311\"},\"projection\" : [{  \"field\": \"termsId\", \"include\": true },{ \"field\": \"acknowledgedDate\", \"include\": true } ]}");
		return client.data(request);
	}
}
