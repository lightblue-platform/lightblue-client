package com.redhat.lightblue.client;

import java.util.Objects;

import org.apache.commons.io.FilenameUtils;

public class LightblueClientConfiguration {

    public enum Compression {
        NONE, LZF;

        public static Compression parseCompression(String str) {
            switch (str) {
            case "NONE":
                return NONE;
            case "LZF":
                return LZF;
            default:
                throw new IllegalArgumentException("Invalid compression " + str + ". Supported values are: NONE, LZF");
            }
        }
    }

    private String dataServiceURI;
    private String metadataServiceURI;
    private boolean useCertAuth = false;
    private String caFilePath;
    private String certFilePath;
    private String certPassword;
    private String certAlias;
    private Compression compression = Compression.LZF;

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
        this.compression = configuration.compression;
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

    /**
     * @return classpath reference for the CA file used to authenticate the validity of
     *         the LB server certificate
     */
    public String getCaFilePath() {
        return caFilePath;
    }

    /**
     * @param caFilePath classpath reference for the CA file used to authenticate the validity of
     *                   the LB server certificate
     */
    public void setCaFilePath(String caFilePath) {
        this.caFilePath = caFilePath;
    }

    /**
     * @return classpath reference for the private key of the LB client
     */
    public String getCertFilePath() {
        return certFilePath;
    }

    /**
     * @param certFilePath classpath reference for the private key of the LB client
     */
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

        if (!Objects.equals(this.compression, compression)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(useCertAuth, caFilePath, certAlias, certFilePath, certPassword,
                dataServiceURI, metadataServiceURI, compression);
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
                + ", compression='" + compression + '\''
                + '}';
    }

    public Compression getCompression() {
        return compression;
    }

    public void setCompression(Compression compression) {
        this.compression = compression;
    }
}
