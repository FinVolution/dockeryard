package com.ppdai.dockeryard.admin.cleanup.executor;

import com.ppdai.dockeryard.core.po.ImageEntity;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.Future;

import static com.ppdai.dockeryard.admin.cleanup.executor.ThreadPoolHelper.getImageCleanupThreadPoolExecutor;
import static java.util.stream.Collectors.toList;

public class ImageRepositoryCleanupExecutor implements Executor<ImageTask, ImageEntity> {

    @Override
    public List<Future<ImageEntity>> execute(Collection<ImageTask> tasks) {
        return tasks.stream()
                .map(task->getImageCleanupThreadPoolExecutor().submit(task))
                .collect(toList());
    }

}
