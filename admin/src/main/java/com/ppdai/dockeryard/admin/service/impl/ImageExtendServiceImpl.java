package com.ppdai.dockeryard.admin.service.impl;

import com.google.common.collect.Lists;
import com.ppdai.dockeryard.admin.service.EventLogService;
import com.ppdai.dockeryard.admin.service.ImageExtendService;
import com.ppdai.dockeryard.admin.service.ImageService;
import com.ppdai.dockeryard.core.dto.ImageExtendDto;
import com.ppdai.dockeryard.core.mapper.ImageExtendMapper;
import com.ppdai.dockeryard.core.po.EventLogEntity;
import com.ppdai.dockeryard.core.po.ImageEntity;
import com.ppdai.dockeryard.core.po.ImageExtendEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class ImageExtendServiceImpl implements ImageExtendService {

    @Autowired
    private ImageExtendMapper imageExtendMapper;

    @Autowired
    private ImageService imageService;

    @Autowired
    private EventLogService eventLogService;

    @Override
    public List<ImageExtendEntity> getAll() {
        return imageExtendMapper.getAll();
    }

    @Override
    public List<ImageExtendEntity> getByParam(ImageExtendDto imageExtendDto) {
        //只有ikey有值，才查。
        if(imageExtendDto != null && StringUtils.hasLength(imageExtendDto.getIKey())){
            if (imageExtendDto.getPageNo() != null && imageExtendDto.getPageSize() != null) {
                int startRecord = imageExtendDto.getPageNo() * imageExtendDto.getPageSize();
                imageExtendDto.setStartRecord(startRecord);
            }
            return imageExtendMapper.getByParam(imageExtendDto);
        }else {
            return Lists.newArrayList();
        }
    }

    @Override
    public int getRecordCountByParam(ImageExtendDto imageExtendDto) {
        return imageExtendMapper.countByParam(imageExtendDto);
    }

    @Override
    public ImageExtendEntity getById(Long id) {
        return imageExtendMapper.getById(id);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
    public void insert(ImageExtendEntity imageExtend, String envName) {
        imageExtendMapper.insert(imageExtend);
        if(StringUtils.hasLength(envName)) {
            saveEventLogForOperator(imageExtend.getImageId(), envName, imageExtend.getIValue(), imageExtend.getInsertBy());
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
    public void update(ImageExtendEntity imageExtend, String envName) {
        imageExtendMapper.update(imageExtend);
        if(StringUtils.hasLength(envName)){
            saveEventLogForOperator(imageExtend.getImageId(), envName, imageExtend.getIValue(), imageExtend.getUpdateBy());
        }
    }

    @Override
    public void delete(Long id) {
        imageExtendMapper.delete(id);
    }

    private void saveEventLogForOperator(Long imageId, String envName, String imageExtendValue, String operatorName) {
        if (StringUtils.hasLength(operatorName)) {
            //镜像的状态变更，需要记录审计日志。
            ImageEntity image = imageService.getById(imageId);
            EventLogEntity eventLog = new EventLogEntity();
            eventLog.setEventName(image.getRepoName());
            eventLog.setEventAddition(image.getTag());
            eventLog.setOptionType(envName + " " + imageExtendValue);
            eventLog.setOperator(operatorName);
            eventLogService.insert(eventLog);
        }
    }
}
