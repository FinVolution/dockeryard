package com.ppdai.dockeryard.proxy.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

public final class IpUtil {
    private IpUtil() {
        throw new AssertionError();
    }

    private static Logger logger = LoggerFactory.getLogger(IpUtil.class);

    public static final String X_FORWARDED_FOR = "X-Forwarded-For";

    public static String getIpOnEth0() {
        String ipOnEth0 = "";
        Enumeration<NetworkInterface> netInterfaces;
        try {
            netInterfaces = NetworkInterface.getNetworkInterfaces();
            InetAddress ip;
            while (netInterfaces.hasMoreElements()) {
                NetworkInterface ni = netInterfaces.nextElement();
                if ("eth0".equals(ni.getName())) {
                    Enumeration<InetAddress> addresses = ni.getInetAddresses();
                    while (addresses.hasMoreElements()) {
                        ip = addresses.nextElement();
                        if (!ip.isLoopbackAddress() && ip.getHostAddress().indexOf(':') == -1) {
                            ipOnEth0 = ip.getHostAddress();
                        }
                    }
                }
            }
            if (!StringUtils.hasLength(ipOnEth0)) {
                InetAddress inetAddress = InetAddress.getLocalHost();
                ipOnEth0 = inetAddress.getHostAddress();
            }
        } catch (Exception e) {
            logger.error("getIpOnEth0 failed", e);
        }
        return ipOnEth0;
    }


}
