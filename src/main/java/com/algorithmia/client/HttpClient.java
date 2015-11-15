package com.algorithmia.client;

import com.algorithmia.APIException;
import com.algorithmia.TypeToken;
import com.algorithmia.client.methods.HttpDelete;
import com.algorithmia.client.methods.HttpGet;
import com.algorithmia.client.methods.HttpPost;
import com.algorithmia.client.methods.HttpPut;
import com.algorithmia.client.methods.HttpHead;
import com.algorithmia.client.HttpResponseHandler.JsonDeserializeResponseHandler;

import java.io.IOException;

// TODO: Async
//  - Stream input
//  - Stream output

/**
 * A minimal HTTP client with no dependencies.
 * Modelled loosely on Apache HttpClient
 */
public class HttpClient {

    final private Auth auth;

    public HttpClient(Auth auth) {
        this.auth = auth;
    }

    /*
     * GET requests
     */

    public HttpResponse get(String url) throws APIException {
        final HttpGet request = new HttpGet(url);
        return execute(request);
    }

    public <T> T get(String url, TypeToken<T> typeToken) throws APIException {
        final HttpGet request = new HttpGet(url);
        HttpResponseHandler<T> consumer = new JsonDeserializeResponseHandler<>(typeToken);
        return execute(request, consumer);
    }

    /*
     * POST requests
     */

    public HttpResponse post(String url, HttpEntity data) throws APIException {
        final HttpPost request = new HttpPost(url);
        request.setEntity(data);
        return execute(request);
    }
    public <T> T post(String url, HttpEntity data, HttpResponseHandler<T> consumer) throws APIException {
        final HttpPost request = new HttpPost(url);
        request.setEntity(data);
        return execute(request, consumer);
    }


    /*
     * PUT requests
     */

    public HttpResponse put(String url, HttpEntity data) throws APIException {
        final HttpPut request = new HttpPut(url);
        request.setEntity(data);
        return execute(request);
    }

    /*
     * DELETE requests
     */

    public HttpResponse delete(String url) throws APIException {
        final HttpDelete request = new HttpDelete(url);
        return execute(request);
    }

    /*
     * HEAD requests
     */

    public HttpResponse head(String url) throws APIException {
        final HttpHead request = new HttpHead(url);
        return execute(request);
    }

    /**        
     * execute methods to execute a request        
     */        
    private HttpResponse execute(HttpRequest request) throws APIException {     
        try {
            return request.execute();
        } catch(IOException e) {
            throw new APIException(e.getMessage());
        }
    }
    private <T> T execute(HttpRequest request, HttpResponseHandler<T> consumer) throws APIException {     
        try {
            return request.execute(consumer);
        } catch(IOException e) {
            throw new APIException(e.getMessage());
        }
    }

}
