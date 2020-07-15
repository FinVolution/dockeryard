package com.ppdai.dockeryard.proxy.util;

import com.ppdai.dockeryard.proxy.constant.ProxyConstant;
import io.netty.handler.codec.http.HttpRequest;
import org.springframework.http.HttpMethod;

public final class DockerRegistryV2Util {

    private DockerRegistryV2Util() {
        throw new AssertionError();
    }

    /**
     * 计算HttpRequest的类型
     */
    public static DockerRegistryV2RequestType getRequestType(HttpRequest httpRequest){
        DockerRegistryV2RequestType requestType = DockerRegistryV2RequestType.OTHER;
        if(httpRequest != null){
            String method = httpRequest.getMethod().name().toUpperCase();
            String uri = httpRequest.getUri();
            if(uri.endsWith(ProxyConstant.V2 + ProxyConstant.SEPARATOR)){
                requestType = DockerRegistryV2RequestType.API_VERSION_CHECK;
            }else{
                if(uri.contains(ProxyConstant.MANIFESTS)){
                    switch (method) {
                        case "GET":
                            requestType = DockerRegistryV2RequestType.PULL_IMAGE_MANIFEST;
                            break;
                        case "HEAD":
                            requestType = DockerRegistryV2RequestType.CHECK_IMAGE_MANIFEST;
                            break;
                        case "PUT":
                            requestType = DockerRegistryV2RequestType.PUSH_MANIFEST;
                            break;
                        case "DELETE":
                            requestType = DockerRegistryV2RequestType.DELETE_IMAGE;
                            break;
                        default:
                            requestType = DockerRegistryV2RequestType.OTHER;
                    }
                }else if(uri.contains(ProxyConstant.BLOBS)){
                    switch (method) {
                        case "GET":
                            if (uri.contains(ProxyConstant.UPLOADS)){
                                requestType = DockerRegistryV2RequestType.GET_UPLOAD_PROCESS;
                            }else{
                                requestType = DockerRegistryV2RequestType.PULL_LAYER;
                            }
                            break;
                        case "POST":
                            requestType = DockerRegistryV2RequestType.PUSH_LAYER;
                            break;
                        case "HEAD":
                            requestType = DockerRegistryV2RequestType.CHECK_LAYER;
                            break;
                        case "PATCH":
                            requestType = DockerRegistryV2RequestType.CHUNKED_UPLOAD;
                            break;
                        case "PUT":
                            requestType = DockerRegistryV2RequestType.MONOLITHIC_UPLOAD;
                            break;
                        case "DELETE":
                            if (uri.contains(ProxyConstant.UPLOADS)){
                                requestType = DockerRegistryV2RequestType.CANCEL_UPLOAD;
                            }else{
                                requestType = DockerRegistryV2RequestType.DELETE_LAYER;
                            }
                            break;
                        default:
                            requestType = DockerRegistryV2RequestType.OTHER;
                    }
                }
            }
        }
        return requestType;
    }

}
