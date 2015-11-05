package com.redhat.lightblue.client.response;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.redhat.lightblue.client.util.JSON;

public class TestDefaultLightblueResponse {

    DefaultLightblueResponse testResponse = new DefaultLightblueResponse();

    private static final String initialResponseText = "{\"name\":\"value\"}";
    private static final String updatedResponseText = "{\"name\":\"value\"}";

    @Before
    public void setUp() throws Exception {
        testResponse = new DefaultLightblueResponse(initialResponseText);
    }

    @Test(expected = LightblueException.class)
    public void testConstructor_BadJson() throws LightblueException {
        new DefaultLightblueResponse("bad json");
    }

    @Test(expected = NullPointerException.class)
    public void testConstructor_NullObjectMapper() {
        ObjectMapper om = null;
        new DefaultLightblueResponse(om);
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
        ObjectMapper mapper = JSON.getDefaultObjectMapper();
        JsonNode node = mapper.readTree(updatedResponseText);

        testResponse.setJson(node);
        Assert.assertEquals(node, testResponse.getJson());
    }

    @Test
    public void testHasError_NoErrorElement_False() {
        assertFalse(testResponse.hasError());
    }

    @Test
    public void testHasError_False() throws LightblueException {
        DefaultLightblueResponse response = new DefaultLightblueResponse("{\"status\":\"successful\"}");
        assertFalse(response.hasError());
    }

    @Test
    public void testLightblueException_Error() throws LightblueException {
        try {
            DefaultLightblueResponse response = new DefaultLightblueResponse(
                    "{ " +
                    "    \"errors\": [ " +
                    "        { " +
                    "            \"context\": \"rest/SaveCommand/attributeCodeSet/save(attributeCodeSet:1.0.0-SNAPSHOT)/validateDocs/validateDoc/ownerCode/required\", " +
                    "            \"errorCode\": \"crud:Required\", " +
                    "            \"msg\": \"ownerCode\", " +
                    "            \"objectType\": \"error\" " +
                    "        } " +
                    "    ], " +
                    "    \"matchCount\": 0, " +
                    "    \"modifiedCount\": 0, " +
                    "    \"status\": \"ERROR\" " +
                    "}"
            );
            fail();
        } catch (LightblueResponseException e) {
            assertNotNull(e.getLightblueResponse());
            assertNull(e.getLightblueResponse().getDataErrors());
            assertNotNull(e.getLightblueResponse().getErrors());
            assertFalse(e.exists(LightblueResponseException.ERR_MONGO_CRUD_NO_ACCESS));
            assertTrue(e.exists(LightblueResponseException.ERR_CRUD_REQUIRED));
        }
    }

    @Test
    public void testLightblueException_DataError() throws LightblueException {
        try {
            DefaultLightblueResponse response = new DefaultLightblueResponse(
                    "{ " +
                    "    \"dataErrors\": [ " +
                    "        { " +
                    "            \"data\": { " +
                    "                \"_id\": \"12345678\" " +
                    "            }, " +
                    "            \"errors\": [ " +
                    "                { " +
                    "                    \"context\": \"rest/InsertCommand/entity1/insert(entity1:1.0.0)/insert/insert\", " +
                    "                    \"errorCode\": \"mongo-crud:SaveError\", " +
                    "                    \"msg\": \"in com.redhat.lightblue.mongo.hystrix.InsertCommand\", " +
                    "                    \"objectType\": \"error\" " +
                    "                } " +
                    "            ] " +
                    "        } " +
                    "    ], " +
                    "    \"matchCount\": 0, " +
                    "    \"modifiedCount\": 0, " +
                    "    \"status\": \"ERROR\" " +
                    "}"
            );
            fail();
        } catch (LightblueResponseException e) {
            assertNotNull(e.getLightblueResponse());
            assertNotNull(e.getLightblueResponse().getDataErrors());
            assertNull(e.getLightblueResponse().getErrors());
            assertFalse(e.exists(LightblueResponseException.ERR_MONGO_CRUD_NO_ACCESS));
            assertTrue(e.exists(LightblueResponseException.ERR_MONGO_CRUD_SAVE_ERROR));
        }
    }

    @Test(expected = LightblueException.class)
    public void testHasError_Partial_True() throws LightblueException {
        DefaultLightblueResponse response = new DefaultLightblueResponse("{\"status\":\"partial\"}");
    }

    @Test
    public void testParseProcessed_EmptyProcessed_ForArrayGeneric() throws LightblueException, LightblueParseException {
        LightblueResponse response = new DefaultLightblueResponse("{\"matchCount\": 0, \"modifiedCount\": 0, \"processed\": [], \"status\": \"COMPLETE\"}");

        Object[] results = response.parseProcessed(Object[].class);

        Assert.assertNotNull(results);
        Assert.assertEquals(0, results.length);
    }

    @Test
    public void testParseProcessed_EmptyProcessed_ForSimpleGeneric() throws LightblueException, LightblueParseException {
        LightblueResponse response = new DefaultLightblueResponse("{\"matchCount\": 0, \"modifiedCount\": 0, \"processed\": [], \"status\": \"COMPLETE\"}");

        Object results = response.parseProcessed(Object.class);

        Assert.assertNull(results);
    }

