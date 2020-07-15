package com.ppdai.dockeryard.admin.cleanup.remote;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpResponseException;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Created by chenlang on 2020/5/14
 **/
public class HttpResponseValidatorTest extends Mockito {

    @Test(expected = RuntimeException.class)
    public void validateResponse_response_code_500() {
        HttpResponseValidator validator = new HttpResponseValidator();
        HttpResponse httpResponse = mock(HttpResponse.class);
        StatusLine statusLine = mock(StatusLine.class);
        when(statusLine.getStatusCode()).thenReturn(500);
        when(httpResponse.getStatusLine()).thenReturn(statusLine);
        try {
            validator.validateResponse(httpResponse);
        } catch (HttpResponseException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void validateResponse_response_code_200() throws HttpResponseException {
        HttpResponseValidator validator = new HttpResponseValidator();
        HttpResponse httpResponse = mock(HttpResponse.class);
        StatusLine statusLine = mock(StatusLine.class);
        when(statusLine.getStatusCode()).thenReturn(200);
        when(httpResponse.getStatusLine()).thenReturn(statusLine);
        validator.validateResponse(httpResponse);
    }
}
