package com.redhat.lightblue.client.expression.query;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import com.redhat.lightblue.client.enums.ExpressionOperation;
import com.redhat.lightblue.client.enums.NaryExpressionOperation;

/**
 * Created by bmiller on 10/10/14.
 *
 * @author bmiller
 * @author mpatercz
 */
public class ValueQuery implements Query {
    private final String field;
    private final String rValue;
    private final String[] values;
    private final String operator;

    public static final Pattern expressionPattern = Pattern.compile("([\\w|\\*|\\.]+)\\s*(\\S+)\\s*(.+)$");

    public ValueQuery(String expression) {
        Matcher m = expressionPattern.matcher(expression);
        if (m.find()) {
            field = m.group(1);
            operator = m.group(2);
            String value = m.group(3);

            if (ExpressionOperation.contains(operator)) {
                rValue = value;
                values = null;
            } else if (NaryExpressionOperation.contains(operator)) {
                rValue = null;
                values = value.substring(1, value.length() - 1).split("\\s*,\\s*");
            } else {
                Set<String> allowedOperators = new HashSet<>(ExpressionOperation.getOperators());
                allowedOperators.addAll(NaryExpressionOperation.getOperators());
                throw new IllegalArgumentException(operator + " operator is not allowed. Allowed options are: " + allowedOperators.toString());
            }
        } else {
            throw new IllegalArgumentException("'" + expression + "' is incorrect");
        }
    }

    public ValueQuery(String field, ExpressionOperation operation, String rValue) {
        this.field = field;
        this.operator = operation.toString();
        this.rValue = rValue;
        this.values = null;
    }

    public ValueQuery(String field, NaryExpressionOperation operation, String... values) {
        this.field = field;
        this.operator = operation.toString();
        this.rValue = null;
        this.values = values;
    }

    @Override
    public String toJson() {
        StringBuilder json = new StringBuilder("{\"field\":");
        json.append("\"").append(field).append("\",");
        json.append("\"op\":");
        json.append("\"").append(operator).append("\",");
        if (rValue == null) {
            json.append("\"values\":");
            json.append("[\"").append(StringUtils.join(values, "\",\"")).append("\"]");
        } else {
            json.append("\"rvalue\":");
            json.append("\"").append(rValue).append("\"");
        }
        json.append("}");
        return json.toString();
    }

    @Override
    public String toString() {
        return toJson();
    }

    /**
     * Expression must follow this pattern: field op value, where field cannot
     * contain any whitespace characters (value can) and op is one of the
     * following operators: {"=", "!=", "<", ">", "<=", ">=", "$eq", "$neq",
     * "$lt", "$lte", "$gte", $in, $not_in, $nin }
     *
     * @param expression
     * @return
     */
    public static ValueQuery withValue(String expression) {
        return new ValueQuery(expression);
    }
}
