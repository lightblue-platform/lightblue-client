package com.redhat.lightblue.client.integration.test;

import static com.redhat.lightblue.util.test.AbstractJsonNodeTest.loadJsonNode;

import java.io.IOException;
import java.util.Arrays;

import com.fasterxml.jackson.databind.JsonNode;
import com.redhat.lightblue.client.integration.test.LightblueExternalResource.LightblueTestHarnessConfig;

public class LightblueTestHarnessConfigFactory {

    /**
     * Combines multiple sources of metadata.
     */
    public static LightblueTestHarnessConfig allOf(LightblueTestHarnessConfig... testMethods) {
        return new LightblueTestHarnessConfig() {
            @Override
            public JsonNode[] getMetadataJsonNodes() throws Exception {
                return Arrays.stream(testMethods)
                        .flatMap(m -> {
                            try {
                                return Arrays.stream(m.getMetadataJsonNodes());
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        })
                        .distinct()
                        .toArray(JsonNode[]::new);
            }
        };
    }

    /**
     * Reads metadata from JSON on the classpath.
     * @param paths Do not start them with a /.
     */
    public static LightblueTestHarnessConfig forClasspathJson(final String... paths) {
        return new LightblueTestHarnessConfig() {
            @Override
            public JsonNode[] getMetadataJsonNodes() throws Exception {
                return Arrays.stream(paths)
                        .distinct()
                        .map(path -> {
                            try {
                                return loadJsonNode(path);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        })
                        .toArray(JsonNode[]::new);
            }
        };
    }

}
