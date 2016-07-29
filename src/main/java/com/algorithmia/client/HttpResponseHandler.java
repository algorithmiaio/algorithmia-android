package com.algorithmia.client;

import com.algorithmia.APIException;
import com.algorithmia.TypeToken;
import com.algorithmia.algo.AlgoResponse;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.lang.reflect.Type;

import static com.algorithmia.algo.Algorithm.AlgorithmOutputType;

public abstract class HttpResponseHandler<T> {

    private static final Gson gson = new Gson();

    protected abstract T handleResponse(HttpResponse response) throws APIException;

    public static class AlgoResponseHandler extends HttpResponseHandler<AlgoResponse> {
        private AlgorithmOutputType outputType;
        public AlgoResponseHandler(AlgorithmOutputType outputType) {
            this.outputType = outputType;
        }

        @Override
        protected AlgoResponse handleResponse(HttpResponse response) throws APIException {
            if(outputType.equals(AlgorithmOutputType.RAW)) {
                return HttpClientHelpers.parseRawOutput(response);
            } else {
                final JsonElement json = HttpClientHelpers.parseResponseJson(response);
                return HttpClientHelpers.jsonToAlgoResponse(json);
            }
        }
    }

    public static class JsonDeserializeResponseHandler<S> extends HttpResponseHandler<S> {
        private final Type typeToken;
        public JsonDeserializeResponseHandler(TypeToken<S> typeToken) {
            this.typeToken = typeToken.getType();
        }
        @Override
        protected S handleResponse(HttpResponse response) throws APIException {
            final JsonElement json = HttpClientHelpers.parseResponseJson(response);
            return gson.fromJson(json, typeToken);
        }
    }
}
