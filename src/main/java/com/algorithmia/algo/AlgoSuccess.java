package com.algorithmia.algo;

import com.algorithmia.AlgorithmException;
import com.algorithmia.TypeToken;
import com.algorithmia.client.Base64;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

import java.lang.UnsupportedOperationException;
import java.lang.reflect.Type;

/**
 * A result representing success
 */
public final class AlgoSuccess extends AlgoResponse {

    private transient JsonElement result;
    private Metadata metadata;
    private String resultJson;
    private String rawOutput;

    private transient final Gson gson = new Gson();
    private transient final Type byteType = new TypeToken<byte[]>(){}.getType();

    public AlgoSuccess(JsonElement result, Metadata metadata, String rawOutput) {
        this.result = result;
        this.metadata = metadata;
        if (result != null) {
            this.resultJson = result.toString();
        } else {
            this.resultJson = null;
        }
        this.rawOutput = rawOutput;
    }

    @Override
    public boolean isSuccess() {
        return true;
    }

    @Override
    public boolean isFailure() {
        return false;
    }

    @Override
    public Metadata getMetadata() {
        return metadata;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected <T> T as(Class<T> returnClass) {
        if(metadata.getContentType() == ContentType.Void) {
            return null;
        } else if(metadata.getContentType() == ContentType.Text) {
            return gson.fromJson(new JsonPrimitive(result.getAsString()), returnClass);
        } else if(metadata.getContentType() == ContentType.Json) {
            return gson.fromJson(result, returnClass);
        } else if(metadata.getContentType() == ContentType.Binary) {
            if(byte[].class == returnClass) {
              return (T)Base64.decodeBase64(result.getAsString());
            } else {
              throw new UnsupportedOperationException("Only support returning as byte[] for Binary data");
            }

        } else {
            throw new UnsupportedOperationException("Unknown ContentType in response: " + metadata.getContentType().toString());
        }

    }

    @Override
    @SuppressWarnings("unchecked")
    protected <T> T as(Type returnType) {
        if(metadata.getContentType() == ContentType.Void) {
            return null;
        } else if(metadata.getContentType() == ContentType.Text) {
            return gson.fromJson(new JsonPrimitive(result.getAsString()), returnType);
        } else if(metadata.getContentType() == ContentType.Json) {
            return gson.fromJson(result, returnType);
        } else if(metadata.getContentType() == ContentType.Binary) {
            if(byteType.equals(returnType)) {
              return (T)Base64.decodeBase64(result.getAsString());
            } else {
              throw new UnsupportedOperationException("Only support returning as byte[] for Binary data");
            }
        } else {
            throw new UnsupportedOperationException("Unknown ContentType in response: " + metadata.getContentType().toString());
        }
    }

    @Override
    public String asJsonString() {
        return result.toString();
    }

    @Override
    public String asString() {
        return as(String.class);
    }

    @Override
    public String getRawOutput() throws AlgorithmException {
        return rawOutput;
    }

}
