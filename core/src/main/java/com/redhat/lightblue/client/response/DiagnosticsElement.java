package com.redhat.lightblue.client.response;

public class DiagnosticsElement {

    private String elementName;
    private boolean isHealthy;
    private String message;

    public DiagnosticsElement(String elementName, boolean isHealthy, String message) {
        super();
        this.elementName = elementName;
        this.isHealthy = isHealthy;
        this.message = message;
    }

    public String getElementName() {
        return elementName;
    }

    public boolean isHealthy() {
        return isHealthy;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(elementName);
        builder.append(" [");
        builder.append("isHealthy=");
        builder.append(isHealthy);
        builder.append(", message=");
        builder.append(message);
        builder.append("]");
        return builder.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((elementName == null) ? 0 : elementName.hashCode());
        result = prime * result + (isHealthy ? 1231 : 1237);
        result = prime * result + ((message == null) ? 0 : message.hashCode());
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
        if (isHealthy != other.isHealthy)
            return false;
        if (message == null) {
            if (other.message != null)
                return false;
        } else if (!message.equals(other.message))
            return false;
        return true;
    }
}
