package com.redhat.lightblue.client.integration.test;

@Deprecated
public abstract class AbstractLightblueClientCRUDController extends LightblueClientTestHarness {

    /*
     * Functionality has been moved to LightblueClientTestHarness
     * class remains for legacy purposes.
     */

    public AbstractLightblueClientCRUDController() throws Exception {
        super();
    }

    public AbstractLightblueClientCRUDController(int httpServerPort) throws Exception {
        super(httpServerPort);
    }

}
