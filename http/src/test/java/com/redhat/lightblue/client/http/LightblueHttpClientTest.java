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
package com.redhat.lightblue.client.http;

import static com.redhat.lightblue.client.expression.query.ValueQuery.withValue;
import static com.redhat.lightblue.client.projection.FieldProjection.includeField;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import com.redhat.lightblue.client.http.model.SimpleModelObject;
import com.redhat.lightblue.client.request.data.DataFindRequest;

public class LightblueHttpClientTest {

    LightblueHttpClientMock client = new LightblueHttpClientMock();

    @Test
    public void testPojoMapping() throws IOException {
        DataFindRequest findRequest = new DataFindRequest("foo", "bar");

        findRequest.where(withValue("foo = bar"));
        findRequest.select(includeField("_id"));

        client.setLightblueResponse("{\"matchCount\": 1, \"modifiedCount\": 0, \"processed\": [{\"_id\": \"idhash\", \"field\":\"value\"}], \"status\": \"COMPLETE\"}");

        SimpleModelObject[] results = client.data(findRequest,
                SimpleModelObject[].class);

        Assert.assertEquals(1, results.length);

        Assert.assertTrue(new SimpleModelObject("idhash", "value")
                .equals(results[0]));
    }

    @Test(expected = LightblueHttpClientException.class)
    public void testPojoMappingWithParsingError() throws IOException {
        DataFindRequest findRequest = new DataFindRequest("foo", "bar");

        findRequest.where(withValue("foo = bar"));
        findRequest.select(includeField("_id"));

        String response = "{\"processed\":\"<p>This is not json</p>\"}";

        client.setLightblueResponse(response);

        client.data(findRequest, SimpleModelObject[].class);
    }

}
