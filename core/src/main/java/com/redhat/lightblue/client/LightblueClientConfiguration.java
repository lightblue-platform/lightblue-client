package com.redhat.lightblue.client;

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
		this.certAlias = configuration.certAlias;
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

	public void setCertAlias(String certAlias) {
		this.certAlias = certAlias;
	}
}
