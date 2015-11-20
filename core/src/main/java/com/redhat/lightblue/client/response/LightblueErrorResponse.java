package com.redhat.lightblue.client.response;

import com.redhat.lightblue.client.model.DataError;
import com.redhat.lightblue.client.model.Error;

/**
 * Lightblue response containing errors.
 *
 * @author mpatercz
 *
 */
public interface LightblueErrorResponse {

    String getText();

    /**
     * @return <code>true</code> if any data errors exist on this response, otherwise <code>false</code>.
     */
    boolean hasDataErrors();

    /**
     * @return <code>true</code> if any lightblue errors exist on this response, otherwise <code>false</code>.
     */
    boolean hasLightblueErrors();

    /**
     * @return returns any {@link Error}s on this response.
     */
    Error[] getLightblueErrors();

    /**
     * @return returns any {@link DataError}s on this reponse.
     */
    DataError[] getDataErrors();
}
