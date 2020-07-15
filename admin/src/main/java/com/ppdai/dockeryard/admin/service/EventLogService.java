package com.ppdai.dockeryard.admin.service;

import com.ppdai.dockeryard.core.dto.EventLogDto;
import com.ppdai.dockeryard.core.po.EventLogEntity;

import java.util.List;

public interface EventLogService {

    /**
     * 根据查询条件审计日志
     *
     * @param eventLogDto 查询条件
     * @return 满足条件的审计日志
     */
    List<EventLogEntity> getByParam(EventLogDto eventLogDto);

    /**
     * 获取满足条件的记录条数
     *
     * @param eventLogDto 查询条件
     * @return 满足条件的记录条数
     */
    int getRecordCountByParam(EventLogDto eventLogDto);

    /**
     * 插入记录
     *
     * @param eventLogDto 审计日志
     */
    void insert(EventLogEntity eventLogDto);

}
