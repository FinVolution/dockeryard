package com.ppdai.dockeryard.core.mapper;

import com.ppdai.dockeryard.core.dto.RepositoryDto;
import com.ppdai.dockeryard.core.po.RepositoryEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface RepositoryMapper {

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
    List<RepositoryEntity> getByOrg(@Param("orgId") Long orgId);

    /**
     * 获取查询条件下的仓库列表
     *
     * @param repositoryDto 查询条件
     * @return 满足条件的仓库列表
     */
    List<RepositoryEntity> getByParam(RepositoryDto repositoryDto);

    /**
     * 计算满足条件的数据条数
     *
     * @param repositoryDto
     * @return
     */
    int countByParam(RepositoryDto repositoryDto);

    /**
     * 获取某个仓库
     *
     * @param id 主键
     * @return RepositoryEntity
     */
    RepositoryEntity getById(@Param("id") long id);

    /**
     * 获取name某个仓库
     *
     * @param name 仓库名称
     * @return RepositoryEntity
     */
    RepositoryEntity getByName(@Param("name") String name);

    /**
     * 插入新仓库
     *
     * @param repository
     */
    void insert(RepositoryEntity repository);

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
    void delete(@Param("id") long id);

    /**
     * 下载次数加1
     *
     * @param id 主键
     */
    void addToDownloadCount(@Param("id") long id);

    /**
     * 根据组织id获取仓库列表
     *
     * @param orgName 组织名称
     * @return 指定组织名称下的仓库列表
     */
    List<RepositoryEntity> getByOrgName(@Param("orgName") String orgName);
}
