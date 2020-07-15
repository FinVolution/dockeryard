package com.ppdai.dockeryard.admin.cleanup.handler;

import com.ppdai.dockeryard.admin.cleanup.ImageRepositoryMarkChain;
import com.ppdai.dockeryard.admin.configuration.RepositoryPolicyProperties;
import com.ppdai.dockeryard.admin.service.JobCleanupLogService;
import com.ppdai.dockeryard.core.mapper.ImageMapper;
import com.ppdai.dockeryard.core.po.ImageEntity;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import static com.ppdai.dockeryard.admin.cleanup.executor.ThreadPoolHelper.getImageRepositoryThreadPoolExecutor;
import static com.ppdai.dockeryard.admin.configuration.RepositoryPolicyProperties.ALL_REPOSITORIES_TAG;
import static com.ppdai.dockeryard.core.po.JobCleanupLogEntity.apply;
import static org.springframework.util.CollectionUtils.isEmpty;

public class ImageOnlyMarkHandler implements Handler {

    private ImageRepositoryMarkChain imageRepositoryMarkChain;
    private RepositoryPolicyProperties policyProperties;
    private ImageMapper imageMapper;
    private JobCleanupLogService jobCleanupLogService;

    public ImageOnlyMarkHandler(ImageRepositoryMarkChain chain,
                                RepositoryPolicyProperties policyProperties,
                                ImageMapper imageMapper,
                                JobCleanupLogService jobCleanupLogService) {
        this.jobCleanupLogService = jobCleanupLogService;
        this.imageMapper = imageMapper;
        this.policyProperties = policyProperties;
        this.imageRepositoryMarkChain = chain;
    }

    @Override
    public void handle() {
        Set<String> appNames = policyProperties.getRepositories();
        if (appNames.contains(ALL_REPOSITORIES_TAG)) {
            List<ImageEntity> images = imageMapper.findAllApps();
            for (ImageEntity image : images) {
                getImageRepositoryThreadPoolExecutor().execute(() -> {
                    Set<ImageEntity> imageEntities = imageRepositoryMarkChain.needCleanupImages(image.getAppName());
                    markNeedCleanImages(imageEntities);
                });
            }
        } else {
            for (String appName : appNames) {
                Set<ImageEntity> imageEntities = imageRepositoryMarkChain.needCleanupImages(appName);
                markNeedCleanImages(imageEntities);
            }
        }
    }

    private void markNeedCleanImages(Collection<ImageEntity> images) {
        if (!isEmpty(images)) {
            jobCleanupLogService.batchInsert(apply(images));
        }
    }

}
