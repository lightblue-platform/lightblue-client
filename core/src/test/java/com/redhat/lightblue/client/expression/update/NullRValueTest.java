package com.redhat.lightblue.client.expression.update;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class NullRValueTest {

    @Test
    public void testToJson(){
        assertEquals("\"$null\"", new NullRValue().toJson());
    }

}
