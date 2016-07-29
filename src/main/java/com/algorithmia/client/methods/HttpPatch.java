package com.algorithmia.client.methods;

import com.algorithmia.APIException;
import com.algorithmia.client.HttpEntity;
import com.algorithmia.client.HttpRequest;

import java.io.IOException;

public class HttpPatch extends HttpRequest {
    /**
     * @param url the url to connect to
     */
    public HttpPatch(String url) throws APIException {
        super(url, "POST");
        this.addHeader("X-HTTP-Method-Override", "PATCH");
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
