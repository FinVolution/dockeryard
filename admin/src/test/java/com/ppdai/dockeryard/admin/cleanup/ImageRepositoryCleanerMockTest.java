package com.ppdai.dockeryard.admin.cleanup;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.ppdai.dockeryard.admin.AbstractUnitTest;
import com.ppdai.dockeryard.admin.cleanup.executor.Executor;
import com.ppdai.dockeryard.admin.cleanup.executor.ImageTask;
import com.ppdai.dockeryard.admin.cleanup.remote.ImageRepositoryServer;
import com.ppdai.dockeryard.admin.configuration.RepositoryPolicyProperties;
import com.ppdai.dockeryard.admin.constants.CleanPolicyType;
import com.ppdai.dockeryard.admin.service.JobCleanupLogService;
import com.ppdai.dockeryard.core.mapper.ImageMapper;
import com.ppdai.dockeryard.core.po.ImageEntity;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.HashSet;

/**
 * Created by chenlang on 2020/4/28
 **/
@Ignore
@Deprecated
public class ImageRepositoryCleanerMockTest extends AbstractUnitTest {
    @InjectMocks
    private ImageRepositoryCleaner cleaner;
    @Mock
    private ImageMapper imageMapper;
    @Mock
    private JobCleanupLogService jobCleanupLogService;
    @Mock
    private RemoteImageRepositoryCleaner remoteImageRepositoryCleaner;
    @Mock
    private RepositoryPolicyProperties policyProperties;

    @Mock
    private Executor<ImageTask, ImageEntity> imageRepositoryCleanupExecutor;


    @Test
    public void testOnlyClean() throws InterruptedException {
        ImageRepositoryServer server = mock(ImageRepositoryServer.class);
        when(policyProperties.getRepositories()).thenReturn(Sets.newHashSet("*"));
        ImageEntity e1 = new ImageEntity();
        e1.setAppName("a");
        ImageEntity e2 = new ImageEntity();
        e2.setAppName("b");
        ImageEntity e3 = new ImageEntity();
        e3.setAppName("c");
        ImageEntity e4 = new ImageEntity();
        e4.setAppName("s");
        ImageEntity e5 = new ImageEntity();
        e5.setAppName("e");
        ImageEntity e6 = new ImageEntity();
        e6.setAppName("f");
        ImageEntity e7 = new ImageEntity();
        e7.setAppName("g");
        when(imageMapper.findAllApps()).thenReturn(Lists.newArrayList(e1, e2, e3, e4, e5, e6, e7));
        HashSet<ImageEntity> images = Sets.newHashSet();
        for (int i = 0; i < 1000; i++) {
            ImageEntity imageEntity = new ImageEntity();
            imageEntity.setAppName("test:" + i);
            imageEntity.setIsActive(false);
            images.add(imageEntity);
        }
        when(jobCleanupLogService.findNeedCleanedImages(anyString())).thenReturn(images);
        when(remoteImageRepositoryCleaner.remoteDeleteImages(images, server)).thenReturn(null).wait(10_000);
        long l = System.currentTimeMillis();
        cleaner.clean(server, CleanPolicyType.ONLY_CLEAN);
        System.out.println(System.currentTimeMillis() - l);
    }


}
