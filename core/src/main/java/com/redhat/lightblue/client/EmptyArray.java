package com.redhat.lightblue.client;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;

public final class EmptyArray extends ExpressionPart implements Update.SetLiteral, Update.AppendInsertLiteral {
    public EmptyArray() {
        super(JsonNodeFactory.instance.arrayNode());
    }
}
