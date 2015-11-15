package com.algorithmia.client.methods;

import com.algorithmia.APIException;
import com.algorithmia.client.HttpRequest;

public class HttpHead extends HttpRequest {
    /**
     * @param url the url to connect to
     */
    public HttpHead(String url) throws APIException {
        super(url, "HEAD");
    }
}
