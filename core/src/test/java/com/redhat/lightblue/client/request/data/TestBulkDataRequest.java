/**
 * 
 */
package com.redhat.lightblue.client.request.data;

import static org.junit.Assert.*;

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
		DataFindRequest dfr = new DataFindRequest("foo", "bar");
		dfr.select(Projection.includeField("*"));
		dfr.where(Query.regex("foo", "*", 0));

		DataFindRequest dfr2 = new DataFindRequest("fooy", "bar");
		dfr2.select(Projection.includeField("*"));
		dfr2.where(Query.regex("fooy", "*", 0));

		DataFindRequest dfr3 = new DataFindRequest("fooz", "bar");
		dfr3.select(Projection.includeField("*"));
		dfr3.where(Query.regex("fooz", "*", 0));

		request.addAll(Arrays.<AbstractLightblueDataRequest> asList(dfr, dfr2));
		// [dfr, dfr2]
		request.insertAfter(dfr3, dfr);
		// [dfr, dfr3, dfr2]
		ArrayNode requests = (ArrayNode) request.getBodyJson().get("requests");
		assertTrue(requests.size() == 3);
		assertTrue(requests.get(0).get("request").get("entity").asText().equals("foo"));
		assertTrue(requests.get(1).get("request").get("entity").asText().equals("fooz"));
		assertTrue(requests.get(2).get("request").get("entity").asText().equals("fooy"));
	}

	@Test
	public void testInsertAfterRequest() {
		DataFindRequest dfr = new DataFindRequest("foo", "bar");
		dfr.select(Projection.includeField("*"));
		dfr.where(Query.regex("foo", "*", 0));

		DataFindRequest dfr2 = new DataFindRequest("fooy", "bar");
		dfr2.select(Projection.includeField("*"));
		dfr2.where(Query.regex("fooy", "*", 0));

		DataFindRequest dfr3 = new DataFindRequest("fooz", "bar");
		dfr3.select(Projection.includeField("*"));
		dfr3.where(Query.regex("fooz", "*", 0));

		request.addAll(Arrays.<AbstractLightblueDataRequest> asList(dfr, dfr2));
		// [dfr, dfr2]
		request.insertBefore(dfr3, dfr);
		// [dfr3, dfr, dfr2]
		ArrayNode requests = (ArrayNode) request.getBodyJson().get("requests");
		assertTrue(requests.size() == 3);
		assertTrue(requests.get(0).get("request").get("entity").asText().equals("fooz"));
		assertTrue(requests.get(1).get("request").get("entity").asText().equals("foo"));
		assertTrue(requests.get(2).get("request").get("entity").asText().equals("fooy"));
	}

	@Test
	public void testGetJson() {
		String expected = "{\"requests\":[{\"seq\":0,\"op\":\"find\",\"request\":{\"query\":{\"field\":\"foo\",\"regex\":\"*\",\"caseInsensitive\":false,\"extended\":false,\"multiline\":false,\"dotall\":false},\"projection\":{\"field\":\"*\",\"include\":true,\"recursive\":false},\"entity\":\"foo\",\"entityVersion\":\"bar\"}},{\"seq\":1,\"op\":\"find\",\"request\":{\"query\":{\"field\":\"fooz\",\"regex\":\"*\",\"caseInsensitive\":false,\"extended\":false,\"multiline\":false,\"dotall\":false},\"projection\":{\"field\":\"*\",\"include\":true,\"recursive\":false},\"entity\":\"fooz\",\"entityVersion\":\"bar\"}}]}";

		DataFindRequest dfr = new DataFindRequest("foo", "bar");
		dfr.select(Projection.includeField("*"));
		dfr.where(Query.regex("foo", "*", 0));

		DataFindRequest dfr2 = new DataFindRequest("fooz", "bar");
		dfr2.select(Projection.includeField("*"));
		dfr2.where(Query.regex("fooz", "*", 0));

		request.addAll(Arrays.<AbstractLightblueDataRequest> asList(dfr, dfr2));

		assertEquals(expected, request.getBody());
	}

	@Test
	public void testGetOperationPathParam() {
		Assert.assertEquals("localhost/bulk", request.getRestURI("localhost"));
	}
}
