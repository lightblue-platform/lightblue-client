package com.redhat.lightblue.client.response;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class DiagnosticsElement implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private String elementName;
    
    private boolean healthy;
    
    private final Map<String, String> parameters;

    public DiagnosticsElement() {
        this.parameters = new LinkedHashMap<>();
    }
    
    public DiagnosticsElement(String elementName, Map<String, String> parameters) {
        super();
        this.elementName = elementName;
        
        if(parameters != null){
            this.parameters = new LinkedHashMap<>(parameters);
        }else{
            this.parameters = new LinkedHashMap<>();
        }
        
        if (parameters.get("healthy") != null) {
            healthy = Boolean.valueOf(parameters.get("healthy"));
        }
    }

    public String getElementName() {
        return elementName;
    }

    public boolean isHealthy() {
        return healthy;
    }

    public Map<String, String> getParameters() {
        return Collections.unmodifiableMap(parameters);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(elementName);
        builder.append(" [");
        builder.append("isHealthy=");
        builder.append(healthy);
        builder.append(", parameters=");
        builder.append(parameters.toString());
        builder.append("]");
        return builder.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((elementName == null) ? 0 : elementName.hashCode());
        result = prime * result + (healthy ? 1231 : 1237);
        result = prime * result + ((parameters == null) ? 0 : parameters.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        DiagnosticsElement other = (DiagnosticsElement) obj;
        if (elementName == null) {
            if (other.elementName != null)
                return false;
        } else if (!elementName.equals(other.elementName))
            return false;
        if (healthy != other.healthy)
            return false;
        if (parameters == null) {
            if (other.parameters != null)
                return false;
        } else if (!parameters.equals(other.parameters))
            return false;
        return true;
    }
}