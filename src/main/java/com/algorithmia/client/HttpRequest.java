package com.algorithmia.client;

import com.algorithmia.APIException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpRequest {
    private static final String userAgent = "algorithmia-java/1.0.4";

    protected final HttpURLConnection connection;

    private final String url;

    /**
     * @param url the url to connect to
     * @throws MalformedURLException if no protocol is specified, or an unknown protocol is found, or spec is null.
     */
    public HttpRequest(String url, String method) throws APIException {
        this.url = url;
        try {
            connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestProperty("User-Agent", userAgent);
            connection.setRequestMethod(method);
        } catch(IOException e) {
            throw new APIException(e.getMessage());
        }
    }
    public void setEntity(HttpEntity entity) throws IOException {
        connection.setDoOutput(true);
        final OutputStream requestBody = connection.getOutputStream();
        entity.writeTo(requestBody);
        requestBody.flush();
        requestBody.close();
    }

    public void addHeader(String key, String value) {
        if(value != null) {
            connection.setRequestProperty(key, value);
        }
    }
    /**
     * Executes HTTP request to the url, and processes the response using the given response handler
     * @param <T> the type returned by the response handler
     * @param responseHandler the handler to process the response
     * @return the processed response from the response handler
     * @throws IOException in case of a problem or the connection was aborted
     */
    public <T> T execute(HttpResponseHandler<T> responseHandler) throws IOException {
        final HttpResponse httpResponse = execute();
        return responseHandler.handleResponse(httpResponse);
    }
    public HttpResponse execute() throws IOException {
        try {
            // Execute HTTP request
            connection.connect();
            final int status = connection.getResponseCode();
            final InputStream body;
            // HttpURLConnection will throw an exception if you try to .getInputStream on status code >= 400
            if(200 <= status && status < 300) {
                body = connection.getInputStream();
            } else {
                body = connection.getErrorStream();
            }
            return new HttpResponse(url, status, body, connection.getHeaderFields());
        } catch(ConnectException e) {
            System.out.println("Error connecting to URL (" + url + "): " + e);
            e.printStackTrace();
            throw new APIException("Error connecting to URL (" + url + "): ", e);
        } catch(FileNotFoundException e) {
            System.out.println("Error connecting to URL (" + url + "): " + e);
            e.printStackTrace();
            throw new APIException("Error connecting to URL (" + url + "): ", e);
        } finally {
            connection.disconnect();
        }
    }
}
