package com.ppdai.dockeryard.admin.cleanup;

import com.ppdai.dockeryard.admin.api.DockeryardProxyFeignClient;
import com.ppdai.dockeryard.admin.cleanup.executor.Executor;
import com.ppdai.dockeryard.admin.cleanup.executor.ImageTask;
import com.ppdai.dockeryard.admin.cleanup.remote.ImageRepositoryServer;
import com.ppdai.dockeryard.core.po.ImageEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

import static java.util.stream.Collectors.toList;

@Slf4j
@Component
public class RemoteImageRepositoryCleaner {

    @Autowired
    private DockeryardProxyFeignClient dockeryardProxyFeignClient;
    @Autowired
    private Executor<ImageTask, ImageEntity> imageRepositoryCleanupExecutor;
    @Value("${dockeryard.multi.thread.executable.number:100}")
    private int executableNumber;
    @Value("dockeryard.garbage.collect")
    private String cleanupCommand;


    /**
     * 远程删除镜像
     *
     * @param images
     * @param imageRepositoryServer
     * @return
     */
    public Collection<ImageEntity> remoteDeleteImages(Collection<ImageEntity> images, ImageRepositoryServer imageRepositoryServer) {
        if (images.size() >= executableNumber) {
            List<ImageTask> imageTasks = images.stream()
                    .map(imageEntity -> new ImageTask(imageEntity, imageRepositoryServer))
                    .collect(toList());
            List<Future<ImageEntity>> futures = imageRepositoryCleanupExecutor.execute(imageTasks);
            return futures.stream().map(this::applyImage).filter(Objects::nonNull).collect(toList());
        } else {
            for (ImageEntity image : images) {
                imageRepositoryServer.remoteDeleteImages(image);
            }
            return images;
        }
    }

    /**
     * 垃圾回收
     */
    public void collectImages() {
        CompletableFuture.runAsync(() -> {
            try {
                long l = System.currentTimeMillis();
                dockeryardProxyFeignClient.execute(cleanupCommand);
                log.info("镜像垃圾回收执行耗时：{}", System.currentTimeMillis() - l);
            } catch (Exception e) {
                log.error("镜像清理回收命令执行失败", e);
            }
        });
    }


    private ImageEntity applyImage(Future<ImageEntity> future) {
        try {
            return future.get();
        } catch (Exception e) {
            log.error("获取image对象失败", e);
        }
        return null;
    }

}
