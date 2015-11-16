package com.algorithmia.client;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public abstract class HttpEntity {

    protected HttpContentType contentType = null;
    public HttpContentType getContentType() {
        return contentType;
    }

    public abstract void writeTo(OutputStream out) throws IOException;

    public static class StringEntity extends HttpEntity {
        private String entity;
        public StringEntity(String entity, HttpContentType contentType) {
            this.entity = entity;
            this.contentType = contentType;
        }
        public void writeTo(OutputStream out) throws IOException {
            out.write(entity.getBytes());
        }
    }

    public static class ByteArrayEntity extends HttpEntity {
        private byte[] entity;
        public ByteArrayEntity(byte[] entity) {
            this.entity = entity;
            this.contentType = HttpContentType.APPLICATION_OCTET_STREAM;
        }
        public ByteArrayEntity(byte[] entity, HttpContentType contentType) {
            this.entity = entity;
            this.contentType = contentType;
        }
        public void writeTo(OutputStream out) throws IOException {
            out.write(entity);
        }
    }

    public static class InputStreamEntity extends HttpEntity {
        private InputStream entity;
        public InputStreamEntity(InputStream entity) {
            this.entity = entity;
            this.contentType = HttpContentType.APPLICATION_OCTET_STREAM;
        }
        public InputStreamEntity(InputStream entity, HttpContentType contentType) {
            this.entity = entity;
            this.contentType = contentType;
        }
        public void writeTo(OutputStream out) throws IOException {
            IOUtils.copy(entity, out);
        }
    }

}
