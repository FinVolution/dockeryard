package com.ppdai.dockeryard.proxy.executor;

import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import org.littleshoot.proxy.impl.ProxyUtils;

public class HealthExecutor implements RequestExecutor {

    private RequestExecutor next = new AuthorizationRequestExecutor();

    private static final String HS_URI = "/hs";

    @Override
    public HttpResponse execute(HttpRequest httpRequest) {
        String uri = httpRequest.getUri();
        if (uri.startsWith(HS_URI)) {
            return ProxyUtils.createFullHttpResponse(HttpVersion.HTTP_1_1,
                    HttpResponseStatus.OK, "OK");
        } else {
            return next.execute(httpRequest);
        }
    }
}
