package com.redhat.lightblue.client.integration.test;

import static com.redhat.lightblue.client.expression.query.ValueQuery.withValue;
import static com.redhat.lightblue.client.projection.FieldProjection.includeField;
import static com.redhat.lightblue.util.test.AbstractJsonNodeTest.loadJsonNode;

import java.net.UnknownHostException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.redhat.lightblue.client.http.LightblueHttpClient;
import com.redhat.lightblue.client.integration.test.pojo.Country;
import com.redhat.lightblue.client.request.data.DataFindRequest;
import com.redhat.lightblue.client.request.data.DataInsertRequest;
import com.redhat.lightblue.client.response.LightblueException;

/**
 * Testing your code against lightblue example.
 *
 * @author mpatercz
 *
 */
public class CountryDAOTest extends AbstractLightblueClientCRUDController {

    private LightblueHttpClient client;

    @Before
    public void before() throws UnknownHostException {
        client = getLightblueClient();
        cleanupMongoCollections(Country.objectType);
    }

    public CountryDAOTest() throws Exception {
        super();
    }

    @Override
    protected JsonNode[] getMetadataJsonNodes() throws Exception {
        return new JsonNode[]{loadJsonNode("metadata/country.json")};
    }

    private Country insertPL() throws LightblueException {
        Country c = new Country();
        c.setName("Poland");
        c.setIso2Code("PL");
        c.setIso3Code("POL");

        DataInsertRequest request = new DataInsertRequest(Country.objectType, Country.objectVersion);

        request.create(c);
        request.returns(includeField("*"));
        return client.data(request, Country[].class)[0];
    }

    @Test
    public void testInsertCountry() throws LightblueException {
        Assert.assertEquals("PL", insertPL().getIso2Code());
    }

    @Test
    public void testDirectMongoCleanup() throws LightblueException, UnknownHostException {
        cleanupMongoCollections(Country.objectType);
        insertPL();

        cleanupMongoCollections(Country.objectType);

        DataFindRequest request = new DataFindRequest(Country.objectType, Country.objectVersion);
        request.where(withValue("objectType = country"));
        request.select(includeField("*"));
        Country[] countries = client.data(request, Country[].class);

        Assert.assertTrue(countries.length == 0);
    }

}
