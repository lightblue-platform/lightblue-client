package com.redhat.lightblue.client.integration.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.redhat.lightblue.client.Locking;
import com.redhat.lightblue.client.Locking.Lock;
import com.redhat.lightblue.client.response.LightblueException;

public class LockingTest extends LightblueClientTestHarness {

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

    @Test
    public void testLock() throws LightblueException, IOException {
        Locking lbLocker = getLightblueClient().getLocking(LOCK_DOMAIN);
        try (Lock lock = lbLocker.lock("test-lock")) {
            assertEquals(1, lock.getLockCount());
        }
        assertEquals(0, lbLocker.getLockCount("test-lock"));
    }

}
