package com.ppdai.dockeryard.proxy.config;

import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class LittleShootProxyConfig implements EnvironmentAware {

    private Environment environment;

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    public String getProxyWrapperFactoryName(){
        return environment.getProperty("proxyWrapperFactoryName", "com.ppdai.dockeryard.proxy.basis.littleshoot.LittleShootProxyWrapperFactory");
    }

    public String getProxyHost() {
        return environment.getProperty("proxyHost", String.class);
    }

    /**
     * 代理真正启动时的端口
     * @return
     */
    public int getProxyPort() {
        return environment.getProperty("proxyPort", Integer.class);
    }

    public String getDockerRegistryHost(){
        return environment.getProperty("dockerRegistryHost", String.class);
    }

    public Integer getDockerRegistryPort(){
        return environment.getProperty("dockerRegistryPort", Integer.class);
    }

    public String getAuthServerHost(){
        return environment.getProperty("authServerHost", String.class);
    }

    public String getAudience() {
        return environment.getProperty("audience", String.class);
    }

    public String getIgnoreAuthenticationIPs(){ return environment.getProperty("ignoreAuthenticationIPs", String.class); }

    public String getResponseDomainName(){ return environment.getProperty("responseDomainName", String.class); }
}
