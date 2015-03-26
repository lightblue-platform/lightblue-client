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
package com.redhat.lightblue.client.expression.update;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by vkumar on 10/10/14.
 */
public class SetUpdateTest {

    @Test
    public void testToJsonConstructedWithStringsAndExpressionOperation() {
        RValue rValueExpression = new RValue() {

            @Override
            public String toJson() {
                return "value";
            }
        };
        PathValuePair pathvaluepair = new PathValuePair("path", rValueExpression);
        SetUpdate expression = new SetUpdate(pathvaluepair);

        String expectedJson = "{\"$set\":{\"path\":value}}";

        Assert.assertEquals(expectedJson, expression.toJson());
    }
}
