package com.redhat.lightblue.client.response;

import com.redhat.lightblue.client.model.DataError;
import com.redhat.lightblue.client.model.Error;

/**
 * Lightblue response containing errors.
 *
 * @author mpatercz
 *
 */
public interface LightblueErrorResponse extends LightblueResponse {

    boolean hasError();

    boolean hasDataErrors();

    Error[] getErrors();

    DataError[] getDataErrors();

}
