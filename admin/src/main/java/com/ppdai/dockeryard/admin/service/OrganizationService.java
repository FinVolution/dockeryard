package com.ppdai.dockeryard.admin.service;

import com.ppdai.dockeryard.core.po.OrganizationEntity;

import java.util.List;

public interface OrganizationService {

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
    OrganizationEntity getById(Long id);

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
    void delete(Long id);

    /**
     * 插入新部门组织
     *
     * @param org
     */
    void insert(OrganizationEntity org);

}
