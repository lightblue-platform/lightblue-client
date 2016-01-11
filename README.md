[![Coverage Status](https://coveralls.io/repos/lightblue-platform/lightblue-client/badge.png?branch=master)](https://coveralls.io/r/lightblue-platform/lightblue-client?branch=master)

# Whis is lightblue?

Cloud focused data services with dynamic querying, versioned schemas, and robust security.

* [Main Project](https://github.com/lightblue-platform/lightblue)
* [Overview](http://jewzaam.gitbooks.io/lightblue/)
* [User Guide](http://jewzaam.gitbooks.io/lightblue-user-guide/)
* [Developer Manual](http://jewzaam.gitbooks.io/lightblue-developer-manual/)

## Java Client Quickstart
The following example demonstrates how a lightblue service call can be used to retrieve data and unmarshal it to a model object.

### Prerequisites
In order to test the java client, the following prerequisites are needed (Parenthetical values are used in example below).

* Existing lightblue installation (@ `https://my.lightblue.com/rest/data`)
* Private key + password (`client-cert.pkcs12`/'s3cr3t')
* Trusted CA file for lightblue server (`ca-cert.pem`)
* lightblue find query (`https://my.lightblue.com/rest/data/foo/1.0?Q=name:bar`)
* Java model object representing the returned projection (`Foo`)

### Maven Dependendencies
Begin by importing the following maven dependencies, or their equivalent in your build system of choice.  This example uses both the core library as well as the http library since the author's lightblue service communicates via http.

        <dependency>
            <groupId>com.redhat.lightblue.client</groupId>
            <artifactId>lightblue-client-core</artifactId>
        </dependency>

        <dependency>
            <groupId>com.redhat.lightblue.client</groupId>
            <artifactId>lightblue-client-http</artifactId>
        </dependency>

### Configuration
First, set configuration objects by creating a `LightblueClientConfiguration` object:

        LightblueClientConfiguration config = new LightblueClientConfiguration();
        config.setDataServiceURI("https://my.lightblue.com/rest/data");
        config.setCertFilePath("client-cert.pkcs12");
        config.setCaFilePath("ca-cert.pem");
        config.setCertPassword("s3cr3t");
        config.setUseCertAuth(true);
        config.setCompression(LightblueClientConfiguration.Compression.NONE);

* Be sure that the certificate files wind up on the same runtime classpath as the lightblue libraries, since this is the source lightblue will use when it attempts to load the certificates.

### Client Instantiation
After a configuration has been defined, a lightblue java client can be instantiated.

        HttpTransport httpTransport = JavaNetHttpTransport.fromLightblueClientConfiguration(config);
        LightblueHttpClient client = new LightblueHttpClient(config, httpTransport);

### Performing a Query
Now simply use the lightblue client to retrieve data:

        DataFindRequest findRequest = new DataFindRequest("foo", "1.0");
        findRequest.where(Query.withValue("name=bar"));
        Foo foo = client.data(findRequest, Foo.class);

First the `findRequest` is given both the metadata model as well as the version of that model.  Also, a where clause is given to define specific parameters for the sort.  In addition to this, selects can be used to define projections, sorts for ordered collections, and ranges to limit returned results.

Now, the `client.data` call can be made in order to call Lightblue, retrieve the find results, and attempt to map the returned JSON entity to the provided Java class.

* Remember, that when attempting to retrieve related entities, be sure to include them recursively in the `DataFindRequest`

        findRequest.select(Projection.includeFieldRecursively("*"));

