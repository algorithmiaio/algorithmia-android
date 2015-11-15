package com.algorithmia.client;

import com.algorithmia.APIException;
import com.algorithmia.AlgorithmException;
import com.algorithmia.algo.AlgoFailure;
import com.algorithmia.algo.AlgoResponse;
import com.algorithmia.algo.AlgoSuccess;
import com.algorithmia.algo.Metadata;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.InputStream;
import java.io.InputStreamReader;

// TODO: Async
//  - Stream input json
//  - Stream output json

/**
 * Various HTTP actions, using our HttpClient class, and automatically adding authorization
 */
public class HttpClientHelpers {
    private HttpClientHelpers() {}  // non-instantiable

    public static void throwIfNotOk(HttpResponse response) throws APIException {
        final int status = response.getStatusCode();
        if(200 > status || status > 300) {
            throw APIException.fromHttpResponse(response);
        }
    }

    public static AlgoResponse jsonToAlgoResponse(JsonElement json) throws APIException {
        if(json != null && json.isJsonObject()) {
            final JsonObject obj = json.getAsJsonObject();
            if(obj.has("error")) {
                final JsonObject error = obj.getAsJsonObject("error");
                final String msg = error.get("message").getAsString();
                final String stacktrace = error.get("stacktrace").getAsString();
                return new AlgoFailure(new AlgorithmException(msg, null, stacktrace));
            } else {
                JsonObject metaJson = obj.getAsJsonObject("metadata");
                Double duration = metaJson.get("duration").getAsDouble();
                com.algorithmia.algo.ContentType contentType = com.algorithmia.algo.ContentType.fromString(metaJson.get("content_type").getAsString());
                JsonElement stdoutJson = metaJson.get("stdout");
                String stdout = (stdoutJson == null) ? null : stdoutJson.getAsString();
                Metadata meta = new Metadata(contentType, duration, stdout);
                return new AlgoSuccess(obj.get("result"), meta);
            }
        } else {
            throw new APIException("Unexpected API response: " + json);
        }
    }

    final static JsonParser parser = new JsonParser();
    public static JsonElement parseResponseJson(HttpResponse response) throws APIException {
        throwIfNotOk(response);

        final InputStream is = response.getContent();
        JsonElement json = parser.parse(new InputStreamReader(is));
        return json;
    }

}
