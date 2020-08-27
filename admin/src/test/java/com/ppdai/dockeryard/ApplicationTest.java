package com.ppdai.dockeryard;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created by chenlang on 2020/4/8
 **/

@SpringBootApplication
//@ComponentScan({"com.ppdai.dockeryard"})
@MapperScan("com.ppdai.dockeryard.core.mapper")
public class ApplicationTest {
}
