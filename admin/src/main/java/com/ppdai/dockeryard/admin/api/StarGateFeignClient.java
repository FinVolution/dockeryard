package com.ppdai.dockeryard.admin.api;

import com.ppdai.dockeryard.core.po.ImageEntity;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Set;

@FeignClient(name = "starGate",url = "${stargate.url}")
public interface StarGateFeignClient {

    @RequestMapping("/api/cloud/image/using-images")
    Set<ImageEntity> obtainIsUsingImage(@RequestParam("appName") String appName, @RequestParam("env") String env);

    @RequestMapping("/api/cloud/image/used-images")
    Set<ImageEntity> obtainUsedImage(@RequestParam("appName") String appName, @RequestParam("number") int number,
                                     @RequestParam("env") String env);

}
