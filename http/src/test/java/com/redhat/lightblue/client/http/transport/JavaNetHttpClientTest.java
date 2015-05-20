package com.redhat.lightblue.client.http.transport;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.isNull;
import static org.mockito.Matchers.startsWith;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.redhat.lightblue.client.http.HttpMethod;
import com.redhat.lightblue.client.http.testing.doubles.FakeLightblueRequest;
import com.redhat.lightblue.client.request.LightblueRequest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.InOrder;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import java.net.HttpURLConnection;

@RunWith(JUnit4.class)
public class JavaNetHttpClientTest {
    private HttpURLConnection mockConnection = mock(HttpURLConnection.class);
    private HttpsURLConnection mockSslConnection = mock(HttpsURLConnection.class);
    private JavaNetHttpClient.ConnectionFactory mockConnectionFactory =
            mock(JavaNetHttpClient.ConnectionFactory.class);

    private JavaNetHttpClient client = new JavaNetHttpClient(mockConnectionFactory);

    private ByteArrayOutputStream requestStream = new ByteArrayOutputStream();
    private PrintStream responseStream;
    private PrintStream errorResponseStream;

    @Before
    public void stubMockConnectionsWithInAndOutStreams() throws IOException {
        PipedInputStream actualResponseStream = new PipedInputStream();
        responseStream = new PrintStream(new PipedOutputStream(actualResponseStream));

        PipedInputStream actualErrorStream = new PipedInputStream();
        errorResponseStream = new PrintStream(new PipedOutputStream(actualErrorStream));

        when(mockConnection.getOutputStream()).then(new CallConnectAndReturn<>(requestStream));
        when(mockConnection.getInputStream()).then(new CallConnectAndReturn<>(actualResponseStream));
        when(mockConnection.getErrorStream()).then(new CallConnectAndReturn<>(actualErrorStream));

        when(mockSslConnection.getOutputStream()).then(new CallConnectAndReturn<>(requestStream));
        when(mockSslConnection.getInputStream()).then(new CallConnectAndReturn<>(actualResponseStream));
        when(mockSslConnection.getErrorStream()).then(new CallConnectAndReturn<>(actualErrorStream));
    }

    @Before
    public void stubMockConnectionFactory() throws IOException {
        when(mockConnectionFactory.openConnection(anyString())).thenReturn(mockConnection);
        when(mockConnectionFactory.openConnection(startsWith("https"))).thenReturn(mockSslConnection);
    }

    @Test(timeout = 500)
    public void shouldConnectToUrlOfRequestUsingBaseUri() throws IOException {
        LightblueRequest getFooBar = new FakeLightblueRequest("", HttpMethod.GET, "/foo/bar");

        client.executeRequest(getFooBar, "http://theblueislight.com");

        verify(mockConnectionFactory).openConnection("http://theblueislight.com/foo/bar");
        verify(mockConnection).connect();
    }

    @Test(timeout = 500)
    public void shouldSetHttpMethod() throws IOException {
        LightblueRequest deleteRequest = new FakeLightblueRequest("", HttpMethod.DELETE, "");
        LightblueRequest getRequest = new FakeLightblueRequest("", HttpMethod.GET, "");

        client.executeRequest(deleteRequest, "");
        client.executeRequest(getRequest, "");

        verify(mockConnection).setRequestMethod("DELETE");
        verify(mockConnection).setRequestMethod("GET");
    }

    @Test(timeout = 500)
    public void shouldSetHttpMethodBeforeSendingRequestWithBody() throws IOException {
        LightblueRequest requestWithBody = new FakeLightblueRequest("Yup", HttpMethod.POST, "");

        client.executeRequest(requestWithBody, "");

        InOrder inOrder = inOrder(mockConnection);
        inOrder.verify(mockConnection).setRequestMethod(anyString());
        inOrder.verify(mockConnection).connect();
    }

    @Test(timeout = 500)
    public void shouldSendBodyUsingUtf8IfNotEmpty() throws IOException {
        LightblueRequest konnichiWa = new FakeLightblueRequest("こんにちは", HttpMethod.POST, "");

        client.executeRequest(konnichiWa, "");

        verify(mockConnection).setDoOutput(true);
        assertThat(requestStream.toString("UTF-8"), is("こんにちは"));
    }

    @Test(timeout = 500)
    public void shouldNotSendEmptyBody() throws IOException {
        LightblueRequest noBody = new FakeLightblueRequest("", HttpMethod.GET, "");

        client.executeRequest(noBody, "");

        verify(mockConnection, never()).setDoOutput(true);
        verify(mockConnection, never()).getOutputStream();
    }

    @Test(timeout = 500)
    public void shouldUseSslSocketFactoryProvidedIfHttpsConnection() throws IOException {
        SSLSocketFactory mockSslSocketFactory = mock(SSLSocketFactory.class);
        JavaNetHttpClient client = new JavaNetHttpClient(mockConnectionFactory, mockSslSocketFactory);

        LightblueRequest getFooBar = new FakeLightblueRequest("", HttpMethod.GET, "/foo/bar");

        client.executeRequest(getFooBar, "https://much_secure.so_ssl");

        InOrder inOrder = inOrder(mockSslConnection);
        inOrder.verify(mockSslConnection).setSSLSocketFactory(mockSslSocketFactory);
        inOrder.verify(mockSslConnection).connect();
    }

    @Test(timeout = 500)
    public void shouldNotUseNullSslSocketFactory_shouldFallBackToDefault() throws IOException {
        JavaNetHttpClient client = new JavaNetHttpClient(mockConnectionFactory, null);

        LightblueRequest getFooBar = new FakeLightblueRequest("", HttpMethod.GET, "/foo/bar");

        client.executeRequest(getFooBar, "https://much_secure.so_ssl");

        verify(mockSslConnection, never()).setSSLSocketFactory(isNull(SSLSocketFactory.class));
    }

