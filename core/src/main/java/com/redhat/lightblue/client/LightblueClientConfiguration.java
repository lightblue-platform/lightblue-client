package com.redhat.lightblue.client;

import com.redhat.lightblue.client.MongoExecution.ReadPreference;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    public static String CA_CERT_FILE_DELIMITER = ",";

    private String dataServiceURI;
    private String metadataServiceURI;
    private boolean useCertAuth = false;
    private String caFilePath;
    private String certFilePath;
    private String certPassword;
    private String certAlias;
    private Compression compression = Compression.LZF;
    private ReadPreference readPreference;
    private String writeConcern;
    private Integer maxQueryTimeMS;
    private transient Execution execution;

    public LightblueClientConfiguration() {
    }

    /**
     * Copy constructor.
     */
    public LightblueClientConfiguration(LightblueClientConfiguration configuration) {
        dataServiceURI = configuration.dataServiceURI;
        metadataServiceURI = configuration.metadataServiceURI;
        useCertAuth = configuration.useCertAuth;
        caFilePath = configuration.caFilePath;
        certFilePath = configuration.certFilePath;
        certPassword = configuration.certPassword;
        certAlias = FilenameUtils.getBaseName(certFilePath);
        compression = configuration.compression;
        readPreference = configuration.readPreference;
        writeConcern = configuration.writeConcern;
        maxQueryTimeMS = configuration.maxQueryTimeMS;
    }

    /**
     *
     * @return boolean whether or not the caFilePath value is actually a delimited list
     * representing multiple CA certs
     */
    public boolean hasMultipleCaCerts() {
        return (caFilePath.contains(CA_CERT_FILE_DELIMITER));
    }

    /**
     *
     * @return List<String> of caFilePaths generated from parsing the caFilePath
     * value using the value specified by the CA_CERT_FILE_DELIMITER constant.
     * If caFilePath value is not delimited, returns a List with only one path in it.
     */
    public List<String> getCaFilePaths() {
        List<String> caFilePaths = new ArrayList<>();

        if(StringUtils.isNotBlank(caFilePath)) {
          if(hasMultipleCaCerts()) {
                caFilePaths = Arrays.asList(caFilePath.split(CA_CERT_FILE_DELIMITER));
            } else {
                caFilePaths.add(caFilePath);
            }
        }

        return caFilePaths;
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
     * @return Reference for the CA file used to authenticate the validity of
     * the LB server certificate. Should either be on the classpath, or prefixed
     * with 'file://'.
     */
    public String getCaFilePath() {
        if(StringUtils.isNotBlank(caFilePath) && hasMultipleCaCerts()) {
            throw new IllegalStateException("There are multiple CA cert paths defined, use getCaFilePaths() instead");
        }
        return caFilePath;
    }

    /**
     * @param caFilePath Reference for the CA file used to authenticate the
     * validity of the LB server certificate. Should either be on the classpath,
     * or prefixed with 'file://'.
     */
    public void setCaFilePath(String caFilePath) {
        this.caFilePath = caFilePath;
    }

    /**
     * @return Reference for the private key of the LB client. Should either be
     * on the classpath, or prefixed with 'file://'/
     */
    public String getCertFilePath() {
        return certFilePath;
    }

    /**
     * @param certFilePath Reference for the private key of the LB client.
     * Should either be on the classpath, or prefixed with 'file://'/
     */
    public void setCertFilePath(String certFilePath) {
        this.certFilePath = certFilePath;
        certAlias = FilenameUtils.getBaseName(this.certFilePath);
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

    public Compression getCompression() {
        return compression;
    }

    public void setCompression(Compression compression) {
        this.compression = compression;
    }

    public ReadPreference getReadPreference() {
        return readPreference;
    }

    public void setReadPreference(ReadPreference readPreference) {
        this.readPreference = readPreference;
    }

    public String getWriteConcern() {
        return writeConcern;
    }

    public void setWriteConcern(String writeConcern) {
        this.writeConcern = writeConcern;
    }

    public Integer getMaxQueryTimeMS() {
        return maxQueryTimeMS;
    }

    public void setMaxQueryTimeMS(Integer maxQueryTimeMS) {
        this.maxQueryTimeMS = maxQueryTimeMS;
    }

    public Execution getExecution() {
        if (execution == null) {
            MongoExecution mongoExecution = new MongoExecution();
            if (getReadPreference() != null) {
                mongoExecution.addReadPreference(getReadPreference());
                execution = mongoExecution;
            }

            if (getWriteConcern() != null) {
                mongoExecution.addWriteConcern(getWriteConcern());
                execution = mongoExecution;
            }

            if (getMaxQueryTimeMS() != null) {
                mongoExecution.addMaxQueryTimeMS(getMaxQueryTimeMS());
                execution = mongoExecution;
            }
        }

        return execution;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        LightblueClientConfiguration other = (LightblueClientConfiguration) obj;
        if (caFilePath == null) {
            if (other.caFilePath != null) {
                return false;
            }
        } else if (!caFilePath.equals(other.caFilePath)) {
            return false;
        }
        if (certAlias == null) {
            if (other.certAlias != null) {
                return false;
            }
        } else if (!certAlias.equals(other.certAlias)) {
            return false;
        }
        if (certFilePath == null) {
            if (other.certFilePath != null) {
                return false;
            }
        } else if (!certFilePath.equals(other.certFilePath)) {
            return false;
        }
        if (certPassword == null) {
            if (other.certPassword != null) {
                return false;
            }
        } else if (!certPassword.equals(other.certPassword)) {
            return false;
        }
        if (compression != other.compression) {
            return false;
        }
        if (dataServiceURI == null) {
            if (other.dataServiceURI != null) {
                return false;
            }
        } else if (!dataServiceURI.equals(other.dataServiceURI)) {
            return false;
        }
        if (maxQueryTimeMS == null) {
            if (other.maxQueryTimeMS != null) {
                return false;
            }
        } else if (!maxQueryTimeMS.equals(other.maxQueryTimeMS)) {
            return false;
        }
        if (metadataServiceURI == null) {
            if (other.metadataServiceURI != null) {
                return false;
            }
        } else if (!metadataServiceURI.equals(other.metadataServiceURI)) {
            return false;
        }
        if (readPreference != other.readPreference) {
            return false;
        }
        if (useCertAuth != other.useCertAuth) {
            return false;
        }
        if (writeConcern == null) {
            if (other.writeConcern != null) {
                return false;
            }
        } else if (!writeConcern.equals(other.writeConcern)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((caFilePath == null) ? 0 : caFilePath.hashCode());
        result = prime * result + ((certAlias == null) ? 0 : certAlias.hashCode());
        result = prime * result + ((certFilePath == null) ? 0 : certFilePath.hashCode());
        result = prime * result + ((certPassword == null) ? 0 : certPassword.hashCode());
        result = prime * result + ((compression == null) ? 0 : compression.hashCode());
        result = prime * result + ((dataServiceURI == null) ? 0 : dataServiceURI.hashCode());
        result = prime * result + ((maxQueryTimeMS == null) ? 0 : maxQueryTimeMS.hashCode());
        result = prime * result + ((metadataServiceURI == null) ? 0 : metadataServiceURI.hashCode());
        result = prime * result + ((readPreference == null) ? 0 : readPreference.hashCode());
        result = prime * result + (useCertAuth ? 1231 : 1237);
        result = prime * result + ((writeConcern == null) ? 0 : writeConcern.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "LightblueClientConfiguration [dataServiceURI=" + dataServiceURI
                + ", metadataServiceURI=" + metadataServiceURI
                + ", useCertAuth=" + useCertAuth
                + ", caFilePath=" + caFilePath
                + ", certFilePath=" + certFilePath
                + ", certPassword=" + certPassword
                + ", certAlias=" + certAlias
                + ", compression=" + compression
                + ", readPreference=" + readPreference
                + ", writeConcern=" + writeConcern
                + ", maxQueryTimeMS=" + maxQueryTimeMS
                + "]";
    }

}
