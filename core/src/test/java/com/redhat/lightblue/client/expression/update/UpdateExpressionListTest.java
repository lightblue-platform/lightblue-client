package com.redhat.lightblue.client.expression.update;

import org.junit.Assert;
import org.junit.Test;

public class UpdateExpressionListTest {

    @Test
    public void test1() {
        Update x=new UpdateExpressionList(new SetUpdate(new PathValuePair("path",new Literal("value"))));
        Assert.assertEquals("{\"$set\":{\"path\":\"value\"}}",x.toJson());
    }

    @Test
    public void test2() {
        Update x=new UpdateExpressionList(new SetUpdate(new PathValuePair("path",new Literal("value"))),
                                          new SetUpdate(new PathValuePair("path2",new Literal("value2"))));
        Assert.assertEquals("[{\"$set\":{\"path\":\"value\"}},{\"$set\":{\"path2\":\"value2\"}}]",x.toJson());
    }
}
