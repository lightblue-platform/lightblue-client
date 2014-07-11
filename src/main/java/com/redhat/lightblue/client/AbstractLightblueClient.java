package com.redhat.lightblue.client;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.io.FileInputStream;
import java.security.Key;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

public abstract class AbstractLightblueClient implements LightblueClient {


    //private static final String FLD_SAML_FORM = "FORM";
    //private static final String FLD_SAML_INPUT = "INPUT";
    //private static final String FLD_SAML_NAME = "NAME";
    //private static final String FLD_SAML_ACTION = "ACTION";

    //private static final String FORM_ENCODING = "UTF-8";

    /**
     * These should be pulled out into property files
     */


    // service endpoint
    private String REST_SERVICE_ENDPOINT = "https://b01.cos.redhat.com:8443/rest/metadata";

    // SSL details
    private String CA_FILE = "/tmp/cacert.pem";         // path to CA Cert file
    private String CERT_FILE = "/tmp/lightbluedev.pcks12";   // path to private key
    private String CERT_PASSWORD = "xjg9owvz";               // private key password
    private String CERT_ALIAS = "lightbluedev";              // pcks12 alias



    public String getCAFile() {
        return this.CA_FILE;
    }

    public void setCAFile(String file) {
        this.CA_FILE = file;
    }

    public String getCertFile() {
        return this.CERT_FILE;
    }
    public void setCertFile(String file) {
        this.CERT_FILE = file;
    }

    public String getCertPassword() {
        return this.getCertPassword();
    }

    public void setCertPassword(String password) {
        this.CERT_PASSWORD = password;
    }

    public String getCertAlias() {
        return this.CERT_ALIAS;
    }

    public void setCertAlias(String alias) {
        this.CERT_ALIAS = alias;
    }

    public String getEndPointURI() {
            return REST_SERVICE_ENDPOINT;
    }


    public void setEndPointURI(String endPointURI) {
        this.REST_SERVICE_ENDPOINT = endPointURI;
    }

    private SSLContext getSSLContext() {
        SSLContext ctx = null;
        try {
            /* Load CA-Chain file */
            CertificateFactory cf = CertificateFactory.getInstance("X509");
            X509Certificate cert = (X509Certificate)cf.generateCertificate(new FileInputStream("/tmp/cacert.pem"));

            KeyStore ks = KeyStore.getInstance("pkcs12");
            KeyStore jks = KeyStore.getInstance("jks");

            char[] ks_password = "xjg9owvz".toCharArray();

            ks.load(new FileInputStream("/tmp/lightbluedev.pkcs12"), ks_password);

            jks.load(null, ks_password);

            jks.setCertificateEntry("lightbluedev",cert);
            Key key = ks.getKey("lightbluedev", ks_password);
            Certificate[] chain = ks.getCertificateChain("lightbluedev");
            jks.setKeyEntry("dparsonsmom", key, ks_password, chain);

            TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
            tmf.init(jks);

            KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
            kmf.init(ks, ks_password);

            ctx = SSLContext.getInstance("TLSv1");
            ctx.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
        }
        catch (Exception e) {
            //ruh-roh!
            System.out.println("some shit went down and it was bad...");
            System.out.println("exception: " + e);
        }
        return(ctx);
    }

    public CloseableHttpClient getClient() throws Exception {

        SSLContext sslcontext = this.getSSLContext();

        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                sslcontext,
                new String[] { "TLSv1" },
                null,
                SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

        CloseableHttpClient httpclient = HttpClients.custom()
                .setSSLSocketFactory(sslsf)
                .setRedirectStrategy(new LaxRedirectStrategy())
                .build();

        return httpclient;
    }

    public String getMetadata() throws Exception {

        StringBuffer response = new StringBuffer();

        System.setProperty("jsse.enableSNIExtension", "false");
        CloseableHttpClient httpClient = getClient();

        CloseableHttpResponse response1 = restRequest(httpClient);

        HttpEntity entity = response1.getEntity();
        response.append(EntityUtils.toString(entity));

        response1.close();

        //System.out.println("\n------------------------------------Second Request starting now (shouldn't do another login sequence)------------------------------------\n");

        printRequestDetails(restRequest(httpClient));

        httpClient.close();

        return response.toString();
    }

    public CloseableHttpResponse restRequest(CloseableHttpClient httpClient) throws Exception {

        HttpGet httpPost = new HttpGet(getEndPointURI());
        StringEntity inEntity = new StringEntity("");
        //httpPost.setEntity(inEntity);

        System.out.println("Executing request: " + httpPost.getRequestLine());

        CloseableHttpResponse response = httpClient.execute(httpPost);

        //printRequestDetails(response);

        return response;
    }

    public void printRequestDetails(CloseableHttpResponse response) throws Exception {
        HttpEntity entity = response.getEntity();

        System.out.println("----------------------------------------");
        System.out.println(response.getStatusLine());
        if (entity != null) {
            System.out.println("Response content length: " + entity.getContentLength());
        }
        System.out.println(EntityUtils.toString(entity));
    }
}
