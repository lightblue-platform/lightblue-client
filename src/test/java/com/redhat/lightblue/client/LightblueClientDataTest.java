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

import com.redhat.lightblue.client.request.DataInsertRequest;


public class LightblueClientDataTest {
	
	@Test
	public void testInsertData() {
		LightblueClient client = new LightblueClient();
		DataInsertRequest request = new DataInsertRequest();
		request.setEntityName("termsAcknowledgement");
		request.setEntityVersion("0.5.0-SNAPSHOT");
		request.setBody("{\"data\":[{\"acknowledgedCode\":\"accepted\",\"acknowledgedDate\":\"20120328T03:19:34.295-0600\",\"objectType\":\"termsAcknowledgement\",\"termsId\":\"16049311\",\"termsVerbageTranslationUid\":\"8675309\",\"userId\":\"060378\"}],\"projection\":[{\"field\":\"*\",\"include\":\"true\"}]}");
		String response = client.data(request);
		Assert.assertNotNull(response);
		System.out.println("testInsertData() response: " +  response);
	}
	
//	@Test
//	public void testFindData() {
//		LightblueClient client = new LightblueClient();
//		DataFindRequest request = new DataFindRequest();
//		request.setEntityName("terms");
//		request.setBody("{\"data\":[{\"acknowledgedCode\":\"accepted\",\"acknowledgedDate\":\"20120328T03:19:34.295-0600\",\"objectType\":\"termsAcknowledgement\",\"termsId\":\"16049311\",\"termsVerbageTranslationUid\":\"8675309\",\"userId\":\"060378\"}],\"projection\":[{\"field\":\"*\",\"include\":\"true\"}]}");
//		String response = client.data(request);
//		Assert.assertNotNull(response);
//		System.out.println("testFindData() response: " +  response);
//	}
	
}
