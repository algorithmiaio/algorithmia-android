package com.algorithmia.client.methods;

import com.algorithmia.APIException;
import com.algorithmia.client.HttpEntity;
import com.algorithmia.client.HttpRequest;

import java.io.IOException;

public class HttpPut extends HttpRequest {
    /**
     * @param url the url to connect to
     */
    public HttpPut(String url) throws APIException {
        super(url, "PUT");
    }

    @Override
    public void setEntity(HttpEntity data) throws APIException {
        try {
            super.setEntity(data);
        } catch(IOException e) {
            throw new APIException(e.getMessage());
        }
    }
}
