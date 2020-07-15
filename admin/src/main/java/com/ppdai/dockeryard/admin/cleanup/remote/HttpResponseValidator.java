package com.ppdai.dockeryard.admin.cleanup.remote;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpResponseException;

public class HttpResponseValidator {

    public void validateResponse(HttpResponse response) throws HttpResponseException {
        int status = response.getStatusLine().getStatusCode();
        if (status < 200 || status >= 400) {
            String message = response.getStatusLine().getReasonPhrase();
            throw new HttpResponseException(status, message);
        }
    }
}