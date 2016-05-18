package com.redhat.lightblue.client.request.execution;

import com.redhat.lightblue.client.Expression;

/**
 * https://jewzaam.gitbooks.io/lightblue-specifications/content/language_specification/execution.html
 *
 * @author mpatercz
 *
 */
public abstract class Execution extends Expression {

    protected Execution() {
        super(false);
    }

}
