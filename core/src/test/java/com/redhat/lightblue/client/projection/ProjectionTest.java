package com.redhat.lightblue.client.projection;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.redhat.lightblue.client.Projection;
import com.redhat.lightblue.client.Query;
import org.json.JSONException;
import org.junit.Test;
import org.junit.Assert;
import org.skyscreamer.jsonassert.JSONAssert;

/**
 * Created by jblashka on 10/10/14.
 */
public class ProjectionTest {
    @Test
    public void testFieldProjectionToJson() throws JSONException {
        String expectedJson = "{\"field\":\"*\",\"include\":true,\"recursive\":true}";
        Projection fp = Projection.field("*", true, true);
        String json = fp.toString();
        JSONAssert.assertEquals(json, expectedJson, false);
    }

    @Test
    public void testArrayProjectionToJson() throws JSONException {
        Projection myProj = Projection.field("*", false, false);
        Query myEx = Query.withValue("statusCode", Query.BinOp.eq, "active");
        String expectedJson = "{\"include\":true,\"field\":\"termsVerbiage\",\"match\":" + myEx.toString() + ",\"projection\":" + myProj.toString() + "}";

        Projection ap = Projection.array("termsVerbiage", myEx, myProj);
        JSONAssert.assertEquals(expectedJson, ap.toString(), false);
    }

    @Test
    public void testRangeProjectionToJson() throws JSONException {
        Projection myProj = Projection.field("*", false, false);

        String expectedJson = "{\"field\":\"termsVerbiageTranslation\",\"include\":true,\"range\":[0,1],\"projection\":" + myProj.toString() + "}";
        Projection rp = Projection.array("termsVerbiageTranslation", 0, 1, myProj);
        JSONAssert.assertEquals(rp.toString(), expectedJson, false);
    }

    @Test
    public void testRangeProjectionToJsonNullTo() throws JSONException {
        Projection myProj = Projection.field("*", false, false);

        String expectedJson = "{\"field\":\"termsVerbiageTranslation\",\"include\":true,\"range\":[0,null],\"projection\":" + myProj.toString() + "}";
        Projection rp = Projection.array("termsVerbiageTranslation", 0, null, myProj);

        JSONAssert.assertEquals(rp.toString(), expectedJson, false);
    }

    @Test
    public void testNestedProjectionShouldBeFlattened() throws JSONException {
        Projection p1 = Projection.project(Projection.includeField("something"),
                Projection.includeField("otherthing"));
        Projection p2 = Projection.project(p1, Projection.includeField("thirdThing"));
        Assert.assertTrue(p2.toJson() instanceof ArrayNode);
        Assert.assertEquals(3, p2.toJson().size());
    }

}
