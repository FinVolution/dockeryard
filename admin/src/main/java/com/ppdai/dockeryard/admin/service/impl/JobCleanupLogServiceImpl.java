package com.ppdai.dockeryard.admin.service.impl;

import com.ppdai.dockeryard.admin.service.JobCleanupLogService;
import com.ppdai.dockeryard.core.mapper.JobCleanupLogMapper;
import com.ppdai.dockeryard.core.po.ImageEntity;
import com.ppdai.dockeryard.core.po.JobCleanupLogEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Service
public class JobCleanupLogServiceImpl implements JobCleanupLogService{

    @Resource
    private JobCleanupLogMapper jobCleanupLogMapper;

    @Override
    public void batchInsert(List<JobCleanupLogEntity> jobCleanupLogEntities) {
        jobCleanupLogMapper.batchInsert(jobCleanupLogEntities);
    }

    @Override
    public void updateStatus(Collection<ImageEntity> images) {
        if (CollectionUtils.isEmpty(images)) return;
        jobCleanupLogMapper.updateStatus(images,"SUCCESS");
    }

    @Override
    public Set<ImageEntity> findNeedCleanedImages(String appName) {
        return jobCleanupLogMapper.findNeedCleanImages(appName);
    }
}
