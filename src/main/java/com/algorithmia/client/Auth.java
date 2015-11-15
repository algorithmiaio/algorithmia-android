package com.algorithmia.client;

/**
 * A result representing success
 */
public abstract class Auth {
    protected abstract void authenticateRequest(HttpRequest request);
}
