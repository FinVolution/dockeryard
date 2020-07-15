package com.ppdai.dockeryard.admin.api;

import com.ppdai.dockeryard.core.po.ImageEntity;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Set;

/**
 * Created by chenlang on 2020/4/7
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
public class StarGateFeignClientTest {

    @Autowired
    private StarGateFeignClient starGateFeignClient;

    @Test
    public void obtainUsingImageTest() {
        Set<ImageEntity> imageEntities = starGateFeignClient.obtainIsUsingImage("hello.test.com", "fat");
        Assert.assertNotNull(imageEntities);
    }

}
