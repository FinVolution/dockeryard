package com.ppdai.dockeryard.proxy.executor;


import com.ppdai.dockeryard.proxy.config.LittleShootProxyConfig;
import com.ppdai.dockeryard.proxy.predicate.PredicateHolder;
import com.ppdai.dockeryard.proxy.util.*;
import io.netty.handler.codec.http.*;
import org.littleshoot.proxy.impl.ProxyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AuthorizationRequestExecutor implements RequestExecutor {

    private static Logger logger = LoggerFactory.getLogger(AuthorizationRequestExecutor.class);

    private RequestExecutor next = new PullRequestExecutor();

    @Override
    public HttpResponse execute(HttpRequest httpRequest) {
        String shortUuid = ShortUUID.generateShortUuid();
        HttpHeaders headers = httpRequest.headers();
        DockerRegistryV2RequestType requestType = DockerRegistryV2Util.getRequestType(httpRequest);
        if (DockerRegistryV2RequestType.API_VERSION_CHECK.equals(requestType)) {
            String auth = headers.get("Authorization");
            boolean canIgnoreAuthentication = PredicateHolder.getPredicate().test(httpRequest);
            logger.info("{} AuthorizationRequestExecutor received a request :{} / {}; Authorization is {}", shortUuid, httpRequest.getMethod(), httpRequest.getUri(), auth);
            if (auth == null && !canIgnoreAuthentication) {
                String errorMsg = "Please login first by command: docker login 'repo_url'.";
                final FullHttpResponse redirectResp = ProxyUtils.createFullHttpResponse(HttpVersion.HTTP_1_1,
                        HttpResponseStatus.UNAUTHORIZED, CommonUtil.formatError("UNAUTHORIZED", errorMsg));
                HttpHeaders.setKeepAlive(redirectResp, false);
                LittleShootProxyConfig littleShootProxyConfig = ApplicationContextHelper.getApplicationContext().getBean("littleShootProxyConfig", LittleShootProxyConfig.class);
                String wwwAuthStr = String.format("Bearer realm=\"%s\",service=\"%s\",scope=\"%s\"",
                        littleShootProxyConfig.getAuthServerHost() + "/oauth2/docker/token",
                        littleShootProxyConfig.getAudience(),
                        "role");
                redirectResp.headers().add("WWW-Authenticate", wwwAuthStr);
                redirectResp.headers().add("Content-Type", "application/json; charset=utf-8");
                redirectResp.headers().add("Docker-Distribution-Api-Version", "registry/2.0");
                return redirectResp;
            } else {
                return null;
            }
        } else {
            httpRequest.headers().set("shortUuid", shortUuid);
            return next.execute(httpRequest);
        }
    }

}
