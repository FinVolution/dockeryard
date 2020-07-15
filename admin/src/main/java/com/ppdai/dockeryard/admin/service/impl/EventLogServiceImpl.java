package com.ppdai.dockeryard.admin.service.impl;

import com.ppdai.dockeryard.admin.service.EventLogService;
import com.ppdai.dockeryard.core.dto.EventLogDto;
import com.ppdai.dockeryard.core.mapper.EventLogMapper;
import com.ppdai.dockeryard.core.po.EventLogEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventLogServiceImpl implements EventLogService {

    @Autowired
    private EventLogMapper eventLogMapper;

    @Override
    public List<EventLogEntity> getByParam(EventLogDto eventLogDto) {
        if (eventLogDto.getPageNo() != null && eventLogDto.getPageSize() != null) {
            int startRecord = eventLogDto.getPageNo() * eventLogDto.getPageSize();
            eventLogDto.setStartRecord(startRecord);
        }
        return eventLogMapper.getByParam(eventLogDto);
    }

    @Override
    public int getRecordCountByParam(EventLogDto eventLogDto) {
        return eventLogMapper.countByParam(eventLogDto);
    }

    @Override
    public void insert(EventLogEntity eventLog) {
        eventLogMapper.insert(eventLog);
    }

}
