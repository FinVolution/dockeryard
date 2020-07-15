package com.ppdai.dockeryard;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * Created by chenlang on 2020/4/8
 **/

@EnableFeignClients
@SpringBootApplication
//@ComponentScan({"com.ppdai.dockeryard"})
@MapperScan("com.ppdai.dockeryard.core.mapper")
public class ApplicationTest {
}
