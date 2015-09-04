/**
 * 
 */
package com.redhat.lightblue.client.request.data;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.redhat.lightblue.client.Projection;
import com.redhat.lightblue.client.Query;
import com.redhat.lightblue.client.request.AbstractLightblueDataRequest;
import com.redhat.lightblue.client.request.BulkLightblueDataRequest;

/**
 * @author bvulaj
 *
 */
public class TestBulkDataRequest {

	private BulkLightblueDataRequest request;

	@Before
	public void setUp() throws Exception {
		request = new BulkLightblueDataRequest();
	}

	@Test
	public void testAddRequest() {
		DataFindRequest dfr = new DataFindRequest("foo", "1.0.0");
		dfr.select(Projection.includeField("*"));
		dfr.where(Query.regex("foo", "*", 0));
		request.add(dfr);
		assertTrue(request.getRequests().size() == 1);
		ArrayNode requests = (ArrayNode) request.getBodyJson().get("requests");
		assertTrue(requests.size() == 1);
		assertTrue(requests.get(0).get("request").get("entity").asText().equals("foo"));

	}

	@Test
	public void testAddAllRequest() {
		DataFindRequest dfr = new DataFindRequest("foo", "bar");
		dfr.select(Projection.includeField("*"));
		dfr.where(Query.regex("foo", "*", 0));

		DataFindRequest dfr2 = new DataFindRequest("fooz", "bar");
		dfr2.select(Projection.includeField("*"));
		dfr2.where(Query.regex("fooz", "*", 0));

		request.addAll(Arrays.<AbstractLightblueDataRequest> asList(dfr, dfr2));
		assertTrue(request.getRequests().size() == 2);
		ArrayNode requests = (ArrayNode) request.getBodyJson().get("requests");
		assertTrue(requests.size() == 2);
		assertTrue(requests.get(0).get("request").get("entity").asText().equals("foo"));
		assertTrue(requests.get(1).get("request").get("entity").asText().equals("fooz"));

	}

	@Test
	public void testInsertBeforeRequest() {

	}

	@Test
	public void testInsertAfterRequest() {

	}

	@Test
	public void testGetJson() {

	}

	@Test
	public void testGetOperationPathParam() {
		Assert.assertEquals("localhost/bulk", request.getRestURI("localhost"));
	}
}
