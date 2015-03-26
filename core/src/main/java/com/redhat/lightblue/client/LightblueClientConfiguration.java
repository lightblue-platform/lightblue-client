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

import java.util.Objects;

import org.apache.commons.io.FilenameUtils;

public class LightblueClientConfiguration {

    private String dataServiceURI;
    private String metadataServiceURI;
    private boolean useCertAuth = false;
    private String caFilePath;
    private String certFilePath;
    private String certPassword;
    private String certAlias;

    public LightblueClientConfiguration() {
    }

    /**
     * Copy constructor.
     */
    public LightblueClientConfiguration(LightblueClientConfiguration configuration) {
        this.dataServiceURI = configuration.dataServiceURI;
        this.metadataServiceURI = configuration.metadataServiceURI;
        this.useCertAuth = configuration.useCertAuth;
        this.caFilePath = configuration.caFilePath;
        this.certFilePath = configuration.certFilePath;
        this.certPassword = configuration.certPassword;
        this.certAlias = FilenameUtils.getBaseName(this.certFilePath);
    }

    public String getDataServiceURI() {
        return dataServiceURI;
    }

    public void setDataServiceURI(String dataServiceURI) {
        this.dataServiceURI = dataServiceURI;
    }

    public String getMetadataServiceURI() {
        return metadataServiceURI;
    }

    public void setMetadataServiceURI(String metadataServiceURI) {
        this.metadataServiceURI = metadataServiceURI;
    }

    public boolean useCertAuth() {
        return useCertAuth;
    }

    public void setUseCertAuth(boolean useCertAuth) {
        this.useCertAuth = useCertAuth;
    }

    public String getCaFilePath() {
        return caFilePath;
    }

    public void setCaFilePath(String caFilePath) {
        this.caFilePath = caFilePath;
    }

    public String getCertFilePath() {
        return certFilePath;
    }

    public void setCertFilePath(String certFilePath) {
        this.certFilePath = certFilePath;
        this.certAlias = FilenameUtils.getBaseName(this.certFilePath);
    }

    public String getCertPassword() {
        return certPassword;
    }

    public void setCertPassword(String certPassword) {
        this.certPassword = certPassword;
    }

    public String getCertAlias() {
        return certAlias;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        LightblueClientConfiguration that = (LightblueClientConfiguration) o;

        if (useCertAuth != that.useCertAuth) {
            return false;
        }

        if (caFilePath != null ? !caFilePath.equals(that.caFilePath) : that.caFilePath != null) {
            return false;
        }

        if (certAlias != null ? !certAlias.equals(that.certAlias) : that.certAlias != null) {
            return false;
        }

        if (certFilePath != null ? !certFilePath.equals(that.certFilePath)
                : that.certFilePath != null) {
            return false;
        }

        if (certPassword != null ? !certPassword.equals(that.certPassword)
                : that.certPassword != null) {
            return false;
        }

        if (dataServiceURI != null ? !dataServiceURI.equals(that.dataServiceURI)
                : that.dataServiceURI != null) {
            return false;
        }

        if (metadataServiceURI != null ? !metadataServiceURI.equals(that.metadataServiceURI)
                : that.metadataServiceURI != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(useCertAuth, caFilePath, certAlias, certFilePath, certPassword,
                dataServiceURI, metadataServiceURI);
    }

    @Override
    public String toString() {
        return "LightblueClientConfiguration{"
                + "dataServiceURI='" + dataServiceURI + '\''
                + ", metadataServiceURI='" + metadataServiceURI + '\''
                + ", useCertAuth=" + useCertAuth
                + ", caFilePath='" + caFilePath + '\''
                + ", certFilePath='" + certFilePath + '\''
                + ", certPassword='" + certPassword + '\''
                + ", certAlias='" + certAlias + '\''
                + '}';
    }
}
