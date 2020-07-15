package com.ppdai.dockeryard.core.mapper;

import com.ppdai.dockeryard.core.po.OrganizationEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrganizationMapper {

    /**
     * 获取所有部门组织
     *
     * @return 部门组织列表
     */
    List<OrganizationEntity> getAll();

    /**
     * 获取指定部门组织信息
     *
     * @param id 主键
     * @return OrganizationEntity
     */
    OrganizationEntity getById(@Param("id") long id);

    /**
     * 根据名称获取指定部门组织信息
     *
     * @param name 名称
     * @return OrganizationEntity
     */
    OrganizationEntity getByName(@Param("name") String name);

    /**
     * 根据shortCode获取指定部门组织信息
     *
     * @param shortCode
     * @return OrganizationEntity
     */
    OrganizationEntity getByShortCode(@Param("shortCode") String shortCode);

    /**
     * 插入新部门组织
     *
     * @param org
     */
    void insert(OrganizationEntity org);

    /**
     * 更新部门组织信息
     *
     * @param org
     */
    void update(OrganizationEntity org);

    /**
     * 删除部门组织信息
     *
     * @param id 主键
     */
    void delete(@Param("id") long id);

}
