package com.algorithmia.client.methods;

import com.algorithmia.APIException;
import com.algorithmia.client.HttpEntity;
import com.algorithmia.client.HttpRequest;

public class HttpPost extends HttpRequest {
    /**
     * @param url the url to connect to
     */
    public HttpPost(String url) throws APIException {
        super(url, "POST");
    }

    public void setEntity(HttpEntity data) {

    }
}
