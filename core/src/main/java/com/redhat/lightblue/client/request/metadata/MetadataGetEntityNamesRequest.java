package com.redhat.lightblue.client.request.metadata;

import org.apache.commons.lang.StringUtils;

import com.redhat.lightblue.client.enums.MetadataStatus;
import com.redhat.lightblue.client.http.HttpMethod;
import com.redhat.lightblue.client.request.AbstractLightblueMetadataRequest;

public class MetadataGetEntityNamesRequest extends AbstractLightblueMetadataRequest {

	private MetadataStatus status;
    public MetadataGetEntityNamesRequest() {
        super();
    }

    public MetadataGetEntityNamesRequest(String entityName, String entityVersion) {
        super(entityName, entityVersion);
    }

    @Override
    public String getOperationPathParam() {
        return "";
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }
    
    @Override
    public String getRestURI(String baseServiceURI) {
    	StringBuilder requestURI = new StringBuilder(baseServiceURI);
    	MetadataStatus requestStatus = this.getStatus();
    	if (requestStatus!=null&&StringUtils.isNotBlank(requestStatus.getStatus())) {
            appendToURI(requestURI,"s="+this.getStatus().getStatus());
        }
    	return requestURI.toString();
    	
    }

	public MetadataStatus getStatus() {
		return status;
	}

	public void setStatus(MetadataStatus status) {
		this.status = status;
	}

}
