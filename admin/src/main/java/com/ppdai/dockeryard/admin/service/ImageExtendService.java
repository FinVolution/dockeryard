package com.ppdai.dockeryard.admin.service;

import com.ppdai.dockeryard.core.dto.ImageExtendDto;
import com.ppdai.dockeryard.core.po.ImageExtendEntity;

import java.util.List;

public interface ImageExtendService {

    /**
     * 获取所有镜像标记列表
     *
     * @return 镜像标记列表
     */
    List<ImageExtendEntity> getAll();

    /**
     * 根据查询条件获取镜像标记列表
     *
     * @param imageExtendDto 查询条件
     * @return 满足条件的镜像标记列表
     */
    List<ImageExtendEntity> getByParam(ImageExtendDto imageExtendDto);

    /**
     * 获取满足条件的记录条数
     *
     * @param imageExtendDto 查询条件
     * @return 满足条件的记录条数
     */
    int getRecordCountByParam(ImageExtendDto imageExtendDto);

    /**
     * 获取指定镜像标记
     *
     * @param id 主键
     * @return ImageExtendEntity
     */
    ImageExtendEntity getById(Long id);

    /**
     * 插入新镜像标记
     *
     * @param imageExtend
     * @param envName     环境名
     */
    void insert(ImageExtendEntity imageExtend, String envName);

    /**
     * 更新镜像标记信息
     * 只更新iValue，updateBy，updateTime。
     *
     * @param imageExtend
     * @param envName     环境名
     */
    void update(ImageExtendEntity imageExtend, String envName);

    /**
     * 删除镜像标记信息
     *
     * @param id 主键
     */
    void delete(Long id);

}
