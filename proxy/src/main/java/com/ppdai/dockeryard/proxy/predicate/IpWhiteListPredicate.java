package com.ppdai.dockeryard.proxy.predicate;

import com.ppdai.dockeryard.proxy.config.LittleShootProxyConfig;
import com.ppdai.dockeryard.proxy.util.ApplicationContextHelper;
import com.ppdai.dockeryard.proxy.util.IpUtil;
import io.netty.handler.codec.http.HttpRequest;
import org.springframework.util.StringUtils;

import java.util.Set;
import java.util.function.Predicate;

class IpWhiteListPredicate implements Predicate<Object> {

    @Override
    public boolean test(Object o) {
        if(o instanceof HttpRequest){
            HttpRequest httpRequest = (HttpRequest)o;
            String remoteIp = httpRequest.headers().get(IpUtil.X_FORWARDED_FOR);
            LittleShootProxyConfig littleShootProxyConfig = ApplicationContextHelper.getApplicationContext().getBean("littleShootProxyConfig",LittleShootProxyConfig.class);
            String ignoreAuthenticationIPs = littleShootProxyConfig.getIgnoreAuthenticationIPs();
            if(StringUtils.hasLength(remoteIp) && StringUtils.hasLength(ignoreAuthenticationIPs)) {
                String[] remoteIps = remoteIp.split(",");
                Set<String> ignoreAuthenticationIPSet = StringUtils.commaDelimitedListToSet(ignoreAuthenticationIPs);
                if(ignoreAuthenticationIPSet.contains(remoteIps[0])){
                    return true;
                }
            }
        }
        return false;
    }
}
