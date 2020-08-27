package com.ppdai.dockeryard.proxy.executor;

import com.ppdai.dockeryard.proxy.config.LittleShootProxyConfig;
import com.ppdai.dockeryard.proxy.dao.UltraDao;
import com.ppdai.dockeryard.proxy.util.ApplicationContextHelper;
import com.ppdai.dockeryard.proxy.util.CommonUtil;
import com.ppdai.dockeryard.proxy.util.DockerRegistryV2RequestType;
import com.ppdai.dockeryard.proxy.util.DockerRegistryV2Util;
//import com.ppdai.pauth.client.api.OAuth2EndpointApi;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import org.littleshoot.proxy.impl.ProxyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PullRequestExecutor implements RequestExecutor {

    private static Logger logger = LoggerFactory.getLogger(PullRequestExecutor.class);

    private RequestExecutor next = new PushRequestExecutor();

//    private OAuth2EndpointApi authApi;

    PullRequestExecutor() {
        LittleShootProxyConfig littleShootProxyConfig = ApplicationContextHelper.getApplicationContext().getBean("littleShootProxyConfig", LittleShootProxyConfig.class);
//        authApi = new OAuth2EndpointApi();
//        authApi.getApiClient().setBasePath(littleShootProxyConfig.getAuthServerHost());
    }

    @Override
    public HttpResponse execute(HttpRequest httpRequest) {
        DockerRegistryV2RequestType requestType = DockerRegistryV2Util.getRequestType(httpRequest);
        if (requestType == DockerRegistryV2RequestType.PULL_IMAGE_MANIFEST || requestType == DockerRegistryV2RequestType.PULL_LAYER) {
            //在Pull的时候，PULL_IMAGE_MANIFEST是docker client第一个先发出的请求，只许对这个请求做校验即可。
            if (requestType == DockerRegistryV2RequestType.PULL_IMAGE_MANIFEST) {
                try {
                    String imageUrl = httpRequest.getUri();
                    UltraDao ultraDao = ApplicationContextHelper.getApplicationContext().getBean("ultraDao", UltraDao.class);
                    //验证指定镜像是否可以被拉取。
                    ultraDao.validateImage(imageUrl);
                    //正常透传
                    return null;
                } catch (Exception ex) {
                    logger.error(ex.getMessage(), ex);
                    return ProxyUtils.createFullHttpResponse(HttpVersion.HTTP_1_1,
                            HttpResponseStatus.FORBIDDEN, CommonUtil.formatError("UNAUTHORIZED", ex.getMessage()));
                }
            } else {
                //PULL_LAYER的请求直接透传。
                return null;
            }
        } else {
            return next.execute(httpRequest);
        }
    }
}
