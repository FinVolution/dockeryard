package com.ppdai.dockeryard.admin.service.impl;

import com.ppdai.dockeryard.admin.service.OrganizationService;
import com.ppdai.dockeryard.admin.service.UserService;
import com.ppdai.dockeryard.core.dto.UserDto;
import com.ppdai.dockeryard.core.mapper.UserMapper;
import com.ppdai.dockeryard.core.po.OrganizationEntity;
import com.ppdai.dockeryard.core.po.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private OrganizationService organizationService;

    @Override
    public List<UserEntity> getAll() {
        return userMapper.getAll();
    }

    @Override
    public List<UserEntity> getByParam(UserDto userDto) {
        if (userDto.getPageNo() != null && userDto.getPageSize() != null) {
            int startRecord = userDto.getPageNo() * userDto.getPageSize();
            userDto.setStartRecord(startRecord);
        }
        return userMapper.getByParam(userDto);
    }

    @Override
    public int getRecordCountByParam(UserDto userDto) {
        return userMapper.countByParam(userDto);
    }


    @Override
    public UserEntity getById(Long id) {
        return userMapper.getById(id);
    }

    @Override
    public void update(UserEntity user) {
        userMapper.update(user);
    }

    @Override
    public void delete(Long id) {
        userMapper.delete(id);
    }

    @Override
    public void insert(UserEntity user) {
        OrganizationEntity organizationEntity = organizationService.getById(user.getOrgId());
        String orgName = organizationEntity.getName();
        if (orgName != null) {
            user.setOrgName(orgName);
        }
        userMapper.insert(user);
    }


}
