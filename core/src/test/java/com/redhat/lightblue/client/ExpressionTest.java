package com.redhat.lightblue.client;

import org.json.JSONException;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

public class ExpressionTest {

    private String esc(String s) {
        return s.replaceAll("\'","\"");
    }
    
    @Test
    public void queryTest() throws Exception {
        eq("{'field':'field','op':'=','rvalue':'value'}",Query.withValue("field",Query.eq,"value"));
        eq("{'field':'field','op':'=','rvalue':1}",Query.withValue("field",Query.eq,1));
        eq("{'field':'field','op':'=','rvalue':true}",Query.withValue("field",Query.eq,true));
        eq("{'field':'field','op':'=','rfield':'x'}",Query.withField("field",Query.eq,"x"));
        eq("{'field':'field','regex':'pattern','caseInsensitive':false,'extended':true,'multiline':true,'dotall':true}",
           Query.regex("field","pattern",false,true,true,true));
        eq("{'field':'field','op':'$in','values':[1,2,3]}",Query.withValues("field",Query.in,Literal.values(1,2,3)));
        eq("{'field':'field','op':'$in','rfield':'x'}",Query.withFieldValues("field",Query.in,"x"));
        eq("{'array':'a','contains':'$all','values':[1,2,3]}",Query.arrayContains("a",Query.all,Literal.values(1,2,3)));
        eq("{'array':'a','elemMatch':{'field':'x','op':'=','rvalue':1}}",Query.arrayMatch("a",Query.withValue("x",Query.eq,1)));
        eq("{'$and':[{'field':'a','op':'=','rvalue':1},{'field':'b','op':'=','rvalue':2}]}",Query.and(Query.withValue("a",Query.eq,1),
                                                                                                      Query.withValue("b",Query.eq,2)));
        eq("{'$or':[{'field':'a','op':'=','rvalue':1},{'field':'b','op':'=','rvalue':2}]}",Query.or(Query.withValue("a",Query.eq,1),
                                                                                                      Query.withValue("b",Query.eq,2)));
        eq("{'field':'field','op':'=','rvalue':'value'}",Query.withValue("field = value"));
        eq("{'field':'field','op':'=','rfield':'x'}",Query.withField("field=x"));
        eq("{'field':'field','op':'=','rfield':'x'}",Query.withField("field = x"));
        eq("{'field':'field','op':'$in','values':['1','2','3']}",Query.withValue("field $in [1,2,3]"));
        eq("{'field':'field','op':'$in','rfield':'x'}",Query.withField("field $in x"));
     }
    
    @Test
    public void projectionTest() throws Exception {
        eq("{'field':'*','recursive':true,'include':true}",Projection.field("*",true,true));
        eq("{'field':'a','match':{'field':'x','op':'=','rvalue':1},'include':true,'projection':{'field':'*','include':true,'recursive':true},'sort':{'x':'$asc'}}",
           Projection.array("a",Query.withValue("x",Query.eq,1),true,Projection.field("*",true,true),Sort.sort("x",true)));
        eq("{'field':'a','match':{'field':'x','op':'=','rvalue':1},'include':true,'projection':{'field':'*','include':true,'recursive':true}}",
           Projection.array("a",Query.withValue("x",Query.eq,1),true,Projection.field("*",true,true),null));
        eq("{'field':'a','range':[1,10],'include':true,'projection':{'field':'*','include':true,'recursive':true},'sort':{'x':'$asc'}}",
           Projection.array("a",1,10,true,Projection.field("*",true,true),Sort.sort("x",true)));
        eq("{'field':'a','range':[1,null],'include':true,'projection':{'field':'*','include':true,'recursive':true},'sort':{'x':'$asc'}}",
                Projection.array("a",1,null,true,Projection.field("*",true,true),Sort.sort("x",true)));
        eq("[{'field':'a','include':true,'recursive':true},{'field':'b','include':true,'recursive':false}]",
           Projection.project(Projection.field("a",true,true),Projection.field("b",true,false)));
    }

    @Test
    public void sortTest() throws Exception {
        eq("[{'x':'$asc'},{'y':'$desc'}]",Sort.sort(Sort.sort("x",true),Sort.sort("y",false)));
    }

    @Test
    public void updateTest() throws Exception {
        eq("{'$set':{'x':1}}",Update.set("x",1));
        eq("{'$set':{'x':'a','y':1}}",Update.set("x","a").more("y",1));
        eq("{'$unset':['a','b']}",Update.unset("a").more("b"));
        eq("{'$add':{'x':1}}",Update.addValue("x",Literal.value(1)));
        eq("{'$append': {'x':[ [],'x'] }}",Update.append("x",Literal.emptyArray()).more(Literal.value("x")));
        eq("{'$insert': {'x':[ [],'x'] }}",Update.insert("x",Literal.emptyArray()).more(Literal.value("x")));
        eq("{'$foreach': { 'x':{'field':'x','op':'=','rvalue':1},'$update':{'$set':{'y':2}}}}",Update.forEach("x",Query.withValue("x",Query.eq,1),Update.set("y",2)));
    }
    
    @Test
    public void testNot() throws JSONException {
        Query testQueryExpression = Query.withValue("test", Query.BinOp.neq, "hack");
        Query queryExpression = Query.not(testQueryExpression);
        String expectedJson = "{\"$not\":" + testQueryExpression.toJson() + "}";
        JSONAssert.assertEquals(expectedJson, queryExpression.toString(), false);
    }

    private void eq(String expected,Object node) throws Exception {
        System.out.println("Expected:"+expected+" node:"+node.toString());
        JSONAssert.assertEquals(esc(expected),node.toString(),false);
    }
}
