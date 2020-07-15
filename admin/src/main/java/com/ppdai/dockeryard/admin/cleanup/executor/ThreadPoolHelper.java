package com.ppdai.dockeryard.admin.cleanup.executor;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PreDestroy;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Slf4j
public class ThreadPoolHelper {

    private static ThreadPoolExecutor imageCleanupThreadPoolExecutor = new ThreadPoolExecutor(
            10, 10,
            10, TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(3000),
            new ThreadFactoryBuilder().setNameFormat("image-cleanup-thread-pool-%d").build());

    private static ThreadPoolExecutor imageRepositoryThreadPoolExecutor = new ThreadPoolExecutor(
            5, 5,
            10, TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(3000),
            new ThreadFactoryBuilder().setNameFormat("image-repository-thread-pool-%d").build());

    public static ThreadPoolExecutor getImageCleanupThreadPoolExecutor() {
        return imageCleanupThreadPoolExecutor;
    }

    public static ThreadPoolExecutor getImageRepositoryThreadPoolExecutor() {
        return imageRepositoryThreadPoolExecutor;
    }

    @PreDestroy
    public void destroy() {
        try {
            imageCleanupThreadPoolExecutor.shutdown();
            imageRepositoryThreadPoolExecutor.shutdown();
            if (!imageCleanupThreadPoolExecutor.awaitTermination(2, TimeUnit.SECONDS)) {
                List<Runnable> droppedTasks = imageCleanupThreadPoolExecutor.shutdownNow();
                log.info("关闭的线程池线程数量：{}", droppedTasks.size());
            }
            if (!imageRepositoryThreadPoolExecutor.awaitTermination(2, TimeUnit.SECONDS)) {
                List<Runnable> droppedTasks = imageRepositoryThreadPoolExecutor.shutdownNow();
                log.info("关闭的线程池线程数量：{}", droppedTasks.size());
            }
            log.info("镜像清理线程池关闭");
        } catch (InterruptedException e) {
            log.error("关闭线程池失败", e);
        }
    }
}