package com.redhat.lightblue.client.http.transport;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;

import com.ning.compress.lzf.LZFOutputStream;
import com.redhat.lightblue.client.LightblueClientConfiguration.Compression;
import com.redhat.lightblue.client.http.HttpMethod;
import com.redhat.lightblue.client.http.LightblueHttpClientException;
import com.redhat.lightblue.client.http.testing.doubles.FakeLightblueRequest;
import com.redhat.lightblue.client.http.transport.JavaNetHttpTransportTest.CallConnectAndReturn;
import com.redhat.lightblue.client.request.LightblueRequest;

/**
 * Testing {@link JavaNetHttpTransport}'s lzf compression support.
 *
 * @author mpatercz
 *
 */
@RunWith(JUnit4.class)
public class JavaNetHttpTransportCompressionTest {

    private HttpURLConnection mockConnection = mock(HttpURLConnection.class);
    private JavaNetHttpTransport.ConnectionFactory mockConnectionFactory = mock(JavaNetHttpTransport.ConnectionFactory.class);

    private JavaNetHttpTransport client = new JavaNetHttpTransport(mockConnectionFactory);

    @Before
    public void before() throws IOException {
        when(mockConnection.getOutputStream()).then(new CallConnectAndReturn<>(new ByteArrayOutputStream()));
        when(mockConnectionFactory.openConnection(anyString())).thenReturn(mockConnection);
    }

    @Test
    public void shouldSetAcceptEncodingHeaderToLzfByDefault() throws LightblueHttpClientException {
        LightblueRequest request = new FakeLightblueRequest("", HttpMethod.GET, "/foo/bar");

        client.executeRequest(request, "");

        Mockito.verify(mockConnection).setRequestProperty("Accept-Encoding", "lzf");
    }

    @Test
    public void shouldNotSetAcceptEncodingHeaderWhenCompressionIsDisabled() throws LightblueHttpClientException {
        LightblueRequest request = new FakeLightblueRequest("", HttpMethod.GET, "/foo/bar");

        client.setCompression(Compression.NONE);
        client.executeRequest(request, "");

        Mockito.verify(mockConnection, never()).setRequestProperty(Mockito.eq("Accept-Encoding"), Mockito.anyString());
    }

    final String TEST_RESPONSE = "Body compressed with lzf. Body compressed with lzf.";

    @Test
    public void shouldDecodeResponseWhenCompressionEnabledAndContentEncodingIsDefinedInResponse() throws LightblueHttpClientException, IOException {
        LightblueRequest request = new FakeLightblueRequest("", HttpMethod.GET, "/foo/bar");

        when(mockConnection.getHeaderField("Content-Encoding")).thenReturn("lzf");
        when(mockConnection.getContentLength()).thenReturn(TEST_RESPONSE.length());

        PipedOutputStream outResponseBody = new PipedOutputStream();
        PipedInputStream inResponseBody = new PipedInputStream(outResponseBody);

        PrintWriter p = new PrintWriter(new LZFOutputStream(outResponseBody));
        p.print(TEST_RESPONSE);
        p.close();

        when(mockConnection.getInputStream()).thenReturn(inResponseBody);

        String response = client.executeRequest(request, "");

        Assert.assertEquals(TEST_RESPONSE, response);
    }

}
