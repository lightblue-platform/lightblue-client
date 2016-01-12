package com.redhat.lightblue.client.response;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.redhat.lightblue.client.LightblueException;
import com.redhat.lightblue.client.util.JSON;

public class TestDefaultLightblueDataResponse {

    private static final String initialResponseText = "{\"name\":\"value\"}";
    private static final String updatedResponseText = "{\"name\":\"value\"}";

    @Test(expected = LightblueException.class)
    public void testConstructor_BadJson() throws LightblueException {
        new DefaultLightblueDataResponse("bad json");
    }

    @Test(expected = NullPointerException.class)
    public void testConstructor_NullObjectMapper() throws Exception {
        ObjectMapper om = null;
        new DefaultLightblueDataResponse("{}", om);
    }

    @Test
    public void testGetText() throws Exception {
        DefaultLightblueDataResponse testResponse = new DefaultLightblueDataResponse(initialResponseText);
        Assert.assertEquals(initialResponseText, testResponse.getText());
    }

    @Test
    public void testSetText() throws Exception {
        DefaultLightblueDataResponse testResponse = new DefaultLightblueDataResponse(initialResponseText);
        Assert.assertEquals(updatedResponseText, testResponse.getText());
    }

    @Test
    public void testSetJson() throws Exception {
        ObjectMapper mapper = JSON.getDefaultObjectMapper();
        JsonNode node = mapper.readTree(updatedResponseText);

        DefaultLightblueDataResponse testResponse = new DefaultLightblueDataResponse(initialResponseText);
        Assert.assertEquals(node, testResponse.getJson());
    }

    @Test
    public void testHasError_NoErrorElement_False() throws Exception {
        assertFalse(
                new DefaultLightblueDataResponse(initialResponseText).hasLightblueErrors());
    }

    @Test
    public void testHasError_False() throws Exception {
        DefaultLightblueDataResponse response = new DefaultLightblueDataResponse("{\"status\":\"successful\"}");
        assertFalse(response.hasLightblueErrors());
    }

