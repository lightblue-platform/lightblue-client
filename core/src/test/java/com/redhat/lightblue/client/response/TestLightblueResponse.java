package com.redhat.lightblue.client.response;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TestLightblueResponse {

	LightblueResponse testResponse = new LightblueResponse();

	private static final String initialResponseText = "{\"name\":\"value\"}";
	private static final String updatedResponseText = "{\"name\":\"value\"}";

	@Before
	public void setUp() throws Exception {
		testResponse = new LightblueResponse(initialResponseText);
	}

	@Test
	public void testGetText() {
		Assert.assertEquals(initialResponseText, testResponse.getText());
	}

	@Test
	public void testSetText() {
		testResponse.setText(updatedResponseText);
		Assert.assertEquals(updatedResponseText, testResponse.getText());
	}

	@Test
	public void testSetJson() throws JsonProcessingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		JsonNode node = mapper.readTree(updatedResponseText);

		testResponse.setJson(node);
		Assert.assertEquals(node, testResponse.getJson());
	}

}
