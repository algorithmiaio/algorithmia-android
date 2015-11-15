package com.algorithmia.client;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

public class HttpResponse {
    public final String url;
    public final int status;
    public final InputStream body;
    public final Map<String,List<String>> headers;

    public HttpResponse(String url, int status, InputStream body, Map<String,List<String>> headers) {
        this.url = url;
        this.status = status;
        this.body = body;
        this.headers = headers;
    }

    public InputStream getContent() {
        return body;
    }

    public int getStatusCode() {
        return status;
    }

    public String getFirstHeader(String header) {
        List<String> headerList = headers.get(header);
        if(headerList != null) {
            return headerList.get(0);
        } else {
            return null;
        }
    }
}
