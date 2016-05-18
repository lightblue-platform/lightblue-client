package com.redhat.lightblue.client;

import java.util.Date;

import org.junit.Assert;
import org.junit.Test;

import com.redhat.lightblue.client.util.ClientConstants;

public class LiteralTest {

    @Test
    public void testLong() {
        long longPrimitive = 1234567l;

        Assert.assertEquals("1234567", Literal.value(longPrimitive).toJson().toString());
        Assert.assertEquals("[1234567,-1234567]", Literal.toJson(Literal.values(new long[]{longPrimitive, -longPrimitive})).toString());

        Long longObject = 1234567l;

        Assert.assertEquals("1234567", Literal.value(longObject).toJson().toString());
        Assert.assertEquals("[1234567,-1234567]", Literal.toJson(new Literal[]{Literal.value(longObject), Literal.value(-longObject)}).toString());
    }

    @Test
    public void testIntPrimitiveArray() {
        int[] values = new int[]{0, 1, 2};

        Literal oldWay = Literal.value(Literal.toJson(Literal.values(values)));
        Literal newWay = Literal.value(values);

        Assert.assertEquals(oldWay.toString(), newWay.toString());
        Assert.assertEquals("[0,1,2]", newWay.toString());
    }

    @Test
    public void testLongPrimitiveArray() {
        long[] values = new long[]{0, 1, 2};

        Literal oldWay = Literal.value(Literal.toJson(Literal.values(values)));
        Literal newWay = Literal.value(values);

        Assert.assertEquals(oldWay.toString(), newWay.toString());
        Assert.assertEquals("[0,1,2]", newWay.toString());
    }

    @Test
    public void testBooleanPrimitiveArray() {
        boolean[] values = new boolean[]{true, false};

        Literal l = Literal.value(values);

        Assert.assertEquals("[true,false]", l.toString());
    }

    @Test
    public void testNumberArray() {
        Number[] values = new Number[]{0, 1, 2};

        Literal oldWay = Literal.value(Literal.toJson(Literal.numbers(values)));
        Literal newWay = Literal.value(values);

        Assert.assertEquals(oldWay.toString(), newWay.toString());
        Assert.assertEquals("[0,1,2]", newWay.toString());
    }

    @Test
    public void testStringArray() {
        String[] values = new String[]{"hello", "world"};

        Literal oldWay = Literal.value(Literal.toJson(Literal.values(values)));
        Literal newWay = Literal.value(values);

        Assert.assertEquals(oldWay.toString(), newWay.toString());
        Assert.assertEquals("[\"hello\",\"world\"]", newWay.toString());
    }

    @Test
    public void testBooleanArray() {
        Boolean[] values = new Boolean[]{true, false};

        Literal l = Literal.value(values);

        Assert.assertEquals("[true,false]", l.toString());
    }

    @Test
    public void testDateArray() {
        Date d1 = new Date();

        Date[] values = new Date[]{d1};

        Literal l = Literal.value(values);

        Assert.assertEquals("[\"" + ClientConstants.getDateFormat().format(d1) + "\"]", l.toString());
    }

    @Test
    public void testLiteralArray() {
        Literal[] values = new Literal[]{Literal.value("hello"), Literal.value("world")};

        Literal l = Literal.value(values);

        Assert.assertEquals("[\"hello\",\"world\"]", l.toString());
    }

}
