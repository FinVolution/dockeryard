package com.ppdai.dockeryard.admin.api;

//import org.springframework.cloud.netflix.feign.FeignClient;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

@FeignClient(name = "dockeryard", url = "${dockeryar.proxy.url}")
public interface DockeryardProxyFeignClient {

    @RequestMapping("/image/clean")
    void execute(@RequestParam("command") String command) throws IOException;

}
