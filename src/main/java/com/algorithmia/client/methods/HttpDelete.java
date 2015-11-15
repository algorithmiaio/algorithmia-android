package com.algorithmia.client.methods;

import com.algorithmia.APIException;
import com.algorithmia.client.HttpRequest;

public class HttpDelete extends HttpRequest {
    /**
     * @param url the url to connect to
     */
    public HttpDelete(String url) throws APIException {
        super(url, "DELETE");
    }
}
