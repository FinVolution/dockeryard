package com.ppdai.dockeryard.admin.cleanup;

import com.google.common.collect.Sets;
import com.ppdai.dockeryard.admin.AbstractUnitTest;
import com.ppdai.dockeryard.admin.api.StarGateFeignClient;
import com.ppdai.dockeryard.admin.cleanup.policy.FatImageMaker;
import com.ppdai.dockeryard.admin.cleanup.policy.Maker;
import com.ppdai.dockeryard.admin.cleanup.policy.ProdImageMaker;
import com.ppdai.dockeryard.core.po.ImageEntity;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.when;

/**
 * Created by chenlang on 2020/4/7
 **/
public class ImageRepositoryMarkChainTest extends AbstractUnitTest {

    @Mock
    private FatImageMaker fatImageMaker;
    @Mock
    private ProdImageMaker prodImageMaker;
    @Mock
    private StarGateFeignClient starGateFeignClient;

    @Test
    public void needCleanupImages() {
        when(fatImageMaker.isActive()).thenReturn(true);
        when(prodImageMaker.isActive()).thenReturn(true);
        ImageEntity a1 = new ImageEntity();
        a1.setAppName("chenlang");
        a1.setTag("2");
        ImageEntity a2 = new ImageEntity();
        a2.setAppName("chenlang");
        a2.setTag("4");
        ImageEntity c3 = new ImageEntity();
        c3.setAppName("chenlang");
        c3.setTag("5");
        ImageEntity d4 = new ImageEntity();
        d4.setAppName("chenlang");
        d4.setTag("9");
        ImageEntity a3 = new ImageEntity();
        a3.setAppName("chenlang");
        a3.setTag("12");
        List<Maker<ImageEntity>> makers1 = new ArrayList<>();
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
        HashSet<ImageEntity> proImags = Sets.newHashSet(i1, i2, i3, i4);
        //获取生产使用过的版本
        when(starGateFeignClient.obtainUsedImage(eq("chenlang"), anyInt(), any()))
                .thenReturn(proImags);
        makers1.add(fatImageMaker);
        makers1.add(prodImageMaker);
        HashSet<ImageEntity> images = Sets.newHashSet(a1, a2, c3, a3, d4);
        when(fatImageMaker.mark(eq("chenlang"), anySet())).thenReturn(images);

        ImageRepositoryMarkChain markChain = new ImageRepositoryMarkChain(makers1);

        Set<ImageEntity> imageEntities = markChain.needCleanupImages("chenlang");
        Assert.assertEquals(0, imageEntities.size());
    }
}
