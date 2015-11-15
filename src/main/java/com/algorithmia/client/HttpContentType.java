package com.algorithmia.client;

public class HttpContentType {
    public static final HttpContentType TEXT_PLAIN = new HttpContentType("text/plain");
    public static final HttpContentType APPLICATION_JSON = new HttpContentType("application/json");
    public static final HttpContentType APPLICATION_OCTET_STREAM = new HttpContentType("application/octet-stream");

    private String contentType;
    public HttpContentType(String contentType) {
        this.contentType = contentType;
    }
    @Override
    public String toString() {
        return contentType;
    }
}
