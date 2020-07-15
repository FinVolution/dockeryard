package com.ppdai.dockeryard.core.mapper;

import com.ppdai.dockeryard.core.dto.UserDto;
import com.ppdai.dockeryard.core.po.UserEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserMapper {

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
     * 计算满足条件的数据条数
     *
     * @param userDto
     * @return
     */
    int countByParam(UserDto userDto);

    /**
     * 获取指定用户信息
     *
     * @param id 主键
     * @return UserEntity
     */
    UserEntity getById(@Param("id") long id);

    /**
     * 插入新用户信息
     *
     * @param user
     */
    void insert(UserEntity user);

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
    void delete(@Param("id") long id);

}
