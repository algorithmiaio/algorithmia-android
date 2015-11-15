package com.algorithmia.algo;

import com.algorithmia.APIException;
import com.algorithmia.client.HttpClient;

import com.algorithmia.client.HttpContentType;
import com.algorithmia.client.HttpEntity;
import com.algorithmia.client.HttpResponseHandler;
import com.google.gson.Gson;

/**
 * Represents an Algorithmia algorithm that can be called.
 */
public final class Algorithm {
    private AlgorithmRef algoRef;
    private HttpClient client;

    final static Gson gson = new Gson();

    public Algorithm(HttpClient client, AlgorithmRef algoRef) {
        this.client = client;
        this.algoRef = algoRef;
    }

    /**
     * Calls the Algorithmia API for a given input.
     * Attempts to automatically serialize the input to JSON.
     *
     * @param input algorithm input, will automatically be converted into JSON
     * @return algorithm result (AlgoSuccess or AlgoFailure)
     * @throws APIException if there is a problem communication with the Algorithmia API.
     */
    public AlgoResponse pipe(Object input) throws APIException {
        if (input instanceof String) {
            return pipeRequest((String)input,ContentType.Text);
        } else if (input instanceof byte[]) {
            return pipeBinaryRequest((byte[])input);
        } else {
            return pipeRequest(gson.toJsonTree(input).toString(),ContentType.Json);
        }
    }


    /**
     * Calls the Algorithmia API for given input that will be treated as JSON
     *
     * @param inputJson json input value
     * @return success or failure
     * @throws APIException if there is a problem communication with the Algorithmia API.
     */
    public AlgoResponse pipeJson(String inputJson) throws APIException {
        return pipeRequest(inputJson, ContentType.Json);
    }

    private AlgoResponse pipeRequest(String input, ContentType content_type) throws APIException {
        HttpEntity.StringEntity requestEntity = null;
        if(content_type == ContentType.Text) {
            requestEntity = new HttpEntity.StringEntity(input, HttpContentType.TEXT_PLAIN);
        } else if(content_type == ContentType.Json) {
            requestEntity = new HttpEntity.StringEntity(input, HttpContentType.APPLICATION_JSON);
        }
        return client.post(
            algoRef.getUrl(),
            requestEntity,
            new HttpResponseHandler.AlgoResponseHandler()
        );
    }

    private AlgoResponse pipeBinaryRequest(byte[] input) throws APIException {
        return client.post(
                algoRef.getUrl(),
                new HttpEntity.ByteArrayEntity(input),
                new HttpResponseHandler.AlgoResponseHandler()
        );
    }

}
