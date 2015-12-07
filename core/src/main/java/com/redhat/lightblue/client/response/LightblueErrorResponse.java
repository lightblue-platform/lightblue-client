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

    /**
     * Use {@link #hasLightblueErrors()}. The fact that a {@link LightblueErrorResponse} was even returned indicates
     * that some sort of error state exists. This method returns an answer that is already known.
     * @return <code>true</code> if the response has either errors or data errors.
     */
    @Deprecated
    boolean hasError();

    /**
     * @return <code>true</code> if any data errors exist on this response, otherwise <code>false</code>.
     */
    boolean hasDataErrors();

    /**
     * @return <code>true</code> if any lightblue errors exist on this response, otherwise <code>false</code>.
     */
    boolean hasLightblueErrors();

    /**
     * Use {@link #getLightblueErrors()}
     */
    @Deprecated
    Error[] getErrors();

    /**
     * @return returns any {@link Error}s on this response.
     */
    Error[] getLightblueErrors();

    /**
     * @return returns any {@link DataError}s on this reponse.
     */
    DataError[] getDataErrors();

}
