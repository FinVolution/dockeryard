package com.ppdai.dockeryard.core.mapper;

import com.google.common.collect.Lists;
import com.ppdai.dockeryard.ApplicationTest;
import com.ppdai.dockeryard.admin.Application;
import com.ppdai.dockeryard.core.po.ImageEntity;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by chenlang on 2020/4/8
 **/
@RunWith(SpringRunner.class)
@Transactional
@SpringBootTest(classes = {ApplicationTest.class})
public class ImageMapperTest {
    @Autowired
    private ImageMapper imageMapper;

    @Test
    public void findNeedMarkAllImages() {
        List<Long> ids = Lists.newArrayList(1L);
        Set<ImageEntity> images = imageMapper.findNeedMarkAllImages("jichu/chenlang", ids, 1);
        Assert.assertNotNull(images);
    }
}
