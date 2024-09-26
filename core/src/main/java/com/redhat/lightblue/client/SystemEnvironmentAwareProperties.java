package com.redhat.lightblue.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Enumeration;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Extended Properties class for working with Strings that have placeholders for system environment variables in them.
 * A placeholder takes the form ${env.NAME}.
 *
 * <p>
 * Example: dataServiceURI=${env.LIGHTBLUE_DATA_SERVICE_URL}
 * </p>
 */
public class SystemEnvironmentAwareProperties extends Properties {

    private static final Pattern PROPERTY_PLACEHOLDER_PATTERN = Pattern.compile("\\$\\{env\\.(.*?)\\}");

    @Override
    public synchronized void load(Reader reader) throws IOException {
        super.load(reader);
        replacePlaceholders();
    }

    @Override
    public synchronized void load(InputStream inStream) throws IOException {
        super.load(inStream);
        replacePlaceholders();
    }

    /**
     * Loop through all properties and replace the placeholders of format ${env.NAME}
     * with the corresponding property value from the system environment.
     */
    private synchronized void replacePlaceholders() {
        for (Enumeration e = super.propertyNames() ; e.hasMoreElements() ;) {
            String name = (String)e.nextElement() ;
            super.setProperty(name, findAndReplacePlaceholders(super.getProperty(name)));
        }
    }


    /**
     * Replace placeholders in the provided value by the appropriate system environment variable, otherwise return the original value.
     * Placeholder referencing non-existing system environment variables are replaced by an empty string.
     *
     * @param value The property value with or without property placeholders.
     * @return the supplied value with placeholders replaced inline
     */
    private String findAndReplacePlaceholders(String value) {
        if (value==null) {
            return value;
        }

        Matcher matchPattern = PROPERTY_PLACEHOLDER_PATTERN.matcher(value);
        StringBuffer sb = new StringBuffer();
        while(matchPattern.find()) {
            String environmentVariable = System.getenv(matchPattern.group(1));
            matchPattern.appendReplacement(sb, environmentVariable!=null?Matcher.quoteReplacement(environmentVariable):"");
        }
        matchPattern.appendTail(sb);
        return sb.toString();
    }
}
