package com.ppdai.dockeryard.admin.cleanup.handler;

import com.ppdai.dockeryard.admin.cleanup.RemoteImageRepositoryCleaner;
import com.ppdai.dockeryard.admin.cleanup.remote.ImageRepositoryServer;
import com.ppdai.dockeryard.admin.service.JobCleanupLogService;
import com.ppdai.dockeryard.core.mapper.ImageMapper;
import com.ppdai.dockeryard.core.po.ImageEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import static com.ppdai.dockeryard.admin.cleanup.executor.ThreadPoolHelper.getImageRepositoryThreadPoolExecutor;
import static java.util.stream.Collectors.toList;
import static org.springframework.util.CollectionUtils.isEmpty;

@Slf4j
public class ImageRemoveHandler implements Handler {

    private JobCleanupLogService jobCleanupLogService;
    private ImageMapper imageMapper;
    private RemoteImageRepositoryCleaner imageRepositoryCleaner;
    private ImageRemoveHandler handler;
    private String imageRegisterUrl;

    public ImageRemoveHandler(JobCleanupLogService jobCleanupLogService,
                              ImageMapper imageMapper,
                              RemoteImageRepositoryCleaner cleaner,
                              ImageRemoveHandler handler,
                              String imageRegisterUrl) {
        this.imageRegisterUrl = imageRegisterUrl;
        this.handler = handler;
        this.imageRepositoryCleaner = cleaner;
        this.imageMapper = imageMapper;
        this.jobCleanupLogService = jobCleanupLogService;
    }

    @Override
    public void handle() {
        List<ImageEntity> imageEntities = imageMapper.findAllApps();
        for (ImageEntity image : imageEntities) {
            getImageRepositoryThreadPoolExecutor().execute(() -> {
                Set<ImageEntity> images = jobCleanupLogService.findNeedCleanedImages(image.getAppName());
                if (!CollectionUtils.isEmpty(images)) {
                    try (ImageRepositoryServer server = new ImageRepositoryServer(imageRegisterUrl)) {
                        long start = System.currentTimeMillis();
                        cleanMarkedImages(images, server);
                        log.info("远程调用删除{}镜像耗时：{}", image.getAppName(), System.currentTimeMillis() - start);
                    }
                }
            });
        }
    }

    private void cleanMarkedImages(Collection<ImageEntity> imageEntities, ImageRepositoryServer imageRepositoryServer) {
        Collection<ImageEntity> images = imageRepositoryCleaner.remoteDeleteImages(imageEntities, imageRepositoryServer);
        handler.updateStatus(images);
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateStatus(Collection<ImageEntity> imageEntities) {
        if (imageEntities == null) return;
        List<ImageEntity> images = imageEntities.stream().filter(imageEntity -> !imageEntity.getIsActive()).collect(toList());
        if (isEmpty(images)) return;
        imageMapper.batchDelete(images);
        jobCleanupLogService.updateStatus(images);
    }

}
