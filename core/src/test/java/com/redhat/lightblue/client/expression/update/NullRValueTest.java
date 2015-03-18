package com.redhat.lightblue.client.expression.update;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class NullRValueTest {

    @Test
    public void testToJson(){
        assertEquals("\"$null\"", new NullRValue().toJson());
    }

}
