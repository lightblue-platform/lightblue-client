package com.redhat.lightblue.client.request.metadata;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;

import org.apache.commons.lang.StringUtils;

import com.redhat.lightblue.client.enums.MetadataStatus;
import com.redhat.lightblue.client.http.HttpMethod;
import com.redhat.lightblue.client.request.AbstractLightblueMetadataRequest;

public class MetadataUpdateSchemaStatusRequest extends AbstractLightblueMetadataRequest {


	protected static final String COMMENT_QUERY_NAME = "comment";

    private MetadataStatus status;
    private String comment;


	public MetadataUpdateSchemaStatusRequest(String entityName, String entityVersion, MetadataStatus status) {
        super(entityName, entityVersion);
        this.status = status;
    }

    public MetadataUpdateSchemaStatusRequest(String entityName, String entityVersion, MetadataStatus status,String comment) {
        super(entityName, entityVersion);
        this.status = status;
        this.comment = comment;

    }

    @Override
    public String getOperationPathParam() {
        return status.getStatus();
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.PUT;
    }

    @Override
    public String getRestURI(String baseServiceURI) {
    	StringBuilder requestURI = new StringBuilder(super.getRestURI(baseServiceURI));
    	if (StringUtils.isNotBlank(getComment())) {
    	    try {
                appendToURI(requestURI, COMMENT_QUERY_NAME, URLEncoder.encode(getComment(), Charset.defaultCharset().toString()));
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        }
    	return requestURI.toString();

    }

    public void setStatus(MetadataStatus status) {
        this.status = status;
    }

	public void setComment(String comment) {
		this.comment = comment;
	}


    public String getComment() {
		return comment;
	}

}
