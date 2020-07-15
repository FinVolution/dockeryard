package com.ppdai.dockeryard.admin.service;

import com.ppdai.atlas.model.EnvDto;
import com.ppdai.dockeryard.core.po.EnvironmentEntity;

import java.util.List;

public interface EnvironmentService {

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
    List<String> getAllNames();

    /**
     * 根据name获取指定环境信息
     *
     * @param name 环境对应的名称（dev fat prod等）
     * @return EnvironmentEntity
     */
    EnvironmentEntity getByName(String name);

    /**
     * 根据url获取指定环境信息
     *
     * @param url 环境对应的ip:port
     * @return EnvironmentEntity
     */
    EnvironmentEntity getByUrl(String url);

    /**
     * 获取指定环境信息
     *
     * @param id 主键
     * @return EnvironmentEntity
     */
    EnvironmentEntity getById(Long id);

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
    void delete(Long id);

    /**
     * 插入新环境
     *
     * @param environment
     */
    void insert(EnvironmentEntity environment);

    /**
     * 从远端同步数据到本地数据库
     *
     * @param remoteEnvList
     */
    void SyncAllEnv(List<EnvDto> remoteEnvList);
}
