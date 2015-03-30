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

public class PathValuePairTest {

    @Test
    public void testToJsonConstructedWithStringsAndExpressionOperation() {
        PathValuePair expression = new PathValuePair("path", new ObjectRValue("value"));

        Assert.assertEquals("\"path\":\"value\"", expression.toJson());
    }

    @Test
    public void testToJsonConstructedWithStringsAndExpressionOperation_CreatedWithNullValue() {
        PathValuePair expression = new PathValuePair("path", null);

        Assert.assertEquals("\"path\":\"$null\"", expression.toJson());
    }

    @Test
    public void testToJsonConstructedWithStringsAndExpressionOperation_CreatedWithNullReturningRValue() {
        PathValuePair expression = new PathValuePair("path", new RValue() {

            @Override
            public String toJson() {
                return null;
            }

        });

        Assert.assertEquals("\"path\":\"$null\"", expression.toJson());
    }

    @Test
    public void testToJsonConstructedWithStringsAndExpressionOperation_CreatedWithNullStringReturningRValue() {
        PathValuePair expression = new PathValuePair("path", new RValue() {

            @Override
            public String toJson() {
                return "null";
            }

        });

        Assert.assertEquals("\"path\":\"$null\"", expression.toJson());
    }

}
