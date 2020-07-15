package com.ppdai.dockeryard.admin.service;

import com.ppdai.dockeryard.core.dto.ImageDto;
import com.ppdai.dockeryard.core.po.ImageEntity;

import java.util.List;
import java.util.Set;

public interface ImageService {

    /**
     * 获取所有镜像列表
     *
     * @return 所有镜像列表
     */
    List<ImageEntity> getAll();

    /**
     * 根据条件获取所有镜像
     *
     * @param imageDto 查询条件
     * @return 镜像列表
     */
    List<ImageEntity> getByParam(ImageDto imageDto);


    /**
     * 根据应用名称获取镜像列表
     *
     * @param appName 应用名称
     * @return ImageEntity
     */
    List<ImageEntity> getByAppName(String appName);

    /**
     * 获取满足条件的记录条数
     *
     * @param imageDto 查询条件
     * @return 满足条件的记录条数
     */
    int getRecordCountByParam(ImageDto imageDto);

    /**
     * 根据主键获取镜像
     *
     * @param id 主键
     * @return ImageEntity
     */
    ImageEntity getById(Long id);

    /**
     * 更新镜像信息 只更新镜像的updateBy和updateTime
     *
     * @param image
     */
    void update(ImageEntity image);

    /**
     * 删除镜像
     *
     * @param id           主键
     * @param operatorName 操作人姓名
     */
    void delete(Long id, String operatorName);

    /**
     * 插入镜像
     *
     * @param image
     */
    void insert(ImageEntity image);


    ImageEntity tagExistingImage(ImageDto imageDto, String expectedTag);

    int countImage(String appName, String beforeMonthFormat);

    Set<ImageEntity> beforeInsertTime(String appName, String time);

    Set<ImageEntity> findNeedMarkAllImages(String appName, Set<ImageEntity> imageEntities, int length);

    Set<ImageEntity> findImages(Set<ImageEntity> imageEntities);
}
