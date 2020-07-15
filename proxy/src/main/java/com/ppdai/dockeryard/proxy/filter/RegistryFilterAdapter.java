package com.ppdai.dockeryard.proxy.filter;

import com.ppdai.dockeryard.proxy.config.LittleShootProxyConfig;
import com.ppdai.dockeryard.proxy.constant.ProxyConstant;
import com.ppdai.dockeryard.proxy.executor.HealthExecutor;
import com.ppdai.dockeryard.proxy.executor.RequestExecutor;
import com.ppdai.dockeryard.proxy.util.ApplicationContextHelper;
import com.ppdai.dockeryard.proxy.util.IpUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import org.littleshoot.proxy.HttpFiltersAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.net.URI;
import java.net.URISyntaxException;

public class RegistryFilterAdapter extends HttpFiltersAdapter {

    private static Logger logger = LoggerFactory.getLogger(RegistryFilterAdapter.class);

    private static final String HEADER_LOCATION = "Location";

    private RequestExecutor executor = new HealthExecutor();

    public RegistryFilterAdapter(HttpRequest originalRequest, ChannelHandlerContext ctx) {
        super(originalRequest, ctx);
    }

    @Override
    public HttpResponse clientToProxyRequest(HttpObject httpObject) {
        HttpResponse response = null;
        if (httpObject instanceof HttpRequest) {
            //
            try {
                HttpRequest httpRequest = (HttpRequest) httpObject;
                response = executor.execute(httpRequest);
                if (response == null) {
                    LittleShootProxyConfig littleShootProxyConfig = ApplicationContextHelper.getApplicationContext().getBean("littleShootProxyConfig", LittleShootProxyConfig.class);
                    httpRequest.headers().remove("Host");
                    httpRequest.headers().add("Host", littleShootProxyConfig.getDockerRegistryHost() + ":" + littleShootProxyConfig.getDockerRegistryPort());
                }
            } catch (Exception ex) {
                logger.error("update http headers failed", ex);
            }
        }
        return response;
    }

    @Override
    public HttpResponse proxyToServerRequest(HttpObject httpObject) {
        return super.proxyToServerRequest(httpObject);
    }

    @Override
    public HttpObject serverToProxyResponse(HttpObject httpObject) {

        if (httpObject instanceof HttpResponse) {
            final HttpResponse httpResponse = (HttpResponse) httpObject;
            HttpHeaders headers = httpResponse.headers();

            // 如果http返回头里包含Location,表示实际上传镜像的url,需要替换成代理服务器的地址
            if (headers.get(ProxyConstant.DOCKER_DISTRIBUTION_API_VERSION) != null) {
                String location = headers.get(HEADER_LOCATION);
                logger.info("serverToProxyResponse location {} ", location);
                if (location != null) {
                    LittleShootProxyConfig littleShootProxyConfig = ApplicationContextHelper.getApplicationContext().getBean("littleShootProxyConfig", LittleShootProxyConfig.class);
                    try {
                        URI uri = new URI(location);
                        if (StringUtils.hasLength(littleShootProxyConfig.getResponseDomainName())) {
                            location = location.replace(uri.getHost() + ":" + uri.getPort(),
                                    littleShootProxyConfig.getResponseDomainName());
                        } else {
                            location = location.replace(uri.getHost() + ":" + uri.getPort(),
                                    IpUtil.getIpOnEth0() + ":" + littleShootProxyConfig.getProxyPort());
                        }
                        logger.info("serverToProxyResponse converted location {} ", location);
                    } catch (URISyntaxException e) {
                        logger.error("serverToProxyResponse converted location error", e);
                    }

                    headers.remove(HEADER_LOCATION);
                    headers.set(HEADER_LOCATION, location);
                }
            }

        }

        return httpObject;
    }

    @Override
    public HttpObject proxyToClientResponse(HttpObject httpObject) {
        return super.proxyToClientResponse(httpObject);
    }

}
