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

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestAbstractLightblueRequest extends AbstractLightblueRequestTest {

	AbstractLightblueRequest testRequest = new AbstractLightblueRequest() {
        private String body;
		@Override
		public String getRestURI(String baseServiceURI) {
			return null;
		}

		@Override
		public String getOperationPathParam() {
			return null;
		}

        public void setBody(String body) {
            this.body = body;
        }

        public String getBody() {
            return body;
        }
	};

	private static final String updatedEntityName = "updatedEntity";
	private static final String updatedEntityVersion = "3.2.1";
	private static final String updatedBody = "{\"value\":\"name\"}";
	private static final String baseURI = "http://lightblue.io";
	private static final String restURI = "http://lightblue.io/rest";

	@Before
	public void setUp() throws Exception {
		testRequest.setEntityName(entityName);
		testRequest.setEntityVersion(entityVersion);
		testRequest.setBody(body);
	}

	@Test
	public void testGetEntityName() {
		Assert.assertEquals(entityName, testRequest.getEntityName());
	}

	@Test
	public void testGetEntityVersion() {
		Assert.assertEquals(entityVersion, testRequest.getEntityVersion());
	}

	@Test
	public void testGetBody() {
		Assert.assertEquals(body, testRequest.getBody());
	}

	@Test
	public void testSetEntityName() {
		testRequest.setEntityName(updatedEntityName);
		Assert.assertEquals(updatedEntityName, testRequest.getEntityName());
	}

	@Test
	public void testSetEntityVersion() {
		testRequest.setEntityVersion(updatedEntityVersion);
		Assert.assertEquals(updatedEntityVersion, testRequest.getEntityVersion());
	}

	@Test
	public void testSetBody() {
		testRequest.setBody(updatedBody);
		Assert.assertEquals(updatedBody, testRequest.getBody());
	}

	@Test
	public void testAppendToURI() {
		StringBuilder initialURI = new StringBuilder();
		initialURI.append(baseURI);
		testRequest.appendToURI(initialURI, "rest");
		Assert.assertEquals(restURI, initialURI.toString());
	}
	
}
