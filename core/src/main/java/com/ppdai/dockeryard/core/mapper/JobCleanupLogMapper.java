package com.ppdai.dockeryard.core.mapper;

import com.ppdai.dockeryard.core.po.ImageEntity;
import com.ppdai.dockeryard.core.po.JobCleanupLogEntity;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface JobCleanupLogMapper {

    void batchInsert(@Param("jobCleanupLogs") List<JobCleanupLogEntity> jobCleanupLogs);

    void updateStatus(@Param("images") Collection<ImageEntity> images, @Param("status") String status);

    Set<ImageEntity> findNeedCleanImages(String appName);
}
