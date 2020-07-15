package com.ppdai.dockeryard.admin.service;

import com.ppdai.dockeryard.core.dto.UserDto;
import com.ppdai.dockeryard.core.po.UserEntity;

import java.util.List;

public interface UserService {

    /**
     * 获取所有用户列表
     *
     * @return 所有用户列表
     */
    List<UserEntity> getAll();

    /**
     * 根据条件查询获取用户列表
     *
     * @param userDto 条件查询
     * @return 满足条件的用户列表
     */
    List<UserEntity> getByParam(UserDto userDto);

    /**
     * 获取满足条件的记录条数
     *
     * @param userDto 查询条件
     * @return 满足条件的记录条数
     */
    int getRecordCountByParam(UserDto userDto);

    /**
     * 获取指定用户信息
     *
     * @param id 主键
     * @return UserEntity
     */
    UserEntity getById(Long id);

    /**
     * 更新用户信息
     *
     * @param user
     */
    void update(UserEntity user);

    /**
     * 删除用户信息
     *
     * @param id 主键
     */
    void delete(Long id);

    /**
     * 插入新用户信息
     *
     * @param user
     */
    void insert(UserEntity user);

}
