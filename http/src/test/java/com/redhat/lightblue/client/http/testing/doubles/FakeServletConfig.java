package com.redhat.lightblue.client.http.testing.doubles;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class FakeServletConfig implements ServletConfig {
    private String name;
    private ServletContext context;
    private Map<String, String> initParameters = new HashMap<>();

    @Override
    public String getServletName() {
        return name;
    }

    public FakeServletConfig setServletName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public ServletContext getServletContext() {
        return context;
    }

    public FakeServletConfig setServletContext(ServletContext context) {
        this.context = context;
        return this;
    }

    @Override
    public String getInitParameter(String name) {
        return initParameters.get(name);
    }

    public FakeServletConfig setInitParameter(String name, String value) {
        initParameters.put(name, value);
        return this;
    }

    @Override
    public Enumeration<String> getInitParameterNames() {
        return new Enumeration<String>() {
            final Iterator<String> paramNames = initParameters.keySet().iterator();

            @Override
            public boolean hasMoreElements() {
                return paramNames.hasNext();
            }

            @Override
            public String nextElement() {
                return paramNames.next();
            }
        };
    }
}