    @Test(timeout = 500)
    public void shouldReturnSuccessResponseDecodedWithUtf8WhenContentLengthIsKnown() throws IOException {
        LightblueRequest helloInJapanese = new FakeLightblueRequest("", HttpMethod.GET, "/hello/japanese");

        String konnichiWa = "こんにちは";

        responseStream.print(konnichiWa);
        when(mockConnection.getContentLength()).thenReturn(konnichiWa.getBytes("UTF-8").length);

        assertThat(client.executeRequest(helloInJapanese, ""), is(konnichiWa));
    }

    @Test
    public void shouldReturnSuccessResponseDecodedWithUtf8WhenContentLengthIsNotKnown() throws IOException {
        LightblueRequest helloInJapanese = new FakeLightblueRequest("", HttpMethod.GET, "/hello/japanese");

        String konnichiWa = "こんにちは";

        responseStream.print(konnichiWa);
        responseStream.close();
        when(mockConnection.getContentLength()).thenReturn(-1);

        assertThat(client.executeRequest(helloInJapanese, ""), is(konnichiWa));
    }

    @Test(timeout = 500)
    public void shouldReturnEmptySuccessResponse() throws IOException {
        LightblueRequest getFooBar = new FakeLightblueRequest("", HttpMethod.GET, "/foo/bar");

        when(mockConnection.getContentLength()).thenReturn(0);

        assertThat(client.executeRequest(getFooBar, ""), is(""));
    }

    @Test(timeout = 500)
    public void shouldReturnErrorResponseUsingUtf8WhenContentLengthIsKnown() throws IOException {
        LightblueRequest badHelloRequest = new FakeLightblueRequest("", HttpMethod.GET, "/hello/%E0%B2%A0_%E0%B2%A0");

        String error = "ಠ_ಠ is not a language";

        doThrow(new IOException()).when(mockConnection).getInputStream();
        errorResponseStream.print(error);
        when(mockConnection.getContentLength()).thenReturn(error.getBytes("UTF-8").length);

        assertThat(client.executeRequest(badHelloRequest, ""), is(error));
    }

    @Test
    public void shouldReturnErrorResponseUsingUtf8WhenContentLengthIsNotKnown() throws IOException {
        LightblueRequest badHelloRequest = new FakeLightblueRequest("", HttpMethod.GET, "/hello/%E0%B2%A0_%E0%B2%A0");

        String error = "ಠ_ಠ is not a language";

        doThrow(new IOException()).when(mockConnection).getInputStream();
        errorResponseStream.print(error);
        errorResponseStream.close();
        when(mockConnection.getContentLength()).thenReturn(-1);

        assertThat(client.executeRequest(badHelloRequest, ""), is(error));
    }

    @Test
    public void shouldReturnEmptyErrorResponse() throws IOException {
        LightblueRequest badHelloRequest = new FakeLightblueRequest("", HttpMethod.GET, "/hello/%E0%B2%A0_%E0%B2%A0");

        doThrow(new IOException()).when(mockConnection).getInputStream();
        when(mockConnection.getContentLength()).thenReturn(0);

        assertThat(client.executeRequest(badHelloRequest, ""), is(""));
    }

    @Test
    public void shouldSetContentLengthHeaderBasedOnUtf8BytesInRequest() throws IOException {
        LightblueRequest newHello = new FakeLightblueRequest("ಠ_ಠ", HttpMethod.POST, "/hello/facelang");

        client.executeRequest(newHello, "");

        verify(mockConnection).setRequestProperty("Content-Length", Integer.toString("ಠ_ಠ".getBytes("UTF-8").length));
    }

    @Test
    public void shouldSetContentTypeToApplicationJsonWithUtf8CharsetWhenMakingRequestWithBody() throws IOException {
        LightblueRequest newHello = new FakeLightblueRequest("ಠ_ಠ", HttpMethod.POST, "/hello/facelang");

        client.executeRequest(newHello, "");

        verify(mockConnection).setRequestProperty("Content-Type", "application/json; charset=utf-8");
    }

    @Test
    public void shouldSetAcceptHeaderToJsonBeforeMakingRequest() throws IOException {
        LightblueRequest request = new FakeLightblueRequest("", HttpMethod.GET, "/foo/bar");

        client.executeRequest(request, "");

        InOrder inOrder = inOrder(mockConnection);
        inOrder.verify(mockConnection).setRequestProperty("Accept", "application/json");
        inOrder.verify(mockConnection).connect();
    }

    @Test
    public void shouldSetAcceptCharsetHeaderToUtf8BeforeMakingRequest() throws IOException {
        LightblueRequest request = new FakeLightblueRequest("", HttpMethod.GET, "/foo/bar");

        client.executeRequest(request, "");

        InOrder inOrder = inOrder(mockConnection);
        inOrder.verify(mockConnection).setRequestProperty("Accept-Charset", "utf-8");
        inOrder.verify(mockConnection).connect();
    }

    static class CallConnectAndReturn<T> implements Answer<T> {
        private final T returnValue;

        CallConnectAndReturn(T returnValue) {
            this.returnValue = returnValue;
        }

        @Override
        public T answer(InvocationOnMock invocation) throws Throwable {
            ((HttpURLConnection) invocation.getMock()).connect();

            return returnValue;
        }
    }

    static class FakeConnectionFactory implements JavaNetHttpClient.ConnectionFactory {
        private final HttpURLConnection connection;

        FakeConnectionFactory(HttpURLConnection connection) {
            this.connection = connection;
        }

        @Override
        public HttpURLConnection openConnection(String uri) throws IOException {
            return connection;
        }
    }
}