   @Test
    public void testParseProcessed() throws LightblueException, LightblueParseException {
        LightblueResponse response = new DefaultLightblueResponse("{\"matchCount\": 1, \"modifiedCount\": 0, \"processed\": [{\"_id\": \"idhash\", \"field\":\"value\"}], \"status\": \"COMPLETE\"}");

        SimpleModelObject[] results = response.parseProcessed(SimpleModelObject[].class);

        Assert.assertNotNull(results);
        Assert.assertEquals(1, results.length);

        Assert.assertEquals(new SimpleModelObject("idhash", "value"), results[0]);
    }

    @Test
    public void testParseProcess_NonArrayResult() throws LightblueException, LightblueParseException {
        LightblueResponse response = new DefaultLightblueResponse("{\"matchCount\": 1, \"modifiedCount\": 0, \"processed\": [{\"_id\": \"idhash\", \"field\":\"value\"}], \"status\": \"COMPLETE\"}");
        SimpleModelObject results = response.parseProcessed(SimpleModelObject.class);

        Assert.assertNotNull(results);
        Assert.assertEquals(new SimpleModelObject("idhash", "value"), results);
    }

    @Test
    public void testParseProcessed_NullProcessedNode() throws LightblueException, LightblueParseException {
        LightblueResponse response = new DefaultLightblueResponse("{\"matchCount\": 0, \"modifiedCount\": 0, \"processed\": null, \"status\": \"COMPLETE\"}");

        Object results = response.parseProcessed(Object.class);

        Assert.assertNull(results);
    }

    @Test
    public void testParseProcessedWithParsingError_DoesNotExist() throws LightblueException, LightblueParseException {
        LightblueResponse response = new DefaultLightblueResponse("{\"matchCount\": 0, \"modifiedCount\": 0, \"status\": \"COMPLETE\"}");

        Object[] results = response.parseProcessed(Object[].class);
        Assert.assertNotNull(results);
        Assert.assertEquals(0, results.length);
    }

    @Test(expected = LightblueParseException.class)
    public void testParseProcessedWithParsingError_InvalidJson() throws LightblueException, LightblueParseException {
        LightblueResponse response = new DefaultLightblueResponse("{\"processed\":\"<p>This is not json</p>\"}");

        response.parseProcessed(Object[].class);
    }

    @Test(expected = LightblueParseException.class)
    public void testParseProcessed_MultipleResults_ForNonArrayResponse() throws LightblueException, LightblueParseException {
        LightblueResponse response = new DefaultLightblueResponse("{\"matchCount\": 2, \"modifiedCount\": 0, \"processed\": [{\"_id\": \"idhash1\", \"field\":\"value1\"},{\"_id\": \"idhash2\", \"field\":\"value2\"}], \"status\": \"COMPLETE\"}");

        response.parseProcessed(Object.class);
    }

    @Test
    public void testParseModifiedCount() throws LightblueException {
        LightblueResponse response = new DefaultLightblueResponse("{\"matchCount\": 0, \"modifiedCount\": 5, \"status\": \"COMPLETE\"}");
        Assert.assertEquals(5, response.parseModifiedCount());
    }

    @Test
    public void testParseModifiedCount_zeroValue() throws LightblueException {
        LightblueResponse response = new DefaultLightblueResponse("{\"matchCount\": 0, \"modifiedCount\": 0, \"status\": \"COMPLETE\"}");
        Assert.assertEquals(0, response.parseModifiedCount());
    }

    @Test
    public void testParseModifiedCount_notExist() throws LightblueException {
        LightblueResponse response = new DefaultLightblueResponse("{\"matchCount\": 0, \"status\": \"COMPLETE\"}");
        Assert.assertEquals(0, response.parseModifiedCount());
    }

    @Test
    public void testParseModifiedCount_nullValue() throws LightblueException {
        LightblueResponse response = new DefaultLightblueResponse("{\"matchCount\": 0, \"modifiedCount\": null, \"status\": \"COMPLETE\"}");
        Assert.assertEquals(0, response.parseModifiedCount());
    }

    @Test
    public void testParseMatchCount() throws LightblueException {
        LightblueResponse response = new DefaultLightblueResponse("{\"matchCount\": 5, \"modifiedCount\": 0, \"status\": \"COMPLETE\"}");
        Assert.assertEquals(5, response.parseMatchCount());
    }

    @Test
    public void testParseMatchCount_zeroValue() throws LightblueException {
        LightblueResponse response = new DefaultLightblueResponse("{\"matchCount\": 0, \"modifiedCount\": 0, \"status\": \"COMPLETE\"}");
        Assert.assertEquals(0, response.parseMatchCount());
    }

    @Test
    public void testParseMatchCount_notExist() throws LightblueException {
        LightblueResponse response = new DefaultLightblueResponse("{\"modifiedCount\": 0, \"status\": \"COMPLETE\"}");
        Assert.assertEquals(0, response.parseMatchCount());
    }

    @Test
    public void testParseMatchCount_nullValue() throws LightblueException {
        LightblueResponse response = new DefaultLightblueResponse("{\"matchCount\": null, \"modifiedCount\": 0, \"status\": \"COMPLETE\"}");
        Assert.assertEquals(0, response.parseMatchCount());
    }

    private static class SimpleModelObject {

        String _id = "", field = "";

        public SimpleModelObject() {}

        public SimpleModelObject(String _id, String field) {
            super();
            this._id = _id;
            this.field = field;
        }

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public String getField() {
            return field;
        }

        public void setField(String field) {
            this.field = field;
        }

        @Override
        public boolean equals(Object obj) {
            SimpleModelObject o = (SimpleModelObject) obj;

            return _id.equals(o._id) && field.equals(o.field);
        }

    }

}
