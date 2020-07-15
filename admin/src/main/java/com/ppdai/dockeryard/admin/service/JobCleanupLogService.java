package com.ppdai.dockeryard.admin.service;

import com.ppdai.dockeryard.core.po.ImageEntity;
import com.ppdai.dockeryard.core.po.JobCleanupLogEntity;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface JobCleanupLogService {

    void batchInsert(List<JobCleanupLogEntity> jobCleanupLogEntities);

    void updateStatus(Collection<ImageEntity> images);

    Set<ImageEntity> findNeedCleanedImages(String repository);
}
