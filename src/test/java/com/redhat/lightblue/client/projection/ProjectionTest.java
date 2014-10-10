package com.redhat.lightblue.client.projection;

import com.redhat.lightblue.client.expression.Expression;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by jblashka on 10/10/14.
 */
public class ProjectionTest {
    @Test
    public void testFieldProjectionToJson() {
        String expectedJson = "{\"field\":\"*\",\"include\":true,\"recursive\":true}";
        FieldProjection fp = new FieldProjection("*",true,true);
        String json = fp.toJson();
        assertEquals(json, expectedJson);
    }

    @Test
    public void testArrayProjectionToJson() {
        String expectedJson = "{\"field\":\"termsVerbiage\",\"include\":true,\"match\":{\"field\":\"statusCode\",\"op\":\"=\",\"rvalue\":\"active\"},\"project\":{\"field\":\"*\"}}";
        Projection myProj = new Projection (){
            public String toJson() {
                return "{\"field\":\"*\"}";
            }
        };
        Expression myEx = new Expression() {
            public String toJson() {
                return "{\"field\":\"statusCode\",\"op\":\"=\",\"rvalue\":\"active\"}";
            }
        };
        ArrayProjection ap = new ArrayProjection("termsVerbiage", true, myEx, myProj);
        assertEquals(ap.toJson(), expectedJson);
    }

    @Test
    public void testRangeProjectionToJson() {
        String expectedJson = "{\"field\":\"termsVerbiageTranslation\",\"include\":true,\"range\":[0,1],\"project\":{\"field\":\"*\"}}";
        Projection myProj = new Projection (){
            public String toJson() {
                return "{\"field\":\"*\"}";
            }
        };
        RangeProjection rp = new RangeProjection("termsVerbiageTranslation", true, 0, 1, myProj);
        assertEquals(rp.toJson(), expectedJson);
    }

}
