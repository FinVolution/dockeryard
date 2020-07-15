package com.ppdai.dockeryard.proxy.executor;

import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;

public interface RequestExecutor {

    /**
     * 处理docker daemon发来的httpRequest
     *
     * @param httpRequest
     * @return
     */
    HttpResponse execute(HttpRequest httpRequest);

}
