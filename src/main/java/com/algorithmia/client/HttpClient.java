package com.algorithmia.client;

import com.algorithmia.APIException;
import com.algorithmia.TypeToken;
import com.algorithmia.client.methods.HttpDelete;
import com.algorithmia.client.methods.HttpGet;
import com.algorithmia.client.methods.HttpPatch;
import com.algorithmia.client.methods.HttpPost;
import com.algorithmia.client.methods.HttpPut;
import com.algorithmia.client.methods.HttpHead;
import com.algorithmia.client.HttpResponseHandler.JsonDeserializeResponseHandler;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

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

    /**
     * Modifies a url to add on any query parameters
     */
    private String addQueryParameters(String baseUrl, Map<String,String> params) {
        String query = "";
        if (params != null && params.size() > 0) {
            int count = 0;
            for(Map.Entry<String,String> param : params.entrySet()) {
                if(count == 0) {
                    query += "?";
                } else {
                    query += "&";
                }
                String key = param.getKey();
                String value = param.getValue();
                try {
                    key = URLEncoder.encode(key, "UTF-8");
                    value = URLEncoder.encode(value, "UTF-8");
                } catch(UnsupportedEncodingException e) {}
                query += key + "=" + value;
                count++;
            }
        }
        return baseUrl + query;
    }
    /*
     * GET requests
     */

    public HttpResponse get(String url) throws APIException {
        final HttpGet request = new HttpGet(url);
        return execute(request);
    }

    public <T> T get(String url, TypeToken<T> typeToken, Map<String,String> params) throws APIException {
        final HttpGet request = new HttpGet(addQueryParameters(url,params));
        HttpResponseHandler<T> consumer = new JsonDeserializeResponseHandler<T>(typeToken);
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
        return post(url, data, consumer, null);
    }
    public <T> T post(String url, HttpEntity data, HttpResponseHandler<T> consumer, Map<String,String> parameters) throws APIException {
        url = addQueryParameters(url, parameters);
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
     * PATCH requests
     */

    public HttpResponse patch(String url, HttpEntity data) throws APIException {
        final HttpPatch request = new HttpPatch(url);
        request.setEntity(data);
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
        if(this.auth != null) {
            this.auth.authenticateRequest(request);
        }
        try {
            return request.execute();
        } catch(APIException e) {
            throw e;
        } catch(IOException e) {
            throw new APIException(e.getMessage());
        }
    }
    private <T> T execute(HttpRequest request, HttpResponseHandler<T> consumer) throws APIException {     
        if(this.auth != null) {
            this.auth.authenticateRequest(request);
        }
        try {
            return request.execute(consumer);
        } catch(APIException e) {
            throw e;
        } catch(IOException e) {
            throw new APIException(e.getMessage());
        }
    }

}
