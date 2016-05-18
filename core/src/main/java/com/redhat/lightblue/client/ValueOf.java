package com.redhat.lightblue.client;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;

public final class ValueOf extends ExpressionPart implements Update.SetLiteral, Update.AddLiteral, Update.AppendInsertLiteral {

    public ValueOf(String p) {
        super(JsonNodeFactory.instance.objectNode());
        ((ObjectNode) node).set("$valueOf", JsonNodeFactory.instance.textNode(p));
    }
}