    @Test
    public void testLightblueException_Error() throws Exception {
        try {
            DefaultLightblueDataResponse response = new DefaultLightblueDataResponse(
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
            assertNotNull(e.getLightblueResponse().getLightblueErrors());
            assertFalse(e.exists(LightblueResponseErrorCodes.ERR_MONGO_CRUD_NO_ACCESS));
            assertTrue(e.exists(LightblueResponseErrorCodes.ERR_CRUD_REQUIRED));
        }
    }

    @Test
    public void testLightblueException_DataError() throws Exception {
        try {
            DefaultLightblueDataResponse response = new DefaultLightblueDataResponse(
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
            assertNull(e.getLightblueResponse().getLightblueErrors());
            assertFalse(e.exists(LightblueResponseErrorCodes.ERR_MONGO_CRUD_NO_ACCESS));
            assertTrue(e.exists(LightblueResponseErrorCodes.ERR_MONGO_CRUD_SAVE_ERROR));
        }
    }

    @Test(expected = LightblueException.class)
    public void testHasError_Partial_True() throws Exception {
        DefaultLightblueDataResponse response = new DefaultLightblueDataResponse("{\"status\":\"partial\"}");
    }

    @Test
    public void testParseProcessed_EmptyProcessed_ForArrayGeneric() throws Exception {
        DefaultLightblueDataResponse response = new DefaultLightblueDataResponse("{\"matchCount\": 0, \"modifiedCount\": 0, \"processed\": [], \"status\": \"COMPLETE\"}");

        Object[] results = response.parseProcessed(Object[].class);

        Assert.assertNotNull(results);
        Assert.assertEquals(0, results.length);
    }

    @Test
    public void testParseProcessed_EmptyProcessed_ForSimpleGeneric() throws Exception {
        DefaultLightblueDataResponse response = new DefaultLightblueDataResponse("{\"matchCount\": 0, \"modifiedCount\": 0, \"processed\": [], \"status\": \"COMPLETE\"}");

        Object results = response.parseProcessed(Object.class);

        Assert.assertNull(results);
    }

   @Test
    public void testParseProcessed() throws Exception {
        DefaultLightblueDataResponse response = new DefaultLightblueDataResponse("{\"matchCount\": 1, \"modifiedCount\": 0, \"processed\": [{\"_id\": \"idhash\", \"field\":\"value\"}], \"status\": \"COMPLETE\"}");

        SimpleModelObject[] results = response.parseProcessed(SimpleModelObject[].class);

        Assert.assertNotNull(results);
        Assert.assertEquals(1, results.length);

        Assert.assertEquals(new SimpleModelObject("idhash", "value"), results[0]);
    }

    @Test
    public void testParseProcess_NonArrayResult() throws Exception {
        DefaultLightblueDataResponse response = new DefaultLightblueDataResponse("{\"matchCount\": 1, \"modifiedCount\": 0, \"processed\": [{\"_id\": \"idhash\", \"field\":\"value\"}], \"status\": \"COMPLETE\"}");
        SimpleModelObject results = response.parseProcessed(SimpleModelObject.class);

        Assert.assertNotNull(results);
        Assert.assertEquals(new SimpleModelObject("idhash", "value"), results);
    }

    @Test
    public void testParseProcessed_NullProcessedNode() throws Exception {
        DefaultLightblueDataResponse response = new DefaultLightblueDataResponse("{\"matchCount\": 0, \"modifiedCount\": 0, \"processed\": null, \"status\": \"COMPLETE\"}");

        Object results = response.parseProcessed(Object.class);

        Assert.assertNull(results);
    }

    @Test
    public void testParseProcessedWithParsingError_DoesNotExist() throws Exception {
        DefaultLightblueDataResponse response = new DefaultLightblueDataResponse("{\"matchCount\": 0, \"modifiedCount\": 0, \"status\": \"COMPLETE\"}");

        Object[] results = response.parseProcessed(Object[].class);
        Assert.assertNotNull(results);
        Assert.assertEquals(0, results.length);
    }

    @Test(expected = LightblueParseException.class)
    public void testParseProcessedWithParsingError_InvalidJson() throws Exception {
        DefaultLightblueDataResponse response = new DefaultLightblueDataResponse("{\"processed\":\"<p>This is not json</p>\"}");

        response.parseProcessed(Object[].class);
    }

    @Test(expected = LightblueParseException.class)
    public void testParseProcessed_MultipleResults_ForNonArrayResponse() throws Exception {
        DefaultLightblueDataResponse response = new DefaultLightblueDataResponse("{\"matchCount\": 2, \"modifiedCount\": 0, \"processed\": [{\"_id\": \"idhash1\", \"field\":\"value1\"},{\"_id\": \"idhash2\", \"field\":\"value2\"}], \"status\": \"COMPLETE\"}");

        response.parseProcessed(Object.class);
    }

    @Test
    public void testParseModifiedCount() throws Exception {
        DefaultLightblueDataResponse response = new DefaultLightblueDataResponse("{\"matchCount\": 0, \"modifiedCount\": 5, \"status\": \"COMPLETE\"}");
        Assert.assertEquals(5, response.parseModifiedCount());
    }

    @Test
    public void testParseModifiedCount_zeroValue() throws Exception {
        DefaultLightblueDataResponse response = new DefaultLightblueDataResponse("{\"matchCount\": 0, \"modifiedCount\": 0, \"status\": \"COMPLETE\"}");
        Assert.assertEquals(0, response.parseModifiedCount());
    }

    @Test
    public void testParseModifiedCount_notExist() throws Exception {
        DefaultLightblueDataResponse response = new DefaultLightblueDataResponse("{\"matchCount\": 0, \"status\": \"COMPLETE\"}");
        Assert.assertEquals(0, response.parseModifiedCount());
    }

    @Test
    public void testParseModifiedCount_nullValue() throws Exception {
        DefaultLightblueDataResponse response = new DefaultLightblueDataResponse("{\"matchCount\": 0, \"modifiedCount\": null, \"status\": \"COMPLETE\"}");
        Assert.assertEquals(0, response.parseModifiedCount());
    }

    @Test
    public void testParseMatchCount() throws Exception {
        DefaultLightblueDataResponse response = new DefaultLightblueDataResponse("{\"matchCount\": 5, \"modifiedCount\": 0, \"status\": \"COMPLETE\"}");
        Assert.assertEquals(5, response.parseMatchCount());
    }

    @Test
    public void testParseMatchCount_zeroValue() throws Exception {
        DefaultLightblueDataResponse response = new DefaultLightblueDataResponse("{\"matchCount\": 0, \"modifiedCount\": 0, \"status\": \"COMPLETE\"}");
        Assert.assertEquals(0, response.parseMatchCount());
    }

    @Test
    public void testParseMatchCount_notExist() throws Exception {
        DefaultLightblueDataResponse response = new DefaultLightblueDataResponse("{\"modifiedCount\": 0, \"status\": \"COMPLETE\"}");
        Assert.assertEquals(0, response.parseMatchCount());
    }

    @Test
    public void testParseMatchCount_nullValue() throws Exception {
        DefaultLightblueDataResponse response = new DefaultLightblueDataResponse("{\"matchCount\": null, \"modifiedCount\": 0, \"status\": \"COMPLETE\"}");
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
