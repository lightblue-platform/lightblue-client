package com.redhat.lightblue.client.expression;

import com.redhat.lightblue.client.enums.SortDirection;
import com.redhat.lightblue.client.request.SortCondition;

import org.junit.Test;
import org.junit.Assert;

/**
 * Created by vkumar on 10/10/14.
 */
public class SortConditionTest {

     
    @Test
    public void testToJsonConstructedSortCondiitionExpression() {
    	
    	
    	 String expectedJson ="{\"field\":\"$asc\"}";
    
		SortCondition condition = new SortCondition("field", SortDirection.ASC)   ;  
        Assert.assertEquals(expectedJson, condition.toJson());
    }
    
}
