package com.ppdai.dockeryard.admin.service.impl;

import com.ppdai.dockeryard.admin.service.EventLogService;
import com.ppdai.dockeryard.admin.service.OrganizationService;
import com.ppdai.dockeryard.admin.service.RepositoryService;
import com.ppdai.dockeryard.core.dto.RepositoryDto;
import com.ppdai.dockeryard.core.mapper.RepositoryMapper;
import com.ppdai.dockeryard.core.po.EventLogEntity;
import com.ppdai.dockeryard.core.po.OrganizationEntity;
import com.ppdai.dockeryard.core.po.RepositoryEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class RepositoryServiceImpl implements RepositoryService {

    @Autowired
    private RepositoryMapper repositoryMapper;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private EventLogService eventLogService;

    @Override
    public List<RepositoryEntity> getAll() {
        return repositoryMapper.getAll();
    }

    @Override
    public List<RepositoryEntity> getByOrg(Long orgId) {
        return repositoryMapper.getByOrg(orgId);
    }

    @Override
    public List<RepositoryEntity> getByOrgName(String orgName) {
        return repositoryMapper.getByOrgName(orgName);
    }

    @Override
    public List<RepositoryEntity> getByParam(RepositoryDto repositoryDto) {
        if (repositoryDto.getPageNo() != null && repositoryDto.getPageSize() != null) {
            Integer startRecord = repositoryDto.getPageNo() * repositoryDto.getPageSize();
            repositoryDto.setStartRecord(startRecord);
        }
        return repositoryMapper.getByParam(repositoryDto);
    }

    @Override
    public int getRecordCountByParam(RepositoryDto repositoryDto) {
        return repositoryMapper.countByParam(repositoryDto);
    }

    @Override
    public RepositoryEntity getById(Long id) {
        return repositoryMapper.getById(id);
    }

    @Override
    public void update(RepositoryEntity repository) {
        repositoryMapper.update(repository);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
    public void delete(Long id, String operatorName) {
        if(StringUtils.hasLength(operatorName)){
            RepositoryEntity repository = repositoryMapper.getById(id);
            EventLogEntity eventLog = new EventLogEntity();
            eventLog.setEventName(repository.getName());
            eventLog.setEventAddition("N/A");
            eventLog.setOptionType("drop");
            eventLog.setOperator(operatorName);
            eventLogService.insert(eventLog);
        }
        repositoryMapper.delete(id);
    }

    @Override
    public void insert(RepositoryEntity repository) {
        OrganizationEntity organization = organizationService.getById(repository.getOrgId());
        if (organization != null) {
            String orgName = organization.getName();
            repository.setOrgName(orgName);
        }
        repositoryMapper.insert(repository);
    }
}
