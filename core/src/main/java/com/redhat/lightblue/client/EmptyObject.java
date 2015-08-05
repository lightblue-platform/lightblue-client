package com.redhat.lightblue.client;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;

public final class EmptyObject extends ExpressionPart implements Update.SetLiteral, Update.AppendInsertLiteral {
    public EmptyObject() {
        super(JsonNodeFactory.instance.objectNode());
    }
}
