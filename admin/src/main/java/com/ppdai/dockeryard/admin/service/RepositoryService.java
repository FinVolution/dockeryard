package com.ppdai.dockeryard.admin.service;

import com.ppdai.dockeryard.core.dto.RepositoryDto;
import com.ppdai.dockeryard.core.po.RepositoryEntity;

import java.util.List;

public interface RepositoryService {

    /**
     * 获取所有仓库列表
     *
     * @return 所有仓库列表
     */
    List<RepositoryEntity> getAll();

    /**
     * 根据组织id获取仓库列表
     *
     * @param orgId 组织id
     * @return 指定组织id下的仓库列表
     */
    List<RepositoryEntity> getByOrg(Long orgId);

    /**
     * 获取某个仓库
     *
     * @param orgName 组织名称
     * @return RepositoryEntity
     */
    List<RepositoryEntity> getByOrgName(String orgName);

    /**
     * 获取查询条件下的仓库列表
     *
     * @param repositoryDto 查询条件
     * @return 满足条件的仓库列表
     */
    List<RepositoryEntity> getByParam(RepositoryDto repositoryDto);

    /**
     * 获取满足条件的记录条数
     *
     * @param repositoryDto 查询条件
     * @return 满足条件的记录条数
     */
    int getRecordCountByParam(RepositoryDto repositoryDto);

    /**
     * 获取某个仓库
     *
     * @param id 主键
     * @return RepositoryEntity
     */
    RepositoryEntity getById(Long id);

    /**
     * 更新仓库信息
     *
     * @param repository
     */
    void update(RepositoryEntity repository);

    /**
     * 删除仓库
     *
     * @param id 主键
     */
    void delete(Long id, String operatorName);

    /**
     * 插入新仓库
     *
     * @param repository
     */
    void insert(RepositoryEntity repository);


}
