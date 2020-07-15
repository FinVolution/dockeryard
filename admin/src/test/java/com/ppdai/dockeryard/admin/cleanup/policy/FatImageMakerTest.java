package com.ppdai.dockeryard.admin.cleanup.policy;


import com.google.common.collect.Sets;
import com.ppdai.dockeryard.admin.AbstractUnitTest;
import com.ppdai.dockeryard.admin.api.StarGateFeignClient;
import com.ppdai.dockeryard.admin.configuration.RepositoryPolicyProperties;
import com.ppdai.dockeryard.admin.service.ImageService;
import com.ppdai.dockeryard.core.po.ImageEntity;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.HashSet;
import java.util.Set;

import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

/**
 * Created by chenlang on 2020/4/7
 **/
public class FatImageMakerTest extends AbstractUnitTest {

    @Mock
    private RepositoryPolicyProperties repositoryPolicyProperties;
    @Mock
    private ImageService imageService;
    @Mock
    private StarGateFeignClient starGateClient;
    @InjectMocks
    private FatImageMaker fatImageMaker;


    @Before
    public void before() {
        when(repositoryPolicyProperties.getKeepFatNumber()).thenReturn(10);
        when(repositoryPolicyProperties.getKeepFatTime()).thenReturn(2);
        when(repositoryPolicyProperties.getRepositories()).thenReturn(Sets.newHashSet("chenlang"));
        when(repositoryPolicyProperties.getKeepProNumber()).thenReturn(100);

        when(imageService.countImage(eq("chenlang"), anyString())).thenReturn(100);
        ImageEntity i1 = new ImageEntity();
        i1.setAppName("chenlang");
        i1.setTag("2");
        ImageEntity i2 = new ImageEntity();
        i2.setAppName("chenlang");
        i2.setTag("4");
        ImageEntity i3 = new ImageEntity();
        i3.setAppName("chenlang");
        i3.setTag("5");
        ImageEntity i4 = new ImageEntity();
        i4.setAppName("chenlang");
        i4.setTag("9");
        //指定环境之外的镜像（比如测试环境保留两个月的，则这个集合为两个月之外的所有镜像）
        HashSet<ImageEntity> beforeImages = Sets.newHashSet(i1, i2, i3, i4);
        when(imageService.beforeInsertTime(eq("chenlang"), anyString())).thenReturn(beforeImages);
    }

    @Test
    public void markTest() {
        ImageEntity fatImage = new ImageEntity();
        fatImage.setAppName("chenlang");
        fatImage.setTag("1");
        ImageEntity uatImage = new ImageEntity();
        uatImage.setAppName("chenlang");
        uatImage.setTag("3");
        //正在使用的非生产版本
        HashSet<ImageEntity> entities = Sets.newHashSet(fatImage, uatImage);
        //获取正在使用的非生产版本
        when(starGateClient.obtainIsUsingImage("chenlang", "FAT"))
                .thenReturn(entities);
        Set<ImageEntity> imageEntities = fatImageMaker.mark("chenlang", null);
        Assert.assertEquals(4, imageEntities.size());


    }

    @Test
    public void markContainsTest() {
        ImageEntity fatImage = new ImageEntity();
        fatImage.setAppName("chenlang");
        fatImage.setTag("1");
        ImageEntity uatImage = new ImageEntity();
        uatImage.setAppName("chenlang");
        uatImage.setTag("3");
        ImageEntity uat1 = new ImageEntity();
        uat1.setAppName("chenlang");
        uat1.setTag("4");
        //正在使用的非生产版本
        HashSet<ImageEntity> entities = Sets.newHashSet(fatImage, uatImage, uat1);
        //获取正在使用的非生产版本
        when(starGateClient.obtainIsUsingImage("chenlang", "FAT"))
                .thenReturn(entities);
        Set<ImageEntity> imageEntities = fatImageMaker.mark("chenlang", null);
        Assert.assertEquals(4, imageEntities.size());


    }


}
