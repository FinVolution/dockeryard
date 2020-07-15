package com.ppdai.dockeryard.proxy.basis.littleshoot;

import com.ppdai.dockeryard.proxy.basis.ProxyWrapper;
import com.ppdai.dockeryard.proxy.basis.ProxyWrapperFactory;

public class LittleShootProxyWrapperFactory implements ProxyWrapperFactory {

    @Override
    public ProxyWrapper createProxyWrapper() {
        return new LittleShootProxyWrapper();
    }

}
