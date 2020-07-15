package com.ppdai.dockeryard.proxy;

import com.ppdai.dockeryard.proxy.basis.ProxyWrapper;
import com.ppdai.dockeryard.proxy.basis.ProxyWrapperFactory;
import com.ppdai.dockeryard.proxy.config.LittleShootProxyConfig;
import com.ppdai.dockeryard.proxy.util.ApplicationContextHelper;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"com.ppdai.dockeryard"})
@MapperScan("com.ppdai.dockeryard.core.mapper")
public class ProxyApplication {
    private static Logger logger = LoggerFactory.getLogger(ProxyApplication.class);

    public static void main(String[] args) {

        SpringApplication.run(ProxyApplication.class, args);
        LittleShootProxyConfig littleShootProxyConfig = ApplicationContextHelper.getApplicationContext().getBean("littleShootProxyConfig",LittleShootProxyConfig.class);
        String factoryName = littleShootProxyConfig.getProxyWrapperFactoryName();
        try {
            ProxyWrapperFactory proxyWrapperFactory = (ProxyWrapperFactory)Class.forName(factoryName).newInstance();
            ProxyWrapper proxyWrapper = proxyWrapperFactory.createProxyWrapper();
            proxyWrapper.run();
        } catch (Exception e) {
            logger.error("proxyWrapper run error",e);
        }
    }

}
