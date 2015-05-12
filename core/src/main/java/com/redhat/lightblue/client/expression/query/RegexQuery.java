package com.redhat.lightblue.client.expression.query;

/**
 * Created by bmiller on 10/10/14.
 */
public class RegexQuery implements Query {
    private final String fieldName;
    private final String pattern;
    private final Boolean isCaseInsensitive;
    private final Boolean isExtended;
    private final Boolean isMultiline;
    private final Boolean isDotAll;

    public RegexQuery(String fieldName, String pattern, Boolean isCaseInsensitive, Boolean isExtended, Boolean isMultiline, Boolean isDotAll) {
        this.fieldName = fieldName;
        this.pattern = pattern;
        this.isCaseInsensitive = isCaseInsensitive;
        this.isExtended = isExtended;
        this.isMultiline = isMultiline;
        this.isDotAll = isDotAll;
    }

    @Override
    public String toJson() {
        StringBuilder json = new StringBuilder("{");
        json.append("\"field\":");
        json.append("\"").append(fieldName).append("\",");
        json.append("\"regex\":");
        json.append("\"").append(pattern).append("\",");
        json.append("\"caseInsensitive\":");
        json.append("\"").append(isCaseInsensitive).append("\",");
        json.append("\"extended\":");
        json.append("\"").append(isExtended).append("\",");
        json.append("\"multiline\":");
        json.append("\"").append(isMultiline).append("\",");
        json.append("\"dotall\":");
        json.append("\"").append(isDotAll).append("\"");
        json.append("}");

        return json.toString();
    }

    @Override
    public String toString() {
        return this.toJson();
    }

    /**
     * Creates and returns a {@link RegexQuery} for the passed in parameters.<br>
     * For this implementation, all optional flag fields are defaulted to <code>false</code>, see
     * {@link #withRegex(String, String, Boolean, Boolean, Boolean, Boolean)} for more granular control.
     * @param field
     * @param pattern
     * @return
     */
    public static RegexQuery withRegex(String field, String pattern) {
        return new RegexQuery(field, pattern, false, false, false, false);
    }

    public static RegexQuery withRegex(String field, String pattern, Boolean isCaseInsensitive, Boolean isExtended, Boolean isMultiline, Boolean isDotAll) {
        return new RegexQuery(field, pattern, isCaseInsensitive, isExtended, isMultiline, isDotAll);
    }
}
