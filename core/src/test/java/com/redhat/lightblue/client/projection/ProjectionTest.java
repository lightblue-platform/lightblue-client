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
package com.redhat.lightblue.client.projection;

import com.redhat.lightblue.client.expression.query.Query;
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
        Query myEx = new Query() {
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
