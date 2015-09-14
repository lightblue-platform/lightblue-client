package com.redhat.lightblue.client.integration.test;

import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.internal.runners.statements.RunAfters;
import org.junit.internal.runners.statements.RunBefores;
import org.junit.rules.RunRules;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.junit.runners.model.TestClass;

public class BeforeAfterTestRule implements TestRule {

    private final TestClass[] extensions;

    public BeforeAfterTestRule(TestClass... extensions) {
        this.extensions = extensions;
    }

    @Override
    public Statement apply(Statement base, Description description) {
        //Create statement chain
        Statement newStatement = base;
        for (TestClass extension : extensions) {
            newStatement = prepareBeforeClasses(extension, newStatement);
            newStatement = prepareAfterClasses(extension, newStatement);
            newStatement = prepareRules(extension, newStatement, description);
            newStatement = prepareBefores(extension, newStatement, null);
            newStatement = prepareAfters(extension, newStatement, null);
        }

        return newStatement;
    }

    protected Statement prepareBeforeClasses(TestClass extension, Statement base) {
        return new RunBefores(
                base, extension.getAnnotatedMethods(BeforeClass.class), null);
    }

    protected Statement prepareAfterClasses(TestClass extension, Statement base) {
        return new RunAfters(
                base, extension.getAnnotatedMethods(AfterClass.class), null);
    }

    protected Statement prepareBefores(TestClass extension, Statement base, Object target) {
        return new RunBefores(
                base, extension.getAnnotatedMethods(Before.class), target);
    }

    protected Statement prepareAfters(TestClass extension, Statement base, Object target) {
        return new RunAfters(
                base, extension.getAnnotatedMethods(After.class), target);
    }

    protected Statement prepareRules(TestClass extension, Statement base, Description description) {
        List<TestRule> rules = extension.getAnnotatedFieldValues(null, Rule.class, TestRule.class);
        rules.addAll(extension.getAnnotatedFieldValues(null, ClassRule.class, TestRule.class));
        return new RunRules(base, rules, description);
    }

}
