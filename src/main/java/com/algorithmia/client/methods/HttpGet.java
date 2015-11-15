package com.algorithmia.client.methods;

import com.algorithmia.APIException;
import com.algorithmia.client.HttpRequest;

public class HttpGet extends HttpRequest {
    /**
     * @param url the url to connect to
     */
    public HttpGet(String url) throws APIException {
        super(url, "GET");
    }
}
