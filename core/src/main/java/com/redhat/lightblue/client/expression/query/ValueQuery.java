package com.redhat.lightblue.client.expression.query;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.redhat.lightblue.client.enums.ExpressionOperation;

/**
 * Created by bmiller on 10/10/14.
 *
 * @author bmiller
 * @author mpatercz
 */
public class ValueQuery implements Query {
    private String field;
    private String rValue;
    private String operator;

    public static final Pattern expressionPattern = Pattern.compile("([\\w|\\*|\\.]+)\\s*(\\S+)\\s*(.+)$");

    // binary_comparison_operator := "=" | "!=" | "<" | ">" | "<=" | ">=" |
    //        "$eq" | "$neq" | "$lt" | "$gt" | "$lte" | "$gte
    public static final String[] allowedOperators = new String[]{"=", "!=", "<", ">", "<=", ">=", "$eq", "$neq", "$lt", "$lte", "$gte" };
    public static final Set<String> allowedOperatorsSet = new HashSet<String>(Arrays.asList(allowedOperators));

    public ValueQuery(String expression) {
        Matcher m = expressionPattern.matcher(expression);
        if (m.find()) {
            field = m.group(1);
            operator = m.group(2);
            rValue = m.group(3);

            if (!allowedOperatorsSet.contains(operator)) {
                throw new IllegalArgumentException(operator+" operator is not allowed. Allowed options are: "+allowedOperatorsSet.toString());
            }
        }
        else {
            throw new IllegalArgumentException("'"+expression +"' is incorrect");
        }
    }

    public ValueQuery(String field, ExpressionOperation operation, String rValue) {
        this(field + " " + operation.toString() + " " + rValue);
    }

    public String toJson() {
        StringBuilder json = new StringBuilder("{\"field\":");
        json.append("\"").append(field).append("\",");
        json.append("\"op\":");
        json.append("\"").append(operator).append("\",");
        json.append("\"rvalue\":");
        json.append("\"").append(rValue).append("\"");
        json.append("}");
        return json.toString();
    }

    @Override
    public String toString() {
        return toJson();
    }

    /**
     * Expression must follow this pattern: field op value, where field cannot
     * contain any whitespace characters (value can) and op is one of the following
     * operators: {"=", "!=", "<", ">", "<=", ">=", "$eq", "$neq", "$lt", "$lte", "$gte" }
     *
     * @param expression
     * @return
     */
    public static ValueQuery withValue(String expression){
        return new ValueQuery(expression);
    }
}
