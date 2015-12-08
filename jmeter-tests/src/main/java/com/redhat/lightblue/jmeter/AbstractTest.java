package com.redhat.lightblue.jmeter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.jmeter.config.Arguments;

import org.apache.jmeter.samplers.SampleResult;

import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;

import org.apache.jmeter.testelement.TestElement;

import com.redhat.lightblue.client.LightblueClient;
import com.redhat.lightblue.client.PropertiesLightblueClientConfiguration;
import com.redhat.lightblue.client.http.LightblueHttpClient;
import com.redhat.lightblue.client.hystrix.LightblueHystrixClient;

/**
 * Extend this class and override
 * runTest(SampleResult,JavaSamplerContext) to define a test. The test
 * will have access to the Lightblue client lbClient. The overriden
 * method does not need to record test stats, they are done here. In
 * case of error, throw exception.
 */
public abstract class AbstractTest extends AbstractJavaSamplerClient {

    private static final Logger LOGGER=LoggerFactory.getLogger(AbstractTest.class);

    protected LightblueClient lbClient;
    protected String name;
    protected String configFile;

    @Override
    public Arguments getDefaultParameters() {
        Arguments params=new Arguments();
        params.addArgument("ConfigFile",PropertiesLightblueClientConfiguration.DEFAULT_CONFIG_FILE);
        return params;
    }

    @Override
    public SampleResult runTest(JavaSamplerContext context) {
        getLogger().debug("Starting test "+name);
        SampleResult results=new SampleResult();
        results.setSampleLabel(name);
        try {
            results.sampleStart();
            getLogger().debug("runTest()");
            runTest(results,context);
            getLogger().debug("runTest() success");
            results.setSuccessful(true);
        } catch(Exception e) {
            getLogger().info("runTest() fail:"+e);
            results.setSuccessful(false);
            results.setResponseMessage(e.toString());
        } finally {
            results.sampleEnd();
        }
        return results;
    }

    protected abstract void runTest(SampleResult results,JavaSamplerContext context) throws Exception;
    
    @Override
    public void setupTest(JavaSamplerContext context) {
        getLogger().debug("Setup test");
        LightblueHttpClient httpClient;
        configFile=context.getParameter("ConfigFile",PropertiesLightblueClientConfiguration.DEFAULT_CONFIG_FILE);
        getLogger().debug("Getting client, config="+configFile);
        if (configFile != null) {
            lbClient = new LightblueHttpClient(configFile);
        } else {
            lbClient = new LightblueHttpClient();
        }
        getLogger().debug("lbClient="+lbClient);
        name = context.getParameter(TestElement.NAME);
    }

    @Override
    public void teardownTest(JavaSamplerContext context) {
    }
}
