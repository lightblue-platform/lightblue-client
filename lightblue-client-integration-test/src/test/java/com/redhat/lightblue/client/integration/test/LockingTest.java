package com.redhat.lightblue.client.integration.test;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.redhat.lightblue.client.Locking;

public class LockingTest extends AbstractLightblueClientCRUDController {

    private static final String LOCK_DOMAIN = "test";

    public LockingTest() throws Exception {
        super();
    }

    @Override
    protected JsonNode[] getMetadataJsonNodes() throws Exception {
        return new JsonNode[]{};
    }

    @Test
    public void testAcquireAndRelease() throws Exception {
        Locking lbLocker = getLightblueClient().getLocking(LOCK_DOMAIN);
        assertTrue(lbLocker.acquire("test-lock", 300000L));
        assertTrue(lbLocker.release("test-lock"));
    }

}
