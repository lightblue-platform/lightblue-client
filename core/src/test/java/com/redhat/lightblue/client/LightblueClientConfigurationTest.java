/*
 Copyright 2015 Red Hat, Inc. and/or its affiliates.

 This file is part of lightblue.

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.redhat.lightblue.client;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class LightblueClientConfigurationTest {
    @Test
    public void shouldCopyAllPropertiesInCopyConstructor() {
        LightblueClientConfiguration original = new LightblueClientConfiguration();

        original.setUseCertAuth(true);
        original.setMetadataServiceURI("metadata");
        original.setDataServiceURI("data");
        original.setCertPassword("pass");
        original.setCertFilePath("certpath");
        original.setCaFilePath("capath");

        LightblueClientConfiguration copy = new LightblueClientConfiguration(original);

        assertEquals(true, copy.useCertAuth());
        assertEquals("metadata", copy.getMetadataServiceURI());
        assertEquals("data", copy.getDataServiceURI());
        assertEquals("pass", copy.getCertPassword());
        assertEquals("certpath", copy.getCertFilePath());
        assertEquals("capath", copy.getCaFilePath());
        assertEquals("certpath", copy.getCertAlias());

        // make sure they are copies
        original.setUseCertAuth(false);
        original.setMetadataServiceURI("1metadata");
        original.setDataServiceURI("1data");
        original.setCertPassword("1pass");
        original.setCertFilePath("1certpath");
        original.setCaFilePath("1capath");

        assertEquals(true, copy.useCertAuth());
        assertEquals("metadata", copy.getMetadataServiceURI());
        assertEquals("data", copy.getDataServiceURI());
        assertEquals("pass", copy.getCertPassword());
        assertEquals("certpath", copy.getCertFilePath());
        assertEquals("capath", copy.getCaFilePath());
        assertEquals("certpath", copy.getCertAlias());
    }
}
