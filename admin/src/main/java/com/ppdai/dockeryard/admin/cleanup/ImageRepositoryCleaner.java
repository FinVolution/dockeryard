package com.ppdai.dockeryard.admin.cleanup;

import com.google.common.annotations.VisibleForTesting;
import com.ppdai.dockeryard.admin.cleanup.remote.ImageRepositoryServer;
import com.ppdai.dockeryard.admin.configuration.RepositoryPolicyProperties;
import com.ppdai.dockeryard.admin.constants.CleanPolicyType;
import com.ppdai.dockeryard.admin.service.JobCleanupLogService;
import com.ppdai.dockeryard.core.mapper.ImageMapper;
import com.ppdai.dockeryard.core.po.ImageEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import static com.ppdai.dockeryard.admin.cleanup.executor.ThreadPoolHelper.getImageRepositoryThreadPoolExecutor;
import static com.ppdai.dockeryard.admin.configuration.RepositoryPolicyProperties.ALL_REPOSITORIES_TAG;
import static com.ppdai.dockeryard.admin.constants.CleanPolicyType.ONLY_MARK;
import static com.ppdai.dockeryard.core.po.JobCleanupLogEntity.apply;
import static java.util.stream.Collectors.toList;
import static org.springframework.util.CollectionUtils.isEmpty;

@Slf4j
@Component
@Deprecated
public class ImageRepositoryCleaner {

    @Autowired
    private ImageRepositoryMarkChain imageRepositoryMarkChain;
    @Resource
    private ImageMapper imageMapper;
    @Autowired
    private JobCleanupLogService jobCleanupLogService;
    @Autowired
    private RemoteImageRepositoryCleaner remoteImageRepositoryCleaner;
    @Autowired
    private ImageRepositoryCleaner repositoryCleaner;
    @Autowired
    private RepositoryPolicyProperties policyProperties;


    @Value("${dockeryard.register.url}")
    private String imageRegisterUrl;

    @Deprecated
    public void clean(CleanPolicyType type) {
        long start = System.currentTimeMillis();
        try (ImageRepositoryServer server = new ImageRepositoryServer(imageRegisterUrl)) {
            clean(server, type);
            log.info("镜像标记清理耗时：{}", (System.currentTimeMillis() - start)  + " ms");
        }
    }

    @VisibleForTesting
    protected void clean(ImageRepositoryServer server, CleanPolicyType type) {
        Set<String> appNames = policyProperties.getRepositories();
        if (appNames.contains(ALL_REPOSITORIES_TAG)) {
            //应用
            List<ImageEntity> images = imageMapper.findAllApps();
            for (ImageEntity image : images) {
                getImageRepositoryThreadPoolExecutor().execute(() -> {
                    clean(image.getAppName(), server, type);
                });
            }
        } else {
            appNames.forEach(appName -> clean(appName, server, type));
        }
    }

    /**
     * 标记并清理
     *
     * @param appName
     */
    @Deprecated
    private void clean(String appName, ImageRepositoryServer server, CleanPolicyType type) {
        try {
            if (ONLY_MARK.equals(type)) {
                Set<ImageEntity> images = imageRepositoryMarkChain.needCleanupImages(appName);
                markNeedCleanImages(images);
            }
            if (CleanPolicyType.ONLY_CLEAN.equals(type)) {
                Set<ImageEntity> images = jobCleanupLogService.findNeedCleanedImages(appName);
                cleanMarkedImages(images, server);
            }
            if (CleanPolicyType.MARK_CLEAN.equals(type)) {
                Set<ImageEntity> images = imageRepositoryMarkChain.needCleanupImages(appName);
                markAndClean(images, server);
            }
        } catch (Exception e) {
            log.error("应用:{}的仓库清理失败:{}", appName, e);
        }
    }


    /**
     * 标记并清理镜像
     *
     * @param images
     */
    private void markAndClean(Collection<ImageEntity> images, ImageRepositoryServer server) {
        markNeedCleanImages(images);
        cleanMarkedImages(images, server);
    }

    /**
     * 标记需要清理的镜像
     *
     * @param images
     */
    private void markNeedCleanImages(Collection<ImageEntity> images) {
        if (!isEmpty(images)) {
            jobCleanupLogService.batchInsert(apply(images));
        }
    }

    /**
     * 清理标记过的需要清理的镜像
     *
     * @param imageEntities 远程已经实际清除了的image
     * @return
     */
    private void cleanMarkedImages(Collection<ImageEntity> imageEntities, ImageRepositoryServer imageRepositoryServer) {

        long l = System.currentTimeMillis();
        Collection<ImageEntity> images = remoteImageRepositoryCleaner.remoteDeleteImages(imageEntities, imageRepositoryServer);
        System.out.println("远程调用耗时：" + (System.currentTimeMillis() - l) / 1000);
        repositoryCleaner.updateStatus(images);
    }

    @Deprecated
    @Transactional(rollbackFor = Exception.class)
    public void updateStatus(Collection<ImageEntity> imageEntities) {
        if (imageEntities == null) return;
        List<ImageEntity> images = imageEntities.stream().filter(imageEntity -> !imageEntity.getIsActive()).collect(toList());
        if (isEmpty(images)) return;
        imageMapper.batchDelete(images);
        jobCleanupLogService.updateStatus(images);
    }


}
