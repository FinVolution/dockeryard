package com.ppdai.dockeryard.core.mapper;

import com.ppdai.dockeryard.core.po.EnvironmentEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface EnvironmentMapper {

    /**
     * 获取所有环境
     *
     * @return 所有环境
     */
    List<EnvironmentEntity> getAll();

    /**
     * 获取所有环境名称
     *
     * @return 所有环境的名称
     */
    List<String> getAllEvnNames();

    /**
     * 获取指定环境信息
     *
     * @param id 主键
     * @return EnvironmentEntity
     */
    EnvironmentEntity getById(@Param("id") long id);

    /**
     * 根据name获取指定环境信息
     *
     * @param name 环境对应的名称（dev fat prod等）
     * @return EnvironmentEntity
     */
    EnvironmentEntity getByName(@Param("name") String name);

    /**
     * 根据url获取指定环境信息
     *
     * @param url 环境对应的ip:port
     * @return EnvironmentEntity
     */
    EnvironmentEntity getByUrl(@Param("url") String url);

    /**
     * 插入新环境
     *
     * @param environment
     */
    void insert(EnvironmentEntity environment);

    /**
     * 更新环境信息
     *
     * @param environment
     */
    void update(EnvironmentEntity environment);

    /**
     * 删除环境
     *
     * @param id 主键
     */
    void delete(@Param("id") long id);

}
