package com.ppdai.dockeryard.core.mapper;

import com.ppdai.dockeryard.core.dto.EventLogDto;
import com.ppdai.dockeryard.core.po.EventLogEntity;

import java.util.List;

public interface EventLogMapper {

    /**
     * 根据条件查询审计日志
     *
     * @param eventLogDto 查询条件
     * @return 审计日志
     */
    List<EventLogEntity> getByParam(EventLogDto eventLogDto);

    /**
     * 计算满足条件的数据条数
     *
     * @param imageDto 查询条件
     * @return 镜像列表
     */
    int countByParam(EventLogDto imageDto);


    /**
     * 插入审计日志
     *
     * @param eventLog
     */
    void insert(EventLogEntity eventLog);


}
