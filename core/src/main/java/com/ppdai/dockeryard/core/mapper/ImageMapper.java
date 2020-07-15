package com.ppdai.dockeryard.core.mapper;

import com.ppdai.dockeryard.core.dto.ImageDto;
import com.ppdai.dockeryard.core.po.ImageEntity;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface ImageMapper {

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
     * 根据条件获取所有镜像（包含环境）
     *
     * @param imageDto 查询条件
     * @return 镜像列表
     */
    List<ImageEntity> getByParamAttachEnv(ImageDto imageDto);

    /**
     * 计算满足条件的数据条数
     *
     * @param imageDto 查询条件
     * @return 镜像列表
     */
    int countByParam(ImageDto imageDto);

    /**
     * 计算满足条件的数据条数（包含环境）
     *
     * @param imageDto 查询条件
     * @return 满足条件的记录条数
     */
    int countByParamAttachEnv(ImageDto imageDto);

    /**
     * 根据主键获取镜像
     *
     * @param id 主键
     * @return ImageEntity
     */
    ImageEntity getById(@Param("id") long id);

    /**
     * 根据应用名称获取镜像列表
     *
     * @param appName 应用名称
     * @return ImageEntity
     */
    List<ImageEntity> getByAppName(@Param("appName") String appName);

    /**
     * 插入镜像
     *
     * @param image
     */
    void insert(ImageEntity image);

    /**
     * 更新镜像信息 只更新镜像的updateBy和updateTime
     *
     * @param image
     */
    void update(ImageEntity image);

    /**
     * 删除镜像
     *
     * @param id 主键
     */
    void delete(@Param("id") long id, @Param("updateBy") String updateBy);


    void batchDelete(@Param("images") Collection<ImageEntity> images);

    int countImageByAppAndTime(@Param("appName") String appName, @Param("beforeMonthFormat") String beforeMonthFormat);

    Set<ImageEntity> findNeedMarkAllImages(@Param("appName") String appName, @Param("ids") List<Long> ids,
                                           @Param("length") int length);

    Set<ImageEntity> beforeImagesInertTime(@Param("appName") String appName, @Param("time") String time);

    Set<ImageEntity> findImageByAppAndTag(@Param("repository") String repository, @Param("tag") String tag);

    List<ImageEntity> findAllApps();

}
