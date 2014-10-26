package com.redhat.lightblue.client.projection;

import com.redhat.lightblue.client.query.QueryExpression;
import org.json.JSONException;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

/**
 * Created by jblashka on 10/10/14.
 */
public class ProjectionTest {
    @Test
    public void testFieldProjectionToJson() throws JSONException {
        String expectedJson = "{\"field\":\"*\",\"include\":true,\"recursive\":true}";
        FieldProjection fp = new FieldProjection("*",true,true);
        String json = fp.toJson();
        JSONAssert.assertEquals(json, expectedJson, false);
    }

    @Test
    public void testArrayProjectionToJson() throws JSONException {

        Projection myProj = new Projection (){
            public String toJson() {
                return "{\"field\":\"*\"}";
            }
        };
        QueryExpression myEx = new QueryExpression() {
            public String toJson() {
                return "{\"field\":\"statusCode\",\"op\":\"=\",\"rvalue\":\"active\"}";
            }
        };
        String expectedJson = "{\"include\":true,\"field\":\"termsVerbiage\",\"match\":" + myEx.toJson() + ",\"project\":[" + myProj.toJson() + "]}";

        ArrayProjection ap = new ArrayProjection("termsVerbiage", true, myEx, myProj);
        JSONAssert.assertEquals(expectedJson, ap.toJson(), false);
    }

    @Test
    public void testRangeProjectionToJson() throws JSONException {

        Projection myProj = new Projection (){
            public String toJson() {
                return "{\"field\":\"*\"}";
            }
        };
        String expectedJson = "{\"field\":\"termsVerbiageTranslation\",\"include\":true,\"range\":[0,1],\"project\":" + myProj.toJson() + "}";
        RangeProjection rp = new RangeProjection("termsVerbiageTranslation", true, 0, 1, myProj);
        JSONAssert.assertEquals(rp.toJson(), expectedJson, false);
    }

}
