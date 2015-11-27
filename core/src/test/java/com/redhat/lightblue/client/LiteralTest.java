package com.redhat.lightblue.client;

import org.junit.Assert;
import org.junit.Test;

public class LiteralTest {

    @Test
    public void testLong() {
        long longPrimitive = 1234567l;

        Assert.assertEquals("1234567", Literal.value(longPrimitive).toJson().toString());
        Assert.assertEquals("[1234567,-1234567]", Literal.toJson( Literal.values( new long[] { longPrimitive, -longPrimitive})).toString());

        Long longObject = 1234567l;

        Assert.assertEquals("1234567", Literal.value(longObject).toJson().toString());
        Assert.assertEquals("[1234567,-1234567]", Literal.toJson(new Literal[] { Literal.value(longObject), Literal.value(-longObject) }).toString());
    }

}
