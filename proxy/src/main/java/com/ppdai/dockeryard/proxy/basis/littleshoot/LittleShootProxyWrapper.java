package com.ppdai.dockeryard.proxy.basis.littleshoot;

import com.ppdai.dockeryard.proxy.basis.ProxyWrapper;
import com.ppdai.dockeryard.proxy.config.LittleShootProxyConfig;
import com.ppdai.dockeryard.proxy.filter.RegistryFilterAdapter;
import com.ppdai.dockeryard.proxy.util.ApplicationContextHelper;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpRequest;
import org.littleshoot.proxy.HttpFilters;
import org.littleshoot.proxy.HttpFiltersSourceAdapter;
import org.littleshoot.proxy.impl.DefaultHttpProxyServer;

public class LittleShootProxyWrapper implements ProxyWrapper {

    @Override
    public void run() {
        LittleShootProxyConfig littleShootProxyConfig = ApplicationContextHelper.getApplicationContext().getBean("littleShootProxyConfig",LittleShootProxyConfig.class);
        DefaultHttpProxyServer.bootstrap()
                .withPort(littleShootProxyConfig.getProxyPort())
                .withAllowRequestToOriginServer(true)
                .withAllowLocalOnly(false)
                .withFiltersSource(new HttpFiltersSourceAdapter() {
                    @Override
                    public HttpFilters filterRequest(HttpRequest originalRequest, ChannelHandlerContext ctx) {
                        return new RegistryFilterAdapter(originalRequest, ctx);
                    }
                })
                .start();
    }

}
